package com.netcracker.edu.bobjects;


import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class Passenger extends HasIdObject implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String passportNumber;
    private String citizenship;

    /*public Passenger(BigInteger id, String passportNumber, String citizenship) {
        super(id);
        setPassportNumber(passportNumber);
        setCitizenship(citizenship);
    }*/

    // i'll refactor this latter, builder pattern is better
    public Passenger(BigInteger id, String email, String firstName, String lastName, Date dateOfBirth, String passportNumber, String citizenship) {
        super(id);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setPassportNumber(passportNumber);
        setCitizenship(citizenship);
    }

    public String getEmail() {
        return email;
    }

    //i'll fix regex later
    public void setEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\n" +
                "\t\t+ \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
       /* if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid email address");
        }*/
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        Pattern pattern = Pattern.compile("^[A-Za-z]?[0-9]*$");
        Matcher matcher = pattern.matcher(passportNumber);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid passport number");
        }
        this.passportNumber = passportNumber;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    @Override
    // TODO: 04.01.2016 CHECK EQUALS AND HASHCODE
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        return passportNumber.equals(passenger.passportNumber) && citizenship.equals(passenger.citizenship);

    }

    @Override
    public int hashCode() {
        int result = passportNumber.hashCode();
        result = 31 * result + citizenship.hashCode();
        return result;
    }
}
