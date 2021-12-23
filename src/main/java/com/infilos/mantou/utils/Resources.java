package com.infilos.mantou.utils;

import javafx.fxml.FXMLLoader;

import java.io.InputStream;
import java.net.URL;

public final class Resources {
    private Resources() {
    }

    public static URL loadURL(String path) {
        return Resources.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return Resources.class.getResourceAsStream(name);
    }

    public static URL ofUrl(String resource) {
        return Resources.class.getResource(resource);
    }

    public static InputStream ofStream(String resource) {
        return Resources.class.getResourceAsStream(resource);
    }

    public static FXMLLoader ofFxml(String resource) {
        return new FXMLLoader(ofUrl(resource));
        
    }
}
