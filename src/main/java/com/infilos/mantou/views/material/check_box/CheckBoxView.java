package com.infilos.mantou.views.material.check_box;

import com.infilos.mantou.api.View;
import com.infilos.mantou.guice.AutoProvider;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLView
@AutoProvider
public class CheckBoxView extends StackPane implements View<Void> {

    @Override
    public void setModel(Void model) {
        
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
