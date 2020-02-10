package com.visualipcv.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ImageWindow extends AnchorPane {
    @FXML
    private FreePane pane;

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

        image = new ImageView();
        image.setLayoutX(0);
        image.setLayoutY(0);

        pane.getInternalPane().getChildren().add(image);
    }

    public ImageView getImage() {
        return image;
    }
}
