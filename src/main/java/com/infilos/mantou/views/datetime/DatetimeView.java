package com.infilos.mantou.views.datetime;

import com.infilos.mantou.api.WorkView;
import com.infilos.mantou.controls.TabPaneSupport;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLView
public class DatetimeView extends AnchorPane implements WorkView<Void>, TabPaneSupport, Loggable {

    @FXML
    public AnchorPane rootPane;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab timestamp;

    @FXML
    private Tab parser;
    
    @Override
    public void setModel(Void model) {
        
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        getStyleClass().add(JMetroStyleClass.BACKGROUND);
        makeTabPaneStretched(tabPane, widthProperty(), heightProperty());
    }

    public List<MenuItem> tabMenus() {
        MenuItem timestampMenu = new MenuItem("Timestamp");
        MenuItem parserMenu = new MenuItem("Parser");

        timestampMenu.setOnAction(e -> tabPane.getSelectionModel().select(timestamp));
        parserMenu.setOnAction(e -> tabPane.getSelectionModel().select(parser));

        return List.of(timestampMenu, parserMenu);
    }
}
