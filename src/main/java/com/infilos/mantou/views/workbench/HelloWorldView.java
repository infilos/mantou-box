package com.infilos.mantou.views.workbench;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class HelloWorldView extends BorderPane {

    public HelloWorldView() {
        getStyleClass().add("module-background");
        setCenter(new Label("Wellcome to Manto Box!"));
    }
}
