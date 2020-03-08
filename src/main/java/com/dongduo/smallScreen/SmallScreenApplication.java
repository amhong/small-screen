package com.dongduo.smallScreen;

import com.dongduo.smallScreen.view.MainView;
import com.gg.reader.api.dal.GClient;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmallScreenApplication extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(SmallScreenApplication.class, MainView.class, args);
    }

    @Bean
    public GClient client() {
        return new GClient();
    }
}