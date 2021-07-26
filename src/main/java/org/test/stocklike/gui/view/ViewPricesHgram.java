package org.test.stocklike.gui.view;

import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.test.stocklike.gui.controller.ControllerPricesHgram;
import org.test.stocklike.gui.viewmodel.ViewModelPricesHgram;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

@Component("mainView")
@Lazy
public class ViewPricesHgram implements View {
    private final ViewModelPricesHgram model;
    private final TextField queryText;
    private final CheckBox newCheck;
    private final CheckBox nowCheck;
    private final ListView<String> categoryListView;
    private final BarChart<String, Number> barChart;
    private final TextField binWidthText;
    private final TextField xRangeMinText;
    private final TextField xRangeMaxText;
    private final TextField yRangeMinText;
    private final TextField yRangeMaxText;
    private final Scene scene;
    private final Label stateText;
    private final Text messageText;
    private final VBox categoriesVBox;
    
    public Scene getScene()
    {
        return scene;
    }
    
    public ViewPricesHgram(ViewModelPricesHgram model, ControllerPricesHgram controller)
    {
        LogManager.getLogger().info("initialize view");
        
        this.model = model;
        
        queryText = new TextField();
        newCheck = new CheckBox("new");
        nowCheck = new CheckBox("now");
        final var toggleCategoriesButton = new Button("categories");
        final var rightAlignmentHBox = new HBox(toggleCategoriesButton);
        rightAlignmentHBox.setAlignment(Pos.BASELINE_RIGHT);
        HBox.setHgrow(rightAlignmentHBox, Priority.ALWAYS);
        final var queryLabel = new Label("query");
        final var toolbarTop = new ToolBar(queryLabel, queryText, newCheck, nowCheck,
                                           rightAlignmentHBox);
        
        categoryListView = new ListView<>();
        categoryListView.setCellFactory(CheckBoxListCell.forListView(
                controller::categoryListViewCellFactory));
        final var refineButton = new Button("update");
        refineButton.widthProperty();
        categoriesVBox = new VBox(categoryListView, refineButton);
        final var xAxis = new CategoryAxis();
        xAxis.setAnimated(false);
        final var yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        HBox.setHgrow(barChart, Priority.ALWAYS);
        final var centralHBox = new HBox(barChart, categoriesVBox);
        VBox.setVgrow(centralHBox, Priority.ALWAYS);
        
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
        stateText = new Label();
        messageText = new Text();
        
        final var vBox = new VBox(toolbarTop, centralHBox, toolbarBot, stateText, messageText);
        final var pane = new BorderPane(vBox);
        scene = new Scene(pane);
        
        messageText.wrappingWidthProperty().bind(scene.widthProperty());
        categoriesVBox.managedProperty().bindBidirectional(categoriesVBox.visibleProperty());
        categoriesVBox.setVisible(false);
        
        bindProperties();
        
        toggleCategoriesButton.setOnAction(
                event -> controller.toggleCategoriesVisibility());
        queryText.setOnAction(event -> controller.submitQuery());
        queryText.textProperty().addListener(
                (obs, oldQ, newQ) -> controller.resetCategoriesOnNewQuery(oldQ, newQ));
        refineButton.setOnAction(event -> {
            model.setCategoriesVisible(false);
            controller.submitQuery();
        });
    }
    
    private void bindProperties()
    {
        model.bindQueryText(queryText);
        model.bindNewCheck(newCheck);
        model.bindNowCheck(nowCheck);
        model.bindBarChart(barChart);
        model.bindBinWidthText(binWidthText);
        model.bindXRangeMinText(xRangeMinText);
        model.bindXRangeMaxText(xRangeMaxText);
        model.bindYRangeMinText(yRangeMinText);
        model.bindYRangeMaxText(yRangeMaxText);
        model.bindStateText(stateText);
        model.bindMessageText(messageText);
        model.bindCategoryListView(categoryListView);
        model.bindCategoriesVisibility(categoriesVBox);
        model.bindQueryReadOnly(queryText);
    }
}
