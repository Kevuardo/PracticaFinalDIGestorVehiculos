package com.kcastilloe.gestorvehiculos.modelo;

/**
 * Clase Java dedicada a la tabla Eficiencias de la BD.
 * 
 * @author Kevin Castillo
 */
public class Eficiencia {
    private int id_eficiencia = 0;
    private String nombre_eficiencia = null;

    /**
     * Constructor de Eficiencia para recogida de datos en BD.
     * 
     * @param nombre_eficiencia El nombre de la eficiencia.
     * @param id_eficiencia El id de la eficiencia.
     */
    public Eficiencia(int id_eficiencia, String nombre_eficiencia) {
        this.id_eficiencia = id_eficiencia;
        this.nombre_eficiencia = nombre_eficiencia;
    }

    public int getId_eficiencia() {
        return id_eficiencia;
    }

    public void setId_eficiencia(int id_eficiencia) {
        this.id_eficiencia = id_eficiencia;
    }

    public String getNombre_eficiencia() {
        return nombre_eficiencia;
    }

    public void setNombre_eficiencia(String nombre_eficiencia) {
        this.nombre_eficiencia = nombre_eficiencia;
    }
    
    
}
