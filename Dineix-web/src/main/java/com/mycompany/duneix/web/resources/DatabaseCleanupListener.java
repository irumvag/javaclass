/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.duneix.web.resources;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class DatabaseCleanupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // No action needed on initialization
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Shut down MySQL abandoned connection cleanup thread
        AbandonedConnectionCleanupThread.checkedShutdown();

        // Deregister JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                sce.getServletContext().log("Error deregistering driver: " + driver, e);
            }
        }
    }
}
