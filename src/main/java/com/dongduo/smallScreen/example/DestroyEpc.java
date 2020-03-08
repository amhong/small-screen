package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.protocol.gx.EnumG;
import com.gg.reader.api.protocol.gx.MsgBaseDestroyEpc;
import com.gg.reader.api.protocol.gx.ParamEpcFilter;

import java.util.Scanner;

public class DestroyEpc {
    public static void main(String[] args) {
        GClient client = new GClient();
        Scanner scanner = new Scanner(System.in);
        if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {

            System.out.println("Press any key to start destruction");
            scanner.nextLine();

            MsgBaseDestroyEpc msg = new MsgBaseDestroyEpc();
            msg.setAntennaEnable(EnumG.AntennaNo_1 | EnumG.AntennaNo_2);
            msg.setHexPassword("87654321");//保留区前4个字节 销毁密码

            //匹配 可选参数
            String tid = "E280110520007B05A8C208A9";
            ParamEpcFilter filter = new ParamEpcFilter();
            filter.setArea(EnumG.ParamFilterArea_TID);
            filter.setHexData(tid);
            filter.setBitStart(0);
            filter.setBitLength(tid.length() * 4);
            msg.setFilter(filter);

            client.sendSynMsg(msg);
            if (0x00 == msg.getRtCode()) {
                System.out.println("Destroy success");
            } else {
                System.out.println(msg.getRtMsg());
            }
        }
    }
}
