package com.infilos.mantou.views.datetime;

import com.infilos.mantou.api.WorkModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

import javax.inject.Inject;

public class DatetimeModule extends WorkModule {
    
    @Inject
    private DatetimeView datetimeView;
    
    public DatetimeModule() {
        super("Datetime", MaterialDesignIcon.CALENDAR_CLOCK);
    }

    @Override
    public Node activate() {
        return datetimeView;
    }
}
