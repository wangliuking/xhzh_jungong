package mapDownload;

import java.io.File;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 下载图片
 */
public class BaiDuMapDownloadByThreadPool {
    public static void main(String[] args)
            throws Exception {

        //根据properties文件配置url和存储路径
        /*Properties properties = new Properties();
        File file = new File("classpath:baiduMap.properties");
        InputStream inputStream = BaiDuMapDownloadByThreadPool.class.getResourceAsStream("baiduMap.properties");
        //判断是否有此文件
        if (inputStream!=null) {
            properties.load(inputStream);
            String link = properties.getProperty("link");
            if (link!=null&&!link.isEmpty()){
                BDTask.link=link;
            }
            String rootDir = properties.getProperty("rootDir");
            if (rootDir!=null&&!rootDir.isEmpty()){
                BDTask.rootDir=rootDir;
            }
        }*/

        BDTask.startDownload();
    }
}

/**
 * 线程池下载图片
 */
class BDTask implements Runnable {
    //正常百度地图String link = "http://online3.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&udt=20170712&scaler=1&p=1";
    //午夜蓝版
    static String link ="http://{baidu}/customimage/tile?&x={x}&y={y}&z={z}&udt=20180829&scale=1&ak=&styles=t%3Aland%7Ce%3Ag%7Cc%3A%23081734%2Ct%3Abuilding%7Ce%3Ag%7Cc%3A%2304406F%2Ct%3Abuilding%7Ce%3Al%7Cv%3Aoff%2Ct%3Ahighway%7Ce%3Ag%7Cc%3A%23015B99%2Ct%3Ahighway%7Ce%3Al%7Cv%3Aoff%2Ct%3Aarterial%7Ce%3Ag%7Cc%3A%23003051%2Ct%3Aarterial%7Ce%3Al%7Cv%3Aoff%2Ct%3Agreen%7Ce%3Ag%7Cv%3Aoff%2Ct%3Awater%7Ce%3Ag%7Cc%3A%23044161%2Ct%3Asubway%7Ce%3Ag.s%7Cc%3A%23003051%2Ct%3Asubway%7Ce%3Al%7Cv%3Aoff%2Ct%3Arailway%7Ce%3Ag%7Cv%3Aoff%2Ct%3Arailway%7Ce%3Al%7Cv%3Aoff%2Ct%3Aall%7Ce%3Al.t.s%7Cc%3A%23313131%2Ct%3Aall%7Ce%3Al.t.f%7Cc%3A%23FFFFFF%2Ct%3Amanmade%7Ce%3Ag%7Cv%3Aoff%2Ct%3Amanmade%7Ce%3Al%7Cv%3Aoff%2Ct%3Alocal%7Ce%3Ag%7Cv%3Aoff%2Ct%3Alocal%7Ce%3Al%7Cv%3Aoff%2Ct%3Asubway%7Ce%3Ag%7Cl%3A-65%2Ct%3Arailway%7Ce%3Aall%7Cl%3A-40%2Ct%3Aboundary%7Ce%3Ag%7Cc%3A%238b8787%7Cl%3A-29%7Cw%3A1";
    static String rootDir = "/home/wlk/jdk";

    int i;  //x坐标
    int j;  //y坐标
    int z;  //缩放级别

    static volatile Integer c = 0;//成功数
    static volatile Integer fail = 0;//失败数量

    public BDTask(String link, int i, int j, int z) {
        this.link = link;
        this.i = i;
        this.j = j;
        this.z = z;

    }

    public static void startDownload() {
        ThreadPoolExecutor threadPoolExecutor = null;
        long start = 0L;
        for(Level c : Level.values()){
            int z = c.getLevel();
            int xmin = c.getX_min();
            int xmax = c.getX_max();
            int ymin = c.getY_min();
            int ymax = c.getY_max();
            start = System.currentTimeMillis();    //开始时间
            threadPoolExecutor = new ThreadPoolExecutor(2, 4, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
            for (int i = xmin; i <= xmax; i++) {   //循环X
                for (int j = ymin; j <= ymax; j++) {    //循环Y
                    threadPoolExecutor.execute(new BDTask(link, i, j, z));  //下载图片
                }    //循环Y结束
            }   //循环X结束
        }

        threadPoolExecutor.shutdown();   //关闭线程池
        while (!threadPoolExecutor.isTerminated()) {
        }     //所有任务被执行完毕时继续往下执行
        System.out.println("-------用时-------:" + (System.currentTimeMillis() - start));
        System.out.println("共下载:   " + c + "   张");
        System.out.println("失败:   " + fail + "   张");
    }

    public void run() {
        try {

            List<String> hostList = new LinkedList<>();
            hostList.add("api0.map.bdimg.com");
            hostList.add("api1.map.bdimg.com");
            hostList.add("api2.map.bdimg.com");
            int index = Math.abs(i + j) % 3;
            URL url = new URL(link.replace("{x}", i + "").replace("{y}", j + "").replace("{z}", z + "").replace("{baidu}",hostList.get(index)));
            System.out.println("=============");
            System.out.println(url);
            System.out.println("=============");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000*5);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding","gzip, deflate");
            conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cache-Control","max-age=0");
            conn.setRequestProperty("Connection","keep-alive");
            conn.setRequestProperty("Host",hostList.get(index));
            conn.setRequestProperty("Upgrade-Insecure-Requests","1");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

            conn.connect();
            InputStream in = conn.getInputStream();

            File file = new File(rootDir+"/tiles/" + z + "/" + i + "/" + j + ".png");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            OutputStream out = new FileOutputStream(file);
            byte[] bytes = new byte[1024 * 20];
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
            synchronized (fail) {
                c++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            synchronized (c) {
                fail++;
            }
        }
    }
}

/**
 * 枚举类型
 * 等级 x最小 x最大 y最小 y最大
 */
enum Level {
    /*Level_3(3, -3, 2, 0, 1),
    Level_4(4, 1, 4, 0, 2),
    Level_5(5, 2, 9, 0, 5);
    Level_6(6, 4, 19, 0, 11),*/
    Level_7(7, 8, 8, 0, 0);
    /*Level_8(8, 50, 50, 14, 14),
    Level_9(9, 101, 101, 28, 29),
    Level_10(10, 202, 203, 57, 59),
    Level_11(11, 404, 407, 115, 119);
    Level_12(12, 808, 814, 230,239),
    Level_13(13, 1617, 1629, 460, 480),
    Level_14(14, 3234, 3259, 920, 960),
    Level_15(15, 6469, 6518, 1840, 1920),
    Level_16(16, 12939, 13036, 3680, 3850),
    Level_17(17, 25878, 26073, 7360, 7670),
    Level_18(18, 51757, 52146, 14720,15400),
    Level_19(19, 103514, 104292, 29400,30700);*/

    private int level;
    private int x_min;
    private int x_max;
    private int y_min;
    private int y_max;

    Level(int level, int x_min, int x_max, int y_min, int y_max) {
        this.level = level;
        this.x_min = x_min;
        this.x_max = x_max;
        this.y_min = y_min;
        this.y_max = y_max;
    }

    public int getLevel() {
        return level;
    }

    public int getX_min() {
        return x_min;
    }

    public int getX_max() {
        return x_max;
    }

    public int getY_min() {
        return y_min;
    }

    public int getY_max() {
        return y_max;
    }
}