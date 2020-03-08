package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTagEpcLog;
import com.gg.reader.api.dal.HandlerTagEpcOver;
import com.gg.reader.api.protocol.gx.*;

import java.util.Scanner;

public class ReadEpc {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openTcp("192.1.1.168:8160", 0)) {
        //if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            subscribeHandler(client);

            System.out.println("Press any key to start reading the card!");
            scanner.nextLine();

            MsgBaseInventoryEpc msgBaseInventoryEpc = new MsgBaseInventoryEpc();
            msgBaseInventoryEpc.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msgBaseInventoryEpc.setInventoryMode(EnumG.InventoryMode_Inventory);
            client.sendSynMsg(msgBaseInventoryEpc);
            if (0x00 == msgBaseInventoryEpc.getRtCode()) {
                System.out.println("MsgBaseInventoryEpc[OK].");
            } else {
                System.out.println(msgBaseInventoryEpc.getRtMsg());
            }

            System.out.println("Press any key to Stop reading card");
            scanner.nextLine();

            MsgBaseStop stopMsg = new MsgBaseStop();
            client.sendSynMsg(stopMsg);
            if (0x00 == stopMsg.getRtCode()) {
                System.out.println("MsgBaseStop Success");
            } else {
                System.out.println("MsgBaseStop Fail");
            }

            System.out.println("Close the connection");
            client.close();
        }
    }

    //订阅6c标签信息上报
    private static void subscribeHandler(GClient client) {
        client.onTagEpcLog = new HandlerTagEpcLog() {
            @Override
            public void log(String s, LogBaseEpcInfo logBaseEpcInfo) {
                if (null != logBaseEpcInfo && logBaseEpcInfo.getResult() == 0) {
                    System.out.println(logBaseEpcInfo);
                }
            }
        };

        client.onTagEpcOver = new HandlerTagEpcOver() {
            @Override
            public void log(String s, LogBaseEpcOver logBaseEpcOver) {
                System.out.println("HandlerTagEpcOver");
            }
        };
    }
}
