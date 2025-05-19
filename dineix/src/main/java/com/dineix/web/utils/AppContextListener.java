/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dineix.web.utils;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application context initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application context is shutting down...");

        // Shut down MySQL cleanup thread
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
            logger.info("Successfully shut down MySQL AbandonedConnectionCleanupThread.");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Interrupted while shutting down MySQL cleanup thread", e);
        }

        // Optional: manually close any DataSource
        Object ds = sce.getServletContext().getAttribute("dataSource");
        if (ds instanceof AutoCloseable) {
            try {
                ((AutoCloseable) ds).close();
                logger.info("DataSource closed successfully.");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to close DataSource", e);
            }
        }

        logger.info("Application context destroyed.");
    }
}
