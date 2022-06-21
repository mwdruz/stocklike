package org.test.stocklike.domain.interactor;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.Category;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.domain.state.StatesMan;

class InteractorPricesHgramCorrectnessTest {
    @Mock
    ResponseBrokerOffersHgram responseBroker;
    @Mock
    GatewayOffers gateway;
    @Mock
    Statistics statistics;
    StatesMan statesMan;
    InteractorPricesHgram interactor;
    InteractorPricesHgramTestConfig config;
    private AutoCloseable openedMocks;
    
    @BeforeEach
    void setUpMocks()
    {
        openedMocks = MockitoAnnotations.openMocks(this);
        statesMan = new StatesMan();
        interactor = new InteractorPricesHgram(responseBroker, gateway, statesMan, statistics,
                                               State.INVALID_STATE);
        config = new InteractorPricesHgramTestConfig();
    }
    
    @AfterEach
    void closeMocks() throws Exception { openedMocks.close(); }
    
    @Test
    void queryCategoriesSingleLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class))).thenReturn(
                Right(config.categoriesSingle()));
        when(gateway.findPrices(any(WebQuery.class),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(any(WebQuery.class));
        inOrder.verify(gateway).findPrices(any(WebQuery.class),
                                           anyDouble(),
                                           anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void queryCategoriesManyLeadsToRefine()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class))).thenReturn(
                Right(config.categoriesMany()));
        List<String> categoryNames = config.categoriesMany().stream().map(Category::name)
                                           .toList();
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(any(WebQuery.class));
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofCategories(categoryNames));
    }
    
    @Test
    void categoriesSelectedLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_CATEGORIES);
        when(gateway.findPricesWithCatFilter(any(),
                                             any(WebQuery.class),
                                             anyDouble(),
                                             anyDouble())).thenReturn(
                Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findPricesWithCatFilter(any(),
                                                        any(WebQuery.class),
                                                        anyDouble(),
                                                        anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void categoriesChangedLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findPricesWithCatFilter(any(),
                                             any(WebQuery.class),
                                             anyDouble(),
                                             anyDouble())).thenReturn(
                Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findPricesWithCatFilter(any(),
                                                        any(WebQuery.class),
                                                        anyDouble(),
                                                        anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void categoryFailedLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class))).thenReturn(
                Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(any(WebQuery.class));
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofError(config.errorMessage()));
    }
    
    @Test
    void queryFailedLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class))).thenReturn(
                Right(config.categoriesSingle()));
        when(gateway.findPrices(any(WebQuery.class),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(any(WebQuery.class));
        inOrder.verify(gateway).findPrices(any(WebQuery.class),
                                           anyDouble(),
                                           anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofError(config.errorMessage()));
    }
}
