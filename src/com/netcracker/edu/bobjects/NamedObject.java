package com.netcracker.edu.bobjects;

import java.io.Serializable;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public abstract class NamedObject implements Serializable {
    private String name;

    public NamedObject(String name) {
        setName(name);
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        if(name==null) {
            throw new IllegalArgumentException();
        }
        this.name=name;
    }
}
