package org.test.stocklike.gui.viewmodel;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

@Component
public final class ViewModelPricesHgram {
    private final SimpleStringProperty query = new SimpleStringProperty();
    private final SimpleListProperty<String> categories = new SimpleListProperty<>();
    private final SimpleBooleanProperty checkNew = new SimpleBooleanProperty();
    private final SimpleBooleanProperty checkNow = new SimpleBooleanProperty();
    private final SimpleDoubleProperty xRangeMin = new SimpleDoubleProperty();
    private final SimpleDoubleProperty xRangeMax = new SimpleDoubleProperty();
    private final SimpleDoubleProperty yRangeMin = new SimpleDoubleProperty();
    private final SimpleDoubleProperty yRangeMax = new SimpleDoubleProperty();
    private final SimpleDoubleProperty binWidth = new SimpleDoubleProperty();
    private final SimpleListProperty<XYChart.Series<String, Number>> dataList =
            new SimpleListProperty<>();
    
    private final SimpleStringProperty state = new SimpleStringProperty();
    private final SimpleStringProperty message = new SimpleStringProperty();
    private final ObservableSet<String> selectedCategories = FXCollections.observableSet();
    
    private final SimpleBooleanProperty areCategoriesVisible = new SimpleBooleanProperty();
    private final SimpleBooleanProperty queryFieldEditable = new SimpleBooleanProperty();
    
    public void bindQueryText(TextField queryText)
    {
        query.bindBidirectional(queryText.textProperty());
    }
    
    public void bindNewCheck(CheckBox newCheckBox)
    {
        checkNew.bindBidirectional(newCheckBox.selectedProperty());
    }
    
    public void bindNowCheck(CheckBox nowCheckBox)
    {
        checkNow.bindBidirectional(nowCheckBox.selectedProperty());
    }
    
    public void bindBarChart(BarChart<String, Number> barChart)
    {
        dataList.bindBidirectional(barChart.dataProperty());
        LogManager.getLogger().info("barChart data: {}", barChart.getData());
    }
    
    public void bindBinWidthText(TextField binWidthText)
    {
        binWidthText.textProperty().bindBidirectional(
                this.binWidth, new NumberStringConverter());
    }
    
    public void bindXRangeMinText(TextField xRangeMinText)
    {
        xRangeMinText.textProperty().bindBidirectional(
                this.xRangeMin, new NumberStringConverter());
    }
    
    public void bindXRangeMaxText(TextField xRangeMaxText)
    {
        xRangeMaxText.textProperty().bindBidirectional(
                this.xRangeMax, new NumberStringConverter());
    }
    
    public void bindYRangeMinText(TextField yRangeMinText)
    {
        yRangeMinText.textProperty().bindBidirectional(
                this.yRangeMin, new NumberStringConverter());
    }
    
    public void bindYRangeMaxText(TextField yRangeMaxText)
    {
        yRangeMaxText.textProperty().bindBidirectional(
                this.yRangeMax, new NumberStringConverter());
    }
    
    public void bindStateText(Label stateText)
    {
        state.bindBidirectional(stateText.textProperty());
    }
    
    public void bindMessageText(Text messageText)
    {
        message.bindBidirectional(messageText.textProperty());
    }
    
    public void bindCategoryListView(ListView<String> categoryListView)
    {
        categories.bindBidirectional(categoryListView.itemsProperty());
    }
    
    public String getQuery()
    {
        return query.get();
    }
    
    public ObservableList<String> getCategories()
    {
        return FXCollections.observableList(categories.get());
    }
    
    public boolean isCheckNew()
    {
        return checkNew.get();
    }
    
    public boolean isCheckNow()
    {
        return checkNow.get();
    }
    
    public double getXRangeMin()
    {
        return xRangeMin.get();
    }
    
    public double getXRangeMax()
    {
        return xRangeMax.get();
    }
    
    public double getYRangeMin()
    {
        return yRangeMin.get();
    }
    
    public double getYRangeMax()
    {
        return yRangeMax.get();
    }
    
    public double getBinWidth()
    {
        return binWidth.get();
    }
    
    public ObservableList<XYChart.Series<String, Number>> getDataList()
    {
        return FXCollections.observableList(dataList.get());
    }
    
    public void setQuery(String query)
    {
        this.query.set(query);
    }
    
    public void setCheckNew(boolean checkNew)
    {
        this.checkNew.set(checkNew);
    }
    
    public void setCheckNow(boolean checkNow)
    {
        this.checkNow.set(checkNow);
    }
    
    public void setXRangeMin(double xRangeMin)
    {
        this.xRangeMin.set(xRangeMin);
    }
    
    public void setXRangeMax(double xRangeMax)
    {
        this.xRangeMax.set(xRangeMax);
    }
    
    public void setYRangeMin(double yRangeMin)
    {
        this.yRangeMin.set(yRangeMin);
    }
    
    public void setYRangeMax(double yRangeMax)
    {
        this.yRangeMax.set(yRangeMax);
    }
    
    public void setBinWidth(double binWidth)
    {
        this.binWidth.set(binWidth);
    }
    
    public void setDataList(ObservableList<XYChart.Series<String, Number>> dataList)
    {
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }
    
    public void setState(String state)
    {
        this.state.set(state);
    }
    
    public void setMessage(String message)
    {
        this.message.set(message);
    }
    
    public ObservableSet<String> getSelectedCategories()
    {
        return FXCollections.observableSet(selectedCategories);
    }
    
    public List<String> getSelectedCategoriesList()
    {
        return selectedCategories.stream().toList();
    }
    
    public void bindCategoriesVisibility(VBox categoriesVBox)
    {
        areCategoriesVisible.bindBidirectional(categoriesVBox.visibleProperty());
    }
    
    public void setCategoriesVisible(boolean value)
    {
        this.areCategoriesVisible.set(value);
    }
    
    public boolean areCategoriesVisible()
    {
        return areCategoriesVisible.get();
    }
    
    public void resetCategories()
    {
        categories.clear();
        selectedCategories.clear();
    }
    
    public void resetDataList()
    {
        dataList.clear();
    }
    
    public void resetMessage()
    {
        this.message.set("");
    }
    
    public void freezeQueryField()
    {
        queryFieldEditable.set(false);
    }
    
    public void thawQueryField()
    {
        queryFieldEditable.set(true);
    }
    
    public void bindQueryReadOnly(TextField queryText)
    {
        queryFieldEditable.bindBidirectional(queryText.editableProperty());
    }
}
