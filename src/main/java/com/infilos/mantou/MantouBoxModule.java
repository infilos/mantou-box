package com.infilos.mantou;

import com.dlsc.workbenchfx.Workbench;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.infilos.mantou.api.WorkModule;
import com.infilos.mantou.api.WorkView;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@SuppressWarnings("rawtypes")
public class MantouBoxModule extends AbstractModule {

    private final Application application;
    private final Stage mainStage;
    private final Workbench workbench;

    public MantouBoxModule(Application application, Stage mainStage, Workbench workbench) {
        this.application = application;
        this.mainStage = mainStage;
        this.workbench = workbench;
    }

    @Override
    protected void configure() {
        // allow view inject workbench instance
        bind(Workbench.class).toInstance(workbench);

        // scan view and module
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath())
            .addScanners(SubTypes)
        );
        Set<Class<? extends WorkView>> views = reflections.getSubTypesOf(WorkView.class);
        Set<Class<? extends WorkModule>> modules = reflections.getSubTypesOf(WorkModule.class);

        // register view and module
        views.forEach(this::bind);
        modules.stream()
            .filter(m -> Arrays.stream(m.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0))
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
    Stage provideMainStage() {
        return mainStage;
    }
}
