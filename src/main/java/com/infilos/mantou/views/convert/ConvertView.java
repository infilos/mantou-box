package com.infilos.mantou.views.convert;

import com.infilos.mantou.api.WorkView;
import com.infilos.mantou.controls.TabPaneSupport;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLView
public class ConvertView extends AnchorPane implements WorkView<Void>, TabPaneSupport, Loggable {

    @FXML
    public AnchorPane rootPane;
    
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab basex;
    
    @FXML
    private Tab md5text;

    @FXML
    private Tab md5file;
    
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
}
