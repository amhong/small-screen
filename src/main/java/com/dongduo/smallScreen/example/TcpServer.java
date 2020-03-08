package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.GServer;
import com.gg.reader.api.dal.HandlerGClientConnected;
import com.gg.reader.api.dal.HandlerTcpDisconnected;
import com.gg.reader.api.protocol.gx.MsgAppSetTcpMode;
import com.gg.reader.api.protocol.gx.MsgBaseStop;

import java.util.Scanner;

/**
 * 处于客户端模式时，tcp连接不可用，若需要使用，请使用串口连接设置为服务器模式
 */
public class TcpServer {

    static GClient client = new GClient();

    public static void main(String[] args) {

        GServer server = new GServer();

        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to set client mode");
            scanner.nextLine();
            //设置设备为客户端模式 ip为上位机ip 端口为所要监听的端口
            MsgAppSetTcpMode msg = new MsgAppSetTcpMode();
            msg.setTcpMode(1);
            msg.setClientIp("192.168.1.119");
            msg.setClientPort(8160);
            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Set success");
            } else {
                System.out.println(msg.getRtMsg());
            }

            System.out.println("Press any key to start listening");
            scanner.nextLine();

            if (server.open(8160)) {
                client.close();//释放串口连接
                subscribeServerHandler(server);
                System.out.println("开始监听");
            } else {
                System.out.println("监听失败");
            }
        }
    }


    //订阅监听上报
    private static void subscribeServerHandler(GServer server) {
        server.onGClientConnected = new HandlerGClientConnected() {
            @Override
            public void log(GClient gClient) {
                System.out.println(gClient.getName() + "---监听成功");
                client = gClient;//切换连接对象
                client.setSendHeartBeat(true);//开启心跳检测Tcp连接状态
                subscribeTcpHandler();//订阅Tcp断连上报

                testStop();//测试监听成功的连接是否通信正常
            }
        };
    }

    //订阅TCP断开连接上报
    private static void subscribeTcpHandler() {
        client.onDisconnected = new HandlerTcpDisconnected() {
            @Override
            public void log(String s) {
                System.out.println("连接" + s + "已断开");
                client.close();//释放当前连接资源
            }
        };
    }

    private static void testStop() {
        MsgBaseStop msg = new MsgBaseStop();
        client.sendSynMsg(msg);
        if (0x00 == msg.getRtCode()) {
            System.out.println("Stop success");
        } else {
            System.out.println(msg.getRtMsg());
        }
    }
}
