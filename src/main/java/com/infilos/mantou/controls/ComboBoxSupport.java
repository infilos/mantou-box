package com.infilos.mantou.controls;

import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ComboBoxSupport {
    
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
    default <T> void enableRefreshComboValue(ComboBox<T> comboBox, Function<String,T> valueParser, Predicate<T> valueFilter) {
        comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (Objects.nonNull(newText)) {
                T value = valueParser.apply(newText);
                if (valueFilter.test(value)) {
                    comboBox.setValue(value);    
                }
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
            return valueParser.apply(value.toString());
        }
    }
}
