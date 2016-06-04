package com.unimagdalena.edu.co.rent;

import java.io.Serializable;

public class City implements Serializable {

    private String ciudad;

    public City(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
