package com.netcracker.edu.bobjects;

import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public abstract class HasIdObject {
    private BigInteger id;

    public HasIdObject(BigInteger id) {
        setId(id);
    }

    private void setId(BigInteger id){
        if(id.compareTo(BigInteger.ZERO)<0){
            throw new IllegalArgumentException();
        }
        this.id=id;
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
}
