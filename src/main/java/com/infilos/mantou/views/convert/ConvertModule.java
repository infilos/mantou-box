package com.infilos.mantou.views.convert;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class ConvertModule extends WorkModule {
    
    @Inject
    private ConvertView convertView;
    
    public ConvertModule() {
        super("Convert", MaterialDesign.MDI_CACHED);
    }

    @Override
    public Node activate() {
        return convertView;
    }
}
