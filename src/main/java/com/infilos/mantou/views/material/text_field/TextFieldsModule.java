package com.infilos.mantou.views.material.text_field;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class TextFieldsModule extends WorkbenchModule {
    
    @Inject
    private TextFieldsView textFieldsView;
    
    public TextFieldsModule() {
        super("Text Fields", MaterialDesignIcon.HUMAN_HANDSUP);
    }

    @Override
    public Node activate() {
        return textFieldsView;
    }
}
