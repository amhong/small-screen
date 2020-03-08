package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseWriteEpc;

import java.util.Scanner;

public class WriteEpc {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            MsgBaseWriteEpc msg = new MsgBaseWriteEpc();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            //字起始地址 第0个为CRC，不可写
            msg.setStart(1);//word
            //写EPC，数据默认为 hex 432
            msg.setArea(EnumG.WriteArea_Epc);
            String sWriteHexData = "432"; // 写入数据 （16进制）
            System.out.println("Write hex " + sWriteHexData);
            System.out.println("Enter any character to start write.");
            scanner.nextLine();

            int iWordLen = PcUtils.getValueLen(sWriteHexData);

            // PC值为EPC区域的长度标识（前5个bit标记长度），参考文档说明
            sWriteHexData = PcUtils.getPc(iWordLen) + PcUtils.padLeft(sWriteHexData.toUpperCase(), 4 * iWordLen, '0'); // PC值+数据内容
            msg.setHexWriteData(sWriteHexData);
            // 若需要写入带特殊编码数据，请自行进行编码并使用 "BwriteData" 属性。
            // msgBaseWriteEpc.setBwriteData(HexUtils.hexString2Bytes(sWriteHexData));
            client.sendSynMsg(msg);
            if (0 == msg.getRtCode()) {
                System.out.println("Write successful.");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
