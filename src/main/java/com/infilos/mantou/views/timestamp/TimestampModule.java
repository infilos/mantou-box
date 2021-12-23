package com.infilos.mantou.views.timestamp;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class TimestampModule extends WorkbenchModule {
    
    @Inject
    private TimestampView timestampView;

    public TimestampModule() {
        super("Timestamp", MaterialDesignIcon.CLOCK_FAST);
    }

    @Override
    public Node activate() {
        return timestampView;
    }
}
