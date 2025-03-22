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

public class Trapezium implements Polygon {
    private float a, b, height;

    public Trapezium(float a, float b, float height) {
        this.a = a;
        this.b = b;
        this.height = height;
    }

    @Override
    public String getName() {
        return "Trapezium";
    }

    @Override
    public int getNumberOfSides() {
        return 4;
    }

    @Override
    public int totalIntAngleSum() {
        return (getNumberOfSides() - 2) * 180;
    }

    public float getArea() {
        Quadriplateral quadrilateral = new Quadriplateral();
        return quadrilateral.getArea(a, b, height);
    }

    public static void printThankYou() {
        System.out.println("Thank you!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the small base (a) of the trapezium: ");
        float a = scanner.nextFloat();
        System.out.print("Enter the big base (b) of the trapezium: ");
        float b = scanner.nextFloat();
        System.out.print("Enter the height of the trapezium: ");
        float height = scanner.nextFloat();

        Trapezium trapezium = new Trapezium(a, b, height);
        System.out.println("Name: " + trapezium.getName());
        System.out.println("Number of Sides: " + trapezium.getNumberOfSides());
        System.out.println("Total Interior Angle Sum: " + trapezium.totalIntAngleSum());
        System.out.println("Area of Trapezium: " + trapezium.getArea());

        printThankYou();
    }
}
