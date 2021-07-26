package org.test.stocklike.domain.interactor;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.request.PricesHgramRequestBroker;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.entity.OfferPrice;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.domain.state.StatesMan;

import io.vavr.control.Either;
import javafx.application.Platform;

@Service
public class InteractorPricesHgram implements PricesHgramRequestBroker {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResponseBrokerOffersHgram responseBroker;
    private final GatewayOffers gateway;
    private final StatesMan statesMan;
    private final Statistics statistics;
    
    public InteractorPricesHgram(ResponseBrokerOffersHgram responseBroker,
                                 GatewayOffers gateway,
                                 StatesMan statesMan,
                                 Statistics statistics, State initialState)
    {
        this.responseBroker = responseBroker;
        this.gateway = gateway;
        this.statesMan = statesMan;
        this.statistics = statistics;
        statesMan.setCurrentState(initialState);
    }
    
    @Override
    public void process(PricesHgramRequest request)
    {
        switch (statesMan.getCurrentState()) {
            case WAIT_FOR_QUERY,
                    DISPLAY_ERROR_AND_WAIT,
                    DISPLAY_HGRAM_AND_WAIT -> processQuery(request);
            case WAIT_FOR_CATEGORIES -> processCategories(request);
            case PROCESS_QUERY,
                    PROCESS_CATEGORIES,
                    PROCESS_PARAMS -> throw new UnsupportedOperationException(
                    "processing of state {}" +
                    statesMan.getCurrentState().toString());
            case INVALID_STATE -> throw new IllegalStateException();
            default -> Platform.exit();
        }
    }
    
    private void processQuery(PricesHgramRequest request)
    {
        final String query = request.getQuery();
        LOGGER.info("process query: \"{}\"", query);
        changeState(State.PROCESS_QUERY);
        if (!request.getCategories().isEmpty())
            processCategories(request);
        else {
            Either<String, List<OfferCategory>> maybeCategories =
                    gateway.findCategoriesForQuery(query);
            if (maybeCategories.isLeft())
                respondWithError(maybeCategories.getLeft());
            else
                processQueryHandleCategories(request, maybeCategories.get());
        }
    }
    
    private void processQueryHandleCategories(PricesHgramRequest request,
                                              List<OfferCategory> categories)
    {
        if (categories.size() > 1)
            respondWithCategories(categories);
        else processQueryHandlePrices(
                gateway.findPrices(request.getQuery(),
                                   request.isCheckNow(),
                                   request.isCheckNew(),
                                   request.getXRangeMin(),
                                   request.getXRangeMax()),
                request.getBinWidth());
    }
    
    private void processQueryHandlePrices(Either<String, List<OfferPrice>> maybePrices,
                                          double binWidth)
    {
        if (maybePrices.isLeft())
            respondWithError(maybePrices.getLeft());
        else respondWithHgram(maybePrices.get(), binWidth);
    }
    
    private void processCategories(PricesHgramRequest request)
    {
        processQueryHandlePrices(gateway.findPricesInCategories(request.getCategories(),
                                                                request.getQuery(),
                                                                request.isCheckNow(),
                                                                request.isCheckNew(),
                                                                request.getXRangeMin(),
                                                                request.getXRangeMax()),
                                 request.getBinWidth());
    }
    
    private void respondWithHgram(List<OfferPrice> offerPrices, double binWidth)
    {
        LOGGER.info("respond with histogram");
        statistics.loadData(offerPrices);
        Map<String, Double> hGram = statistics.getHgram(binWidth);
        changeState(State.DISPLAY_HGRAM_AND_WAIT);
        responseBroker.accept(PricesHgramResponse.ofHgram(hGram));
    }
    
    private void respondWithCategories(List<OfferCategory> categories)
    {
        changeState(State.WAIT_FOR_CATEGORIES);
        List<String> categoryNames = categories.stream()
                                               .map(OfferCategory::getName)
                                               .toList();
        LOGGER.info("respond with categories: {}", categoryNames);
        responseBroker.accept(
                PricesHgramResponse.ofCategories(categoryNames));
    }
    
    private void respondWithError(String message)
    {
        changeState(State.DISPLAY_ERROR_AND_WAIT);
        LOGGER.info("respond with error: {}", message);
        responseBroker.accept(PricesHgramResponse.ofError(message));
    }
    
    private void changeState(State toState)
    {
        statesMan.setCurrentState(toState);
        responseBroker.adviseState(toState);
    }
}
