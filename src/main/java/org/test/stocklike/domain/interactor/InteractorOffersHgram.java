package org.test.stocklike.domain.interactor;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.request.RequestBrokerOffersHgram;
import org.test.stocklike.domain.boundary.dto.OffersHgramRequest;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;

@Service
public class InteractorOffersHgram implements RequestBrokerOffersHgram {
    private final ResponseBrokerOffersHgram responseBroker;
    private final GatewayOffers gateway;

    public InteractorOffersHgram(ResponseBrokerOffersHgram responseBroker,
                                 GatewayOffers gateway) {
        this.responseBroker = responseBroker;
        this.gateway = gateway;
    }

    @Override
    public void loadOffersHgram(OffersHgramRequest request) {
        LogManager.getLogger().info("accepts request");
        LogManager.getLogger().info("calls data gateway");
        final var response = gateway.request();
        LogManager.getLogger().info("sends response");
        responseBroker.accept(response);
    }
}
