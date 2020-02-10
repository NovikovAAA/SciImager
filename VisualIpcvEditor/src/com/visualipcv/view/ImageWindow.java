package com.visualipcv.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ImageWindow extends AnchorPane {
    @FXML
    private ImageView image;

    public ImageWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ImageWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ImageView getImage() {
        return image;
    }
}
