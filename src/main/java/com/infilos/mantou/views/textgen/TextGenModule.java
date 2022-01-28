package com.infilos.mantou.views.textgen;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class TextGenModule extends WorkModule {
    
    @Inject
    private TextGenView textGenView;
    
    public TextGenModule() {
        super("Text Generate", MaterialDesign.MDI_FORMAT_TEXT);
    }

    @Override
    public Node activate() {
        return textGenView;
    }
}
