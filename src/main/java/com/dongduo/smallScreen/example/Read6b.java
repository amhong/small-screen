package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTag6bLog;
import com.gg.reader.api.dal.HandlerTag6bOver;
import com.gg.reader.api.protocol.gx.*;

import java.util.Scanner;

public class Read6b {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            subscribeHandler(client);

            System.out.println("Press any key to start reading the card!");
            scanner.nextLine();

            MsgBaseInventory6b msgBaseInventory6b = new MsgBaseInventory6b();
            msgBaseInventory6b.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msgBaseInventory6b.setArea(EnumG.ReadMode6b_Tid);
            msgBaseInventory6b.setInventoryMode(EnumG.InventoryMode_Inventory);
            client.sendSynMsg(msgBaseInventory6b);
            if (0x00 == msgBaseInventory6b.getRtCode()) {
                System.out.println("MsgBaseInventory6b[OK].");
            } else {
                System.out.println(msgBaseInventory6b.getRtMsg());
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


    //订阅6b标签信息上报
    private static void subscribeHandler(GClient client) {
        client.onTag6bLog = new HandlerTag6bLog() {
            @Override
            public void log(String s, LogBase6bInfo logBase6bInfo) {
                if (null != logBase6bInfo && logBase6bInfo.getResult() == 0) {
                    System.out.println(logBase6bInfo);
                }
            }
        };

        client.onTag6bOver = new HandlerTag6bOver() {
            @Override
            public void log(String s, LogBase6bOver logBase6bOver) {
                System.out.println("HandlerTag6bOver");
            }
        };
    }
}
