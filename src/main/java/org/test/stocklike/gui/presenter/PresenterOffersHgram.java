package org.test.stocklike.gui.presenter;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;

@Component
public class PresenterOffersHgram implements ResponseBrokerOffersHgram {
    @Override
    public void accept(Object response) {
        LogManager.getLogger().info("accepts response");
    }
}
