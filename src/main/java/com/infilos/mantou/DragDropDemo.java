package com.infilos.mantou;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DragDropDemo extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        ImageView image = new ImageView();
        StackPane myPane = new StackPane();
        myPane.getChildren().add(image);

        // starting drag and drop from the pane
        myPane.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // create a new file object representing a temporary file and
                // put it in the dragboard. create the temporary file and save
                // the image to it.
                Dragboard db = myPane.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                File tempFile = new File(System.getProperty("java.io.tmpdir"), "test_image.png");
                BufferedImage bImage = SwingFXUtils.fromFXImage(image.getImage(), null);
                try {
                    ImageIO.write(bImage, "png", tempFile);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                content.putFiles(Arrays.asList(tempFile));
                db.setContent(content);
                event.consume();
            }
        });

        // completing drag and drop from the pane
        myPane.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // if the transfer is not accepted delete the temporary file.
                if (!event.isAccepted()) {
                    Dragboard db = event.getDragboard();
                    List<File> files = db.getFiles();
                    if (files != null && !files.isEmpty()) {
                        files.get(0).delete();
                    }
                }
                event.consume();
            }
        });

        // accepting drag and drop to the pane
        myPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // the first step is when the mouse enters the target. accept
                // copy or move of one file into the target
                if (event.getGestureSource() != myPane && event.getDragboard().hasFiles()
                    && event.getDragboard().getFiles().size() == 1) {
                    if (!new Image(event.getDragboard().getFiles().get(0).toURI().toString()).isError()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                }
                event.consume();
            }
        });

        // accepting drag and drop to the pane
        myPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                // complete the drag and drop event reading the dropped file.
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<File> files = db.getFiles();
                    if (files != null && !files.isEmpty()) {
                        image.setImage(new Image(files.get(0).toURI().toString()));
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
