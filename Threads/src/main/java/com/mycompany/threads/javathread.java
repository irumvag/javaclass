package com.mycompany.threads;
public class javathread {
    public static void main(String[] args)
    {
    task1 t1=new task1();
    task2 t2=new task2();
    //    t1.print();
    //    t2.dosomething();
    /*Thread th1=new Thread(t1);
    Thread th2=new Thread(t2);
    th1.start();
    th2.start();*/
    t1.start();
    //t1.join();
    t2.start();
    }
}
