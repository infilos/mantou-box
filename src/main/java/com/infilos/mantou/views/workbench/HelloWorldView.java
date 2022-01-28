package com.infilos.mantou.views.workbench;

import com.dlsc.gemsfx.richtextarea.*;
import com.infilos.mantou.utils.AwareResource;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HelloWorldView extends BorderPane implements AwareResource {

    public HelloWorldView() {
        getStyleClass().add("module-background");
        
        Text title = new Text("Wellcome to Manto Box!");
        Text descr = new Text("More features are working in progress!");
        title.setStyle("-fx-font: 24 Ubuntu;");
        descr.setStyle("-fx-font: 18 Ubuntu;");
        
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(4);
        box.getChildren().addAll(title, descr);

        setCenter(box);
    }
}
