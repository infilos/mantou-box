package com.infilos.mantou.views.workbench;

import com.infilos.mantou.api.WorkModule;
import javafx.scene.Node;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class HelloWorldModule extends WorkModule {

    public HelloWorldModule() {
        super("Wellcome", MaterialDesign.MDI_HUMAN_GREETING);
    }

    @Override
    public Node activate() {
        return new HelloWorldView();
    }
}
