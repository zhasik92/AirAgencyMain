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
    private String secondName;
    private Date dateOfBirth;
    private String passportNumber;
    private String citizenship;
    private Passenger(){
        super();
    }
    public Passenger(BigInteger id, String passportNumber, String citizenship) {
        super(id);
        setPassportNumber(passportNumber);
        setCitizenship(citizenship);
    }

    // i'll refactor this latter, cause it's bad practise to put to constructor a lot of fields.
    public Passenger(BigInteger id, String email, String firstName, String secondName, Date dateOfBirth, String passportNumber, String citizenship) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
        this.citizenship = citizenship;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\n" +
                "\t\t+ \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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
        Pattern pattern = Pattern.compile("^[A-Za-z]+[0-9]*$");
        Matcher matcher = pattern.matcher(passportNumber);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        if (!passportNumber.equals(passenger.passportNumber)) return false;
        return citizenship.equals(passenger.citizenship);

    }

    @Override
    public int hashCode() {
        int result = passportNumber.hashCode();
        result = 31 * result + citizenship.hashCode();
        return result;
    }
}
