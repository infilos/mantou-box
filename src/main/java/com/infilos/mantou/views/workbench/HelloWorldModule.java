package com.infilos.mantou.views.workbench;

import com.infilos.mantou.api.WorkModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class HelloWorldModule extends WorkModule {

    public HelloWorldModule() {
        super("Hello World", MaterialDesignIcon.HUMAN_GREETING);
    }

    @Override
    public Node activate() {
        return new HelloWorldView();
    }
}
