package com.netcracker.edu.dao;

import com.netcracker.edu.util.PropertiesHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Zhassulan on 25.12.2015.
 */
public class DAOFactory {
    private static final Logger logger = LogManager.getLogger(DAOFactory.class);
    private static final DAObject DAO_INSTANCE;

    static {
        if ("OracleDAO".equals(PropertiesHandler.getDAO())) {
            DAO_INSTANCE = OracleDAO.getInstance();
            logger.trace("Factory initialized OracleDAO");
        } else if ("InMemoryDAO".equals(PropertiesHandler.getDAO())) {
            DAO_INSTANCE = InMemoryDAO.getInstance();
            logger.trace("Factory initialized InMemoryDAO");
        } else {
            DAO_INSTANCE = null;
            logger.error("Invalid DAO property value");
            System.exit(-1);
        }
        logger.trace("end of static block");
    }

    public static DAObject getDAObject() {
        return DAO_INSTANCE;
    }
}
