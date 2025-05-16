package com.dineix.web.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Chairman
 */
public class DBConnection {
    public static Connection getConnection() throws SQLException, NamingException {
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/dineix");
        return ds.getConnection();
    }
}