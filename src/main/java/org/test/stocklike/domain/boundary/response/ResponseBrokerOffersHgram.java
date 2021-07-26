package org.test.stocklike.domain.boundary.response;

import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.state.State;

public interface ResponseBrokerOffersHgram {
    void adviseState(State state);
    
    void accept(PricesHgramResponse response);
}
