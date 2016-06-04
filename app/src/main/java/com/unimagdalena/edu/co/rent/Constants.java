package com.unimagdalena.edu.co.rent;

public class Constants {
    private static final String PORT = "80";

    private static final String IP = "http://10.0.2.2:";

    public static final String GET = IP + PORT + "/WebServiceArriendos/acciones/obtener_inmuebles.php";
    public static final String GET_CITIES = IP + PORT + "/WebServiceArriendos/acciones/obtener_ciudades.php";
    public static final String GET_TYPES = IP + PORT + "/WebServiceArriendos/acciones/obtener_tipo_inmueble.php";
    public static final String GET_BY_CITY = IP + PORT + "/WebServiceArriendos/acciones/obtener_inmuebles_por_ciudad.php";
    public static final String GET_BY_TYPE = IP + PORT + "/WebServiceArriendos/acciones/obtener_inmuebles_por_tipo.php";
    public static final String DELETE = IP + PORT + "/WebServiceArriendos/acciones/eliminar_inmuebles.php";
    public static final String INSERT = IP + PORT + "/WebServiceArriendos/acciones/insertar_inmuebles.php";
    public static final String UPDATE = IP + PORT + "/WebServiceArriendos/acciones/actualizar_inmuebles.php";
}
