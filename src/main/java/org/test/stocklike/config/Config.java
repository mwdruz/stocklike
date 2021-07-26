package org.test.stocklike.config;

import org.springframework.context.annotation.*;
import org.test.stocklike.Loader;
import org.test.stocklike.domain.state.State;
import org.test.stocklike.gui.LoaderJFX;
import org.test.stocklike.gui.StageManager;

@Configuration
@ComponentScan("org.test.stocklike")
public class Config {
    @Profile("jfx")
    @Bean("loader")
    public Loader loaderJFX() {
        return new LoaderJFX();
    }

    @Bean("stageManager")
    public StageManager stageManager() {
        return new StageManager();
    }
    
    @Bean("initialState")
    public State initialState() { return State.WAIT_FOR_QUERY; }
}
