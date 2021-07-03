package org.test.stocklike;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.test.stocklike.config.AppContext;
import org.test.stocklike.config.Config;

import java.util.ResourceBundle;

@Configuration
@ComponentScan("org.test.stocklike")
public class Main
{
    public static void main(final String[] args )
    {
        final var ctx = AppContext.getContext();
        final var resources = ResourceBundle.getBundle("application");
        ctx.getEnvironment().addActiveProfile(resources.getString("app.gui"));
        ctx.register(Config.class);
        ctx.refresh();
        final var loader = (Loader) ctx.getBean("loader");
        loader.run(ctx);
        ctx.close();
    }
}
