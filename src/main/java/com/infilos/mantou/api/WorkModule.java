package com.infilos.mantou.api;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import org.kordamp.ikonli.Ikon;

import javafx.scene.control.Menu;

public abstract class WorkModule extends WorkbenchModule {
    
    public WorkModule(String name, Ikon icon) {
        super(name, icon);
    }
    
    public abstract Menu navMenu();
}
