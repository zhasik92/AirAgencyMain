package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public abstract class HasIdObject extends BusinessObject implements Serializable {
    private BigInteger id;

    public HasIdObject(BigInteger id) {
        setId(id);
    }

    private void setId(BigInteger id) {
        if (id.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("id can't be negative");
        }
        this.id = id;
    }

    public BigInteger getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HasIdObject that = (HasIdObject) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        result.append(this.getClass().getName());
        result.append(newLine);

        Field[] fields = this.getClass().getDeclaredFields();
        Field[] superClassFields = this.getClass().getSuperclass().getDeclaredFields();
        for (Field it : superClassFields) {
            it.setAccessible(true);
        }
        for (Field it : fields) {
            it.setAccessible(true);
        }
        try {
            for (Field it : superClassFields) {

                result.append(it.getName());
                result.append(": ");
                //requires role to private field:
                result.append(it.get(this));
                result.append(newLine);
            }
            for (Field field : fields) {
                result.append(field.getName());
                result.append(": ");
                //requires role to private field:
                result.append(field.get(this));
                result.append(newLine);
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }
}
