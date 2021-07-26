package org.test.stocklike.domain.interactor;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.data.math.StatisticsImpl;
import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.OfferCategory;
import org.test.stocklike.domain.entity.OfferPrice;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.domain.state.StatesMan;

class InteractorPricesHgramStatesConsistencyTest {
    @Mock
    ResponseBrokerOffersHgram responseBroker;
    @Mock
    GatewayOffers gateway;
    StatesMan statesMan;
    Statistics statistics;
    InteractorPricesHgram interactor;
    InteractorPricesHgramTestConfig config;
    
    @BeforeEach
    void init()
    {
        MockitoAnnotations.openMocks(this);
        statesMan = new StatesMan();
        statistics = new StatisticsImpl();
        interactor = new InteractorPricesHgram(responseBroker, gateway, statesMan, statistics,
                                               State.INVALID_STATE);
        config = new InteractorPricesHgramTestConfig();
        statistics = new StatisticsImpl();
    }
    
    @Test()
    void interactWithInvalidStateThrowsException()
    {
        PricesHgramRequest reqWithCat = config.requestWithCategories();
        PricesHgramRequest reqWoutCat = config.requestWithoutCategories();
        assertEquals(State.INVALID_STATE, statesMan.getCurrentState());
        assertThrows(IllegalStateException.class, () -> interactor.process(reqWithCat));
        assertThrows(IllegalStateException.class, () -> interactor.process(reqWoutCat));
        assertEquals(State.INVALID_STATE, statesMan.getCurrentState());
    }
    
    @Test
    void queryCategoriesSingleLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString()))
                .thenReturn(Right(config.categoriesSingle()));
        when(gateway.findPrices(anyString(),
                                anyBoolean(),
                                anyBoolean(),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Right(config.pricesReturned()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_QUERY);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_HGRAM_AND_WAIT);
        assertEquals(State.DISPLAY_HGRAM_AND_WAIT, statesMan.getCurrentState());
    }
    
    @Test
    void queryCategoriesManyLeadsToRefine()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString()))
                .thenReturn(Right(config.categoriesMany()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder(responseBroker).verify(responseBroker).adviseState(State.WAIT_FOR_CATEGORIES);
        inOrder(responseBroker).verify(responseBroker).adviseState(State.PROCESS_QUERY);
        assertEquals(State.WAIT_FOR_CATEGORIES, statesMan.getCurrentState());
    }
    
    @Test
    void queryFailedCategoriesLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString())).thenReturn(Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_QUERY);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_ERROR_AND_WAIT);
        assertEquals(State.DISPLAY_ERROR_AND_WAIT, statesMan.getCurrentState());
    }
    
    @Test
    void queryFailedQueryLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_QUERY);
        when(gateway.findCategoriesForQuery(anyString()))
                .thenReturn(Right(config.categoriesSingle()));
        when(gateway.findPrices(anyString(),
                                anyBoolean(),
                                anyBoolean(),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_QUERY);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_ERROR_AND_WAIT);
        assertEquals(State.DISPLAY_ERROR_AND_WAIT, statesMan.getCurrentState());
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
                                            anyDouble()))
                .thenReturn(Right(config.pricesReturned()));
        
        // when
        interactor.process(config.requestWithCategories());
        
        // then
        verify(responseBroker).adviseState(State.DISPLAY_HGRAM_AND_WAIT);
        assertEquals(State.DISPLAY_HGRAM_AND_WAIT, statesMan.getCurrentState());
    }
}
