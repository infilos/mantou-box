package com.infilos.mantou.views.json;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class JsonModule extends WorkModule {
    
    @Inject
    private JsonView jsonView;
    
    public JsonModule() {
        super("Json", MaterialDesign.MDI_JSON);
    }

    @Override
    public Node activate() {
        return jsonView;
    }
}
