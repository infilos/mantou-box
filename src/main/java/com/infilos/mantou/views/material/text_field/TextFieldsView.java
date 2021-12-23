package com.infilos.mantou.views.material.text_field;

import com.infilos.mantou.api.View;
import com.tangorabox.reactivedesk.FXMLView;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.BindingUtils;
import io.github.palexdev.materialfx.utils.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;

@FXMLView
public class TextFieldsView extends AnchorPane implements View<TextFieldsModel>,Initializable {
    @FXML
    private MFXTextField validated;

    @FXML
    private MFXCheckbox checkbox;

    @FXML
    private MFXDatePicker picker;

    @FXML
    private MFXPasswordField passwordValidated;

    @Override
    public void setModel(TextFieldsModel model) {

    }

    @Override
    public TextFieldsModel getModel() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getStylesheets().add(
            Objects.requireNonNull(
                getClass().getResource("/style/TextFieldsDemo.css")).toExternalForm()
        );
        
        BooleanProperty checkboxValidation = BindingUtils.toProperty(
            Bindings.createBooleanBinding(
                () -> checkbox.isSelected(),
                checkbox.selectedProperty()
            )
        );
        BooleanProperty datePickerValidation = BindingUtils.toProperty(
            Bindings.createBooleanBinding(
                () -> {
                    LocalDate value = picker.getDatePicker().getValue();
                    if (value != null) {
                        return value.equals(LocalDate.of(1911, Month.OCTOBER, 3));
                    } else {
                        return false;
                    }
                },
                picker.getDatePicker().valueProperty()
            )
        );
        validated.getValidator().add(checkboxValidation, "Checkbox must be selected");
        validated.getValidator().add(datePickerValidation, "Selected date must be 03/10/1911");
        validated.setValidated(true);
        validated.setIcon(new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#8FF7A7")));
        validated.getIcon().visibleProperty().bind(validated.getValidator().validProperty());

        passwordValidated.setValidated(true);
        passwordValidated.getValidator().add(
            BindingUtils.toProperty(passwordValidated.passwordProperty().length().greaterThanOrEqualTo(8)),
            "Must be at least 8 characters long"
        );
        passwordValidated.getValidator().add(BindingUtils.toProperty(
                Bindings.createBooleanBinding(() -> passwordValidated.getPassword().matches(".*\\d.*"), passwordValidated.passwordProperty())),
            "Must contain at least one digit"
        );
        passwordValidated.getValidator().add(BindingUtils.toProperty(
                Bindings.createBooleanBinding(() -> StringUtils.containsAny(passwordValidated.getPassword(), "", "?", "!", "@", "(", ")", "[", "]", "{", "}", "-", "_"), passwordValidated.passwordProperty())),
            "Must contain at least one among these: ?!@()[]{}-_"
        );
    }
}
