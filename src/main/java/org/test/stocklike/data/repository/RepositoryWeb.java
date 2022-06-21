package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.test.stocklike.data.repository.parser.Parse;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Offer;
import org.test.stocklike.domain.entity.Price;

import io.vavr.control.Either;
import okhttp3.HttpUrl;

@Repository
@Profile("web")
final class RepositoryWeb implements GatewayOffers {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String URL_SCHEME = "https";
    private static final String URL_HOST = "allegro.pl";
    private static final String URL_QUERY_ROOT = "listing";
    private static final String URL_QUERY_PARAM = "string";
    private static final IntFunction<String> urlMakePageSuffix = n -> "&p=" + n;
    
    private WebQuery cachedCategoryQuery = null;
    private Either<String, List<Category>> maybeCachedCategoryList = Left("nope");
    private final Downloader downloader;
    private final Parse parse;
    
    public RepositoryWeb(Downloader downloader, Parse parse)
    {
        this.downloader = downloader;
        this.parse = parse;
    }
    
    private static String urlFromQuery(WebQuery query)
    {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(URL_SCHEME)
                .host(URL_HOST)
                .addPathSegment(URL_QUERY_ROOT)
                .addQueryParameter(URL_QUERY_PARAM, query.getQueryString())
                .build();
        return url.toString();
    }
    
    @Override
    public Either<String, List<Category>> findCategoriesForQuery(WebQuery query)
    {
        String url = urlFromQuery(query);
        LOGGER.info("Retrieving categories from URL: {}", url);
        Either<String, Document> maybeDoc = downloader.download(url);
        if (maybeDoc.isLeft()) return Left(maybeDoc.getLeft());
        else return parse.parseCategories(maybeDoc.get());
    }
    
    @Override
    public Either<String, List<Price>> findPricesWithCatFilter(Predicate<String> catNameFilter,
                                                               WebQuery query,
                                                               double minPrice,
                                                               double maxPrice)
    {
        var maybeSelectedCategoryList = selectFromCachedCategories(query, catNameFilter);
        if (maybeSelectedCategoryList.isLeft()) return Left(maybeSelectedCategoryList.getLeft());
        
        List<Offer> results = new ArrayList<>();
        for (Category category : maybeSelectedCategoryList.get()) {
            var maybeOffersList = findOffersInCat(query, category);
            if (maybeOffersList.isLeft()) return Left(maybeOffersList.getLeft());
            else results.addAll(maybeOffersList.get());
        }
        
        return Right(
                results.stream()
                       .sorted(Comparator.comparing(offer -> offer.price().getIntValue()))
                       .distinct()
                       .map(Offer::price)
                       .filter(price -> price.getIntValue() >= (int) (minPrice * 100) &&
                                        price.getIntValue() <= (int) (maxPrice * 100))
                       .toList()
                    );
    }
    
    private Either<String, List<Offer>> findOffersInCat(WebQuery query,
                                                        Category category)
    {
        String url = category.getQueryLink(query.getQueryString());
        LOGGER.info("Retrieving prices from URL: {}", url);
        var maybeDoc = downloader.download(url);
        if (maybeDoc.isLeft()) return Left(maybeDoc.getLeft());
        var pagesCountOrFail = parse.parsePagesCount(maybeDoc.get());
        if (pagesCountOrFail.isLeft())
            return Left(pagesCountOrFail.getLeft());
        int pagesCount = pagesCountOrFail.get();
        var maybePages = downloader.downloadPages(url, pagesCount, urlMakePageSuffix);
        if (maybePages.isLeft()) return Left(maybePages.getLeft());
        
        List<Offer> results = new ArrayList<>();
        for (Document page : maybePages.get()) {
            var offersOrFails = parse.parseOffers(page);
            if (offersOrFails.isLeft()) return Left(offersOrFails.getLeft());
            else results.addAll(offersOrFails.get());
        }
        return Right(results);
    }
    
    private Either<String, List<Category>> selectFromCachedCategories(WebQuery query,
                                                                      Predicate<String> catNameFilter)
    {
        if (!query.equals(cachedCategoryQuery)) {
            cachedCategoryQuery = query;
            maybeCachedCategoryList = findCategoriesForQuery(query);
        }
        if (maybeCachedCategoryList.isLeft()) return Left(maybeCachedCategoryList.getLeft());
        else return Right(maybeCachedCategoryList.get()
                                                 .stream()
                                                 .filter(cat -> catNameFilter.test(cat.name()))
                                                 .toList());
    }
}
