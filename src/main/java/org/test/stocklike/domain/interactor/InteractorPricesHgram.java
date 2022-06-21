package org.test.stocklike.domain.interactor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.request.PricesHgramRequestBroker;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;
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
            case WAIT_FOR_REQUEST,
                    DISPLAY_ERROR_AND_WAIT,
                    DISPLAY_HGRAM_AND_WAIT -> processRequest(request);
            case WAIT_FOR_CATEGORIES -> processRequestRefineCategories(request);
            case PROCESS_REQUEST,
                    PROCESS_REFINED_CATEGORIES,
                    PROCESS_REFINED_PARAMS -> throw new UnsupportedOperationException(
                    "processing of state {}" + statesMan.getCurrentState().toString());
            case INVALID_STATE -> throw new IllegalStateException();
            default -> Platform.exit();
        }
    }
    
    private void processRequest(PricesHgramRequest request)
    {
        LOGGER.info("process query: \"{}\"", request.getQuery());
        changeStateTo(State.PROCESS_REQUEST);
        if (request.getCategories().isEmpty()) processRequestWithoutCategories(request);
        else processRequestRefineCategories(request);
    }
    
    private void processRequestWithoutCategories(PricesHgramRequest request)
    {
        assert request.getCategories().isEmpty();
        Either<String, List<Category>> maybeCategories =
                gateway.findCategoriesForQuery(request.getQuery());
        if (maybeCategories.isLeft())
            respondWithError(maybeCategories.getLeft());
        else
            processRequestWithCategories(request, maybeCategories.get());
    }
    
    private void processRequestWithCategories(PricesHgramRequest request,
                                              List<Category> categories)
    {
        if (categories.size() > 1) respondWithCategories(categories);
        else processPrices(
                gateway.findPrices(request.getQuery(),
                                   request.getXRangeMin(),
                                   request.getXRangeMax()),
                request.getBinWidth());
    }
    
    private void processPrices(Either<String, List<Price>> maybePrices,
                               double binWidth)
    {
        if (maybePrices.isLeft()) respondWithError(maybePrices.getLeft());
        else respondWithHgram(maybePrices.get(), binWidth);
    }
    
    private void processRequestRefineCategories(PricesHgramRequest request)
    {
        processPrices(
                gateway.findPricesWithCatFilter(request.getCategories()::contains,
                                                request.getQuery(),
                                                request.getXRangeMin(),
                                                request.getXRangeMax()),
                request.getBinWidth());
    }
    
    private void respondWithHgram(List<Price> prices, double binWidth)
    {
        LOGGER.info("respond with histogram");
        statistics.loadData(prices);
        Hgram hGram = statistics.getHgram(binWidth);
        changeStateTo(State.DISPLAY_HGRAM_AND_WAIT);
        responseBroker.accept(PricesHgramResponse.ofHgram(hGram));
    }
    
    private void respondWithCategories(List<Category> categories)
    {
        changeStateTo(State.WAIT_FOR_CATEGORIES);
        List<String> categoryNames = categories.stream()
                                               .map(Category::name)
                                               .toList();
        LOGGER.info("respond with categories: {}", categoryNames);
        responseBroker.accept(
                PricesHgramResponse.ofCategories(categoryNames));
    }
    
    private void respondWithError(String message)
    {
        changeStateTo(State.DISPLAY_ERROR_AND_WAIT);
        LOGGER.info("respond with error: {}", message);
        responseBroker.accept(PricesHgramResponse.ofError(message));
    }
    
    private void changeStateTo(State state)
    {
        statesMan.setCurrentState(state);
        responseBroker.adviseState(state);
    }
}
