package com.infilos.mantou.views.setting;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class SettingModule extends WorkModule {
    
    @Inject
    private SettingView settingView;
    
    @Inject
    private Workbench workbench;
    
    public SettingModule() {
        super("Settings", MaterialDesign.MDI_SETTINGS);
    }

    @Override
    public Node activate() {
        return settingView;
    }

    @Override
    public Menu navMenu() {
        Menu menu = new Menu("Settings");
        menu.getItems().addAll(settingView.tabMenus());
        menu.setOnAction(e -> {
            workbench.hideNavigationDrawer();
            workbench.openModule(this);
        });

        return menu;
    }
}
