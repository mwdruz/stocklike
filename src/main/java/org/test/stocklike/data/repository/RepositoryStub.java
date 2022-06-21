package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Offer;
import org.test.stocklike.domain.entity.Price;

import io.vavr.control.Either;

@Repository
@Profile("stub")
class RepositoryStub implements GatewayOffers {
    
    private UnaryOperator<String> createLinkMaker(String cat)
    {
        return str -> "linkTo=" + cat;
    }
    
    private final Category category1 =
            new Category("1", "category1", 0, createLinkMaker("category1"));
    private final Category category2 =
            new Category("2", "category2", 0, createLinkMaker("category2"));
    private final Category category3 =
            new Category("3", "category3", 0, createLinkMaker("category3"));
    private final List<Category> allCategories = List.of(category1, category2, category3);
    private final Offer offer1 = new Offer(1, "url1", "title1", new Price(1), List.of("1"));
    private final Offer offer2 = new Offer(2, "url2", "title2", new Price(2), List.of("2"));
    private final Offer offer3 = new Offer(3, "url3", "title3", new Price(3), List.of("3"));
    private final Offer offer4 = new Offer(4, "url4", "title4", new Price(4), List.of("3"));
    private final List<Offer> allOffers = List.of(offer1, offer2, offer3, offer4);
    
    @Override
    public Either<String, List<Category>> findCategoriesForQuery(WebQuery query)
    {
        final Predicate<Offer> offerFilter =
                offer -> offer.title().contains(query.getQueryString());
        final List<String> matchingCatIds =
                allOffers.stream()
                         .filter(offerFilter)
                         .flatMap(offer -> offer.categoryPath().stream())
                         .distinct()
                         .toList();
        final List<Category> matchingCats =
                allCategories.stream()
                             .filter(category -> matchingCatIds.contains(category.id()))
                             .toList();
        if (matchingCats.isEmpty()) return Left("no categories found for query");
        else return Right(matchingCats);
    }
    
    @Override
    public Either<String, List<Price>> findPricesWithCatFilter(Predicate<String> catNameFilter,
                                                               WebQuery query,
                                                               double minPrice, double maxPrice)
    {
        final List<String> categoryIds =
                allCategories.stream()
                             .filter(category -> catNameFilter.test(category.name()))
                             .map(Category::id)
                             .toList();
        final Predicate<Offer> offerFilter =
                offer -> offer.title().contains(query.getQueryString());
        final Predicate<Offer> categoryFilter =
                offer -> offer.categoryPath()
                              .stream()
                              .anyMatch(categoryIds::contains);
        final List<Price> matchingPrices = allOffers.stream()
                                                    .filter(offerFilter.and(categoryFilter))
                                                    .map(Offer::price)
                                                    .toList();
        if (matchingPrices.isEmpty()) return Left("query failed");
        else return Right(matchingPrices);
    }
}
