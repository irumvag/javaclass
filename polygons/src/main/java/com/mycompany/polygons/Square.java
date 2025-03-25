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

public class Square {
    public static void main(String[] args) {
        try{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the side length of the square: ");
        float side = scanner.nextFloat();
        if (side <= 0) {
                throw new IllegalArgumentException("Side length must be greater than zero.");
        }
        Quadriplateral quadrilateral = new Quadriplateral("Square");
        if (quadrilateral == null) {
                throw new NullPointerException("Quadrilateral object is not initialized.");
            }
        float area = quadrilateral.getArea(side);
        System.out.println("Area of Square: " + area);
        }
        catch(Exception e)
        {
            System.out.println("Error message"+e);
        }
    }
}
