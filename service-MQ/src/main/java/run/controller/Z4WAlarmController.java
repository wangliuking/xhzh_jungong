package run.controller;

import net.sf.json.JSONObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import protobuf.Conf;
import protobuf.jsonbean.AlarmInfo;
import run.redis.RedisTest;
import run.service.AlarmInfoService;
import run.smsnet.SendSms;
import run.smsnet.SmsTcp;
import run.websocket.WebSocketServer;

import javax.jms.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Z4WAlarmController {
    @Autowired
    AlarmInfoService alarmInfoService;
    @Autowired
    private FeignForStructure feignForStructure;

    @RequestMapping(value = "/selectAllAlarmInfo",method = RequestMethod.GET)
    public Map<String,Object> selectAllAlarmInfo (HttpServletRequest req, HttpServletResponse resp){
        int start = -1;
        if(req.getParameter("start") != null && !"".equals(req.getParameter("start"))){
            start = Integer.parseInt(req.getParameter("start"));
        }
        int limit = -1;
        if(req.getParameter("limit") != null && !"".equals(req.getParameter("limit"))){
            limit = Integer.parseInt(req.getParameter("limit"));
        }
        int site_id = -1;
        if(req.getParameter("site_id") != null && !"".equals(req.getParameter("site_id"))){
            site_id = Integer.parseInt(req.getParameter("site_id"));
        }
        int rtuId = -1;
        if(req.getParameter("rtuId") != null && !"".equals(req.getParameter("rtuId"))){
            rtuId = Integer.parseInt(req.getParameter("rtuId"));
        }
        int type = -1;
        if(req.getParameter("type") != null && !"".equals(req.getParameter("type"))){
            type = Integer.parseInt(req.getParameter("type"));
        }
        int alarmStatus = -1;
        if(req.getParameter("alarmStatus") != null && !"".equals(req.getParameter("alarmStatus"))){
            alarmStatus = Integer.parseInt(req.getParameter("alarmStatus"));
        }
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        Map<String,Object> params = new HashMap<>();
        params.put("start",start);
        params.put("limit",limit);
        params.put("site_id",site_id);
        params.put("rtuId",rtuId);
        params.put("type",type);
        params.put("alarmStatus",alarmStatus);
        params.put("startTime",startTime);
        params.put("endTime",endTime);

        String structure = req.getParameter("structure");
        List<Integer> strList = feignForStructure.foreachIdAndPId(structure);
        System.out.println("strList : ++++++++++++"+strList);
        params.put("strList",strList);

        List<Map<String,Object>> list = alarmInfoService.selectAllAlarmInfo(params);
        int count = alarmInfoService.selectAllAlarmInfoCount(params);

        Map<String,Object> listMap = new HashMap<>();
        listMap.put("items",list);
        listMap.put("totals",count);
        return listMap;

    }

    @RequestMapping(value = "/selectAllAlarmInfoNow",method = RequestMethod.GET)
    public Map<String,Object> selectAllAlarmInfoNow (HttpServletRequest req, HttpServletResponse resp){
        int start = -1;
        if(req.getParameter("start") != null && !"".equals(req.getParameter("start"))){
            start = Integer.parseInt(req.getParameter("start"));
        }
        int limit = -1;
        if(req.getParameter("limit") != null && !"".equals(req.getParameter("limit"))){
            limit = Integer.parseInt(req.getParameter("limit"));
        }
        int site_id = -1;
        if(req.getParameter("site_id") != null && !"".equals(req.getParameter("site_id"))){
            site_id = Integer.parseInt(req.getParameter("site_id"));
        }
        int rtuId = -1;
        if(req.getParameter("rtuId") != null && !"".equals(req.getParameter("rtuId"))){
            rtuId = Integer.parseInt(req.getParameter("rtuId"));
        }
        int type = -1;
        if(req.getParameter("type") != null && !"".equals(req.getParameter("type"))){
            type = Integer.parseInt(req.getParameter("type"));
        }
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        Map<String,Object> params = new HashMap<>();
        params.put("start",start);
        params.put("limit",limit);
        params.put("site_id",site_id);
        params.put("rtuId",rtuId);
        params.put("type",type);
        params.put("startTime",startTime);
        params.put("endTime",endTime);

        String structure = req.getParameter("structure");
        List<Integer> strList = feignForStructure.foreachIdAndPId(structure);
        System.out.println("strList : ++++++++++++"+strList);
        params.put("strList",strList);

        List<Map<String,Object>> list = alarmInfoService.selectAllAlarmInfoNow(params);
        int count = alarmInfoService.selectAllAlarmInfoNowCount(params);

        Map<String,Object> listMap = new HashMap<>();
        listMap.put("items",list);
        listMap.put("totals",count);
        return listMap;

    }

    @RequestMapping(value = "/selectSmsConf",method = RequestMethod.GET)
    public Map<String,Object> selectSmsConf (){
        Map<String,Object> param = new HashMap<>();
        param.put("smsConf",alarmInfoService.smsConf());
        return param;
    }

    @RequestMapping(value = "/saveSmsConf",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveSmsConf(@RequestBody Map<String,Object> param) {
        System.out.println("==========================");
        System.out.println(param);
        System.out.println("==========================");
        alarmInfoService.smsSave(param);
        Map<String,Object> res = new HashMap<>();
        res.put("smsConf",alarmInfoService.smsConf());
        return res;
    }

    void openTest(){
        //spd
        new Thread(()->{
            System.out.println("SPDAlarm Running");
                try {
                    commonMethod(Conf.z2w_SPD_alarm,255);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //spd
        //etcr
        new Thread(()->{
            System.out.println("EtcrAlarm Running");
            try {
                commonMethod(Conf.z2w_ETCR_alarm,0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //etcr
        //etcrb
        new Thread(()->{
            System.out.println("EtcrBAlarm Running");
            try {
                commonMethod(Conf.z2w_EtcrB_alarm,1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //etcrb
        //lightning
        new Thread(()->{
            System.out.println("LightningAlarm Running");
            try {
                commonMethod(Conf.z2w_Lct_alarm,2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //lightning
        //static
        new Thread(()->{
            System.out.println("StaticAlarm Running");
            try {
                commonMethod(Conf.z2w_Mems_alarm,4);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //static
        //rsws
        new Thread(()->{
            System.out.println("RswsAlarm Running");
            try {
                commonMethod(Conf.z2w_rsws_alarm,3);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //rsws
        //svt
        new Thread(()->{
            System.out.println("SvtAlarm Running");
            try {
                commonMethod(Conf.z2w_Svt_alarm,5);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //svt
        //hc
        new Thread(()->{
            System.out.println("HcAlarm Running");
            try {
                commonMethod(Conf.z2w_Hc_alarm,6);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //hc
        //stray
        new Thread(()->{
            System.out.println("StrayAlarm Running");
            try {
                commonMethod(Conf.z2w_Stray_alarm,7);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //stray
        //cat
        new Thread(()->{
            System.out.println("CatAlarm Running");
            try {
                commonMethod(Conf.z2w_Cathode_alarm,8);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //cat
        //rtuAlarm
        new Thread(()->{
            System.out.println("RTUAlarm Running");
            try {
                rtuAlarm();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //rtuAlarm
        //Z4WMQ
        new Thread(()->{
            System.out.println("Z4WMQ Running");
            try {
                alarmMQ();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        //Z4WMQ
    }

    private void commonMethod(String queueName,int dType) throws Exception {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Conf.ip_port);
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Queue queue = session.createQueue(queueName);
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 第七步：接收消息。
        consumer.setMessageListener((message)->{
            try {
                TextMessage textMessage = (TextMessage) message;
                //取消息的内容
                String text = textMessage.getText();
                JSONObject jsonObject = JSONObject.fromObject(text);
                AlarmInfo d = (AlarmInfo) JSONObject.toBean(jsonObject, AlarmInfo.class);
                // 第八步：打印消息
                System.out.println("==================================");
                d.setDevicetype(dType);
                System.out.println(d);
                //调用入库函数
                alarmInfoService.insertAlarmInfo(d);
                alarmInfoService.changeAlarmNow(d);
                String deviceTypeMes = "";
                if(d.getStatus() == 1){
                    if(d.getDevicetype() == 0 || d.getDevicetype() == 1){
                        deviceTypeMes = "地阻";
                    }else if(d.getDevicetype() == 2){
                        deviceTypeMes = "雷电流";
                    }else if(d.getDevicetype() == 3){
                        deviceTypeMes = "温湿度";
                    }else if(d.getDevicetype() == 4){
                        deviceTypeMes = "静电";
                    }else if(d.getDevicetype() == 5){
                        deviceTypeMes = "倾斜度";
                    }else if(d.getDevicetype() == 6){
                        deviceTypeMes = "电气安全";
                    }else if(d.getDevicetype() == 7){
                        deviceTypeMes = "杂散电流";
                    }else if(d.getDevicetype() == 8){
                        deviceTypeMes = "阴极保护";
                    }
                    String str = RedisTest.searchAlarmTypeConf("windowType3")+RedisTest.searchAlarmTypeConf("voiceType3");
                    //推送告警到前台
                    String tempMsg = d.getRtuId()+"号RTU"+d.getChanno()+"端口"+d.getDeviceid()+"号"+deviceTypeMes+"设备异常";
                    String pushMessage = tempMsg+str;
                    WebSocketServer.sendInfo(pushMessage, null);
                    if(RedisTest.searchAlarmTypeConf("smsType3").equals("1")){
                        sendSms(tempMsg);
                    }
                }
                System.out.println("==================================");
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        //等待键盘输入
        /*System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();*/
    }

    private void rtuAlarm() throws Exception {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Conf.ip_port);
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Queue queue = session.createQueue(Conf.z2w_RTu_alarm);
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 第七步：接收消息。
        consumer.setMessageListener((message)->{
            try {
                TextMessage textMessage = (TextMessage) message;
                //取消息的内容
                String text = textMessage.getText();
                JSONObject jsonObject = JSONObject.fromObject(text);
                AlarmInfo d = (AlarmInfo) JSONObject.toBean(jsonObject, AlarmInfo.class);
                // 第八步：打印消息。
                System.out.println("==================================");
                System.out.println(d);
                System.out.println("==================================");
                //调用更新函数
                //判断是设备离线还是rtu离线
                String deviceTypeMes = "";
                if(d.getAlarmType() == 1){//设备离线
                    if(d.getDevicetype() == 0 || d.getDevicetype() == 1){
                        alarmInfoService.updateEtcrNow(d);
                        deviceTypeMes = "地阻";
                    }else if(d.getDevicetype() == 2){
                        alarmInfoService.updateLightningNow(d);
                        deviceTypeMes = "雷电流";
                    }else if(d.getDevicetype() == 3){
                        alarmInfoService.updateRswsNow(d);
                        deviceTypeMes = "温湿度";
                    }else if(d.getDevicetype() == 4){
                        alarmInfoService.updateStaticNow(d);
                        deviceTypeMes = "静电";
                    }else if(d.getDevicetype() == 5){
                        alarmInfoService.updateSvtNow(d);
                        deviceTypeMes = "倾斜度";
                    }else if(d.getDevicetype() == 6){
                        alarmInfoService.updateHcNow(d);
                        deviceTypeMes = "电气安全";
                    }else if(d.getDevicetype() == 7){
                        alarmInfoService.updateStrayNow(d);
                        deviceTypeMes = "杂散电流";
                    }else if(d.getDevicetype() == 8){
                        alarmInfoService.updateCatNow(d);
                        deviceTypeMes = "阴极保护";
                    }
                }
                if(d.getStatus() == 1){
                    String pushMessage;
                    //推送告警到前台
                    if(d.getAlarmType() == 1){//设备离线
                        String str = RedisTest.searchAlarmTypeConf("windowType1")+RedisTest.searchAlarmTypeConf("voiceType1");
                        String tempMsg = d.getRtuId()+"号RTU"+d.getChanno()+"端口"+d.getDeviceid()+"号"+deviceTypeMes+"设备离线";
                        pushMessage = tempMsg+str;
                        if(RedisTest.searchAlarmTypeConf("smsType1").equals("1")){
                            sendSms(tempMsg);
                        }
                    }else if(d.getAlarmType() == 2){//RTU离线
                        String str = RedisTest.searchAlarmTypeConf("windowType2")+RedisTest.searchAlarmTypeConf("voiceType2");
                        pushMessage = d.getRtuId()+"号RTU离线"+str;
                        if(RedisTest.searchAlarmTypeConf("smsType2").equals("1")){
                            sendSms(d.getRtuId()+"号RTU离线");
                        }
                    }else{
                        pushMessage = "其他告警";
                    }
                    WebSocketServer.sendInfo(pushMessage, null);
                }

            } catch (JMSException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
        //等待键盘输入
        /*System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();*/
    }

    private void alarmMQ() throws Exception {
        // 第一步：创建一个ConnectionFactory对象。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Conf.ip_port);
        // 第二步：从ConnectionFactory对象中获得一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接。调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
        Queue queue = session.createQueue(Conf.z2w_MQ);
        // 第六步：使用Session对象创建一个Consumer对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 第七步：接收消息。
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    //取消息的内容
                    text = textMessage.getText();
                    JSONObject jsonObject = JSONObject.fromObject(text);
                    /*Map<String,Class> classMap = new HashMap<>();
                    classMap.put("DBInfo", DBInfo.class);
                    classMap.put("values", Values.class);
                    DeviceRES d = (DeviceRES) JSONObject.toBean(jsonObject,DeviceRES.class,classMap);*/
                    SendController.putMap(jsonObject.get("callId").toString(),jsonObject);
                    // 第八步：打印消息。
                    System.out.println("==================================");
                    System.out.println(jsonObject);
                    System.out.println("==================================");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //等待键盘输入
        /*System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();*/
    }

    public void sendSms(String msg){
        System.out.println("ready to send");
        AlarmInfoService alarmInfoService = new AlarmInfoService();
        Map<String,Object> paramMap = alarmInfoService.smsConf();
        String phone = paramMap.get("phone")+"";
        String[] strs = phone.split(";");
        SendSms sendSms = new SendSms();
        System.out.println("AT^SMS="+strs+" \""+msg+"\"\n");
        try {
            Thread.sleep(5000);
            if (SmsTcp.getSocket().isConnected()) {
                sendSms.sendAt("+++");
                Thread.sleep(1000);
                sendSms.sendAt("AT\n");
                if(strs.length>0){
                    for(int i=0;i<strs.length;i++){
                        Thread.sleep(1000);
                        sendSms.sendAt("AT^SMS="+strs[i]+" \""+msg+"\"\n");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
