package com.infilos.mantou;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.infilos.mantou.api.View;
import com.infilos.mantou.guice.AutoProvider;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Set;

public class MantouBoxModule extends AbstractModule {

    private final Application application;
    private final Stage primaryStage;

    public MantouBoxModule(Application application, Stage primaryStage) {
        this.application = application;
        this.primaryStage = primaryStage;
    }

    @Override
    protected void configure() {
        Set<Class<?>> autoClasses = Set.of(WorkbenchModule.class, View.class);

        new Reflections("com.infilos.mantou.views")
            .getTypesAnnotatedWith(AutoProvider.class)
            .stream()
            .filter(autoClasses::contains)
            .forEach(this::bind);
    }

    @Provides
    @Singleton
    HostServices provideHostService() {
        return application.getHostServices();
    }

    @Provides
    @Singleton
    Application.Parameters provideApplicationParameters() {
        return application.getParameters();
    }

    @Provides
    @Singleton
    Stage providePrimaryStage() {
        return primaryStage;
    }
}
