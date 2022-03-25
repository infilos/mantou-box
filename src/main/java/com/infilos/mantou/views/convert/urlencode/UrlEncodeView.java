package com.infilos.mantou.views.convert.urlencode;

import com.dlsc.gemsfx.richtextarea.*;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
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
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

//import static io.ipfs.multibase.Multibase.Base.*;

@FXMLView
public class UrlEncodeView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private TextArea input;

    @FXML
    private Button encode;

    @FXML
    private Button decode;

    @FXML
    private TextArea output;

    @FXML
    private ScrollPane notePane;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    private TextArea currentResult = output;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleEncode(final ActionEvent event) {
        String originInput = input.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(encode, "Input require Non Blank!", 3);
            return;
        }

        try {
            String encodedOutput = URLEncoder.encode(originInput, StandardCharsets.UTF_8);

            output.setText(encodedOutput);
            currentResult = output;
        } catch (Exception e) {
            log().error("Encode url failed", e);
            workbench.showDialog(WorkbenchDialog
                .builder(
                    "Encode failed!",
                    String.format("Check the value to encode: \"%s\". \n%s", originInput, e.getMessage()),
                    ButtonType.CLOSE)
                .exception(e)
                .build()
            );
        }
    }

    @FXML
    private void handleDecode(final ActionEvent event) {
        String originInput = output.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(decode, "Input require Non Blank!", 3);
            return;
        }

        try {
            String decodedOutput = URLDecoder.decode(originInput, StandardCharsets.UTF_8);

            input.setText(decodedOutput);
            currentResult = input;
        } catch (Exception e) {
            log().error("Decode url failed", e);
            workbench.showDialog(WorkbenchDialog
                .builder(
                    "Decode failed!",
                    String.format("Check the value to decode: \"%s\". \n%s", originInput, e.getMessage()),
                    ButtonType.CLOSE)
                .exception(e)
                .build()
            );
        }
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();

        if (currentResult == output) {
            content.putString(output.getText());

            if (StringUtils.isBlank(content.getString())) {
                showInfoMessage("No result Copied! Encode first.");
            } else {
                Clipboard.getSystemClipboard().setContent(content);
                showInfoMessage("Encode result Copied!");
            }
        } else {
            content.putString(input.getText());

            if (StringUtils.isBlank(content.getString())) {
                showInfoMessage("No result Copied! Decode first.");
            } else {
                Clipboard.getSystemClipboard().setContent(content);
                showInfoMessage("Decode result Copied!");
            }
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        if (currentResult == output) {
            output.setText("");
        } else {
            input.setText("");
        }
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("Url Encode"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
