package com.infilos.mantou.controls;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import javafx.scene.control.ButtonType;

public interface DialogSupport {

    Workbench workbench();

    default void showErrorDialog(String pattern, Object value, Exception error) {
        workbench().showDialog(WorkbenchDialog
            .builder(
                "Convert failed!",
                String.format("Check the time pattern and value: \"%s\", \"%s\". \n%s", pattern, value, error.getMessage()),
                ButtonType.CLOSE)
            .exception(error)
            .build()
        );
    }

    default void showErrorDialog(String title, String message, Exception error) {
        workbench().showDialog(WorkbenchDialog
            .builder(title, message, ButtonType.CLOSE)
            .exception(error)
            .build()
        );
    }

    default void showErrorDialog(String title, Exception error) {
        workbench().showDialog(WorkbenchDialog
            .builder(title, error.getMessage() != null ? error.getMessage() : "", ButtonType.CLOSE)
            .exception(error)
            .build()
        );
    }
}
