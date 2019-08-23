package run.smsnet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import run.controller.Z4WAlarmController;
import run.service.AlarmInfoService;
import run.util.FunUtil;


public class SmsTcp extends Thread {
    private static Socket socket;
    private static int timeout = 1 * 60 * 1000;
    protected final Log log = LogFactory.getLog(SmsTcp.class);
    public static String ip;
    public static int port;
    private FunUtil func = new FunUtil();
    private boolean connected = false;
    private static String recvStr = "";
    private SendSms sendSms = new SendSms();
    private Sms sms = new Sms();

    private static Timer timer = null;
    private static Timer hearttimer = null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SmsTcp smsTcp = new SmsTcp();
        SendSms sendSms = new SendSms();
        smsTcp.start();
        System.out.println("ready to send");
        try {
            Thread.sleep(5000);
            if (SmsTcp.getSocket().isConnected()) {
                sendSms.sendAt("+++");
                Thread.sleep(1000);
                sendSms.sendAt("AT\n");
                Thread.sleep(1000);
                sendSms.sendAt("AT^SMS=13558735210 \"测试短信功能\"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        receive();
    }

    public void connect() {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            socket = new Socket();
            System.out.println("ip: ==================== "+ip+" port:=================="+port);
            InetSocketAddress addr = new InetSocketAddress(ip, port);
            try {
                socket.connect(addr, timeout);
                socket.setTcpNoDelay(true);
                socket.setKeepAlive(true);
            } catch (IOException e) {

            }
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {

                try {
                    socket.close();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                connected = false;
            }

        }
    }

    // 接收数据
    public void receive() {
        //InputStream input = null;
        while (!connected) {
            try {
                connect();
                //input = socket.getInputStream();
                if (socket.isConnected()) {
                    connected = true;
                    log.info("sms TCP Connected success!!");
                    //sendSms.sendAt("AT+CSCS=GSM\r");
                    //Timer timer = new Timer();
                    if (timer == null) {
                        timer = new Timer();
                        //timer.schedule(new AlarmSms(), 2000, 10*1000);
                        timer.scheduleAtFixedRate(new HeartBeat(), 2000, 1000 * 50);
                        //timer.schedule(new HeartBeat(), 1000, 60*1000);
                    }
                    //timer.schedule(new HeartBeat(), 1000, 100 * 1000);
                }
                //DataInputStream in = new DataInputStream(socket.getInputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                // read body
                //byte[] buf = new byte[4096];// 收到的包字节数组
                InputStream is = socket.getInputStream();
                byte b[] = new byte[1024];

                while (connected) {
                    //String res = in.readLine();
                    int len = is.read(b);

                    if (len > 0) {
                        byte c[] = new byte[len];

                        System.arraycopy(b, 0, c, 0, len);
                        String str = new String(c);
                        recvStr += str;
						/* log.info("16进制:"+func.BytesToHexS(str.getBytes()));
						 log.info("16进制123:"+str.indexOf("\r\n"));
						 log.info("recv:"+func.BytesToHexS(recvStr.getBytes()));
						 log.info("recv2:"+recvStr.lastIndexOf("\r\n")+"data:"+(recvStr.length()-2));*/

                        if (recvStr.lastIndexOf("\r\n") == recvStr.length() - 2) {
                            handler(recvStr);
                        } else {

                        }

                    } else {
                        socket.close();
                        log.info("====TCP connection is closed!!====");
                        log.info("====reconnection now!!====");
                        connected = false;
                    }
                }

            } catch (SocketException e) {
                log.info("sms tcp connect trying");
                try {
                    sleep(10000);
                } catch (InterruptedException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                if (socket.isConnected() || socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    connected = false;
                }

            } catch (UnknownHostException e) {
                System.out.println("UnknownHostException");
            } catch (IOException e) {
                log.info("sms tcp connect timeout,socket is closed and reconnecting!");
                try {
                    socket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                connected = false;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void handler(String str) {
        log.info("smsnet:" + str);
		/*if (str.indexOf("CSQ")==-1) {
			String[] ss=str.split("\r\n");
			for (int i = 0; i < ss.length; i++) {
				if (!ss[i].equals("") && ss[i].indexOf("CSQ")==-1) {
					log.info("smsNet:"+ss[i]);
				}
				
			}
		}*/

        recvStr = "";

    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        SmsTcp.socket = socket;
    }

    public static String getRecvStr() {
        return recvStr;
    }

    public static void setRecvStr(String recvStr) {
        SmsTcp.recvStr = recvStr;
    }


}

class HeartBeat extends TimerTask {
    private Socket socket = null;
    private SendSms sendSms = new SendSms();
    protected final Log log = LogFactory.getLog(HeartBeat.class);


    public HeartBeat() {

    }

    public void run() {
        try {
            if (SmsTcp.getSocket().isConnected()) {
                sendSms.sendAt("AT+csq\r\n");
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
            e.getMessage();
            log.error("没有建立短信网关tcp连接");
        }

    }
}