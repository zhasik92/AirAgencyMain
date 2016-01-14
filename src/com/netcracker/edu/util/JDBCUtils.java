package com.netcracker.edu.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Util class for OracleDAO
 * Created by Zhassulan on 08.01.2016.
 */
public class JDBCUtils {
    private static final Logger logger = LogManager.getLogger(JDBCUtils.class);

    public static void handleStatement(Statement st) throws SQLException {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
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
