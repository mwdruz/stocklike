package org.test.stocklike.gui.presenter;

import java.util.List;

import org.springframework.stereotype.Component;
import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.gui.viewmodel.ViewModelPricesHgram;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

@Component
public class PresenterPricesHgram implements ResponseBrokerOffersHgram {
    private final ViewModelPricesHgram model;
    
    public PresenterPricesHgram(ViewModelPricesHgram model) { this.model = model; }
    
    @Override
    public void adviseState(State state) { model.setState(state.toString()); }
    
    @Override
    public void accept(PricesHgramResponse response)
    {
        model.resetMessage();
        switch (response.getType()) {
            case CATEGORIES -> acceptCategories(response.getCategories());
            case HGRAM -> acceptHgram(response.getHgram());
            case ERROR -> acceptError(response.getMessage());
        }
    }
    
    private void acceptCategories(List<String> categories)
    {
        model.getCategories().clear();
        model.getCategories().addAll(categories);
        model.setCategoriesVisible(true);
        model.freezeQueryField();
    }
    
    private void acceptHgram(Hgram hgram)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> data = series.getData();
        hgram.getLabeledList().forEach(tuple -> data.add(
                new XYChart.Data<>(tuple._1, tuple._2)));
        model.setBinWidth(hgram.getBinWidth());
        model.getDataList().clear();
        model.getDataList().add(series);
        model.thawQueryField();
    }
    
    private void acceptError(String message)
    {
        model.setMessage(message);
        model.thawQueryField();
        model.resetDataList();
    }
}
