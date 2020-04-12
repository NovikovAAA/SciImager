package com.visualipcv.controller;

import com.visualipcv.editor.Editor;
import com.visualipcv.view.FreePane;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageWindow extends Controller<AnchorPane> {
    @FXML
    private FreePane pane;
    @FXML
    private Label saveButton;

    private ImageView image;

    public ImageWindow() {
        super(AnchorPane.class, "ImageWindow.fxml");

        image = new ImageView();
        image.setLayoutX(0);
        image.setLayoutY(0);

        pane.getInternalPane().getChildren().add(image);

        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Image image = ImageWindow.this.image.getImage();

                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("png image", ".png");
                FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("jpeg image", ".jpeg");
                FileChooser.ExtensionFilter filter3 = new FileChooser.ExtensionFilter("bpm image", ".bmp");
                chooser.getExtensionFilters().addAll(filter1, filter2, filter3);
                File file = chooser.showSaveDialog(Editor.getPrimaryStage().getScene().getWindow());

                if(file == null) {
                    return;
                }

                String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), ext, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ImageView getImage() {
        return image;
    }
}
