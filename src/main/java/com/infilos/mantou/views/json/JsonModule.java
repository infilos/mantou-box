package com.infilos.mantou.views.json;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class JsonModule extends WorkModule {
    
    @Inject
    private JsonView jsonView;
    
    @Inject
    private Workbench workbench;
    
    public JsonModule() {
        super("Json", MaterialDesign.MDI_JSON);
    }

    @Override
    public Node activate() {
        return jsonView;
    }

    @Override
    public Menu navMenu() {
        Menu menu = new Menu("Json");
        menu.getItems().addAll(jsonView.tabMenus());
        menu.setOnAction(e -> {
            workbench.hideNavigationDrawer();
            workbench.openModule(this);
        });

        return menu;
    }
}
