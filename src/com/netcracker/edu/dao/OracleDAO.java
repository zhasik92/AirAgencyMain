package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

/**
 * Oracle Data Access Object.
 * Created by Zhassulan on 25.12.2015.
 */
public class OracleDAO implements DAObject {
    private static final Logger logger = LogManager.getLogger(OracleDAO.class);
    private static final String DBURL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String LOGIN = "ADMIN";
    private static final String PASSWORD = "ADMIN";
    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
    private FastDateFormat timeFormat = FastDateFormat.getInstance("HH:mm:ss");
    private static OracleDAO instance;
    private Connection conn;

    private OracleDAO() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(DBURL, LOGIN, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
        }
    }

    public static synchronized OracleDAO getInstance() {
        if (instance == null) {
            instance = new OracleDAO();
        }
        return instance;
    }

    @Override
    public void addAirplane(Airplane airplane) throws SQLException {
        if (airplane == null) {
            throw new IllegalArgumentException("airplane can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("INSERT INTO AIRPLANE (NAME,CAPACITY) VALUES ('" + airplane.getName() + "', '" + airplane.getCapacity() + "')");
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addCity(City city) throws SQLException {
        if (city == null) {
            throw new IllegalArgumentException("city can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("INSERT INTO CITIES (NAME) VALUES ('" + city.getName() + "')");
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addFlight(Flight flight) throws SQLException {
        if (flight == null) {
            throw new IllegalArgumentException("flight can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("INSERT INTO FLIGHTS (ID,DEP_AIRPORT,ARR_AIRPORT,DEP_TIME,ARR_TIME,AIRPLANE_ID, PRICE) VALUES " +
                    "('" + flight.getId() + "', '" + flight.getDepartureAirportName() + "', '" + flight.getArrivalAirportName() + "',TO_TIMESTAMP( '" +
                    timeFormat.format(flight.getDepartureTime()) + "','HH24:MI:SS'),TO_TIMESTAMP( '" + timeFormat.format(flight.getArrivalTime()) + "','HH24:MI:SS'), '" + flight.getAirplaneName() + "', " +
                    flight.getPrice() + ")");
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addPassenger(Passenger passenger) throws SQLException {
        if (passenger == null) {
            throw new IllegalArgumentException("passenger can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("INSERT INTO PASSENGERS (ID,EMAIL,FIRST_NAME,LAST_NAME,DATE_OF_BIRTH,PASSPORT_NUMBER,CITIZENSHIP)" +
                    " VALUES (" + passenger.getId() + ", '" + passenger.getEmail() + "', '" + passenger.getFirstName() + "', '" +
                    passenger.getLastName() + "', TO_DATE('" + dateFormat.format(passenger.getDateOfBirth()) + "','YYYY-MM-DD'), '" + passenger.getPassportNumber()
                    + "', '" + passenger.getCitizenship() + "')");
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addTicket(Ticket ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("INSERT INTO TICKETS (ID,PASSENGER_ID,FLIGHT_ID,STATUS,FLIGHT_DATE,TICKET_BOUGHT_DATE) VALUES ('" +
                    ticket.getId() + "', '" + ticket.getPassengerId() + "', '" + ticket.getFlightId() + "', '" +
                    (ticket.isCanceled() ? 1 : 0) + "'," +
                    "TO_TIMESTAMP('" + new Timestamp(ticket.getFlightDate().getTime().getTime()) + "','YYYY-MM-DD'), " +
                    "TO_TIMESTAMP('" + new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()) + "','YYYY-MM-DD'))");
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addAllTickets(Collection<Ticket> tickets) throws SQLException {
        if (tickets == null) {
            throw new IllegalArgumentException("tickets can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            conn.setAutoCommit(false);
            for (Ticket ticket : tickets) {
                st.executeQuery("INSERT INTO TICKETS (ID,PASSENGER_ID,FLIGHT_ID,STATUS,FLIGHT_DATE,TICKET_BOUGHT_DATE) VALUES ('" +
                        ticket.getId() + "', '" + ticket.getPassengerId() + "', '" + ticket.getFlightId() + "', '" +
                        (ticket.isCanceled() ? 1 : 0) + "'," +
                        "TO_TIMESTAMP('" + new Timestamp(ticket.getFlightDate().getTime().getTime()) + "','YYYY-MM-DD HH24:MI:SS.FF'), " +
                        "TO_TIMESTAMP('" + new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()) + "','YYYY-MM-DD HH24:MI:SS.FF'))");
            }
            conn.commit();
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        Statement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            st.executeQuery("INSERT INTO USERS (LOGIN,PASSWORD,ROLE) VALUES ('" + user.getLogin() + "', '" + String.copyValueOf(user.getPassword()) +
                    "', '" + user.role() + "')");
            for (BigInteger it : user.getTickets()) {
                st.executeQuery("INSERT INTO USERS_TICKETS (USER_LOGIN,TICKET_ID) VALUES ('" + user.getLogin() + "', " + it + ")");
            }
            conn.commit();
        } catch (SQLException sqle) {
            logger.error(sqle);
            conn.rollback();
            throw sqle;
        } finally {
            conn.setAutoCommit(true);
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public Collection<Airplane> getAllAirplanes() throws SQLException {
        HashSet<Airplane> airplanes = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM AIRPLANE");
            rs = st.getResultSet();
            while (rs.next()) {
                airplanes.add(new Airplane(rs.getString(1), rs.getInt(2)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return airplanes;
    }

    @Override
    public Collection<City> getAllCities() throws SQLException {
        HashSet<City> cities = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM CITIES");
            rs = st.getResultSet();
            while (rs.next()) {
                cities.add(new City(rs.getString(1)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return cities;
    }

    @Override
    public Collection<Flight> getAllFlights() throws SQLException {
        HashSet<Flight> flights = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM FLIGHTS");
            rs = st.getResultSet();
            while (rs.next()) {
                flights.add(new Flight(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                        new Time(rs.getTimestamp(4).getTime()), new Time(rs.getTime(5).getTime()), rs.getString(6), rs.getDouble(7)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return flights;
    }

    @Override
    //tested
    public Collection<Passenger> getAllPassengers() throws SQLException {
        HashSet<Passenger> passengers = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM PASSENGERS");
            rs = st.getResultSet();
            while (rs.next()) {
                passengers.add(new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),
                        rs.getString(6), rs.getString(7)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return passengers;
    }

    @Override
    //tested
    public Collection<Ticket> getAllTickets() throws SQLException {
        HashSet<Ticket> tickets = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM TICKETS");
            rs = st.getResultSet();
            while (rs.next()) {
                Calendar flightDate = Calendar.getInstance();
                flightDate.setTime(rs.getDate(5));
                Calendar ticketBoughtDate = Calendar.getInstance();
                ticketBoughtDate.setTime(rs.getDate(6));
                tickets.add(new Ticket(BigInteger.valueOf(rs.getLong(1)), BigInteger.valueOf(rs.getLong(2)), BigInteger.valueOf(rs.getLong(3)), rs.getByte(4) == 1,
                        flightDate, ticketBoughtDate));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return tickets;
    }

    @Override
    //tested
    public Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar flightDate) throws SQLException {
        HashSet<Ticket> tickets = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM TICKETS WHERE FLIGHT_ID=" + flightId + " AND  TRUNC(FLIGHT_DATE,'DD')=TO_DATE('" + dateFormat.format(flightDate) + "','YYYY-MM-DD')"
                    + " AND STATUS=1");
            rs = st.getResultSet();
            while (rs.next()) {
                Calendar flightDate1 = Calendar.getInstance();
                flightDate1.setTime(rs.getDate(5));
                Calendar ticketBoughtDate = Calendar.getInstance();
                ticketBoughtDate.setTime(rs.getDate(6));
                tickets.add(new Ticket(BigInteger.valueOf(rs.getLong(1)), BigInteger.valueOf(rs.getLong(2)), BigInteger.valueOf(rs.getLong(3)), rs.getByte(4) == 1,
                        flightDate1, ticketBoughtDate));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return tickets;
    }

    @Override
    public int getNumberOfSoldTicketsInFlight(BigInteger flightId, Calendar flightDate) throws SQLException {
        int result;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT COUNT(*) FROM TICKETS WHERE FLIGHT_ID=" + flightId + " AND  " +
                    "TRUNC(FLIGHT_DATE,'DD')=TO_DATE('" + dateFormat.format(flightDate) + "','YYYY-MM-DD') AND STATUS=0");
            rs = st.getResultSet();
            rs.next();
            result = rs.getInt(1);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return result;
    }

    @Override
    public Collection<User> getAllUsers() throws SQLException {
        HashSet<User> users = new HashSet<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM USERS");
            rs = st.getResultSet();
            while (rs.next()) {
                users.add(new User(rs.getString(1), rs.getString(2).toCharArray(), User.Roles.valueOf(rs.getString(3))));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return users;
    }

    @Override
    public Airplane findAirplaneByName(String airplane) throws SQLException {
        if (airplane == null || airplane.isEmpty()) {
            throw new IllegalArgumentException("airplane can't be null or empty");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM AIRPLANE WHERE NAME= '" + airplane + "'");
            rs = st.getResultSet();
            if (rs.next()) {
                return new Airplane(rs.getString(1), rs.getInt(2));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public City findCityByName(String city) throws SQLException {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("city can't be null or empty");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM CITIES WHERE NAME= '" + city + "'");
            rs = st.getResultSet();
            if (rs.next()) {
                return new City(rs.getString(1));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public Flight findFlightById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM FLIGHTS WHERE ID= " + id);
            rs = st.getResultSet();
            if (rs.next()) {
                return new Flight(BigInteger.valueOf(rs.getLong(1)), rs.getString(2),
                        rs.getString(3), new Time(rs.getTimestamp(4).getTime()), new Time(rs.getTime(5).getTime()), rs.getString(6), rs.getDouble(7));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public Passenger findPassengerById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM PASSENGERS WHERE ID= " + id);
            rs = st.getResultSet();
            if (rs.next()) {
                return new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) throws SQLException {
        if (passportNumber == null || citizenship == null || passportNumber.isEmpty() || citizenship.isEmpty()) {
            throw new IllegalArgumentException("passportNumber/citizenship can't be null or empty");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM PASSENGERS WHERE PASSPORT_NUMBER= '" + passportNumber + "' AND CITIZENSHIP= '" + citizenship + "'");
            rs = st.getResultSet();
            if (rs.next()) {
                return new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public Ticket findTicketById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM TICKETS WHERE ID= " + id);
            rs = st.getResultSet();
            if (rs.next()) {
                Calendar flightDate = Calendar.getInstance();
                flightDate.setTime(rs.getDate(5));
                Calendar ticketBoughtDate = Calendar.getInstance();
                ticketBoughtDate.setTime(rs.getDate(6));
                return new Ticket(BigInteger.valueOf(rs.getLong(1)), BigInteger.valueOf(rs.getLong(2)), BigInteger.valueOf(rs.getLong(3)), rs.getByte(4) == 1,
                        flightDate, ticketBoughtDate);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public User findUserByLogin(String login) throws SQLException {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("login can't be null or empty");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery("SELECT * FROM USERS WHERE LOGIN='" + login + "'");
            rs = st.getResultSet();
            if (rs.next()) {
                return new User(rs.getString(1), rs.getString(2).toCharArray(), User.Roles.valueOf(rs.getString(3)));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return null;
    }

    @Override
    public void updateAirplane(Airplane airplane) throws SQLException {
        if (airplane == null) {
            throw new IllegalArgumentException("airplane can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("UPDATE AIRPLANE SET CAPACITY= " + airplane.getCapacity() + " WHERE NAME='" + airplane.getName() + "'");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void updateCity(City city) throws SQLException {
        if (city == null) {
            throw new IllegalArgumentException("city can't be null");
        }
        //Just stub
    }

    @Override
    public void updateFlight(Flight flight) throws SQLException {
        if (flight == null) {
            throw new IllegalArgumentException("flight can't be null");
        }
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("UPDATE FLIGHTS SET DEP_AIRPORT= '" + flight.getDepartureAirportName() + "', ARR_AIRPORT='" + flight.getArrivalAirportName() +
                    "', DEP_TIME=" + flight.getDepartureTime() + "', ARR_TIME=" + flight.getArrivalTime() + "', AIRPLANE_ID='" + flight.getAirplaneName() +
                    "', PRICE=" + flight.getPrice() + " WHERE ID=" + flight.getId());
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void updatePassenger(Passenger passenger) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("UPDATE PASSENGERS SET EMAIL='" + passenger.getEmail() +
                    "', FIRST_NAME='" + passenger.getFirstName() + "', LAST_NAME='" + passenger.getLastName() +
                    "', DATE_OF_BIRTH=" + passenger.getDateOfBirth() + ", PASSPORT_NUMBER='" + passenger.getPassportNumber() +
                    "', CITIZENSHIP='" + passenger.getCitizenship() + "' WHERE ID=" + passenger.getId());
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void updateTicket(Ticket ticket) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeQuery("UPDATE TICKETS SET PASSENGER_ID=" + ticket.getPassengerId() +
                    ", FLIGHT_ID=" + ticket.getFlightId() + ", STATUS='" + (ticket.isCanceled() ? 1 : 0) +
                    "', FLIGHT_DATE=TO_TIMESTAMP('" + new Timestamp(ticket.getFlightDate().getTime().getTime()) + "','YYYY-MM-DD')" +
                    ", TICKET_BOUGHT_DATE=TO_TIMESTAMP('" + new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()) + "','YYYY-MM-DD')" +
                    " WHERE ID=" + ticket.getId());
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            conn.setAutoCommit(false);
            st.executeQuery("UPDATE USERS SET PASSWORD='" + String.copyValueOf(user.getPassword()) + "', ROLE='" + user.role() + "' WHERE LOGIN='" + user.getLogin() + "'");
            for (BigInteger it : user.getTickets()) {
                st.executeQuery("call if_empty_USERS_TICKETS(" + it + ",'" + user.getLogin() + "')");
            }
            conn.commit();
        } catch (SQLException sqle) {
            logger.error(sqle);
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.error("transaction in updateUSER() rolled back, user's login: " + user.getLogin());
                } catch (SQLException ex) {
                    logger.error(ex);
                }
            }
            throw sqle;
        } finally {
            conn.setAutoCommit(true);
            if (st != null) {
                st.close();
            }
        }
    }
}
