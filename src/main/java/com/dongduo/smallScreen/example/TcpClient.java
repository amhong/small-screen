package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerTcpDisconnected;


public class TcpClient {
    public static void main(String[] args) {
        GClient client = new GClient();
        if (client.openTcp(ConnectedConstant.TCPClient, 2000)) {
            System.out.println("连接成功");
            subscribeHandler(client);
            client.setSendHeartBeat(true);//设置发送心跳检测tcp连接是否正常  默认关闭
            System.out.println("------拔掉网线、关闭wifi、通信通道被其它占用15秒之后即可触发断连上报----------");
        }
    }


    //订阅TCP断开连接上报
    private static void subscribeHandler(final GClient client) {
        client.onDisconnected = new HandlerTcpDisconnected() {
            @Override
            public void log(String s) {
                System.out.println("连接" + s + "已断开");
                client.close();//释放当前连接资源
            }
        };
    }
}
