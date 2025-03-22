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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the length of the rectangle: ");
        float length = scanner.nextFloat();
        System.out.print("Enter the width of the rectangle: ");
        float width = scanner.nextFloat();

        Quadriplateral quadrilateral = new Quadriplateral("John", "Rectangle");
        float area = quadrilateral.getArea(length, width);
        System.out.println("Area of Rectangle: " + area);
    }
}
