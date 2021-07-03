package org.test.stocklike.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppContext {
    private static final AnnotationConfigApplicationContext CTX =
            new AnnotationConfigApplicationContext();

    private AppContext() { }

    public static AnnotationConfigApplicationContext getContext() {
        return CTX;
    }

    public static Object getBean(String name) {
        return CTX.getBean(name);
    }
}
