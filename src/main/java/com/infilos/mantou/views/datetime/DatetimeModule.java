package com.infilos.mantou.views.datetime;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class DatetimeModule extends WorkModule {
    
    @Inject
    private DatetimeView datetimeView;
    
    public DatetimeModule() {
        super("Datetime", MaterialDesign.MDI_CALENDAR_CLOCK);
    }

    @Override
    public Node activate() {
        return datetimeView;
    }
}
