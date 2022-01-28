package com.infilos.mantou.views.material.date_picker;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class DatePickerModule extends WorkbenchModule {
    
    @Inject
    private DatePickerView datePickerView;
    
    public DatePickerModule() {
        super("Date Pickers", MaterialDesign.MDI_HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return datePickerView;
    }
}
