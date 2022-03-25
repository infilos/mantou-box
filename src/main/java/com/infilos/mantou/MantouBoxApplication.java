package com.infilos.mantou;

import com.dlsc.workbenchfx.Workbench;
import com.google.inject.Module;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.mantou.views.convert.ConvertModule;
import com.infilos.mantou.views.datetime.DatetimeModule;
import com.infilos.mantou.views.json.JsonModule;
import com.infilos.mantou.views.setting.SettingModule;
import com.infilos.mantou.views.textgen.TextGenModule;
import com.infilos.mantou.views.workbench.HelloWorldModule;
import com.infilos.utils.*;
import com.tangorabox.reactivedesk.ReactiveApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MantouBoxApplication extends ReactiveApplication implements AwareResource, Loggable {

    public static void main(String[] args) {
        launch(args);
    }

    private final Workbench workbench = Workbench.builder().build();

    @Inject
    private SettingModule settingModule;

    @Inject
    private TextGenModule textGenModule;

    @Inject
    private DatetimeModule datetimeModule;

    @Inject
    private ConvertModule convertModule;

    @Inject
    private JsonModule jsonModule;

    @Override
    protected Collection<Module> getApplicationModules(Stage mainStage) {
        return List.of(new MantouBoxModule(this, mainStage, workbench));
    }

    @Override
    protected void startReactiveApp(Stage stage) {
        workbench.getStylesheets().add(loadStyle("Workbench.css"));

        // start add modules
        workbench.getModules().add(new HelloWorldModule());
        workbench.getModules().add(settingModule);
        workbench.getModules().add(datetimeModule);
        workbench.getModules().add(textGenModule);
        workbench.getModules().add(convertModule);
        workbench.getModules().add(jsonModule);
        // end add modules

        // build navigation       
        workbench.getNavigationDrawerItems().addAll(
            settingModule.navMenu(),
            datetimeModule.navMenu(),
            convertModule.navMenu(),
            textGenModule.navMenu(),
            jsonModule.navMenu()
        );

        // build scene
        Scene mainScene = new Scene(workbench);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(mainScene);

        stage.setTitle("馒头盒子");
        stage.setScene(mainScene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
        stage.centerOnScreen();

        // detect memory
        Scheduler scheduler = Scheduler.create().startup();
        scheduler.schedule("Memory", () -> {
            DataSize max = DataSize.ofSuccinct(ObjectSize.ofMaxJvmHeap());
            DataSize free = DataSize.ofSuccinct(ObjectSize.ofFreeJvmHeap());
            DataSize used = DataSize.ofSuccinct(ObjectSize.ofUsedJvmHeap());
            log().info(
                "Mantou memory: max({}), free({}), used({})",
                max.succinctDataSize(), free.succinctDataSize(), used.succinctDataSize()
            );
        }, 1, 1, TimeUnit.MINUTES);
    }
}
