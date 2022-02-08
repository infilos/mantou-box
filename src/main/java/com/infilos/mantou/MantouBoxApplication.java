package com.infilos.mantou;

import com.dlsc.workbenchfx.Workbench;
import com.google.inject.Module;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.mantou.views.convert.ConvertModule;
import com.infilos.mantou.views.datetime.DatetimeModule;
import com.infilos.mantou.views.setting.SettingModule;
import com.infilos.mantou.views.textgen.TextGenModule;
import com.infilos.mantou.views.workbench.HelloWorldModule;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.ReactiveApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class MantouBoxApplication extends ReactiveApplication implements AwareResource, Loggable {

    public static void main(String[] args) {
        launch(args);
    }

    private final Workbench workbench = Workbench.builder().build();

    @Inject
    private ApplicationContext context;
    
    @Inject
    private SettingModule settingModule;
    
    @Inject
    private TextGenModule textGenModule;
    
    @Inject
    private DatetimeModule datetimeModule;
    
    @Inject
    private ConvertModule convertModule;

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
        // end add modules

        Scene mainScene = new Scene(workbench);
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(mainScene);

        stage.setTitle("馒头盒子");
        stage.setScene(mainScene);
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.show();
        stage.centerOnScreen();
    }
}
