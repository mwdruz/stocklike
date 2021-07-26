package org.test.stocklike.domain.boundary.gateway;

import java.util.List;

import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.entity.OfferPrice;

import io.vavr.control.Either;

public interface GatewayOffers {
    Either<String, List<OfferCategory>> findCategoriesForQuery(String query);
    
    
    Either<String, List<OfferPrice>> findPricesInCategories(List<String> categoryNames,
                                                            String query,
                                                            boolean checkNow,
                                                            boolean checkNew,
                                                            double minPrice,
                                                            double maxPrice);

    default Either<String, List<OfferPrice>> findPrices(String query, boolean checkNow,
                                                               boolean checkNew, double minPrice,
                                                               double maxPrice)
    {
        return findPricesInCategories(List.of(), query, checkNow, checkNew, minPrice, maxPrice);
    }
}
