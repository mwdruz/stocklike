package org.test.stocklike.data.repository;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.text.Normalizer;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.stocklike.data.repository.parser.Parse;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Offer;
import org.test.stocklike.domain.entity.Price;

import io.vavr.control.Either;

class RepositoryWebTest {
    private static final WebQuery someQuery =
            WebQuery.builder().setQueryString("someQueryString").build();
    private static final Document someDoc1 = new Document("some url");
    private static final Document someDoc2 = new Document("another url");
    private static final List<Document> someDocs = List.of(someDoc1, someDoc2);
    
    private static final int lowestPriceInt = 1100;
    private static final double lowestPrice = lowestPriceInt / 100.0;
    private static final int highestPriceInt = 1400;
    private static final double highestPrice = highestPriceInt / 100.0;
    private static final int lowRangePriceInt = 1200;
    private static final double lowRangePrice = lowRangePriceInt / 100.0;
    private static final int highRangePriceInt = 1300;
    private static final double highRangePrice = highRangePriceInt / 100.0;
    private static final Offer someOffer1 =
            new Offer(1, "1", "off1", new Price(lowestPrice), List.of("1"));
    private static final Offer someOffer2 =
            new Offer(2, "2", "off2", new Price(lowRangePrice), List.of("2"));
    private static final Offer someOffer3 =
            new Offer(3, "1", "off1", new Price(highRangePrice), List.of("1"));
    private static final Offer someOffer4 =
            new Offer(4, "2", "off2", new Price(highestPrice), List.of("2"));
    private static final List<Offer> allOffersList = List.of(someOffer1, someOffer2,
                                                             someOffer3, someOffer4);
    private static final List<Price> allPricesList =
            allOffersList.stream().map(Offer::price).toList();
    private static final List<Offer> rangeOffersList = List.of(someOffer2, someOffer3);
    private static final List<Price> rangePricesList =
            rangeOffersList.stream().map(Offer::price).toList();
    
    private static UnaryOperator<String> catLinkMaker(String base)
    {
        return str -> base + "?string=" + str;
    }
    
    private static final Category someCategory1 =
            new Category("1", "cat1", 1, catLinkMaker("\u002Fkategoria\u002Fkomputery"));
    private static final Category someCategory2 =
            new Category("2", "cat2", 2, catLinkMaker("\u002Fkategoria\u002Felektronika"));
    private static final List<Category> someCategories = List.of(someCategory1, someCategory2);
    private static final Predicate<String> someCatFilter = str -> someCategories.stream().anyMatch(
            cat -> str.equals(cat.name()));
    
    private static final int somePagesNo = 7;
    
    private static final String downloadErrorMsg = "download failed";
    private static final String parseErrorMsgPrefix = "failed parsing of ";
    
    private static final Either<String, Document> downloadFailed = Left(downloadErrorMsg);
    private static final Either<String, Document> downloadSuccess = Right(someDoc1);
    
    private static final Either<String, List<Document>> downloadPagesFailed =
            Left(downloadErrorMsg);
    private static final Either<String, List<Document>> downloadPagesSuccess =
            Right(someDocs);
    
    private static final Either<String, List<Category>> parseCategoriesFailed =
            Left(parseErrorMsgPrefix + "categories");
    private static final Either<String, Integer> parsePagesFailed =
            Left(parseErrorMsgPrefix + "pages");
    private static final Either<String, List<Offer>> parseOffersFailed =
            Left(parseErrorMsgPrefix + "offers");
    
    private static final Either<String, List<Category>> parseCategoriesSuccess =
            Right(someCategories);
    private static final Either<String, Integer> parsePagesCountSuccess =
            Right(somePagesNo);
    private static final Either<String, List<Offer>> parseOffersSuccess =
            Right(allOffersList);
    
    private static final Either<String, List<Category>> findCategoriesDownloadFailed =
            Left(downloadFailed.getLeft());
    private static final Either<String, List<Category>> findCategoriesParseFailed =
            Left(parseCategoriesFailed.getLeft());
    private static final Either<String, List<Category>> findCategoriesSuccess =
            Right(someCategories);
    
