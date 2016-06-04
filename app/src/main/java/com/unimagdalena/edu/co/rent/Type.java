package com.unimagdalena.edu.co.rent;

import java.io.Serializable;

public class Type implements Serializable {

    private String nombre;

    public Type(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
