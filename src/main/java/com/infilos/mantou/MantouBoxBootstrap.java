package com.infilos.mantou;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.concurrent.FutureTask;

public class MantouBoxBootstrap {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(ALERT_EXCEPTION_HANDLER);

        Application.launch(MantouBoxApplication.class, args);
    }

    public static final Thread.UncaughtExceptionHandler ALERT_EXCEPTION_HANDLER = (thread, cause) -> {
        try {
            cause.printStackTrace();
            final Runnable showDialog = () -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("An unknown error occurred");
                alert.showAndWait();
            };
            if (Platform.isFxApplicationThread()) {
                showDialog.run();
            } else {
                FutureTask<Void> showDialogTask = new FutureTask<Void>(showDialog, null);
                Platform.runLater(showDialogTask);
                showDialogTask.get();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.exit(-1);
        }
    };
}
