package com.infilos.mantou.views.datetime.parser;

import com.dlsc.gemsfx.richtextarea.*;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.github.sisyphsu.dateparser.DateParser;
import com.github.sisyphsu.dateparser.DateParserBuilder;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.infilos.mantou.views.datetime.parser.DatetimeParser.*;

@FXMLView
public class DatetimeParserView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private CheckBox preferMonth;

    @FXML
    private TextArea customRules;

    @FXML
    private ComboBox<String> resultPattern;

    @FXML
    private TextArea generated;

    @FXML
    private Button parse;

    @FXML
    private ScrollPane notePane;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        buildComboItems(resultPattern, DefaultTimePatternIndex, TimePatterns);
        enableRefreshComboValue(resultPattern, String::toString, DefaultTimePattern);

        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleCopyCustomRules(final ActionEvent event) {
        if (StringUtils.isBlank(customRules.getText())) {
            showInfoMessage("Custom Rules is Blank!");
            return;
        }

        final ClipboardContent content = new ClipboardContent();
        content.putString(customRules.getText());
        Clipboard.getSystemClipboard().setContent(content);

        showInfoMessage("Custom Rules Copied!");
    }

    @FXML
    private void handleClearCustomRules(final ActionEvent event) {
        customRules.setText("");
    }

    @FXML
    private void handleResetResultPattern(final ActionEvent event) {
        resultPattern.setValue(DefaultTimePattern);
    }

    @FXML
    private void handleParse(final ActionEvent event) {
        try {
            boolean isPreferMonth = preferMonth.isSelected();
            List<String> inputCustomRules = Arrays
                .stream(customRules.getText().split("\n"))
                .filter(StringUtils::isNotBlank).toList();
            String slectedResultPattern = comboValueOf(resultPattern, String.class, String::toString);
            DateTimeFormatter resultFormatter = TimePatternMappings.get(slectedResultPattern);
            List<String> inputDatetimes = Arrays
                .stream(generated.getText().split("\n"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank).toList();

            if (inputDatetimes.isEmpty()) {
                showErrorTooltip(parse, "Input must not be Blank!", 5);
                return;
            }

            DateParserBuilder builder = DateParser.newBuilder()
                .preferMonthFirst(isPreferMonth);
            inputCustomRules.forEach(builder::addRule);
            DateParser parser = builder.build();

            String result = inputDatetimes.stream()
                .map(parser::parseDateTime)
                .map(parsed -> parsed.format(resultFormatter))
                .collect(Collectors.joining("\n"));

            generated.setText(result);
        } catch (Exception e) {
            workbench.showDialog(WorkbenchDialog
                .builder(
                    "Parse failed!",
                    String.format("Check the input and rules. \n%s", e.getMessage()),
                    ButtonType.CLOSE)
                .exception(e)
                .build()
            );
        }
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(generated.getText());

        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("No history Copied! Convert first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage(String.format("%s Items Copied!", content.getString().split("\n").length));
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        generated.setText("");
    }

    @FXML
    private void handleGuess(final ActionEvent event) {
        // TODO
    }

    @FXML
    private void handleCopyAllGuess(final ActionEvent event) {
        // TODO
    }

    @FXML
    private void handleCopyGuessPattern(final ActionEvent event) {
        // TODO
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("Datetime Parser"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
