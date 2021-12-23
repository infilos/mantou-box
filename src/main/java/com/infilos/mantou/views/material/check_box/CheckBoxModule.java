package com.infilos.mantou.views.material.check_box;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.infilos.mantou.guice.AutoProvider;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

@AutoProvider
public class CheckBoxModule extends WorkbenchModule {
    
    @Inject
    private CheckBoxView checkBoxView;
    
    public CheckBoxModule() {
        super("Check Boxes", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return checkBoxView;
    }
}
