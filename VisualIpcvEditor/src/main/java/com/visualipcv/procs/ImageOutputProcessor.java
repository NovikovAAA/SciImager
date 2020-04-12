package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorCommand;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.editor.Editor;
import com.visualipcv.controller.ImageWindow;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import com.visualipcv.view.docking.DockNode;
import com.visualipcv.view.docking.DockPos;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class ImageOutputProcessor extends Processor {
    public ImageOutputProcessor() {
        super(new ProcessorBuilder()
            .setName("ImageOutput")
            .setModule("Core")
            .setCategory("Output")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE)));

        addCommand(new ProcessorCommand() {
            @Override
            public void execute(DataBundle state) {
                showWindow(state);
            }

            @Override
            public String getName() {
                return "Open window";
            }
        });
    }

    private void showWindow(DataBundle state) {
        DockNode stage = state.read("Stage");
        ImageWindow window = state.read("Image");

        if(stage.isDocked() || stage.isFloating())
            return;

        stage.addTab(window, new Tab("Output", window.getView()));
        stage.dock(Editor.getPrimaryPane(), DockPos.RIGHT);
    }

    private DockNode createWindow(DataBundle state) {
        ImageWindow window = new ImageWindow();
        DockNode demoStage = new DockNode(window, new Tab("Output", window.getView()));
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
