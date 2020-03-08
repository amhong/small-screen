package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseLock6b;

import java.util.Scanner;

public class Lock6b {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to write the password");
            scanner.nextLine();

            MsgBaseLock6b msg = new MsgBaseLock6b();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            String tid = "E0040000B6B3E808";
            msg.setHexMatchTid(tid);
            msg.setLockIndex(9);//byte  从0开始

            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Lock successful.");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
