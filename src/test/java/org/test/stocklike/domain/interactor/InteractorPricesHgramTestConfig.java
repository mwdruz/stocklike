package org.test.stocklike.domain.interactor;

import static io.vavr.API.Tuple;

import java.util.List;

import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;

class InteractorPricesHgramTestConfig {
    String errorMessage() { return "error message"; }
    
    String catName1() { return "category 1"; }
    
    String catName2() { return "category 2"; }
    
    Category category1() { return new Category("1", "category1", 0, str -> "link1"); }
    
    Category category2() { return new Category("2", "category2", 0, str -> "link2"); }
    
    List<Category> categoriesSingle() { return List.of(category1()); }
    
    List<Category> categoriesMany() { return List.of(category1(), category2()); }
    
    private static final WebQuery WHATEVER_QUERY = WebQuery.builder()
                                                           .setQueryString("whatever query")
                                                           .build();
    
    PricesHgramRequest requestWithoutCategories()
    {
        return PricesHgramRequest.builder()
                                 .setQuery(WHATEVER_QUERY)
                                 .build();
    }
    
    PricesHgramRequest requestWithCategories()
    {
        return PricesHgramRequest.builder()
                                 .setQuery(WHATEVER_QUERY)
                                 .setCategories(List.of(catName1(), catName2()))
                                 .build();
    }
    
    List<Price> pricesReturned() { return List.of(new Price(1), new Price(2), new Price(3)); }
    
    Hgram hgramReturned() { return Hgram.fromList(1.0, List.of(Tuple(1.0, 3), Tuple(2.0, 5))); }
    
    InteractorPricesHgramTestConfig() { }
}
