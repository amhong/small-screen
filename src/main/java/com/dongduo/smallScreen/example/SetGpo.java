package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.MsgAppSetGpo;

import java.util.Scanner;

public class SetGpo {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            //高低电平切换会发出声响

            System.out.println("Press any key to set GPO");
            scanner.nextLine();

            MsgAppSetGpo msg = new MsgAppSetGpo();
            msg.setGpo1(0);//gpo1 设置低电平
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Set success");
            } else {
                System.out.println(msg.getRtMsg());
            }

            System.out.println("Press any key to continue setting the GPO");
            scanner.nextLine();
            msg.setGpo1(1);//gpo1 设置高电平
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Set success");
            } else {
                System.out.println(msg.getRtMsg());
            }

        }
    }
}
