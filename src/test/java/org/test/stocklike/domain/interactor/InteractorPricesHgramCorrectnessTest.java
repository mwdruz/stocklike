package org.test.stocklike.domain.interactor;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.domain.boundary.dto.PricesHgramResponse;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.domain.state.StatesMan;

import io.vavr.control.Either;

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
    
    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
        statesMan = new StatesMan();
        interactor = new InteractorPricesHgram(responseBroker, gateway, statesMan, statistics,
                                               State.INVALID_STATE);
        config = new InteractorPricesHgramTestConfig();
    }
    
    @Test
    void queryCategoriesSingleLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString())).thenReturn(
                Right(config.categoriesSingle()));
        when(gateway.findPrices(anyString(),
                                anyBoolean(),
                                anyBoolean(),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(anyString());
        inOrder.verify(gateway).findPrices(anyString(),
                                           anyBoolean(),
                                           anyBoolean(),
                                           anyDouble(),
                                           anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void queryCategoriesManyLeadsToRefine()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString())).thenReturn(
                Right(config.categoriesMany()));
        List<String> categoryNames = config.categoriesMany().stream().map(OfferCategory::getName)
                                           .toList();
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(anyString());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofCategories(categoryNames));
    }
    
    @Test
    void categoriesSelectedLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_CATEGORIES);
        when(gateway.findPricesInCategories(anyList(),
                                            anyString(),
                                            anyBoolean(),
                                            anyBoolean(),
                                            anyDouble(),
                                            anyDouble())).thenReturn(
                Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        verify(gateway).findPricesInCategories(anyList(),
                                               anyString(),
                                               anyBoolean(),
                                               anyBoolean(),
                                               anyDouble(),
                                               anyDouble());
        verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void categoriesChangedLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findPricesInCategories(anyList(),
                                            anyString(),
                                            anyBoolean(),
                                            anyBoolean(),
                                            anyDouble(),
                                            anyDouble())).thenReturn(
                Right(config.pricesReturned()));
        when(statistics.getHgram(anyDouble())).thenReturn(config.hgramReturned());
        
        // when
        interactor.process(config.requestWithCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        verify(gateway).findPricesInCategories(anyList(),
                                               anyString(),
                                               anyBoolean(),
                                               anyBoolean(),
                                               anyDouble(),
                                               anyDouble());
        verify(responseBroker).accept(PricesHgramResponse.ofHgram(config.hgramReturned()));
    }
    
    @Test
    void categoryFailedLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString())).thenReturn(
                Left(config.errorMessage()));
   
        // when
        interactor.process(config.requestWithoutCategories());
    
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(anyString());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofError(config.errorMessage()));
    }
    
    @Test
    void queryFailedLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString())).thenReturn(
                Right(config.categoriesSingle()));
        when(gateway.findPrices(anyString(),
                                anyBoolean(),
                                anyBoolean(),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(gateway, responseBroker);
        inOrder.verify(gateway).findCategoriesForQuery(anyString());
        inOrder.verify(gateway).findPrices(anyString(),
                                           anyBoolean(),
                                           anyBoolean(),
                                           anyDouble(),
                                           anyDouble());
        inOrder.verify(responseBroker).accept(PricesHgramResponse.ofError(config.errorMessage()));
   }
}
