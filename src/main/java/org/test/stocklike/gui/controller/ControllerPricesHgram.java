package org.test.stocklike.gui.controller;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.boundary.request.PricesHgramRequestBroker;
import org.test.stocklike.gui.viewmodel.ViewModelPricesHgram;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

@Component
public class ControllerPricesHgram {
    private final PricesHgramRequestBroker requestBroker;
    private final ViewModelPricesHgram model;
    
    public ControllerPricesHgram(ViewModelPricesHgram model,
                                 PricesHgramRequestBroker requestBroker)
    {
        this.requestBroker = requestBroker;
        this.model = model;
    }
    
    public void submitQuery()
    {
        final WebQuery webQuery = WebQuery.builder()
                                          .setQueryString(model.getQuery())
                                          .setStateNew(model.isCheckNew())
                                          .setTypeNow(model.isCheckNow())
                                          .build();
        
        final PricesHgramRequest request =
                PricesHgramRequest.builder()
                                  .setQuery(webQuery)
                                  .setCategories(model.getSelectedCategoriesList())
                                  .setXRangeMin(model.getXRangeMin())
                                  .setXRangeMax(model.getXRangeMax())
                                  .setBinWidth(model.getBinWidth())
                                  .build();
        LogManager.getLogger().info("send request");
        LogManager.getLogger().info(request);
        requestBroker.process(request);
    }
    
    public ObservableValue<Boolean> categoryListViewCellFactory(String item)
    {
        ObservableSet<String> selected = model.getSelectedCategories();
        BooleanProperty observable = new SimpleBooleanProperty();
        observable.addListener((obs, wasSelected, isSelected) -> {
            if (Boolean.TRUE.equals(isSelected)) selected.add(item);
            else selected.remove(item);
        });
        observable.set(selected.contains(item));
        selected.addListener((SetChangeListener<? super String>) change ->
                observable.set(selected.contains(item)));
        return observable;
    }
    
    public void toggleCategoriesVisibility()
    {
        model.setCategoriesVisible(!model.areCategoriesVisible());
    }
    
    public void resetCategoriesOnNewQuery(String oldQuery, String newQuery)
    {
        if (!newQuery.contains(oldQuery) || newQuery.equals(""))
            model.resetCategories();
    }
}
