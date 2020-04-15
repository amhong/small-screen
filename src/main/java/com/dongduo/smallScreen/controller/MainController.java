package com.dongduo.smallScreen.controller;

import com.dongduo.smallScreen.entity.Stat;
import com.dongduo.smallScreen.repository.StatRepository;
import com.dongduo.smallScreen.service.MainService;
import com.dongduo.smallScreen.util.HttpUtil;
import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerGpiStart;
import com.gg.reader.api.protocol.gx.LogAppGpiStart;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@FXMLController
public class MainController implements Initializable, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("今天是yyyy年M月d日");
    private static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${gate.endpoint}")
    private String gateEndpoint;

    //两组红外探测器响应的间隔时间
    @Value("${gate.gpi.interval}")
    private long interval;

    @Autowired
    private GClient client;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private MainService mainService;

    @FXML
    private Label date;

    @FXML
    private Label stat;

    @FXML
    private Label esc;

    static int statDate = 0;
    static long inTime = 0;//进触发时间
    static long outTime = 0;//出触发时间
    static AtomicInteger inCount = new AtomicInteger(0);//进计数
    static AtomicInteger outCount = new AtomicInteger(0);//出计数


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = AbstractJavaFxApplicationSupport.getStage();
        stage.setFullScreen(true);
        if (client.openTcp(gateEndpoint, 2000)) {
            logger.info("通道门连接成功！");
            LocalDateTime now = LocalDateTime.now();
            date.setText(now.format(dtf1));
            statDate = Integer.valueOf(now.format(dtf2));
            Optional<Stat> statOptional = statRepository.findById(statDate);
            if (statOptional.isPresent()) {
                Stat stat = statOptional.get();
                inCount.set(stat.getInCount());
                outCount.set(stat.getOutCount());
            }
            stat.setText(String.format("进%d人  出%d人", inCount.get(), outCount.get()));
            subscribeHandler(client);
            esc.setOnMouseClicked(event -> {
                if (event.getClickCount() == 3) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
        } else {
            logger.error("通道门连接失败！");
            date.setText("通道门连接失败！");
        }
    }

    @Override
    public void destroy() {
        client.close();
        System.out.println("通道门连接已断开！");
    }

    @Scheduled(cron="1 0 0 * * ?")
    public void reset() {
        LocalDateTime now = LocalDateTime.now();
        Platform.runLater(()-> {
            date.setText(now.format(dtf1));
        });
        statDate = Integer.valueOf(now.format(dtf2));
        inCount.set(0);
        outCount.set(0);
    }

    // 订阅gpi触发上报
    private void subscribeHandler(GClient client) {
        client.onGpiStart = new HandlerGpiStart() {
            public void log(String s, LogAppGpiStart logAppGpiStart) {
                // 索引从0开始
                if (null != logAppGpiStart) {
                    //可自行切换任意配置好的gpi触发索引   0-1为进  1-0为出
                    if (logAppGpiStart.getGpiPort() == 0) {
                        inTime = logAppGpiStart.getSystemTime().getTime();
                        // 出
                        if (outTime != 0) {
                            if (inTime - outTime <= interval) {
                                outCount.set(outCount.addAndGet(1));
                                logger.info("---------出--"+outCount.get()+"---------");
                                refreshStat();
                                saveStat();
                                mainService.uploadStat(inCount.get(), outCount.get());
                                inTime = 0;
                                outTime = 0;
                            }
                        }
                    }

                    if (logAppGpiStart.getGpiPort() == 1) {
                        outTime = logAppGpiStart.getSystemTime().getTime();
                        // 进
                        if (inTime != 0) {
                            if (outTime - inTime <= interval) {
                                inCount.set(inCount.addAndGet(1));
                                logger.info("---------进--"+inCount.get()+"---------");
                                refreshStat();
                                saveStat();
                                mainService.uploadStat(inCount.get(), outCount.get());
                                inTime = 0;
                                outTime = 0;
                            }
                        }
                    }
                }
            }
        };
    }

    private void refreshStat() {
        Platform.runLater(()-> {
            stat.setText(String.format("进%d人  出%d人", inCount.get(), outCount.get()));
        });
    }

    public void saveStat() {
        statRepository.save(new Stat(statDate, inCount.get(), outCount.get()));
    }
}
