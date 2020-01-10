package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
    public static void main(String[] args) {
        try {
            System.out.println("准备连接主机");
            Socket socket = new Socket("127.0.0.1",8888);
            System.out.println("连接成功！远程主机IP为："+socket.getRemoteSocketAddress());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("Hello from "+socket.getLocalSocketAddress());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            System.out.println("服务器回复："+dataInputStream.readUTF());
            socket.close();
        }catch (UnknownHostException e){
            System.out.println("找不到远程主机");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("io异常");
            e.printStackTrace();
        }
    }
}
