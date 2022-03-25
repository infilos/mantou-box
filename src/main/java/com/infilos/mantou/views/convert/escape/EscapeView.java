package com.infilos.mantou.views.convert.escape;

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
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

@FXMLView
public class EscapeView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private TextArea input;

    @FXML
    private ChoiceBox<String> textMode;

    @FXML
    private Button escape;

    @FXML
    private Button unescape;

    @FXML
    private TextArea output;

    @FXML
    private ScrollPane notePane;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    private TextArea currentResult = output;
    
    private static final Map<String, CharSequenceTranslator> EscapeTranslators = new HashMap<>() {{
        put("JAVA", StringEscapeUtils.ESCAPE_JAVA);
        put("ECMASCRIPT", StringEscapeUtils.ESCAPE_ECMASCRIPT);
        put("JSON", StringEscapeUtils.ESCAPE_JSON);
        put("XML10", StringEscapeUtils.ESCAPE_XML10);
        put("XML11", StringEscapeUtils.ESCAPE_XML11);
        put("HTML3", StringEscapeUtils.ESCAPE_HTML3);
        put("HTML4", StringEscapeUtils.ESCAPE_HTML4);
        put("CSV", StringEscapeUtils.ESCAPE_CSV);
        put("XSI", StringEscapeUtils.ESCAPE_XSI);
    }};

    private static final Map<String, CharSequenceTranslator> UnescapeTranslators = new HashMap<>() {{
        put("JAVA", StringEscapeUtils.UNESCAPE_JAVA);
        put("ECMASCRIPT", StringEscapeUtils.UNESCAPE_ECMASCRIPT);
        put("JSON", StringEscapeUtils.UNESCAPE_JSON);
        put("XML", StringEscapeUtils.UNESCAPE_XML);
        put("HTML3", StringEscapeUtils.UNESCAPE_HTML3);
        put("HTML4", StringEscapeUtils.UNESCAPE_HTML4);
        put("CSV", StringEscapeUtils.UNESCAPE_CSV);
        put("XSI", StringEscapeUtils.UNESCAPE_XSI);
    }};

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textMode.getItems().addAll(List.of(
            "JAVA",
            "ECMASCRIPT",
            "JSON",
            "XML10",
            "XML11",
            "HTML3",
            "HTML4",
            "CSV",
            "XSI",
            "XML"   // only unescape
        ));
        textMode.getSelectionModel().select("JSON");

        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleEscape(final ActionEvent event) {
        String originInput = input.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(escape, "Input require Non Blank!", 3);
            return;
        }

        if(StringUtils.equals(textMode.getValue(), "XML")){
            showErrorTooltip(escape, "Choose XML10/XML11 for escape!", 3);
            return;
        }
        
        String escapedOutput = StringEscapeUtils.builder(EscapeTranslators.get(textMode.getValue())).escape(originInput).toString();
        output.setText(escapedOutput);
        currentResult = output;
    }

    @FXML
    private void handleUnescape(final ActionEvent event) {
        String originInput = output.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(unescape, "Input require Non Blank!", 3);
            return;
        }
        
        if (StringUtils.equalsAny(textMode.getValue(), "XML10", "XML11")) {
            showErrorTooltip(escape, "Choose XML for unescape!", 3);
            return;
        }
        
        String unescapedOutput = StringEscapeUtils.builder(UnescapeTranslators.get(textMode.getValue())).escape(originInput).toString();
        input.setText(unescapedOutput);
        currentResult = input;
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
                RTHeading.create("Escape"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
