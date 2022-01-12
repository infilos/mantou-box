package com.infilos.mantou.controls;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.stage.Stage;

public interface NotifySupport {
    
    Stage mainStage();
    
    default void showInfoMessage(String title, String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showInfoMessage(title, message);
        }
    }
    
    default void showInfoMessage(String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showInfoMessage(message);
        }
    }
    
    default void showWarningMessage(String title, String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showWarningMessage(title, message);
        }
    }
    
    default void showWarningMessage(String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showWarningMessage(message);
        }
    }
    
    default void showErrorMessage(String title, String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showErrorMessage(title, message);
        }
    }
    
    default void showErrorMessage(String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showErrorMessage(message);
        }
    }
    
    default void showMessage(String title, String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showMessage(title, message);
        }
    }

    default void showMessage(String message) {
        if (FXTrayIcon.isSupported()) {
            FXTrayIcon trayIcon = new FXTrayIcon.Builder(mainStage()).build();
            trayIcon.showMessage(message);
        }
    }
}
