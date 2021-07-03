package org.test.stocklike.domain.boundary.request;

import org.test.stocklike.domain.boundary.dto.OffersHgramRequest;

public interface RequestBrokerOffersHgram {
    void loadOffersHgram(OffersHgramRequest request);
}
