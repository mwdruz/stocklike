package org.test.stocklike.db.memory;

import org.springframework.stereotype.Repository;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;

@Repository
public class RepositoryInMem implements GatewayOffers {
    @Override
    public Object request() {
        return null;
    }
}