    private static final Either<String, List<Price>> findPricesInCategoriesDownloadFailed =
            Left(downloadFailed.getLeft());
    private static final Either<String, List<Price>> findPricesInCategoriesParsePagesFailed =
            Left(parsePagesFailed.getLeft());
    private static final Either<String, List<Price>> findPricesInCategoriesDownloadOffersFailed =
            Left(downloadPagesFailed.getLeft());
    private static final Either<String, List<Price>> findPricesInCategoriesParseOffersFailed =
            Left(parseOffersFailed.getLeft());
    private static final Either<String, List<Price>> findPricesInCategoriesSuccessPricesAll =
            Right(allPricesList);
    private static final Either<String, List<Price>> findPricesInCategoriesSuccessPricesRange =
            Right(rangePricesList);
    
    @Mock
    private Downloader downloaderMock;
    @Mock
    private Parse parseMock;
    private RepositoryWeb repository;
    private AutoCloseable openedMocks;
    
    @BeforeEach
    void setUpMocks()
    {
        openedMocks = MockitoAnnotations.openMocks(this);
        repository = new RepositoryWeb(downloaderMock, parseMock);
    }
    
    @AfterEach
    void closeMocks() throws Exception { openedMocks.close(); }
    
    @Test
    void findCategoriesForQueryDownloadFailed()
    {
        // given
        when(downloaderMock.download(anyString())).thenReturn(downloadFailed);
        // when
        Either<String, List<Category>> result = repository.findCategoriesForQuery(someQuery);
        // then
        assertTrue(result.isLeft());
        assertEquals(findCategoriesDownloadFailed, result);
    }
    
    @Test
    void findCategoriesForQueryParseFailed()
    {
        // given
        when(downloaderMock.download(anyString())).thenReturn(downloadSuccess);
        when(parseMock.parseCategories(downloadSuccess.get())).thenReturn(parseCategoriesFailed);
        // when
        Either<String, List<Category>> result = repository.findCategoriesForQuery(someQuery);
        // then
        assertTrue(result.isLeft());
        assertEquals(findCategoriesParseFailed, result);
    }
    
    @Test
    void findCategoriesSuccess()
    {
        // given
        when(downloaderMock.download(anyString())).thenReturn(downloadSuccess);
        when(parseMock.parseCategories(downloadSuccess.get())).thenReturn(parseCategoriesSuccess);
        // when
        Either<String, List<Category>> result = repository.findCategoriesForQuery(someQuery);
        // then
        assertTrue(result.isRight());
        assertEquals(findCategoriesSuccess, result);
    }
    
    @Test
    void findPricesInCategoriesDownloadPagesFailed()
    {
        // given
        when(downloaderMock.download(anyString())).thenReturn(downloadFailed);
        when(parseMock.parseCategories(downloadSuccess.get())).thenReturn(parseCategoriesSuccess);
        // when
        Either<String, List<Price>> result =
                repository.findPricesWithCatFilter(someCatFilter,
                                                   someQuery,
                                                   lowestPrice,
                                                   highestPrice);
        // then
        assertTrue(result.isLeft());
        assertEquals(findPricesInCategoriesDownloadFailed, result);
        Normalizer.normalize("ą", Normalizer.Form.NFKC);
    }
    
    @Test
    void findPricesInCategoriesParsePagesFailed()
    {
        // given
        when(downloaderMock.download(anyString())).thenReturn(downloadSuccess);
        when(parseMock.parseCategories(downloadSuccess.get())).thenReturn(parseCategoriesSuccess);
        when(parseMock.parsePagesCount(downloadSuccess.get())).thenReturn(parsePagesFailed);
        // when
        var result = repository.findPricesWithCatFilter(someCatFilter,
                                                        someQuery,
                                                        lowestPrice,
                                                        highestPrice);
        // then
        assertTrue(result.isLeft());
        assertEquals(findPricesInCategoriesParsePagesFailed, result);
    }
    
