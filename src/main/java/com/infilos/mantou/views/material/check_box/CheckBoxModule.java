package com.infilos.mantou.views.material.check_box;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class CheckBoxModule extends WorkbenchModule {
    
    @Inject
    private CheckBoxView checkBoxView;
    
    public CheckBoxModule() {
        super("Check Boxes", MaterialDesign.MDI_HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return checkBoxView;
    }
}
