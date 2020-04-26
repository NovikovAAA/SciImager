package com.visualipcv.view;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class NormalStage extends Stage {
    private static List<Stage> stages = new ArrayList<>();

    public NormalStage() {
        stages.add(this);
        setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                stages.remove(NormalStage.this);
            }
        });
    }

    public static List<Stage> getStages() {
        return stages;
    }

    public static void addPrimaryStage(Stage stage) {
        stages.add(stage);
    }
}
