package com.infilos.mantou.views.material.combo_box;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class ComboBoxModule extends WorkbenchModule {

    @Inject
    private ComboBoxesView comboBoxesView;

    public ComboBoxModule() {
        super("Combox Boxes", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return comboBoxesView;
    }
}
