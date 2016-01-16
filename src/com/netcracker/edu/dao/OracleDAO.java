package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

/**
 * Oracle Data Access Object.
 * Created by Zhassulan on 25.12.2015.
 */
// TODO: 11.01.2016 Increase granularity of using connection, (FE: create BigDecimal outside of connection blocking)
public class OracleDAO implements DAObject {
    private static final Logger logger = LogManager.getLogger(OracleDAO.class);
    private static final OracleDAO INSTANCE = new OracleDAO();
    private final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
    private FastDateFormat timeFormat = FastDateFormat.getInstance("HH:mm:ss");

    public static OracleDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void addAirplane(Airplane airplane) throws SQLException {
        if (airplane == null) {
            throw new IllegalArgumentException("airplane can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO AIRPLANE (NAME, CAPACITY) VALUES (?,?)")) {
            ps.setString(1, airplane.getName());
            ps.setInt(2, airplane.getCapacity());
            ps.executeUpdate();
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
        }
    }

    @Override
    public void addCity(City city) throws SQLException {
        if (city == null) {
            throw new IllegalArgumentException("city can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement st = connection.prepareStatement("INSERT INTO CITIES (NAME) VALUES(?)")) {
            st.setString(1, city.getName());
            st.executeUpdate();
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    @Override
    public void addFlight(Flight flight) throws SQLException {
        if (flight == null) {
            throw new IllegalArgumentException("flight can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO FLIGHTS (ID,DEP_AIRPORT,ARR_AIRPORT,DEP_TIME,ARR_TIME,AIRPLANE_ID, PRICE) VALUES (?,?,?,TO_TIMESTAMP(?,'HH24:MI:SS'),TO_TIMESTAMP(?,'HH24:MI:SS'),?,?)")) {
            ps.setBigDecimal(1, new BigDecimal(flight.getId()));
            ps.setString(2, flight.getDepartureAirportName());
            ps.setString(3, flight.getArrivalAirportName());
            ps.setString(4, timeFormat.format(flight.getDepartureTime()));
            ps.setString(5, timeFormat.format(flight.getArrivalTime()));
            ps.setString(6, flight.getAirplaneName());
            ps.setDouble(7, flight.getPrice());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    @Override
    public void addPassenger(Passenger passenger) throws SQLException {
        if (passenger == null) {
            throw new IllegalArgumentException("passenger can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO PASSENGERS (ID,EMAIL,FIRST_NAME,LAST_NAME,DATE_OF_BIRTH,PASSPORT_NUMBER,CITIZENSHIP) VALUES(?,?,?,?,TO_DATE(?,'YYYY-MM-DD'),?,?)")) {
            ps.setBigDecimal(1, new BigDecimal(passenger.getId()));
            ps.setString(2, passenger.getEmail());
            ps.setString(3, passenger.getFirstName());
            ps.setString(4, passenger.getLastName());
            ps.setString(5, dateFormat.format(passenger.getDateOfBirth()));
            ps.setString(6, passenger.getPassportNumber());
            ps.setString(7, passenger.getCitizenship());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    @Override
    public void addTicket(Ticket ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO TICKETS (ID,PASSENGER_ID,FLIGHT_ID,STATUS,FLIGHT_DATE,TICKET_BOUGHT_DATE) VALUES(?,?,?,?,TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF'))")) {
            ps.setBigDecimal(1, new BigDecimal(ticket.getId()));
            ps.setBigDecimal(2, new BigDecimal(ticket.getPassengerId()));
            ps.setBigDecimal(3, new BigDecimal(ticket.getFlightId()));
            ps.setInt(4, (ticket.isCanceled() ? 1 : 0));
            // TODO: 10.01.2016
            ps.setString(5, new Timestamp(ticket.getFlightDate().getTime().getTime()).toString());
            ps.setString(6, new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()).toString());
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    @Override
    public void addAllTickets(Collection<Ticket> tickets) throws SQLException {
        if (tickets == null) {
            throw new IllegalArgumentException("tickets can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO TICKETS (ID,PASSENGER_ID,FLIGHT_ID,STATUS,FLIGHT_DATE,TICKET_BOUGHT_DATE) VALUES(?,?,?,?,TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF'),TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF'))")) {
            try {
                connection.setAutoCommit(false);
                for (Ticket ticket : tickets) {
                    ps.setBigDecimal(1, new BigDecimal(ticket.getId()));
                    ps.setBigDecimal(2, new BigDecimal(ticket.getPassengerId()));
                    ps.setBigDecimal(3, new BigDecimal(ticket.getFlightId()));
                    ps.setInt(4, (ticket.isCanceled() ? 1 : 0));
                    // TODO: 10.01.2016
                    ps.setString(5, new Timestamp(ticket.getFlightDate().getTime().getTime()).toString());
                    ps.setString(6, new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()).toString());
                    ps.executeUpdate();
                }
                connection.commit();
            } catch (SQLException e) {
                logger.error(e);
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement psUser = connection.prepareStatement("INSERT INTO USERS (LOGIN,PASSWORD,ROLE) VALUES (?,?,?)");
             PreparedStatement psTicket = connection.prepareStatement("INSERT INTO USERS_TICKETS (USER_LOGIN,TICKET_ID) VALUES (?,?)")) {
            try {
                connection.setAutoCommit(false);
                psUser.setString(1, user.getLogin());
                psUser.setString(2, String.valueOf(user.getPassword()));
                psUser.setString(3, user.role().toString());
                psUser.executeUpdate();
                for (BigInteger it : user.getTickets()) {
                    psTicket.setString(1, user.getLogin());
                    psTicket.setBigDecimal(2, new BigDecimal(it));
                    psTicket.executeUpdate();
                }
                connection.commit();
            } catch (SQLException sqle) {
                logger.error(sqle);
                connection.rollback();
                throw sqle;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public Collection<Airplane> getAllAirplanes() throws SQLException {
        HashSet<Airplane> airplanes = new HashSet<>();
        try (Connection connection = JDBCPool.getConnection();
             Statement st = connection.createStatement()) {
            st.executeQuery("SELECT NAME, CAPACITY FROM AIRPLANE");
            try (ResultSet rs = st.getResultSet()) {
                while (rs.next()) {
                    airplanes.add(new Airplane(rs.getString(1), rs.getInt(2)));
                }
            }
            return airplanes;
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
        }
    }

    @Override
    public Collection<City> getAllCities() throws SQLException {
        HashSet<City> cities = new HashSet<>();
        try (Connection connection = JDBCPool.getConnection();
             Statement st = connection.createStatement()) {
            st.executeQuery("SELECT NAME FROM CITIES");
            try (ResultSet rs = st.getResultSet()) {
                while (rs.next()) {
                    cities.add(new City(rs.getString(1)));
                }
            }
            return cities;
        } catch (SQLException sql) {
            logger.error(sql);
            throw sql;
        }
    }

    @Override
    public Collection<Flight> getAllFlights() throws SQLException {
        HashSet<Flight> flights = new HashSet<>();
        try (Connection connection = JDBCPool.getConnection();
             Statement st = connection.createStatement()) {
            st.executeQuery("SELECT ID,DEP_AIRPORT,ARR_AIRPORT,DEP_TIME,ARR_TIME,AIRPLANE_ID,PRICE FROM FLIGHTS");
            try (ResultSet rs = st.getResultSet()) {
                while (rs.next()) {
                    flights.add(new Flight(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                            new Time(rs.getTimestamp(4).getTime()), new Time(rs.getTime(5).getTime()), rs.getString(6), rs.getDouble(7)));
                }
            }
            return flights;
        } catch (SQLException sqle) {
            logger.error(sqle);
            throw sqle;
        }
    }

    @Override
    //tested
    // TODO: 11.01.2016 do i need this method?
    public Collection<Passenger> getAllPassengers() throws SQLException {
        HashSet<Passenger> passengers = new HashSet<>();
        try (Connection connection = JDBCPool.getConnection();
             Statement st = connection.createStatement()) {
            st.executeQuery("SELECT ID,EMAIL,FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER, CITIZENSHIP FROM PASSENGERS");
            try (ResultSet rs = st.getResultSet()) {
                while (rs.next()) {
                    passengers.add(new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),
                            rs.getString(6), rs.getString(7)));
                }
            }
            return passengers;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    //tested
    // TODO: 11.01.2016 do i need this method?
    public Collection<Ticket> getAllTickets() throws SQLException {
        HashSet<Ticket> tickets = new HashSet<>();
        try (Connection connection = JDBCPool.getConnection();
             Statement st = connection.createStatement()) {
            st.executeQuery("SELECT ID,PASSENGER_ID,FLIGHT_ID,STATUS,FLIGHT_DATE,TICKET_BOUGHT_DATE FROM TICKETS");
            try (ResultSet rs = st.getResultSet()) {
                while (rs.next()) {
                    Calendar flightDate = Calendar.getInstance();
                    flightDate.setTime(rs.getDate(5));
                    Calendar ticketBoughtDate = Calendar.getInstance();
                    ticketBoughtDate.setTime(rs.getDate(6));
                    tickets.add(new Ticket(BigInteger.valueOf(rs.getLong(1)), BigInteger.valueOf(rs.getLong(2)), BigInteger.valueOf(rs.getLong(3)), rs.getByte(4) == 1,
                            flightDate, ticketBoughtDate));
                }
            }
            return tickets;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    //tested
    // TODO: 11.01.2016 do i need this method?
    public Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar flightDate) throws
            SQLException {
        /*HashSet<Ticket> tickets = new HashSet<>();
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
        return tickets;*/
        throw new SQLException("Stub");
    }

    @Override
    public int getNumberOfSoldTicketsInFlight(BigInteger flightId, Calendar flightDate) throws SQLException {
        int result;
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM TICKETS WHERE FLIGHT_ID=? AND TRUNC(FLIGHT_DATE,'DD')=TO_DATE(?,'YYYY-MM-DD') AND STATUS=0")) {
            ps.setBigDecimal(1, new BigDecimal(flightId));
            ps.setString(2, dateFormat.format(flightDate));
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                rs.next();
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    // TODO: 11.01.2016 do i need this method?
    public Collection<User> getAllUsers() throws SQLException {
       /* HashSet<User> users = new HashSet<>();
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
        return users;*/
        throw new SQLException("stub");
    }

    @Override
    public Airplane findAirplaneByName(String airplane) throws SQLException {
        if (airplane == null || airplane.isEmpty()) {
            throw new IllegalArgumentException("airplane can't be null or empty");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT NAME,CAPACITY FROM AIRPLANE WHERE NAME=?")) {
            ps.setString(1, airplane);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new Airplane(rs.getString(1), rs.getInt(2));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public City findCityByName(String city) throws SQLException {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("city can't be null or empty");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT NAME FROM CITIES WHERE NAME=?")) {
            ps.setString(1, city);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new City(rs.getString(1));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Flight findFlightById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT ID, DEP_AIRPORT, ARR_AIRPORT, DEP_TIME, ARR_TIME, AIRPLANE_ID, PRICE FROM FLIGHTS WHERE ID=?")) {
            ps.setBigDecimal(1, new BigDecimal(id));
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new Flight(BigInteger.valueOf(rs.getLong(1)), rs.getString(2),
                            rs.getString(3), new Time(rs.getTimestamp(4).getTime()), new Time(rs.getTime(5).getTime()), rs.getString(6), rs.getDouble(7));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Passenger findPassengerById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT ID,EMAIL, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER, CITIZENSHIP FROM PASSENGERS WHERE ID=?")) {
            ps.setBigDecimal(1, new BigDecimal(id));
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) throws
            SQLException {
        if (passportNumber == null || citizenship == null || passportNumber.isEmpty() || citizenship.isEmpty()) {
            throw new IllegalArgumentException("passportNumber/citizenship can't be null or empty");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT ID,EMAIL, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, PASSPORT_NUMBER, CITIZENSHIP FROM PASSENGERS WHERE PASSPORT_NUMBER=? AND CITIZENSHIP=?")) {
            ps.setString(1, passportNumber);
            ps.setString(2, citizenship);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new Passenger(BigInteger.valueOf(rs.getLong(1)), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getDate(5), rs.getString(6), rs.getString(7));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Ticket findTicketById(BigInteger id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("id can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT ID, PASSENGER_ID,FLIGHT_ID,STATUS, FLIGHT_DATE, TICKET_BOUGHT_DATE FROM TICKETS WHERE ID=?")) {
            ps.setBigDecimal(1, new BigDecimal(id));
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    Calendar flightDate = Calendar.getInstance();
                    flightDate.setTime(rs.getDate(5));
                    Calendar ticketBoughtDate = Calendar.getInstance();
                    ticketBoughtDate.setTime(rs.getDate(6));
                    return new Ticket(BigInteger.valueOf(rs.getLong(1)), BigInteger.valueOf(rs.getLong(2)), BigInteger.valueOf(rs.getLong(3)), rs.getByte(4) == 1,
                            flightDate, ticketBoughtDate);
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public User findUserByLogin(String login) throws SQLException {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("login can't be null or empty");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT LOGIN, PASSWORD, ROLE FROM USERS WHERE LOGIN=?")) {
            ps.setString(1, login);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return new User(rs.getString(1), rs.getString(2).toCharArray(), User.Roles.valueOf(rs.getString(3)));
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void updateAirplane(Airplane airplane) throws SQLException {
        if (airplane == null) {
            throw new IllegalArgumentException("airplane can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE AIRPLANE SET CAPACITY= ? WHERE NAME=?")) {
            ps.setInt(1, airplane.getCapacity());
            ps.setString(2, airplane.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    // TODO: 11.01.2016 do i need this method?
    public void updateCity(City city) throws SQLException {
        if (city == null) {
            throw new IllegalArgumentException("city can't be null");
        }
        throw new SQLException("stub");
    }

    @Override
    // TODO: 11.01.2016
    public void updateFlight(Flight flight) throws SQLException {
       /* if (flight == null) {
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
        }*/
        throw new SQLException("stub");
    }

    @Override
    public void updatePassenger(Passenger passenger) throws SQLException {
        if (passenger == null) {
            throw new IllegalArgumentException("passenger ca't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE PASSENGERS SET EMAIL=?, FIRST_NAME=?, LAST_NAME=?, DATE_OF_BIRTH=?, PASSPORT_NUMBER=?, CITIZENSHIP=? WHERE ID=?")) {
            ps.setString(1, passenger.getEmail());
            ps.setString(2, passenger.getFirstName());
            ps.setString(3, passenger.getLastName());
            ps.setDate(4, passenger.getDateOfBirth());
            ps.setString(5, passenger.getPassportNumber());
            ps.setString(6, passenger.getCitizenship());
            ps.setBigDecimal(7, new BigDecimal(passenger.getId()));
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void updateTicket(Ticket ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE TICKETS SET PASSENGER_ID=?," +
                     " FLIGHT_ID=?," +
                     " STATUS=?," +
                     " FLIGHT_DATE=TO_TIMESTAMP(?,'YYYY-MM-DD')," +
                     " TICKET_BOUGHT_DATE=TO_TIMESTAMP(?,'YYYY-MM-DD') WHERE ID=?")) {
            ps.setBigDecimal(1, new BigDecimal(ticket.getPassengerId()));
            ps.setBigDecimal(2, new BigDecimal(ticket.getFlightId()));
            ps.setInt(3, (ticket.isCanceled() ? 1 : 0));
            ps.setString(4, new Timestamp(ticket.getFlightDate().getTime().getTime()).toString());
            ps.setString(5, new Timestamp(ticket.getTicketBoughtDate().getTime().getTime()).toString());
            ps.setBigDecimal(6, new BigDecimal(ticket.getId()));
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        try (Connection connection = JDBCPool.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE USERS SET PASSWORD=?, ROLE=? WHERE LOGIN=?");
             CallableStatement pc = connection.prepareCall("call if_empty_USERS_TICKETS(?,?)")) {
            try {
                connection.setAutoCommit(false);
                ps.setString(1, String.valueOf(user.getPassword()));
                ps.setString(2, user.role().toString());
                ps.setString(3, user.getLogin());
                for (BigInteger it : user.getTickets()) {
                    pc.setBigDecimal(1, new BigDecimal(it));
                    pc.setString(2, user.getLogin());
                    pc.executeUpdate();
                }
                connection.commit();
            } catch (SQLException sqle) {
                logger.error(sqle);
                connection.rollback();
                logger.error("connection rolled back");
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
