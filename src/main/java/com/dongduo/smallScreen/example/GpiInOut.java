package com.dongduo.smallScreen.example;

import com.gg.reader.api.dal.GClient;
import com.gg.reader.api.dal.HandlerGpiOver;
import com.gg.reader.api.dal.HandlerGpiStart;
import com.gg.reader.api.protocol.gx.LogAppGpiOver;
import com.gg.reader.api.protocol.gx.LogAppGpiStart;

import java.util.Scanner;

public class GpiInOut {

	static long inTime = 0;//进触发时间
	static long outTime = 0;//出触发时间
	static long interval = 2000;//ms 间隔时间
	static int inCount = 0;//进计数
	static int outCount = 0;//出计数

	public static void main(String[] args) {
		GClient client = new GClient();
		Scanner scanner = new Scanner(System.in);
		if (client.openTcp("192.168.1.168:8160", 0)) {
		//if (client.openSerial(ConnectedConstant.Serial_RS232, 2000)) {
			subscribeHandler(client);
			// 执行触发GPI动作 即可收到上报
		}
	}

	// 订阅gpi触发上报
	private static void subscribeHandler(GClient client) {
		client.onGpiStart = new HandlerGpiStart() {
			public void log(String s, LogAppGpiStart logAppGpiStart) {
				// 索引从0开始
				if (null != logAppGpiStart) {
					//可自行切换任意配置好的gpi触发索引   0-1为进  1-0为出
					if (logAppGpiStart.getGpiPort() == 0) {
						inTime = logAppGpiStart.getSystemTime().getTime();
						// 出
						if (outTime != 0) {
							if (inTime - outTime <= interval) {
								outCount++;
								System.out.println("---------出--"+outCount+"---------");
								inTime = 0;
								outTime = 0;
							}
						}
					}

					if (logAppGpiStart.getGpiPort() == 1) {
						outTime = logAppGpiStart.getSystemTime().getTime();
						// 进
						if (inTime != 0) {
							if (outTime - inTime <= interval) {
								inCount++;
								System.out.println("---------进--"+inCount+"---------");
								inTime = 0;
								outTime = 0;
							}
						}
					}
				}
			}
		};

		client.onGpiOver = new HandlerGpiOver() {
			public void log(String s, LogAppGpiOver logAppGpiOver) {
				if (null != logAppGpiOver) {
					System.out.println(logAppGpiOver);
				}
			}
		};
	}

}
