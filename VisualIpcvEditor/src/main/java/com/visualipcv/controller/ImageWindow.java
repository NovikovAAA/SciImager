package com.visualipcv.controller;

import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.view.FreePane;
import com.visualipcv.view.docking.DockPos;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@EditorWindow(path = "", name = "Image output", dockPos = DockPos.LEFT, prefWidth = 600.0, prefHeight = 400.0)
public class ImageWindow extends Controller<AnchorPane> {
    @FXML
    private Label saveButton;
    @FXML
    private ImageView image;
    @FXML
    private StackPane imageContainer;
    @FXML
    private ScrollPane scroll;

    public ImageWindow() {
        super(AnchorPane.class, "ImageWindow.fxml");

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

    public void setImage(Image image) {
        this.image.setImage(image);

        double width = image.getWidth() + 200.0;
        double height = image.getHeight() + 200.0;
        width = Math.max(scroll.getWidth(), width);
        height = Math.max(scroll.getHeight(), height);

        imageContainer.setPrefWidth(width);
        imageContainer.setPrefHeight(height);
    }
}
