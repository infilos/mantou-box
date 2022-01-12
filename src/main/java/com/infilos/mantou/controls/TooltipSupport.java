package com.infilos.mantou.controls;

import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public interface TooltipSupport {

    default void showInfoTooltip(Control control, String message, int displaySeconds) {
        // green
        showTooltip(
            control,
            message,
            displaySeconds,
            "-fx-font-family: Ubuntu BoldItalic; -fx-font-size: 14pt; -fx-background-color: #2E2E2E; -fx-text-fill: #F26D50;"
        );
    }

    default void showWarnTooltip(Control control, String message, int displaySeconds) {
        // yellow
        showTooltip(
            control,
            message,
            displaySeconds,
            "-fx-font-family: Ubuntu BoldItalic; -fx-font-size: 14pt; -fx-background-color: #2E2E2E; -fx-text-fill: #5F996D;"
        );
    }
    
    default void showErrorTooltip(Control control, String message, int displaySeconds) {
        // red
        showTooltip(
            control,
            message,
            displaySeconds,
            "-fx-font-family: Ubuntu BoldItalic; -fx-font-size: 14pt; -fx-background-color: #2E2E2E; -fx-text-fill: #F17C37;"
        );
    }

    default void showTooltip(Control control, String message, int displaySeconds, String style) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(message);
        tooltip.setAutoHide(false);
        tooltip.setStyle(style);

        Bounds bounds = control.localToScreen(control.getBoundsInLocal());

        // show at right-top corner
        tooltip.show(
            control,
            bounds.getMaxX(),
            bounds.getMinY() - control.getHeight()
        );

        PauseTransition pt = new PauseTransition(Duration.millis(displaySeconds * 1000));
        pt.setOnFinished(e -> {
            tooltip.hide();
        });
        pt.play();
    }
}
