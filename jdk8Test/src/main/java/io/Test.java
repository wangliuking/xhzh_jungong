package io;

import java.io.*;
import java.util.Scanner;

public class Test {

    public void test1() throws Exception{
        char c;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入字符，按q退出");
        do {
            c = (char)br.read();
            System.out.println(c);
        }while(c != 'q');
    }

    public void test2() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("enter lines of text");
        System.out.println("enter end to exit");
        String str;
        do{
            str = br.readLine();
            System.out.println(str);
        }while (!"end".equals(str));
    }

    public void scannerMethod(){
        Scanner scanner = new Scanner(System.in);
        //键盘接收数据
        System.out.println("wait input");
        if(scanner.hasNext()){
            String str = scanner.next();
            System.out.println(str);
        }
        scanner.close();
    }

    public void writeFile() throws Exception{
        OutputStream os = new FileOutputStream("/home/wlk/jdk/wlk.txt");
        byte[] byteArr = {1,2,3,4,5,6};
        for(int i=0;i<byteArr.length;i++){
            os.write(byteArr[i]);
        }
        os.close();
        InputStream is = new FileInputStream("/home/wlk/jdk/wlk.txt");
        int size = is.available();
        for (int i=0;i<size;i++){
            System.out.println(is.read());
        }
        is.close();
    }

    public void readFile() throws Exception{
        String path = "/home/wlk/jdk/qqq";
        File file = new File(path);
        if(file.isDirectory()){
            String[] arr = file.list();
            for(int i=0;i<arr.length;i++){
                File f = new File(path + "/" +arr[i]);
                if(f.isDirectory()){
                    System.out.println("文件夹 ： "+arr[i]);
                }else{
                    System.out.println("文件 ： "+arr[i]);
                }
            }
        }
    }

    public void deleteFolder(File folder){
        File[] fileArr = folder.listFiles();
        if(fileArr != null){
            for(File f : fileArr){
                if(f.isDirectory()){
                    deleteFolder(f);
                }else{
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void paramSend(int a){
        a = 10;
        System.out.println(a);
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        int a = 1;
        test.paramSend(a);
        System.out.println(a);
    }
}

