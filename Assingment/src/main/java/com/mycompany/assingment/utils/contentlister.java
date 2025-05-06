package com.mycompany.assingment.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class contentlister implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialization code if needed
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Deregister JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Stop any background threads if needed
        // Example:
        // if (someThread != null) {
        //     someThread.interrupt();
        // }
    }
}

