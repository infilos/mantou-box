package com.infilos.mantou.api;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import org.kordamp.ikonli.Ikon;

public abstract class WorkModule extends WorkbenchModule {
    
    public WorkModule(String name, Ikon icon) {
        super(name, icon);
    }
}
