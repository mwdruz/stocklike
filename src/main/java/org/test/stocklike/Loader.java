package org.test.stocklike;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public interface Loader {
    void run(AnnotationConfigApplicationContext ctx);
}
