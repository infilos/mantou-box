package com.infilos.mantou.views.convert.md5file;

import com.dlsc.gemsfx.richtextarea.*;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;

@FXMLView
public class MD5FileView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @Inject
    private Stage mainStage;

    @FXML
    private VBox droparea;

    @FXML
    private Label fileInfo;
    
    @FXML
    private Button encode;
    
    @FXML
    private Button checksum;
    
    @FXML
    private CheckBox uppercase;
    
    @FXML
    private TextField encoded;
    
    @FXML
    private ScrollPane notePane;

    private final FileChooser fileChooser = new FileChooser();
    
    private String provideFilePath = null;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        droparea.setStyle("-fx-background-color: #282828;");
        fileChooser.setTitle("Select file...");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );

        // accepting drag and drop to the pane
        droparea.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // the first step is when the mouse enters the target. accept
                // copy or move of one file into the target
                if (event.getGestureSource() != droparea && event.getDragboard().hasFiles()
                    && event.getDragboard().getFiles().size() == 1) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        // accepting drag and drop to the pane
        droparea.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // complete the drag and drop event reading the dropped file.
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<File> files = db.getFiles();
                    if (files != null && !files.isEmpty()) {
                        provideFilePath = files.get(0).getAbsolutePath();
                        fileInfo.setText(provideFilePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        notePane.setContent(buildNoteArea());
    }

    @FXML
    public void handleOpen(final ActionEvent event) {
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            provideFilePath = file.getAbsolutePath(); 
            fileInfo.setText(provideFilePath);
        }
    }

    @FXML
    private void handleEncode(final ActionEvent event) {
        if (StringUtils.isBlank(provideFilePath)) {
            showErrorTooltip(encode, "Select file first!", 3);
            return;
        }
        
        File file = new File(provideFilePath);
        
        if(!file.exists() || !file.isFile()) {
            showErrorTooltip(encode, "Provided file invalid!", 3);
            return;
        }
        if (file.length()/1024/1024 > 1024) { // 1GB
            showErrorTooltip(encode, "Provided file too large!", 3);
            return;
        }
        
        try {
            String encodedOutput = DigestUtils.md5Hex(Files.readAllBytes(file.toPath()));
            if (uppercase.isSelected()) {
                encodedOutput = encodedOutput.toUpperCase();
            }

            encoded.setText(encodedOutput);
        } catch (IOException e) {
            showErrorTooltip(encode, "Encode failed!", 3);
        }
    }
    
    @FXML
    private void handleChecksum(final ActionEvent event) {
        if (StringUtils.isBlank(encoded.getText())) {
            showErrorTooltip(checksum, "Blank checksum!", 3);
            return;
        }

        String provideChecksum = encoded.getText();
        
        if (StringUtils.isBlank(provideFilePath)) {
            showErrorTooltip(checksum, "Select file first!", 3);
            return;
        }

        File file = new File(provideFilePath);

        if (!file.exists() || !file.isFile()) {
            showErrorTooltip(checksum, "Provided file invalid!", 3);
            return;
        }
        if (file.length() / 1024 / 1024 > 1024) { // 1GB
            showErrorTooltip(checksum, "Provided file too large!", 3);
            return;
        }

        try {
            String encodedOutput = DigestUtils.md5Hex(Files.readAllBytes(file.toPath()));
            if (uppercase.isSelected()) {
                encodedOutput = encodedOutput.toUpperCase();
            }

            if (provideChecksum.equals(encodedOutput)) {
                encoded.setStyle("-fx-text-fill: #239f23;");
            } else {
                encoded.setStyle("-fx-text-fill: #b74c4c;");
            }
            
        } catch (IOException e) {
            showErrorTooltip(encode, "Checksum failed!", 3);
        }
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
                RTHeading.create("MD5 File Checksum"),
                RTParagraph.create(
                    RTText.create("More features are working in progress! ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
