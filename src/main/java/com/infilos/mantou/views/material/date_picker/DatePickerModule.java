package com.infilos.mantou.views.material.date_picker;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class DatePickerModule extends WorkbenchModule {
    
    @Inject
    private DatePickerView datePickerView;
    
    public DatePickerModule() {
        super("Date Pickers", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return datePickerView;
    }
}
