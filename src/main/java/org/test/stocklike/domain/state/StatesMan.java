package org.test.stocklike.domain.state;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

@Component
public class StatesMan {
    public StatesMan()
    {
        LogManager.getLogger().info("hello world ");
    }
    
    private States currentState;
    
    public States getCurrentState()
    {
        return currentState;
    }
    
    public void setCurrentState(States currentState)
    {
        this.currentState = currentState;
    }
    
}
