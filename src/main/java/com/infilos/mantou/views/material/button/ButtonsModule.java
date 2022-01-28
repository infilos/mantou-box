package com.infilos.mantou.views.material.button;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class ButtonsModule extends WorkbenchModule {
    
    @Inject
    private ButtonsView buttonsView;
    
    public ButtonsModule() {
        super("Buttons", MaterialDesign.MDI_HAND_POINTING_RIGHT);
    }

    @Override
    public Node activate() {
        return buttonsView;
    }
}
