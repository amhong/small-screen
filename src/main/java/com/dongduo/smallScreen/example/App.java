package com.dongduo.smallScreen.example;


import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTagEpcLog;
import com.gg.reader.api.dal.HandlerTagEpcOver;
import com.gg.reader.api.protocol.gx.*;

import java.util.Hashtable;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner sc = new Scanner(System.in);
        if (client.openTcp("192.1.1.168:8160", 0)) {
//        if (client.openSerial("COM11:115200", 2000)) {
            // 订阅标签上报事件
            client.onTagEpcLog = new HandlerTagEpcLog() {
                @Override
                public void log(String readName, LogBaseEpcInfo logBaseEpcInfo) {
                    // 回调内部如有阻塞，会影响API正常使用
                    // 标签回调数量较多，请将标签数据先缓存起来再作业务处理
                    if (null != logBaseEpcInfo && 0 == logBaseEpcInfo.getResult()) {
                        System.out.println(logBaseEpcInfo);
                    }
                }
            };
            client.onTagEpcOver = new HandlerTagEpcOver() {
                @Override
                public void log(String readName, LogBaseEpcOver logBaseEpcOver) {
                    if (null != logBaseEpcOver) {
                        System.out.println("Epc log over.");
                    }
                }
            };
            // 停止指令，空闲态
            MsgBaseStop msgBaseStop = new MsgBaseStop();
            client.sendSynMsg(msgBaseStop);
            if (0 == msgBaseStop.getRtCode()) {
                System.out.println("Stop successful.");
            } else {
                System.out.println("Stop error.");
            }

            // 功率配置, 将4个天线功率都设置为30dBm.
            MsgBaseSetPower msgBaseSetPower = new MsgBaseSetPower();
            Hashtable<Integer, Integer> hashtable = new Hashtable<>();
            hashtable.put(1, 30);
            hashtable.put(2, 30);
            hashtable.put(3, 30);
            hashtable.put(4, 30);
            msgBaseSetPower.setDicPower(hashtable);
            client.sendSynMsg(msgBaseSetPower);
            if (0 == msgBaseSetPower.getRtCode()) {
                System.out.println("Power configuration successful.");
            } else {
                System.out.println("Power configuration error.");
            }

            //按任意键开始读卡
            System.out.println("Enter any character to start reading the tag.");
            sc.nextLine();

            // 4个天线读卡, 读取EPC数据区以及TID数据区
            MsgBaseInventoryEpc msgBaseInventoryEpc = new MsgBaseInventoryEpc();
            msgBaseInventoryEpc.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2 | EnumG.AntennaNo_3 | EnumG.AntennaNo_4);
            msgBaseInventoryEpc.setInventoryMode(EnumG.InventoryMode_Inventory);

            ParamEpcReadTid tid = new ParamEpcReadTid();
            tid.setMode(EnumG.ParamTidMode_Auto);
            tid.setLen(6);
            msgBaseInventoryEpc.setReadTid(tid);

            client.sendSynMsg(msgBaseInventoryEpc);
            if (0 == msgBaseInventoryEpc.getRtCode()) {
                System.out.println("Inventory epc successful.");
            } else {
                System.out.println("Inventory epc error.");
            }

            //按任意键停止读卡(停止前请拿开标签)
            sc.nextLine();
            // 停止读卡，空闲态
            client.sendSynMsg(msgBaseStop);
            if (0 == msgBaseStop.getRtCode()) {
                System.out.println("Stop successful.");
            } else {
                System.out.println("Stop error.");
            }

        } else {
            System.out.println("Connect failure.");
        }
    }

}
