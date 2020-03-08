package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseWrite6b;

import java.util.Scanner;

public class Write6bUserData {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Enter any character to start write.");
            scanner.nextLine();

            MsgBaseWrite6b msg = new MsgBaseWrite6b();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msg.setStart(8);//byte 前八个字节为本身TID
            msg.setHexMatchTid("E0040000F8B3E808");//匹配TID

            String data = "12";
            int len = PcUtils.getValueLen(data);
            data = PcUtils.padLeft(data, len * 2, '0');
            msg.setHexWriteData(data);
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Write successful.");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
