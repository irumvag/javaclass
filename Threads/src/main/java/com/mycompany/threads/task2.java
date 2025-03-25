package com.mycompany.threads;
public class task2  /*implements Runnable*/extends Thread{
 public void run()
 {
     dosomething();
 }
 public void dosomething(){
 System.out.println("Perform task 1");
 }   
}
