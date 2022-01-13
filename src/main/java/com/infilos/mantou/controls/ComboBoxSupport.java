package com.infilos.mantou.controls;

import com.infilos.utils.Loggable;
import javafx.scene.control.ComboBox;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ComboBoxSupport extends Loggable {

    default <T> void buildComboItems(ComboBox<T> comboBox, int initIdx, List<T> items) {
        comboBox.getItems().setAll(items);
        comboBox.setValue(items.get(initIdx));
        comboBox.getSelectionModel().select(initIdx);
    }

    default <T> void buildComboItems(ComboBox<T> comboBox, List<T> items) {
        comboBox.getItems().setAll(items);
        comboBox.setValue(items.get(0));
        comboBox.getSelectionModel().select(0);
    }

    default <T> void buildComboItems(ComboBox<T> comboBox, int initIdx, T... items) {
        comboBox.getItems().setAll(items);
        comboBox.setValue(items[initIdx]);
        comboBox.getSelectionModel().select(initIdx);
    }

    default <T> void buildComboItems(ComboBox<T> comboBox, T... items) {
        comboBox.getItems().setAll(items);
        comboBox.setValue(items[0]);
        comboBox.getSelectionModel().select(0);
    }

    /**
     * Refresh value after combo-box selected or edited.
     */
    default <T> void enableRefreshComboValue(ComboBox<T> comboBox, Function<String, T> valueParser, T fallback) {
        comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (StringUtils.isNotBlank(newText)) {
                comboBox.setValue(tryIgnore(() -> valueParser.apply(newText), fallback));
            }
        });
    }

    /**
     * Get refreshed combo-box's value by type.
     */
    default <T> T comboValueOf(ComboBox<T> comboBox, Class<T> valueType, Function<String, T> valueParser) {
        Object value = comboBox.getValue();

        if (value.getClass() == value) {
            return valueType.cast(value);
        } else {
            return tryIgnore(
                () -> valueParser.apply(value.toString()),
                valueType,
                valueParser
            );
        }
    }

    private <T> T tryIgnore(Supplier<T> supplier, T fallback) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return fallback;
        }
    }

    private <T> T tryIgnore(Supplier<T> supplier, Class<T> valueType, Function<String, T> valueParser) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return defaultValueOf(valueType, valueParser);
        }
    }

    private <T> T defaultValueOf(Class<T> type, Function<String, T> valueParser) {
        if (Number.class.isAssignableFrom(type)) {
            return valueParser.apply("0");
        }

        return valueParser.apply("");
    }
}
