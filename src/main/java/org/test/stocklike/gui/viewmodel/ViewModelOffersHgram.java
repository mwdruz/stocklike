package org.test.stocklike.gui.viewmodel;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import org.springframework.stereotype.Component;

@Component
public final class ViewModelOffersHgram {
    private final SimpleStringProperty query = new SimpleStringProperty();
    private final SimpleBooleanProperty checkNew = new SimpleBooleanProperty();
    private final SimpleBooleanProperty checkNow = new SimpleBooleanProperty();
    private final SimpleDoubleProperty xRangeMin = new SimpleDoubleProperty();
    private final SimpleDoubleProperty xRangeMax = new SimpleDoubleProperty();
    private final SimpleDoubleProperty yRangeMin = new SimpleDoubleProperty();
    private final SimpleDoubleProperty yRangeMax = new SimpleDoubleProperty();
    private final SimpleDoubleProperty binWidth = new SimpleDoubleProperty();
    private final SimpleListProperty<XYChart.Series<String, Number>> dataList =
            new SimpleListProperty<>();

    public void bindQueryText(TextField queryText) {
        queryText.textProperty().bindBidirectional(this.query);
    }

    public void bindNewCheck(CheckBox newCheckBox) {
        newCheckBox.selectedProperty().bindBidirectional(this.checkNew);
    }

    public void bindNowCheck(CheckBox nowCheckBox) {
        nowCheckBox.selectedProperty().bindBidirectional(this.checkNow);
    }

    public void bindBarChart(BarChart<String, Number> barChart) {
        barChart.dataProperty().bindBidirectional(this.dataList);
    }

    public void bindBinWidthText(TextField binWidth) {
        binWidth.textProperty().bindBidirectional(
                this.binWidth, new NumberStringConverter());
    }

    public void bindXRangeMinText(TextField xRangeMinText) {
        xRangeMinText.textProperty().bindBidirectional(
                this.xRangeMin, new NumberStringConverter());
    }

    public void bindXRangeMaxText(TextField xRangeMaxText) {
        xRangeMaxText.textProperty().bindBidirectional(
                this.xRangeMax, new NumberStringConverter());
    }

    public void bindYRangeMinText(TextField yRangeMinText) {
        yRangeMinText.textProperty().bindBidirectional(
                this.yRangeMin, new NumberStringConverter());
    }

    public void bindYRangeMaxText(TextField yRangeMaxText) {
        yRangeMaxText.textProperty().bindBidirectional(
                this.yRangeMax, new NumberStringConverter());
    }

    public String getQuery() {
        return query.get();
    }

    public boolean isCheckNew() {
        return checkNew.get();
    }

    public boolean isCheckNow() {
        return checkNow.get();
    }

    public double getXRangeMin() {
        return xRangeMin.get();
    }

    public double getXRangeMax() {
        return xRangeMax.get();
    }

    public double getYRangeMin() {
        return yRangeMin.get();
    }

    public double getYRangeMax() {
        return yRangeMax.get();
    }

    public double getBinWidth() {
        return binWidth.get();
    }

    public ObservableList<XYChart.Series<String, Number>> getDataList() {
        return dataList.get();
    }
}
