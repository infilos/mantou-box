package com.infilos.mantou.views.textgen;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class TextGenModule extends WorkModule {
    
    @Inject
    private TextGenView textGenView;
    
    @Inject
    private Workbench workbench;
    
    public TextGenModule() {
        super("Text Generate", MaterialDesign.MDI_FORMAT_TEXT);
    }

    @Override
    public Node activate() {
        return textGenView;
    }

    @Override
    public Menu navMenu() {
        Menu menu = new Menu("Text Generate");
        menu.getItems().addAll(textGenView.tabMenus());
        menu.setOnAction(e -> {
            workbench.hideNavigationDrawer();
            workbench.openModule(this);
        });

        return menu;
    }
}
