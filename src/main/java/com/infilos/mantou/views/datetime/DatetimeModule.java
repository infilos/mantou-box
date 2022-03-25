package com.infilos.mantou.views.datetime;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javax.inject.Inject;

public class DatetimeModule extends WorkModule {
    
    @Inject
    private DatetimeView datetimeView;
    
    @Inject
    private Workbench workbench;
    
    public DatetimeModule() {
        super("Datetime", MaterialDesign.MDI_CALENDAR_CLOCK);
    }

    @Override
    public Node activate() {
        return datetimeView;
    }

    @Override
    public Menu navMenu() {
        Menu menu = new Menu("Datetime");
        menu.getItems().addAll(datetimeView.tabMenus());
        menu.setOnAction(e -> {
            workbench.hideNavigationDrawer();
            workbench.openModule(this);
        });

        return menu;
    }
}
