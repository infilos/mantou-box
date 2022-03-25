package com.infilos.mantou.views.convert;

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
public class ConvertView extends AnchorPane implements WorkView<Void>, TabPaneSupport, Loggable {

    @FXML
    public AnchorPane rootPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab basex;

    @FXML
    private Tab escape;

    @FXML
    private Tab md5text;

    @FXML
    private Tab md5file;

    @FXML
    private Tab urlencode;

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
        //;
    }

    public List<MenuItem> tabMenus() {
        MenuItem basexMenu = new MenuItem("BaseX");
        MenuItem escapeMenu = new MenuItem("Escape");
        MenuItem md5textMenu = new MenuItem("MD5 Text");
        MenuItem md5fileMenu = new MenuItem("MD5 File");
        MenuItem urlEncodeMenu = new MenuItem("Url Encode");

        basexMenu.setOnAction(e -> tabPane.getSelectionModel().select(basex));
        escapeMenu.setOnAction(e -> tabPane.getSelectionModel().select(escape));
        md5textMenu.setOnAction(e -> tabPane.getSelectionModel().select(md5text));
        md5fileMenu.setOnAction(e -> tabPane.getSelectionModel().select(md5file));
        urlEncodeMenu.setOnAction(e -> tabPane.getSelectionModel().select(urlencode));

        return List.of(basexMenu, escapeMenu, md5textMenu, md5fileMenu, urlEncodeMenu);
    }
}
