package org.test.stocklike.config;

import org.springframework.context.annotation.*;
import org.test.stocklike.Loader;
import org.test.stocklike.gui.LoaderJFX;

@Configuration
@ComponentScan("org.test.stocklike")
public class Config {
    @Profile("jfx")
    @Bean("loader")
    public Loader loaderJFX() {
        return new LoaderJFX();
    }
}
