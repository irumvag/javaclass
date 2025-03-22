/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.threads;

/**
 *
 * @author Chairman
 */
public class SharedResources {
    int count=0;
//    public synchronised void increment()
//    {
//    count++;
//    }
    public static void main(String[] args)
    {
    SharedResources s1=new  SharedResources();
    Thread th1=new Thread(new Runnable()
    {
        @Override
        public void run()
        {
        }
    }
    );
    
    }
}
