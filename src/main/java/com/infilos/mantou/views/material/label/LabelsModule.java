package com.infilos.mantou.views.material.label;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class LabelsModule extends WorkbenchModule {
    
    public LabelsModule() {
        super("Labels", MaterialDesign.MDI_LABEL);
    }
    
    @Inject
    private LabelsView labelsView;
    
    @Override
    public Node activate() {
        return labelsView;
    }
}