    @Test
    void findPricesInCategoriesDownloadOffersFailed()
    {
        // given
        when(downloaderMock.download(anyString()))
                .thenReturn(downloadSuccess);
        when(downloaderMock.downloadPages(anyString(), anyInt(), any()))
                .thenReturn(downloadPagesFailed);
        when(parseMock.parseCategories(downloadSuccess.get()))
                .thenReturn(parseCategoriesSuccess);
        when(parseMock.parsePagesCount(downloadSuccess.get()))
                .thenReturn(parsePagesCountSuccess);
        // when
        var result = repository.findPricesWithCatFilter(someCatFilter,
                                                        someQuery,
                                                        lowestPrice,
                                                        highestPrice);
        // then
        assertTrue(result.isLeft());
        assertEquals(findPricesInCategoriesDownloadOffersFailed, result);
    }
    
    @Test
    void findPricesInCategoriesParseOffersFailed()
    {
        // given
        when(downloaderMock.download(anyString()))
                .thenReturn(downloadSuccess);
        when(downloaderMock.downloadPages(anyString(), anyInt(), any()))
                .thenReturn(downloadPagesSuccess);
        when(parseMock.parseCategories(downloadSuccess.get()))
                .thenReturn(parseCategoriesSuccess);
        when(parseMock.parsePagesCount(downloadSuccess.get()))
                .thenReturn(parsePagesCountSuccess);
        when(parseMock.parseOffers(any(Document.class)))
                .thenReturn(parseOffersFailed);
        // when
        var result = repository.findPricesWithCatFilter(someCatFilter,
                                                        someQuery,
                                                        lowestPrice,
                                                        highestPrice);
        // then
        assertTrue(result.isLeft());
        assertEquals(findPricesInCategoriesParseOffersFailed, result);
    }
    
    @Test
    void findPricesInCategoriesSuccess()
    {
        // given
        when(downloaderMock.download(anyString()))
                .thenReturn(downloadSuccess);
        when(downloaderMock.downloadPages(anyString(), anyInt(), any()))
                .thenReturn(downloadPagesSuccess);
        when(parseMock.parsePagesCount(downloadSuccess.get()))
                .thenReturn(parsePagesCountSuccess);
        when(parseMock.parseOffers(any(Document.class)))
                .thenReturn(parseOffersSuccess);
        when(repository.findCategoriesForQuery(someQuery))
                .thenReturn(Right(someCategories));
        // when
        var result = repository.findPricesWithCatFilter(someCatFilter,
                                                        someQuery,
                                                        lowestPrice,
                                                        highestPrice);
        // then
        assertTrue(result.isRight());
        assertEquals(findPricesInCategoriesSuccessPricesAll, result);
    }
    
    @Test
    void findPricesInCategoriesSuccessWithRange()
    {
        // given
        when(downloaderMock.download(anyString()))
                .thenReturn(downloadSuccess);
        when(downloaderMock.downloadPages(anyString(), anyInt(), any()))
                .thenReturn(downloadPagesSuccess);
        when(parseMock.parsePagesCount(downloadSuccess.get()))
                .thenReturn(parsePagesCountSuccess);
        when(parseMock.parseOffers(any(Document.class)))
                .thenReturn(parseOffersSuccess);
        when(repository.findCategoriesForQuery(someQuery))
                .thenReturn(Right(someCategories));
        // when
        var result = repository.findPricesWithCatFilter(someCatFilter,
                                                        someQuery,
                                                        lowRangePrice,
                                                        highRangePrice);
        // then
        assertTrue(result.isRight());
        assertEquals(findPricesInCategoriesSuccessPricesRange, result);
        List<Integer> resultValues = result.get().stream().map(Price::getIntValue).toList();
        assertEquals(resultValues, resultValues.stream().sorted().toList());
        assertEquals(lowRangePriceInt, resultValues.get(0));
        assertEquals(highRangePriceInt, resultValues.get(resultValues.size() - 1));
    }
}
