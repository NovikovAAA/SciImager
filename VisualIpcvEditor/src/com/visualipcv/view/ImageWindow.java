package com.visualipcv.view;

import com.visualipcv.controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ImageWindow extends Controller<AnchorPane> {
    @FXML
    private FreePane pane;

    private ImageView image;

    public ImageWindow() {
        super(AnchorPane.class, "ImageWindow.fxml");

        image = new ImageView();
        image.setLayoutX(0);
        image.setLayoutY(0);

        pane.getInternalPane().getChildren().add(image);
    }

    public ImageView getImage() {
        return image;
    }
}
