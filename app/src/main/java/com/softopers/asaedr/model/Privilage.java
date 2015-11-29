package com.softopers.asaedr.model;

import java.io.Serializable;

public class Privilage implements Serializable {

    private String Name;

    private boolean Value;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public boolean getValue() {
        return Value;
    }

    public void setValue(boolean Value) {
        this.Value = Value;
    }

    @Override
    public String toString() {
        return "Privilage{" +
                "Name='" + Name + '\'' +
                ", Value=" + Value +
                '}';
    }
}