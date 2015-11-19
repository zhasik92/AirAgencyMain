package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhassulan on 15.11.2015.
 */
public class User implements Serializable {
    private String login;
    private char[] password;
    boolean isAdmin;
    private Set<BigInteger> tickets=new HashSet<>();

    public User(String login,char[] password){
        setLogin(login);
        setPassword(password);
        this.isAdmin=false;
    }

    public User(String login, char[] password, boolean isAdmin) {
        setLogin(login);
        setPassword(password);
        this.isAdmin = isAdmin;
    }

    private void setLogin(String login){
        if(login==null||login.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.login=login;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        if(password==null||password.length<3){
            throw new IllegalArgumentException();
        }
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Set<BigInteger> getTickets() {
        return tickets;
    }
    public void addTicket(BigInteger ticketId){
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
