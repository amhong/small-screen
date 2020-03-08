package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTagEpcLog;
import com.gg.reader.api.dal.HandlerTagEpcOver;
import com.gg.reader.api.protocol.gx.*;

import java.util.Scanner;

public class ReadEpcFilter {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            subscribeHandler(client);

            System.out.println("Press any key to start reading the card!");
            scanner.nextLine();

            MsgBaseInventoryEpc msg = new MsgBaseInventoryEpc();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msg.setInventoryMode(EnumG.InventoryMode_Inventory);

            //匹配TID读 E280110520007993A8F708A8  可选参数
            ParamEpcFilter filter = new ParamEpcFilter();
            String tid = "E280110520007993A8F708A8";
            filter.setArea(EnumG.ParamFilterArea_TID);
            filter.setBitStart(0);
            filter.setHexData(tid);
            filter.setBitLength(tid.length() * 4);
            msg.setFilter(filter);

            //读TID 默认只读EPC 可选参数
            ParamEpcReadTid readTid = new ParamEpcReadTid();
            readTid.setMode(EnumG.ParamTidMode_Auto);
            readTid.setLen(6);//word
            msg.setReadTid(readTid);

            //读UserData 可选参数
            ParamEpcReadUserdata readUserdata = new ParamEpcReadUserdata();
            readUserdata.setStart(0);
            readUserdata.setLen(4);//word
            msg.setReadUserdata(readUserdata);

            //读保留区 可选参数
            ParamEpcReadReserved readReserved = new ParamEpcReadReserved();
            readReserved.setStart(0);
            readReserved.setLen(4);//word
            msg.setReadReserved(readReserved);

            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("MsgBaseInventoryEpc[OK].");
            } else {
                System.out.println(msg.getRtMsg());
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
