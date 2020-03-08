package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseLock6bGet;

import java.util.Scanner;

public class Lock6bGet {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to write the password");
            scanner.nextLine();

            MsgBaseLock6bGet msg = new MsgBaseLock6bGet();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            String tid = "E0040000B6B3E808";
            msg.setHexMatchTid(tid);
            //查询第9个字节锁定状态
            msg.setLockIndex(9);//byte 从0开始
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Query success");
                if (msg.getLockState() == 1) {
                    System.out.println("已锁定");
                } else {
                    System.out.println("未锁定");
                }
            }
        }
    }
}
