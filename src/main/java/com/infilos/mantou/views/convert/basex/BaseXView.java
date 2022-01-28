package com.infilos.mantou.views.convert.basex;

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
import org.apache.commons.codec.binary.*;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;

//import static io.ipfs.multibase.Multibase.Base.*;

@FXMLView
public class BaseXView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private TextArea input;

    @FXML
    private ChoiceBox<String> baseMode;

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

    private final Base16 base16 = new Base16();
    private final Base32 base32 = new Base32();
    private final Base32 base32Hex = new Base32(true);
    private final Base64 base64 = new Base64();
    private final Base64 base64Url = new Base64(true);

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        baseMode.getItems().addAll(List.of(
            "16",
            "32",
            //"32Upper",
            "32Hex",
            //"32HexUpper",
            //"36",
            //"58BTC",
            "64",
            "64Url"
            //,
            //"64Pad"
        ));
        baseMode.getSelectionModel().select("64");
        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleEncode(final ActionEvent event) {
        String originInput = input.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(encode, "Input require Non Blank!", 3);
            return;
        }

        // io.ipfs.multibase exists bug
        //String encodedOutput = switch (baseMode.getValue()) {
        //    case "16" -> Multibase.encode(Base16, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "32" -> Multibase.encode(Base32, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "32Upper" -> Multibase.encode(Base32Upper, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "32Hex" -> Multibase.encode(Base32Hex, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "32HexUpper" -> Multibase.encode(Base32HexUpper, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "36" -> Multibase.encode(Base36, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "58BTC" -> Multibase.encode(Base58BTC, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "64" -> Multibase.encode(Base64, originInput.getBytes(StandardCharsets.UTF_8));
        //    case "64Pad" -> Multibase.encode(Base64Pad, originInput.getBytes(StandardCharsets.UTF_8));
        //    default -> "";
        //};
        String encodedOutput = switch (baseMode.getValue()) {
            case "16" -> base16.encodeAsString(originInput.getBytes(StandardCharsets.UTF_8));
            case "32" -> base32.encodeAsString(originInput.getBytes(StandardCharsets.UTF_8));
            case "32Hex" -> base32Hex.encodeAsString(originInput.getBytes(StandardCharsets.UTF_8));
            case "64" -> base64.encodeAsString(originInput.getBytes(StandardCharsets.UTF_8));
            case "64Url" -> base64Url.encodeAsString(originInput.getBytes(StandardCharsets.UTF_8));
            default -> "";
        };

        output.setText(encodedOutput);
        currentResult = output;
    }

    @FXML
    private void handleDecode(final ActionEvent event) {
        String originInput = output.getText();
        if (StringUtils.isBlank(originInput)) {
            showErrorTooltip(decode, "Input require Non Blank!", 3);
            return;
        }

        try {
            String decodedOutput = switch (baseMode.getValue()) {
                case "16" -> new String(base16.decode(originInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                case "32" -> new String(base32.decode(originInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                case "32Hex" -> new String(base32Hex.decode(originInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                case "64" -> new String(base64.decode(originInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                case "64Url" -> new String(base64Url.decode(originInput.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                default -> "";
            };

            input.setText(decodedOutput);
            currentResult = input;
        } catch (Exception e) {
            log().error("Decode base X failed", e);
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
                RTHeading.create("Base X"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
