package com.infilos.mantou.views.setting;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class SettingModule extends WorkModule {
    
    @Inject
    private SettingView settingView;
    
    public SettingModule() {
        super("Settings", MaterialDesign.MDI_SETTINGS);
    }

    @Override
    public Node activate() {
        return settingView;
    }
}
