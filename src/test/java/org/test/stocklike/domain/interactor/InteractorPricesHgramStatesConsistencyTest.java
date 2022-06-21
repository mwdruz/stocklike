package org.test.stocklike.domain.interactor;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.test.stocklike.data.math.Statistics;
import org.test.stocklike.data.math.StatisticsImpl;
import org.test.stocklike.domain.boundary.dto.PricesHgramRequest;
import org.test.stocklike.domain.boundary.dto.WebQuery;
import org.test.stocklike.domain.boundary.gateway.GatewayOffers;
import org.test.stocklike.domain.boundary.response.ResponseBrokerOffersHgram;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.domain.state.StatesMan;

class InteractorPricesHgramStatesConsistencyTest {
    @Mock
    ResponseBrokerOffersHgram responseBroker;
    @Mock
    GatewayOffers gateway;
    Statistics statistics;
    StatesMan statesMan;
    InteractorPricesHgram interactor;
    InteractorPricesHgramTestConfig config;
    private AutoCloseable openedMocks;
    
    @BeforeEach
    void setUpMocks()
    {
        openedMocks = MockitoAnnotations.openMocks(this);
        statistics = new Statistics() {
            @Override
            public void loadData(List<Price> prices) { }
            
            @Override
            public Hgram getHgram(double binWidth) { return Hgram.fromList(binWidth, List.of()); }
        };
        statesMan = new StatesMan();
        interactor = new InteractorPricesHgram(responseBroker, gateway, statesMan, statistics,
                                               State.INVALID_STATE);
        config = new InteractorPricesHgramTestConfig();
        statistics = new StatisticsImpl();
    }
    
    @AfterEach
    void closeMocks() throws Exception { openedMocks.close(); }
    
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
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class)))
                .thenReturn(Right(config.categoriesSingle()));
        when(gateway.findPrices(any(WebQuery.class),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Right(config.pricesReturned()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_REQUEST);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_HGRAM_AND_WAIT);
        assertEquals(State.DISPLAY_HGRAM_AND_WAIT, statesMan.getCurrentState());
    }
    
    @Test
    void queryCategoriesManyLeadsToRefine()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class)))
                .thenReturn(Right(config.categoriesMany()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_REQUEST);
        inOrder.verify(responseBroker).adviseState(State.WAIT_FOR_CATEGORIES);
        assertEquals(State.WAIT_FOR_CATEGORIES, statesMan.getCurrentState());
    }
    
    @Test
    void queryFailedCategoriesLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class))).thenReturn(
                Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_REQUEST);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_ERROR_AND_WAIT);
        assertEquals(State.DISPLAY_ERROR_AND_WAIT, statesMan.getCurrentState());
    }
    
    @Test
    void queryFailedQueryLeadsToError()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_REQUEST);
        when(gateway.findCategoriesForQuery(any(WebQuery.class)))
                .thenReturn(Right(config.categoriesSingle()));
        when(gateway.findPrices(any(WebQuery.class),
                                anyDouble(),
                                anyDouble()))
                .thenReturn(Left(config.errorMessage()));
        
        // when
        interactor.process(config.requestWithoutCategories());
        
        // then
        InOrder inOrder = inOrder(responseBroker);
        inOrder.verify(responseBroker).adviseState(State.PROCESS_REQUEST);
        inOrder.verify(responseBroker).adviseState(State.DISPLAY_ERROR_AND_WAIT);
        assertEquals(State.DISPLAY_ERROR_AND_WAIT, statesMan.getCurrentState());
    }
    
    @Test
    void categoriesSelectedLeadsToDisplay()
    {
        // given
        statesMan.setCurrentState(State.WAIT_FOR_CATEGORIES);
        when(gateway.findPricesWithCatFilter(any(),
                                             any(WebQuery.class),
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
