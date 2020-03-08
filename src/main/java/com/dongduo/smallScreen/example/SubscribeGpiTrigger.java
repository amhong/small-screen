package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerGpiOver;
import com.gg.reader.api.dal.HandlerGpiStart;
import com.gg.reader.api.protocol.gx.LogAppGpiOver;
import com.gg.reader.api.protocol.gx.LogAppGpiStart;

import java.util.Scanner;

public class SubscribeGpiTrigger {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            subscribeHandler(client);
			//执行触发GPI动作 即可收到上报
        }
    }

    //订阅gpi触发上报
    private static void subscribeHandler(GClient client) {
        client.onGpiStart = new HandlerGpiStart() {
            @Override
            public void log(String s, LogAppGpiStart logAppGpiStart) {
                //索引从0开始
                if (null != logAppGpiStart) {
                    System.out.println(logAppGpiStart);
                }
            }
        };

        client.onGpiOver = new HandlerGpiOver() {
            @Override
            public void log(String s, LogAppGpiOver logAppGpiOver) {
                if (null != logAppGpiOver) {
                    System.out.println(logAppGpiOver);
                }
            }
        };
    }
}
