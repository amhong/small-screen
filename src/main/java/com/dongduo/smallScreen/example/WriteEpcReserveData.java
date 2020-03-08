package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseWriteEpc;

import java.util.Scanner;

public class WriteEpcReserveData {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
            System.out.println("Press any key to write the password");
            scanner.nextLine();

            MsgBaseWriteEpc writeEpc = new MsgBaseWriteEpc();
            writeEpc.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            writeEpc.setArea(EnumG.WriteArea_Reserved);//写保留区
            //写入访问密码
            writeEpc.setStart(2);//word  前4个字节代表销毁密码 后4个字节代表访问密码
            String pas = "1234";
            writeEpc.setHexWriteData(pas);
            client.sendSynMsg(writeEpc);
            if (0x00 == writeEpc.getRtCode()) {
                System.out.println("Write successful." + "pas-->12340000");
            } else {
                System.out.println(writeEpc.getRtMsg());
            }
        }
    }
}
