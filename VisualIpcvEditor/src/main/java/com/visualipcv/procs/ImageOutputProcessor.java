package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.GraphExecutionData;
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
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE)));

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
        ImageWindow window = state.read("Image");

        if(window == null) {
            window = new ImageWindow();
            window.setContext(state);
            state.write("Image", window);
        }

        Editor.openWindow(window);
        window.setImage(state.read("Preview"));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");

        if(image == null) {
            state.write("Preview", null);

            ImageWindow output = state.read("Image");

            if(output != null) {
                output.setImage(null);
            }

            return new DataBundle();
        }

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", image, buffer);

        state.write("Preview", new Image(new ByteArrayInputStream(buffer.toArray())));
        ImageWindow output = state.read("Image");

        if(output != null) {
            output.setImage(state.read("Preview"));
        }

        return new DataBundle();
    }
}
