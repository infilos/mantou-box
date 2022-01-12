package com.infilos.mantou.views.textgen.password;

import com.dlsc.gemsfx.richtextarea.*;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLView
public class PasswordGeneratorView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private ComboBox<Integer> genCount;

    @FXML
    private ComboBox<Integer> expectLength;

    @FXML
    private CheckBox useLower;

    @FXML
    private CheckBox useUpper;

    @FXML
    private CheckBox useDigits;

    @FXML
    private CheckBox useSymbols;

    @FXML
    private TextField symbolChars;

    @FXML
    private Button generate;

    @FXML
    private Button copy;

    @FXML
    private Button clear;

    @FXML
    private TextArea generated;

    @FXML
    private ScrollPane notePane;

    @Inject
    private Stage mainStage;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        buildComboItems(genCount, List.of(1, 2, 3, 5, 10, 20, 50, 100));
        enableRefreshComboValue(genCount, Integer::parseInt, 0);

        //
        buildComboItems(expectLength, List.of(6, 8, 10, 12, 14, 16, 18, 20));
        enableRefreshComboValue(expectLength, Integer::parseInt, 0);

        //
        useLower.setSelected(true);
        useUpper.setSelected(false);
        useDigits.setSelected(true);
        useSymbols.setSelected(false);

        //
        symbolChars.setVisible(false);
        symbolChars.setText(PasswordGenerator.SYMBOL_CHARS);

        //
        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleUseSymbols(final ActionEvent event) {
        if (useSymbols.isSelected()) {
            symbolChars.setVisible(true);
        } else {
            symbolChars.setVisible(false);
        }
    }

    @FXML
    private void handleGenerate(final ActionEvent event) {
        if (useSymbols.isSelected() && StringUtils.isBlank(symbolChars.getText())) {
            showErrorTooltip(generate, "Non Blank Symblos Required!", 3);
            return;
        }

        Integer expectCount = comboValueOf(genCount, Integer.class, Integer::parseInt);
        Integer passwordLength = comboValueOf(expectLength, Integer.class, Integer::parseInt);

        if (expectCount <= 0) {
            showErrorTooltip(generate, "Generate count must > 0!", 3);
            return;
        }
        if (passwordLength <= 0) {
            showErrorTooltip(generate, "Expect length must > 0!", 3);
            return;
        }

        boolean isUseLower = useLower.isSelected();
        boolean isUseUpper = useUpper.isSelected();
        boolean isUseDigits = useDigits.isSelected();
        boolean isUseSymbols = useSymbols.isSelected();
        String symbolCanditates = symbolChars.getText();

        List<String> passwords = PasswordGenerator.generate(
            expectCount,
            passwordLength,
            isUseLower,
            isUseUpper,
            isUseDigits,
            isUseSymbols,
            symbolCanditates
        );

        generated.setText(String.join("\n", passwords));
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(generated.getText());

        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("No Password Copied! Generate first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage(String.format("%s Items Copied!", content.getString().split("\n").length));
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        generated.setText("");
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("Password Generator"),
                RTParagraph.create(
                    RTText.create("More features are working in progress. ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
