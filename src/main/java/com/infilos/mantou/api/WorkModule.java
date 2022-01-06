package com.infilos.mantou.api;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;

public abstract class WorkModule extends WorkbenchModule {
    
    public WorkModule(String name, MaterialDesignIcon icon) {
        super(name, icon);
    }
}
