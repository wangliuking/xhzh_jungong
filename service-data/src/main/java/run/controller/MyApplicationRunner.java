package run.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import run.service.DataService;

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
    private DataService dataService;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        System.out.println("MyApplicationRunner class will be execute when the project was started!");
        scheduledInit();
    }

    public void scheduledInit() {

    }

}
