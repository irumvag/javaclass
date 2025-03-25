/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polygons;

/**
 *
 * @author Chairman
 */
import java.util.Scanner;

public class Rectangle {
    public static void main(String[] args) {
        try{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the length of the rectangle: ");
        float length = scanner.nextFloat();
        System.out.print("Enter the width of the rectangle: ");
        float width = scanner.nextFloat();
        if (length <= 0 || width <= 0) {
                throw new IllegalArgumentException("Length and width must be greater than zero.");
        }
        Quadriplateral quadrilateral = new Quadriplateral("Gad", "Rectangle");
        if (quadrilateral == null) {
                throw new NullPointerException("Quadrilateral object is not initialized.");
            }
        float area = quadrilateral.getArea(length, width);
        System.out.println("Area of Rectangle: " + area);
        }catch(Exception e)
        {
            System.out.println("Error happened");
        }
    }
}
