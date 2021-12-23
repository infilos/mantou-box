package com.infilos.mantou.views.material.button;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class ButtonsModule extends WorkbenchModule {
    
    @Inject
    private ButtonsView buttonsView;
    
    public ButtonsModule() {
        super("Buttons", MaterialDesignIcon.HAND_POINTING_RIGHT);
    }

    @Override
    public Node activate() {
        return buttonsView;
    }
}
