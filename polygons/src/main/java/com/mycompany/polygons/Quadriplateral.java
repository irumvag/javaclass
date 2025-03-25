/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polygons;

/**
 *
 * @author Chairman
 */
public class Quadriplateral /*implements Polygon*/{
      String Name;
      public Quadriplateral(String Name){
      this.Name=Name;
      System.out.println("The Type of Quadrilateral:"+this.Name);
      }
      public Quadriplateral(){
      System.out.println("Hello Student");
      }
      public Quadriplateral(String user,String Name){
      System.out.println("The name of user "+user+" The type of Quadrilateral: "+Name);
      }
      public float getArea(float side)
      {
          return side*side;
      }
      public float getArea(float length, float width)
      {
      return length*width;
      }
      public float getArea(float a, float b, float h)
      {
          return ((a+b)*h)/2;
      }
}
