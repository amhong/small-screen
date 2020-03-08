package com.dongduo.smallScreen.simulator;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.LogAppGpiStart;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulatorGClient extends GClient {
    private static AtomicBoolean stop = new AtomicBoolean(false);

    @Override
    public boolean openTcp(String readerName, int timeout) {
        new Thread(() -> {
            while (!stop.get()) {
                try {
                    Thread.sleep(1000L);
                    LogAppGpiStart l = new LogAppGpiStart();
                    l.setGpiPort(0);
                    l.setSystemTime(new Date());
                    onGpiStart.log("", l);
                    Thread.sleep(1000L);
                    l.setGpiPort(1);
                    l.setSystemTime(new Date());
                    onGpiStart.log("", l);
                    Thread.sleep(1000L);
                    l.setGpiPort(1);
                    l.setSystemTime(new Date());
                    onGpiStart.log("", l);
                    Thread.sleep(1000L);
                    l.setGpiPort(0);
                    l.setSystemTime(new Date());
                    onGpiStart.log("", l);
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    @Override
    public boolean close() {
        stop.set(true);
        return true;
    }
}
