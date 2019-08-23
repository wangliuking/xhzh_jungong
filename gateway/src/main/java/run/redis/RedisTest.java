package run.redis;

import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisTest {
    private static String ip = "localhost";

    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(1);
        //jedis.del("181");
        //jedis.flushDB();
        Set<String> strs = jedis.keys("*");
        System.out.println("strs : "+strs);
        Iterator<String> it = strs.iterator();
        while(it.hasNext()){
            System.out.println(jedis.get(it.next()));
        }
        //jedis.flushAll();
        //System.out.println("Connection to server sucessfully");
        //查看服务是否运行
        //System.out.println("Server is running: "+jedis.ping());
        jedis.close();
    }

    /**
     * 遍历所有值，根据传入用户判断是否删除
     * @param username
     * @return
     */
    public static boolean searchForDel(String username){
        boolean status = false;
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(0);
        Set<String> strs = jedis.keys("*");
        Iterator<String> it = strs.iterator();
        while(it.hasNext()){
            String key = it.next();
            String val = jedis.get(key);
            if(username.equals(val)){
                jedis.del(key);
                status = true;
            }
        }
        jedis.close();
        return status;
    }

    public static boolean searchLoginUser(String sessionId){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(0);
        String result = jedis.get(sessionId);
        System.out.println("result :"+result);
        jedis.close();
        if(result != null && !"".equals(result)){
            return true;
        }else{
            return false;
        }

    }

    public static String getLoginUser(String sessionId){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(0);
        String result = jedis.get(sessionId);
        //System.out.println("result :"+result);
        jedis.close();
        if(result != null && !"".equals(result)){
            return result;
        }else{
            return "";
        }

    }

    public static void addLoginUser(String sessionId,String userId){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(0);
        jedis.set(sessionId,userId);
        jedis.expire(sessionId,3600*5);
        jedis.close();
    }

    public static void removeLoginUser(String sessionId){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(0);
        jedis.del(sessionId);
        jedis.close();
    }

    //查询公告栏
    public static String searchMarquee(){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(3);
        String res = jedis.get("marquee");
        jedis.close();
        return res;
    }

    //修改公告栏
    public static void updateMarquee(String marquee){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(3);
        jedis.del("marquee");
        jedis.set("marquee",marquee);
        jedis.close();
    }

    //查询弹窗或声音告警配置
    public static Map<String,String> searchAlarmConf(int type){
        String alarmType = "";
        if(type == 1){
            //弹窗配置
            alarmType = "windowType";
        }else if(type == 2){
            //声音配置
            alarmType = "voiceType";
        }else if(type == 3){
            //声音配置
            alarmType = "smsType";
        }
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(3);
        Map<String,String> map = new HashMap<>();
        for(int i=1;i<4;i++){
            String key = alarmType+i;
            map.put(key,jedis.get(key));
        }
        jedis.close();
        return map;
    }

    //保存弹窗或声音告警配置
    public static void saveAlarmConf(Map<String,String> map){
        Jedis jedis = new Jedis(ip,6379);
        jedis.auth("XinHong12345");
        jedis.select(3);
        for(String key : map.keySet()){
            jedis.set(key,map.get(key));
        }
        jedis.close();
    }


}
