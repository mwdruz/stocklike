package org.test.stocklike.gui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageManager {
    
    private final List<Stage> stages = new ArrayList<>();
    private int currIndex = -1;
    
    public Scene getScene()
    {
        return currentStage().getScene();
    }
    
    private Stage currentStage()
    {
        return stages.get(currIndex);
    }
    
    public void init(Stage stage)
    {
        stages.add(stage);
        currIndex = 0;
    }
}
