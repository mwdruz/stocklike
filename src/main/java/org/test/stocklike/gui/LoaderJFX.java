package org.test.stocklike.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.test.stocklike.Loader;
import org.test.stocklike.gui.view.View;

@Component
public class LoaderJFX extends Application implements Loader {
    private static AnnotationConfigApplicationContext appCtx;

    @Override
    public void start(Stage stage) {
        final var stageManager = (StageManager) appCtx.getBean("stageManager");
        final var mainView = (View) appCtx.getBean("mainView");
        stage.setScene(mainView.getScene());
        stage.show();
        stageManager.init(stage);
    }

    @Override
    public void run(AnnotationConfigApplicationContext ctx) {
        LogManager.getLogger().info("LoaderJFX: run {}", this);
        try {
            ctxAwareLaunch(ctx);
        } catch (Exception e) {
            LogManager.getLogger().error(e);
        }
    }

    public static void ctxAwareLaunch(AnnotationConfigApplicationContext ctx) {
        appCtx = ctx;
        Application.launch();
    }
}
