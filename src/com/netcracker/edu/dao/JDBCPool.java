package com.netcracker.edu.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

import static com.netcracker.edu.util.PropertiesHandler.*;

/**
 * Created by Zhassulan on 08.01.2016.
 */
public class JDBCPool {
    private static final Logger logger = LogManager.getLogger(JDBCPool.class);
    private static final ArrayBlockingQueue<Connection> CONNECTIONS = new ArrayBlockingQueue<>(getJdbcPoolSize());
    private static final JDBCPool INSTANCE = new JDBCPool();


    private JDBCPool() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            for (int i = 0; i < CONNECTIONS.remainingCapacity(); i++) {
                Connection connection = new UnclosableConnection(DriverManager.getConnection(getDBURL(), getDatabaseLogin(), String.valueOf(getDatabasePassword())));
                CONNECTIONS.add(connection);
                logger.trace("CONNECTIONS.add: " + connection);

            }
        } catch (ClassNotFoundException | SQLException e) {
            //// TODO: 08.01.2016  
            logger.error(e);
            System.exit(-1);
        }
    }

    /* public static JDBCPool getInstance(){
         return INSTANCE;
     }*/
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = CONNECTIONS.take();
            logger.trace("getConnection: " + connection);
            return connection;
        } catch (InterruptedException e) {
            // TODO: 08.01.2016 correctly handle exception
            e.printStackTrace();
            System.exit(-1);
            return connection;
            // throw new UnhandledException(e);
        }

    }

    // TODO: 08.01.2016 Нужно ли защититься от возврата постороннего конекта ?
    public static void releaseConnection(Connection connection) {
        try {
            CONNECTIONS.put(connection);
        } catch (InterruptedException e) {
            // TODO: 08.01.2016  correctly handle exception
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
