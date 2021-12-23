package com.infilos.mantou.views.material.label;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class LabelsModule extends WorkbenchModule {
    
    public LabelsModule() {
        super("Labels", MaterialDesignIcon.LABEL);
    }
    
    @Inject
    private LabelsView labelsView;
    
    @Override
    public Node activate() {
        return labelsView;
    }
}
