package com.infilos.mantou.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;

public interface AwareResource {

    default URL readAsUrl(String resource) {
        return getClass().getResource(resource);
    }

    default String readAsUrlString(String resource) {
        return Objects.requireNonNull(getClass().getResource(resource)).toExternalForm();
    }
    
    default String loadStyle(String resource) {
        return Objects.requireNonNull(getClass().getResource("/style/" + resource)).toExternalForm();
    }
    
    default Image loadImage(String resource) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/" + resource)));
    }
    
    default ImageView loadImageView(String resource) {
        return new ImageView(loadImage(resource));
    }
}
