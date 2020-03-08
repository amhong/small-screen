package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.MsgBaseGetPower;
import com.gg.reader.api.protocol.gx.MsgBaseSetPower;

import java.util.Hashtable;
import java.util.Scanner;

public class SetPower {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to set the power");
            scanner.nextLine();

            Hashtable<Integer, Integer> powers = new Hashtable<>();
            powers.put(1, 28);
            powers.put(2, 29);

            MsgBaseSetPower msg = new MsgBaseSetPower();
            msg.setDicPower(powers);
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Set success");
            } else {
                System.out.println(msg.getRtMsg());
            }

            System.out.println("Press any key to query power");
            scanner.nextLine();

            MsgBaseGetPower getPower = new MsgBaseGetPower();
            client.sendSynMsg(getPower);
            if (0x00 == getPower.getRtCode()) {
                System.out.println(getPower);
            } else {
                System.out.println(getPower.getRtMsg());
            }
        }
    }
}
