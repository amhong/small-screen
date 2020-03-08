package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseWriteEpc;
import com.gg.reader.api.protocol.gx.ParamEpcFilter;

import java.util.Scanner;

public class WriteEpcFilter {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to start Write a card!");
            scanner.nextLine();

            MsgBaseWriteEpc msg = new MsgBaseWriteEpc();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msg.setArea(EnumG.WriteArea_Userdata);//写UserData
            msg.setStart(0);
            String data = "1234";
            int len = PcUtils.getValueLen(data);
            data = PcUtils.padLeft(data, len * 4, '0');
            msg.setHexWriteData(data);

            //匹配参数
            String tid = "E280110520007A5CA8AB08A8";
            ParamEpcFilter filter = new ParamEpcFilter();
            filter.setArea(EnumG.ParamFilterArea_TID);
            filter.setHexData(tid);
            filter.setBitStart(0);
            filter.setBitLength(tid.length() * 4);
            msg.setFilter(filter);

            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Write successful.");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
