package org.test.stocklike.gui.controller;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.boundary.request.RequestBrokerOffersHgram;
import org.test.stocklike.domain.boundary.dto.OffersHgramRequest;
import org.test.stocklike.gui.viewmodel.ViewModelOffersHgram;

@Component
public class ControllerOffersHgram {
    private final RequestBrokerOffersHgram requestBroker;
    private final ViewModelOffersHgram model;
    
    public ControllerOffersHgram(ViewModelOffersHgram model,
                                 RequestBrokerOffersHgram requestBroker)
    {
        this.requestBroker = requestBroker;
        this.model = model;
    }
    
    public void initialize()
    {
        final OffersHgramRequest request =
                OffersHgramRequest.builder()
                                  .setQuery(model.getQuery())
                                  .setCheckNew(model.isCheckNew())
                                  .setCheckNow(model.isCheckNow())
                                  .setXRangeMin(model.getXRangeMin())
                                  .setXRangeMax(model.getXRangeMax())
                                  .setYRangeMin(model.getYRangeMin())
                                  .setYRangeMax(model.getYRangeMax())
                                  .setBinWidth(model.getBinWidth())
                                  .build();
        LogManager.getLogger().info("sends request");
        requestBroker.loadOffersHgram(request);
    }
}
