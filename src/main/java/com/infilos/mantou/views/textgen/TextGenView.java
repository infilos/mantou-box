package com.infilos.mantou.views.textgen;

import com.infilos.mantou.api.WorkView;
import com.infilos.mantou.controls.TabPaneSupport;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLView
public class TextGenView extends AnchorPane implements WorkView<Void>, TabPaneSupport, Loggable {

    @FXML
    public AnchorPane rootPane;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab passTab;

    @FXML
    private Tab uuidTab;
    
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
        MenuItem passMenu = new MenuItem("Password");
        MenuItem uuidMenu = new MenuItem("UUID");

        passMenu.setOnAction(e -> tabPane.getSelectionModel().select(passTab));
        uuidMenu.setOnAction(e -> tabPane.getSelectionModel().select(uuidTab));

        return List.of(passMenu, uuidMenu);
    }
}
