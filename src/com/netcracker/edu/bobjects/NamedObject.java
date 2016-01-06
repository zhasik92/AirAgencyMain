package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public abstract class NamedObject extends BusinessObject implements Serializable {
    private String name;

    public NamedObject(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }
    //Whitespaces not allowed in name
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        this.name = name.toLowerCase();
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
            System.out.println(ex);
        }

        return result.toString();
    }
}
