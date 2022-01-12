package com.infilos.mantou.controls;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public interface TabPaneSupport {
    
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    default void makeTabPaneStretched(TabPane tab, ReadOnlyDoubleProperty parentWidthProperty, ReadOnlyDoubleProperty parentHeightProperty) {
        parentWidthProperty.addListener((value, oldWidth, newWidth) -> {
            Side side = tab.getSide();
            int numTabs = tab.getTabs().size();

            if ((side == Side.BOTTOM || side == Side.TOP) && numTabs != 0) {
                tab.setTabMinWidth(newWidth.intValue() / numTabs - (20));
                tab.setTabMaxWidth(newWidth.intValue() / numTabs - (20));
            }
        });

        parentHeightProperty.addListener((value, oldHeight, newHeight) -> {
            Side side = tab.getSide();
            int numTabs = tab.getTabs().size();

            if ((side == Side.LEFT || side == Side.RIGHT) && numTabs != 0) {
                tab.setTabMinWidth(newHeight.intValue() / numTabs - (20));
                tab.setTabMaxWidth(newHeight.intValue() / numTabs - (20));
            }
        });

        tab.getTabs().addListener((ListChangeListener<Tab>) change -> {
            Side side = tab.getSide();
            int numTabs = tab.getTabs().size();

            if (numTabs != 0) {
                if (side == Side.LEFT || side == Side.RIGHT) {
                    tab.setTabMinWidth(parentHeightProperty.intValue() / numTabs - (20));
                    tab.setTabMaxWidth(parentHeightProperty.intValue() / numTabs - (20));
                }
                if (side == Side.BOTTOM || side == Side.TOP) {
                    tab.setTabMinWidth(parentWidthProperty.intValue() / numTabs - (20));
                    tab.setTabMaxWidth(parentWidthProperty.intValue() / numTabs - (20));
                }
            }
        });
    }
}
