package org.test.stocklike.domain.boundary.gateway;

import java.util.List;
import java.util.function.Predicate;

import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Price;

import io.vavr.control.Either;

public interface GatewayOffers {
    Either<String, List<Category>> findCategoriesForQuery(WebQuery query);
    
    Either<String, List<Price>> findPricesWithCatFilter(Predicate<String> catNameFilter,
                                                        WebQuery query,
                                                        double minPrice,
                                                        double maxPrice);
    
    default Either<String, List<Price>> findPrices(WebQuery query,
                                                   double minPrice,
                                                   double maxPrice)
    {
        return findPricesWithCatFilter(cat -> true, query, minPrice, maxPrice);
    }
}
