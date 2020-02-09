package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ImageOutputProcessor extends Processor {
    private Stage demoStage = null;
    private ImageView output = null;

    public ImageOutputProcessor() {
        super("ImageOutput", "Core", "Output",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                    }
                },
                new ArrayList<>());
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        Mat image = inputs.read("Image");

        if(image == null)
            return new DataBundle();

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", image, buffer);

        if(demoStage == null) {
            demoStage = new Stage();
            demoStage.setTitle("Output");
            demoStage.setAlwaysOnTop(true);
            demoStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    demoStage = null;
                }
            });

            output = new ImageView();
            Pane root = new Pane();
            root.getChildren().add(output);
            demoStage.setScene(new Scene(root, image.width(), image.height()));
            demoStage.show();
        }

        output.setImage(new Image(new ByteArrayInputStream(buffer.toArray())));
        return new DataBundle();
    }
}
