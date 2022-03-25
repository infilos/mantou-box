package com.infilos.mantou.views.convert;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class ConvertModule extends WorkModule {
    
    @Inject
    private ConvertView convertView;
    
    @Inject
    private Workbench workbench;
    
    public ConvertModule() {
        super("Text Convert", MaterialDesign.MDI_CACHED);
    }

    @Override
    public Node activate() {
        return convertView;
    }
    
    public Menu navMenu() {
        Menu menu = new Menu("Text Convert");
        menu.getItems().addAll(convertView.tabMenus());
        menu.setOnAction(e -> {
            workbench.hideNavigationDrawer();
            workbench.openModule(this);
        });
        
        return menu;
    }
}
