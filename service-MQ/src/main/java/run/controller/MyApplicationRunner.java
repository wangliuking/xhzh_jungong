package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import run.service.AlarmInfoService;
import run.smsnet.SendSms;
import run.smsnet.SmsTcp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 开机启动类及定时任务
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    Z4WAlarmController z4WAlarmController;
    @Autowired
    AlarmInfoService alarmInfoService;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        Map<String,Object> map = alarmInfoService.smsConf();
        String ip = map.get("ip")+"";
        int port = Integer.parseInt(map.get("port")+"");
        SmsTcp.ip = ip;
        SmsTcp.port = port;
        System.out.println("MyApplicationRunner class will be execute when the project was started!");
        z4WAlarmController.openTest();
        /*SmsTcp smsTcp = new SmsTcp();
        smsTcp.start();*/
    }

}
