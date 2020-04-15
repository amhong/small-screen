package com.dongduo.smallScreen;

import com.dongduo.smallScreen.simulator.SimulatorGClient;
import com.dongduo.smallScreen.view.MainView;
import com.gg.reader.api.dal.GClient;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SmallScreenApplication extends AbstractJavaFxApplicationSupport {
    public static void main(String[] args) {
        launch(SmallScreenApplication.class, MainView.class, args);
    }

    @Bean
    public GClient client(@Value("${gate.simulator}") boolean simulator) {
        return simulator ? new SimulatorGClient() : new GClient();
    }
}
