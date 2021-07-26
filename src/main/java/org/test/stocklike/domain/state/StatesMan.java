package org.test.stocklike.domain.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class StatesMan {
    private static final Logger LOGGER = LogManager.getLogger(StatesMan.class);
    private State currentState;
    
    public StatesMan(State state)
    {
        currentState = state;
        LOGGER.info("State manager initialized to: {}", currentState);
    }
    
    public StatesMan()
    {
        this(State.INVALID_STATE);
    }
    
    public State getCurrentState()
    {
        return currentState;
    }
    
    public void setCurrentState(State currentState)
    {
        LOGGER.info("Application state changed to: {}", currentState);
        this.currentState = currentState;
    }
    
}
