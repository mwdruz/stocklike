package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.entity.Offer;
import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.entity.OfferPrice;

import io.vavr.control.Either;

@Component
public class RepositoryStub implements GatewayOffers {
    private final OfferCategory category1 = new OfferCategory(List.of(1), "category1");
    private final OfferCategory category2 = new OfferCategory(List.of(2), "category2");
    private final OfferCategory category3 = new OfferCategory(List.of(3), "category3");
    private final Offer offer1 = new Offer(1, "url1", "title1", new OfferPrice(1), category1);
    private final Offer offer2 = new Offer(2, "url2", "title2", new OfferPrice(2), category2);
    private final Offer offer3 = new Offer(3, "url3", "title3", new OfferPrice(3), category3);
    private final Offer offer4 = new Offer(4, "url4", "title4", new OfferPrice(4), category3);
    private final List<Offer> offers = List.of(offer1, offer2, offer3, offer4);
    
    @Override
    public Either<String, List<OfferCategory>> findCategoriesForQuery(String query)
    {
        final Predicate<Offer> offerFilter = offer -> offer.getTitle().contains(query);
        final List<OfferCategory> matchingCategories = offers.stream()
                                                             .filter(offerFilter)
                                                             .map(Offer::getOfferCategory)
                                                             .distinct()
                                                             .toList();
        if (matchingCategories.isEmpty()) return Left("no categories found for query");
        else return Right(matchingCategories);
    }
    
    @Override
    public Either<String, List<OfferPrice>> findPricesInCategories(List<String> categoryNames,
                                                                   String query, boolean checkNow,
                                                                   boolean checkNew,
                                                                   double minPrice, double maxPrice)
    {
        final Predicate<Offer> offerFilter = offer -> offer.getTitle().contains(query);
        final Predicate<Offer> categoryFilter = offer -> categoryNames.contains(
                offer.getOfferCategory().getName());
        final List<OfferPrice> matchingPrices = offers.stream()
                                                      .filter(offerFilter.and(categoryFilter))
                                                      .map(Offer::getOfferPrice)
                                                      .toList();
        if (matchingPrices.isEmpty()) return Left("query failed");
        else return Right(matchingPrices);
    }
}
