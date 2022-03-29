package com.infilos.mantou.views.json;

import com.infilos.mantou.api.WorkView;
import com.infilos.mantou.controls.TabPaneSupport;
import com.infilos.mantou.utils.AwareResource;
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
public class JsonView extends AnchorPane implements WorkView<Void>, TabPaneSupport, AwareResource, Loggable {

    @FXML
    public AnchorPane rootPane;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab formatTab;
    
    @FXML
    private Tab compareTab;
    
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
        getStylesheets().add(loadStyle("JsonView.css"));
        getStylesheets().add(loadStyle("JsonHighlight.css"));
        makeTabPaneStretched(tabPane, widthProperty(), heightProperty());
    }

    public List<MenuItem> tabMenus() {
        MenuItem formatMenu = new MenuItem("Format");
        MenuItem compareMenu = new MenuItem("Compare");

        formatMenu.setOnAction(e -> tabPane.getSelectionModel().select(formatTab));
        compareMenu.setOnAction(e -> tabPane.getSelectionModel().select(compareTab));

        return List.of(formatMenu);
    }
}
