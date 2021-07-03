package org.test.stocklike.gui.view;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.test.stocklike.gui.controller.ControllerOffersHgram;
import org.test.stocklike.gui.viewmodel.ViewModelOffersHgram;

@Component("mainView")
@Lazy
public class ViewOffersHgram implements View {
    private final ViewModelOffersHgram model;
    private final ControllerOffersHgram controller;
    private final TextField queryText;
    private final CheckBox newCheck;
    private final CheckBox nowCheck;
    private final BarChart<String, Number> barChart;
    private final TextField binWidthText;
    private final TextField xRangeMinText;
    private final TextField xRangeMaxText;
    private final TextField yRangeMinText;
    private final TextField yRangeMaxText;
    private final Scene scene;

    public Scene getScene() {
        return scene;
    }

    public ViewOffersHgram(ViewModelOffersHgram model, ControllerOffersHgram controller) {
        this.model = model;
        this.controller = controller;

        queryText = new TextField();
        newCheck = new CheckBox("new");
        nowCheck = new CheckBox("now");
        final var queryLabel = new Label("query");
        final var toolbarTop = new ToolBar(queryLabel, queryText, newCheck, nowCheck);
    
        final var xAxis = new CategoryAxis();
        final var yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
    
        final var binWidthLabel = new Label("bin width");
        binWidthText = new TextField();
        final var xRangeLabel = new Label("x range");
        xRangeMinText = new TextField();
        xRangeMaxText = new TextField();
        final var yRangeLabel = new Label("y range");
        yRangeMinText = new TextField();
        yRangeMaxText = new TextField();
        final var toolbarBot = new ToolBar(binWidthLabel, binWidthText,
                xRangeLabel, xRangeMinText, xRangeMaxText,
                yRangeLabel, yRangeMinText, yRangeMaxText);
    
        final var vBox = new VBox(toolbarTop, barChart, toolbarBot);
        final var pane = new BorderPane(vBox);
        scene = new Scene(pane);

        bindProperties();
    
        LogManager.getLogger().info("initializes controller");
        this.controller.initialize();
    }

    private void bindProperties() {
        model.bindQueryText(queryText);
        model.bindNewCheck(newCheck);
        model.bindNowCheck(nowCheck);
        model.bindBarChart(barChart);
        model.bindBinWidthText(binWidthText);
        model.bindXRangeMinText(xRangeMinText);
        model.bindXRangeMaxText(xRangeMaxText);
        model.bindYRangeMinText(yRangeMinText);
        model.bindYRangeMaxText(yRangeMaxText);
    }
}
