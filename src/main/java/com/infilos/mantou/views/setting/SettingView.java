package com.infilos.mantou.views.setting;

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
public class SettingView extends AnchorPane implements WorkView<Void>, TabPaneSupport, Loggable {

    @FXML
    public AnchorPane rootPane;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab h2Tab;
    
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
        MenuItem h2Menu = new MenuItem("H2");

        h2Menu.setOnAction(e -> tabPane.getSelectionModel().select(h2Tab));

        return List.of(h2Menu);
    }
}
