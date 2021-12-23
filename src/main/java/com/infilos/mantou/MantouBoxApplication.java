package com.infilos.mantou;

import com.dlsc.workbenchfx.Workbench;
import com.google.inject.Module;
import com.infilos.mantou.views.material.button.ButtonsModule;
import com.infilos.mantou.views.material.check_box.CheckBoxModule;
import com.infilos.mantou.views.material.combo_box.ComboBoxModule;
import com.infilos.mantou.views.material.date_picker.DatePickerModule;
import com.infilos.mantou.views.material.label.LabelsModule;
import com.infilos.mantou.views.material.text_field.TextFieldsModule;
import com.infilos.mantou.views.timestamp.TimestampModule;
import com.infilos.mantou.views.workbench.HelloWorldModule;
import com.tangorabox.reactivedesk.ReactiveApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class MantouBoxApplication extends ReactiveApplication {

    private Workbench workbench;

    @Inject
    private ApplicationContext context;
    
    @Inject
    private ButtonsModule buttonsModule;
    
    @Inject
    private CheckBoxModule checkBoxModule;
    
    @Inject
    private DatePickerModule datePickerModule;

    @Inject
    private TextFieldsModule textFieldsModule;

    @Inject
    private ComboBoxModule comboBoxModule;
    
    @Inject
    private LabelsModule labelsModule;
    
    @Inject
    private TimestampModule timestampModule;

    @Override
    protected Collection<Module> getApplicationModules(Stage primaryStage) {
        return List.of(new MantouBoxModule(this, primaryStage));
    }

    @Override
    protected void startReactiveApp(Stage stage) {
        //Parent mainComponent = initMainComponent();
        workbench = buildWorkbench();
        Scene mainScene = new Scene(workbench);

        stage.setTitle("馒头盒子");
        stage.setScene(mainScene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
        stage.centerOnScreen();
    }
    
    private Workbench buildWorkbench() {
        return Workbench.builder(
            timestampModule,
            new HelloWorldModule(),
            buttonsModule,
            checkBoxModule,
            datePickerModule,
            textFieldsModule,
            comboBoxModule,
            labelsModule
        ).build();
    }
}
