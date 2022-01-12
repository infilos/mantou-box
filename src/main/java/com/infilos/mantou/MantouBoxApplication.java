package com.infilos.mantou;

import com.dlsc.workbenchfx.Workbench;
import com.google.inject.Module;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.mantou.views.textgen.TextGenModule;
import com.infilos.mantou.views.timestamp.TimestampModule;
import com.infilos.mantou.views.workbench.HelloWorldModule;
import com.tangorabox.reactivedesk.ReactiveApplication;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class MantouBoxApplication extends ReactiveApplication implements AwareResource {

    private final Workbench workbench = Workbench.builder().build();

    @Inject
    private ApplicationContext context;

    @Inject
    private TimestampModule formsModule;
    
    @Inject
    private TextGenModule textGenModule;

    @Override
    protected Collection<Module> getApplicationModules(Stage mainStage) {
        return List.of(new MantouBoxModule(this, mainStage, workbench));
    }

    @Override
    protected void startReactiveApp(Stage stage) {
        workbench.getStylesheets().add(loadStyle("Workbench.css"));
        
        // add more modules here
        workbench.getModules().add(new HelloWorldModule());
        workbench.getModules().add(formsModule);
        workbench.getModules().add(textGenModule);

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
