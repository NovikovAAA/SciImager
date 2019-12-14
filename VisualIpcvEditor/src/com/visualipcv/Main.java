package com.visualipcv;

import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.NodeView;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.toString());

    static
    {
        System.loadLibrary("ext/opencv_world411d");
        System.loadLibrary("ext/VisualIPCV");
        System.loadLibrary("ext/VisualIpcvJava");
    }

    private static ProcessorLibrary processorLibrary = new ProcessorLibrary();

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = buildFrame();
                //frame.setLayout(null);
                //GraphView canvas = new GraphView();
                //NodeView view = new NodeView(processorLibrary.getProcessors()[0]);
                //view.setLocation(10 + canvas.getGraphWidth() / 2, 10 + canvas.getGraphHeight() / 2);
                //canvas.add(view);
                //frame.add(canvas);

                FunctionListView list = new FunctionListView(processorLibrary);
                frame.add(list);
            }
        });
    }

    private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("VisualIPCV");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setVisible(true);
        return frame;
    }
}
