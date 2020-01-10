package thread;

public class RunnableDemo implements Runnable {
    public static void main(String[] args) {
        RunnableDemo runnableDemo = new RunnableDemo();
        new Thread(runnableDemo,"wlk").start();
        new ThreadDemo("aaa").start();
        runnableDemo.run();
    }

    @Override
    public void run() {
        System.out.println("执行了线程的方法---"+Thread.currentThread().getName());
    }
}

class ThreadDemo extends Thread{
    public ThreadDemo(String name){
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println(this.getId()+"---"+this.getName()+"---"+this.getState());
    }
}
