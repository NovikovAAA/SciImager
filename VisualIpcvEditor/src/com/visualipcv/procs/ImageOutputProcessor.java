package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.editor.Editor;
import com.visualipcv.view.ImageWindow;
import javafx.scene.image.Image;
import com.visualipcv.view.docking.DockNode;
import com.visualipcv.view.docking.DockPos;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ImageOutputProcessor extends Processor {
    public ImageOutputProcessor() {
        super("ImageOutput", "Core", "Output",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                    }
                },
                new ArrayList<>());
    }

    private DockNode createWindow(DataBundle state) {
        ImageWindow window = new ImageWindow();
        DockNode demoStage = new DockNode(window, "Output", null);
        demoStage.setPrefWidth(500.0);
        demoStage.setPrefHeight(500.0);
        demoStage.setLayoutX(10.0);
        demoStage.setLayoutY(10.0);
        demoStage.dock(Editor.getPrimaryPane(), DockPos.RIGHT);

        state.write("Stage", demoStage);
        state.write("Image", window);

        return demoStage;
    }

    private void destroyWindow(DataBundle state) {
        DockNode demoStage = state.read("Stage");
        demoStage.close();
        state.clear();
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");

        if(image == null)
            return new DataBundle();

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", image, buffer);

        DockNode demoStage = state.read("Stage");
        ImageWindow output = state.read("Image");

        output.getImage().setImage(new Image(new ByteArrayInputStream(buffer.toArray())));
        return new DataBundle();
    }

    @Override
    public void onCreated(DataBundle state) {
        createWindow(state);
    }

    @Override
    public void onDestroyed(DataBundle state) {
        destroyWindow(state);
    }
}
