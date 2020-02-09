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
import org.dockfx.DockNode;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ImageOutputProcessor extends Processor {
    private DockNode demoStage = null;
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

    private DockNode createWindow() {
        output = new ImageView();
        demoStage = new DockNode(output, "Output");
        demoStage.setFloating(true);
        return demoStage;
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        Mat image = inputs.read("Image");

        if(image == null)
            return new DataBundle();

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", image, buffer);

        if(demoStage == null) {
            demoStage = createWindow();
        }

        output.setImage(new Image(new ByteArrayInputStream(buffer.toArray())));
        return new DataBundle();
    }
}
