package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhassulan on 15.11.2015.
 */
public class User extends BusinessObject implements Serializable {
    private static final long serialVersionUID= -439309840366060163L;
    private String login;
    private char[] password;
    private final Roles role;
    private Set<BigInteger> tickets = new HashSet<>();

    public enum Roles {USER, ADMIN}

    public User(String login, char[] password) {
        setLogin(login);
        setPassword(password);
        this.role = Roles.USER;
    }

    public User(String login, char[] password, Roles role) {
        setLogin(login);
        setPassword(password);
        this.role = role;

    }

    public void setLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("invalid login");
        }
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        if (password == null || password.length < 3) {
            throw new IllegalArgumentException("password can't be null, or less than 3 chars");
        }
        this.password = password;
    }

    public Roles role() {
        return this.role;
    }

    public Set<BigInteger> getTickets() {
        return tickets;
    }

    public void addTicket(BigInteger ticketId) {
        tickets.add(ticketId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login.equals(user.login);

    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
