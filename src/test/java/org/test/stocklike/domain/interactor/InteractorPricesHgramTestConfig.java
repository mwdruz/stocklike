package org.test.stocklike.domain.interactor;

import java.util.List;
import java.util.Map;

import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.entity.OfferPrice;

class InteractorPricesHgramTestConfig {
    String errorMessage() { return "error message"; }
    
    String catName1() { return "category 1"; }
    
    String catName2() { return "category 2"; }
    
    OfferCategory category1() { return new OfferCategory(List.of(1), catName1()); }
    
    OfferCategory category2() { return new OfferCategory(List.of(2), catName2()); }
    
    List<OfferCategory> categoriesSingle() { return List.of(category1()); }
    
    List<OfferCategory> categoriesMany() { return List.of(category1(), category2()); }
    
    PricesHgramRequest requestWithoutCategories()
    {
        return
                PricesHgramRequest.builder()
                                  .setQuery("whatever query")
                                  .build();
    }
    
    PricesHgramRequest requestWithCategories()
    {
        return
                PricesHgramRequest.builder()
                                  .setQuery("whatever query")
                                  .setCategories(List.of(catName1(), catName2()))
                                  .build();
    }
    
    List<OfferPrice> pricesReturned()
    {
        return List.of(new OfferPrice(1), new OfferPrice(2), new OfferPrice(3));
    }
    
    Map<String, Double> hgramReturned()
    {
        return Map.of("1", 3.4, "2", 5.6);
    }
    
    
    InteractorPricesHgramTestConfig() { }
}
