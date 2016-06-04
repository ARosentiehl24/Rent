package com.unimagdalena.edu.co.rent;

import java.io.Serializable;

public class Rent implements Serializable {

    private String codigo_arriendo;
    private String ciudad;
    private String direccion;
    private String agencia;
    private String valor_arriendo;
    private String tipo_vivenda;

    public Rent(String codigo_arriendo, String ciudad, String direccion, String agencia, String valor_arriendo, String tipo_vivenda) {
        this.codigo_arriendo = codigo_arriendo;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.agencia = agencia;
        this.valor_arriendo = valor_arriendo;
        this.tipo_vivenda = tipo_vivenda;
    }

    public String getCodigo_arriendo() {
        return codigo_arriendo;
    }

    public void setCodigo_arriendo(String codigo_arriendo) {
        this.codigo_arriendo = codigo_arriendo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getValor_arriendo() {
        return valor_arriendo;
    }

    public void setValor_arriendo(String valor_arriendo) {
        this.valor_arriendo = valor_arriendo;
    }

    public String getTipo_vivenda() {
        return tipo_vivenda;
    }

    public void setTipo_vivenda(String tipo_vivenda) {
        this.tipo_vivenda = tipo_vivenda;
    }
}
