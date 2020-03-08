package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseLockEpc;
import com.gg.reader.api.protocol.gx.MsgBaseWriteEpc;
import com.gg.reader.api.protocol.gx.ParamEpcFilter;

import java.util.Scanner;

public class LockEpc {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to write the password");
            scanner.nextLine();

            //先写入密码
            MsgBaseWriteEpc writeEpc = new MsgBaseWriteEpc();
            writeEpc.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            writeEpc.setArea(EnumG.WriteArea_Reserved);//写保留区
            writeEpc.setStart(2);//word  前4个字节代表销毁密码 后4个字节代表访问密码
            String pas = "1234";
            writeEpc.setHexWriteData(pas);
            client.sendSynMsg(writeEpc);
            if (0x00 == writeEpc.getRtCode()) {
                System.out.println("Write successful." + "pas-->12340000");
            } else {
                System.out.println(writeEpc.getRtMsg());
            }


            System.out.println("Press any key to start locking");
            scanner.nextLine();
            //锁EPC
            MsgBaseLockEpc msg = new MsgBaseLockEpc();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msg.setArea(2);//锁EPC区  锁定成功后写epc需要访问密码
            msg.setMode(EnumG.LockMode_Lock);
//
            //匹配可选参数
            String tid = "E280110520007B05A8C208A8";
            ParamEpcFilter filter = new ParamEpcFilter();
            filter.setArea(EnumG.ParamFilterArea_TID);
            filter.setHexData(tid);
            filter.setBitStart(0);
            filter.setBitLength(tid.length() * 4);
            msg.setFilter(filter);

            msg.setHexPassword("12340000");

            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Lock successful.");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
