/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.threads;

/**
 *
 * @author Chairman
 */
public class task1 extends Thread /*implements Runnable*/ {
    public void run()
    {
        print();
    }
    public void print(){
    System.out.println("Printing from task 1");
    try
    {
        Thread.sleep(5000);
    }catch(Exception e)
    {
        e.printStackTrace();
    }
    
    }
    
}
