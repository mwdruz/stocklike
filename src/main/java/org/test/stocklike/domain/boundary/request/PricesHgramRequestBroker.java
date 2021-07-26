package org.test.stocklike.domain.boundary.request;

import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;

public interface PricesHgramRequestBroker {
    void process(PricesHgramRequest request);
}
