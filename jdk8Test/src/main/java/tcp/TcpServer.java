package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Thread {
    private ServerSocket serverSocket;
    public TcpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while(true){
            try{
                System.out.println("等待远程端口连接，当前开启端口为："+serverSocket.getLocalPort()+"........");
                Socket server = serverSocket.accept();
                System.out.println("远程主机IP为："+server.getRemoteSocketAddress());
                DataInputStream dataInputStream = new DataInputStream(server.getInputStream());
                System.out.println(dataInputStream.readUTF());
                DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());
                dataOutputStream.writeUTF("感谢连接！"+server.getLocalSocketAddress()+"\nGoodbye");
                server.close();
            }catch (IOException e){
                System.out.println("出现IO异常!!!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            TcpServer tcpServer = new TcpServer(8888);
            tcpServer.start();
            tcpServer.run();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
