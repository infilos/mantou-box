package com.infilos.mantou.views.convert.md5text;

import com.dlsc.gemsfx.richtextarea.*;
import com.dlsc.workbenchfx.Workbench;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLView
public class MD5TextView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private TextArea origin;
    
    @FXML
    private CheckBox uppercase;

    @FXML
    private Button encode;

    @FXML
    private TextArea encoded;

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
        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleEncode(final ActionEvent event) {
        String originInput = origin.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(encode, "Input require Non Blank!", 3);
            return;
        }

        String encodedOutput = DigestUtils.md5Hex(originInput);
        if(uppercase.isSelected()) {
            encodedOutput = encodedOutput.toUpperCase();
        }

        encoded.setText(encodedOutput);
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(encoded.getText());

        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("No result Copied! Encode first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage("MD5 result Copied!");
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        encoded.setText("");
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("MD5 Encoder"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
