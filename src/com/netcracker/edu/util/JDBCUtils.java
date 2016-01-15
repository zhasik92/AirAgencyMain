package com.netcracker.edu.util;

import com.netcracker.edu.dao.JDBCPool;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Util class for OracleDAO
 * Created by Zhassulan on 08.01.2016.
 */
public class JDBCUtils {
    private static final Logger logger = LogManager.getLogger(JDBCUtils.class);

    public static int execUpdate(Connection conn, Statement st, String sql) throws SQLException {
        try {
            if (st != null) {
                return st.executeUpdate(sql);
            }
            logger.warn("statement is null");
            return -1;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                close(st);
            } finally {
                JDBCPool.releaseConnection(conn);
            }
        }
    }

    public static int execUpdate(Connection conn, PreparedStatement ps) throws SQLException {
        try {
            if (ps != null) {
                return ps.executeUpdate();
            }
            logger.warn("prepared statement is null");
            return -1;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            try {
                close(ps);
            } finally {
                JDBCPool.releaseConnection(conn);
            }
        }
    }

   /* public static void execQuery(Connection conn, PreparedStatement st, ResultHandler rh) throws SQLException {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
        }
    }*/

    public static void releaseResources(Connection conn){
       JDBCPool.releaseConnection(conn);
    }
    public static void releaseResources(Connection conn,Statement st) throws SQLException{
        try{
            close(st);
        }finally {
            JDBCPool.releaseConnection(conn);
        }
    }

    private static void close(Statement st) throws SQLException {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    private static void close(ResultSet rs) throws SQLException {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    public static void handleResultSet(ResultSet rs) throws SQLException {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
        }
    }
}
