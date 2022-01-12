package com.infilos.mantou.views.textgen;

import com.infilos.mantou.api.WorkModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class TextGenModule extends WorkModule {
    
    @Inject
    private TextGenView textGenView;
    
    public TextGenModule() {
        super("Text Generate", MaterialDesignIcon.FORMAT_TEXT);
    }

    @Override
    public Node activate() {
        return textGenView;
    }
}
