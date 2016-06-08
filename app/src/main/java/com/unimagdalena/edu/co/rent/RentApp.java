package com.unimagdalena.edu.co.rent;

import android.app.Application;

import es.dmoral.prefs.Prefs;

public class RentApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getIP() {
        return Prefs.with(this).read("ip", "192.168.0.16");
    }

    public String getPORT() {
        return Prefs.with(this).read("port", "80");
    }

    public String GET() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/obtener_inmuebles.php";
    }

    public String GET_CITIES() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/obtener_ciudades.php";
    }

    public String GET_TYPES() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/obtener_tipo_inmueble.php";
    }

    public String GET_BY_CITY() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/obtener_inmuebles_por_ciudad.php";
    }

    public String GET_BY_TYPE() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/obtener_inmuebles_por_tipo.php";
    }

    public String DELETE() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/eliminar_inmuebles.php";
    }

    public String INSERT() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/insertar_inmuebles.php";
    }

    public String UPDATE() {
        return "http://" + getIP() + ":" + getPORT() + "/WebServiceArriendos/acciones/actualizar_inmuebles.php";
    }
}
