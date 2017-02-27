package com.kcastilloe.gestorvehiculos.modelo;

/**
 * Clase Java dedicada a la tabla Marcas de la BD.
 * 
 * @author Kevin Castillo
 */
public class Marca {
   
    private int id_marca = 0;
    private String nombre_marca = null;

    /**
     * Constructor de Marca para inserción de datos en BD.
     * 
     * @param nombre_marca El nombre de la marca.
     */
    public Marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }
    
    /**
     * Constructor de Marca para recogida de datos en BD.
     * 
     * @param id_marca El id característico de la marca (clave primaria).
     * @param nombre_marca El nombre de la marca.
     */
    public Marca(int id_marca, String nombre_marca) {
        this.id_marca = id_marca;
        this.nombre_marca = nombre_marca;
    }

    /* Getters y Setters. */
    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }
    
}
