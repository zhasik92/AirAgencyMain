package com.netcracker.edu.util;

import com.netcracker.edu.dao.JDBCPool;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class IdGenerator {
    private static IdGenerator instance;
    private static volatile BigInteger idCounter;

    private IdGenerator() {
        {
            // TODO: 13.01.2016  
            /*File file = new File("idGen.out");
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file); ObjectInputStream oin = new ObjectInputStream(fis)) {
                    idCounter = (BigInteger) oin.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                idCounter = BigInteger.ZERO;
            }*/
            Connection connection= JDBCPool.getConnection();
            ResultSet rs=null;
            try(Statement st=connection.createStatement()){
                st.executeQuery("SELECT MAX(T) FROM(select MAX(FLIGHTS.ID) as T FROM FLIGHTS,PASSENGERS,TICKETS UNION select MAX(PASSENGERS.ID) " +
                        " FROM FLIGHTS,PASSENGERS,TICKETS UNION select MAX(TICKETS.ID) FROM FLIGHTS,PASSENGERS,TICKETS)");
                rs=st.getResultSet();
                if(rs.next()){
                    idCounter= rs.getBigDecimal(1)==null?BigInteger.ONE:rs.getBigDecimal(1).toBigInteger();
                }
                rs.close();
            }catch (SQLException e){
                e.printStackTrace();

            }finally {
                JDBCPool.releaseConnection(connection);

            }
        }
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public synchronized BigInteger getId() {
        idCounter = idCounter.add(BigInteger.ONE);
        return idCounter;
    }
}
