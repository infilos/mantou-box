package com.infilos.mantou.views.material.date_picker;

import com.infilos.mantou.api.WorkView;
import com.tangorabox.reactivedesk.FXMLView;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@FXMLView
public class DatePickerView extends StackPane implements WorkView<Void> {

    @FXML
    private StackPane pane;

    @FXML
    private MFXDatePicker customPicker;

    @Override
    public void setModel(Void model) {

    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customPicker.getContent().getStylesheets().add(loadStyle("CustomDatePicker.css"));

        MFXDatePicker initialized = new MFXDatePicker(LocalDate.now());
        initialized.setColorText(true);
        pane.getChildren().add(initialized);
        StackPane.setMargin(initialized, new Insets(10, 0, 0, 0));
    }
}
