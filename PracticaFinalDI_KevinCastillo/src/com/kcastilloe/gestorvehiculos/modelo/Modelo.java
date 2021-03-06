package com.kcastilloe.gestorvehiculos.modelo;

/**
 * Clase Java dedicada a la tabla Modelos de la BD.
 * 
 * @author Kevin Castillo
 */
public class Modelo {
    
    private int id_modelo;
    private String nombre_modelo;
    private int id_marca;
    private String nombre_marca;
    private int id_eficiencia;
    private String nombre_eficiencia = null;
    private float consumo_modelo; 
    private float emisiones_modelo;

    /**
     * Constructor de Modelo para inserción de datos en BD.
     * 
     * @param nombre_modelo El nombre del modelo.
     * @param id_marca El id de la marca a la que pertenece el modelo.
     * @param id_eficiencia El id de la eficiencia que le corresponde al modelo.
     * @param consumo_modelo El consumo del modelo, expresado en litros/100km.
     * @param emisiones_modelo Las emisiones del modleo, expresadas en gCO2/km.
     */
    public Modelo(String nombre_modelo, int id_marca, int id_eficiencia, float consumo_modelo, float emisiones_modelo) {
        this.nombre_modelo = nombre_modelo;
        this.id_marca = id_marca;
        this.id_eficiencia = id_eficiencia;
        this.consumo_modelo = consumo_modelo;
        this.emisiones_modelo = emisiones_modelo;
    }
    
    /**
     * Constructor de Modelo para recogida de datos en BD.
     * 
     * @param id_modelo El id característico del modelo (clave primaria).
     * @param nombre_modelo El nombre del modelo.
     * @param id_marca El id de la marca a la que pertenece el modelo.
     * @param id_eficiencia El id de la eficiencia que le corresponde al modelo.
     * @param consumo_modelo El consumo del modelo, expresado en litros/100km.
     * @param emisiones_modelo Las emisiones del modleo, expresadas en gCO2/km.
     */
    public Modelo(int id_modelo, String nombre_modelo, int id_marca, int id_eficiencia, float consumo_modelo, float emisiones_modelo) {
        this.id_modelo = id_modelo;
        this.nombre_modelo = nombre_modelo;
        this.id_marca = id_marca;
        this.id_eficiencia = id_eficiencia;
        this.consumo_modelo = consumo_modelo;
        this.emisiones_modelo = emisiones_modelo;
    }
    
    /**
     * Constructor usado para la búsqueda individual de cara a eliminación de datos en BD.
     * 
     * @param nombre_modelo El nombre del modelo a eliminar.
     */
    public Modelo(String nombre_modelo){
        this.nombre_modelo = nombre_modelo;
    }
    
    /**
     * Constructor usado para la eliminación de datos en BD.
     * 
     * @param id_modelo El id del modelo a eliminar.
     * @param nombre_modelo El nombre del modelo a eliminar.
     */
    public Modelo(int id_modelo, String nombre_modelo){
        this.id_modelo = id_modelo;
        this.nombre_modelo = nombre_modelo;
    }

    /**
     * Constructor usado para la búsqueda de modelos específicos para consulta
     * según sus parámetros.
     * 
     * @param id_marca El id de la marca del modelo a consultar.
     * @param consumo_maximo_modelo El consumo máximo del modelo a consultar.
     * @param emisiones_maximas_modelo Las emisiones máximas del modelo a consultar.
     * @param id_eficiencia El id de la eficiencia del modelo a consultar.
     */
    public Modelo(int id_marca, float consumo_maximo_modelo, float emisiones_maximas_modelo, int id_eficiencia) {
        this.id_marca = id_marca;
        this.consumo_modelo = consumo_modelo;
        this.emisiones_modelo = emisiones_modelo;
        this.id_eficiencia = id_eficiencia;
    }

    /**
     * Constructor de Modelo para recogida de datos en tabla de Consulta y 
     * posterior exportación a documento .csv.
     * 
     * @param nombre_modelo El nombre del modelo.
     * @param nombre_marca El nombre de la marca a la que pertenece el modelo.
     * @param nombre_eficiencia El nombre de la eficiencia que le corresponde al modelo.
     * @param consumo_modelo El consumo del modelo, expresado en litros/100km.
     * @param emisiones_modelo Las emisiones del modleo, expresadas en gCO2/km.
     */
    public Modelo(String nombre_modelo, String nombre_marca, String nombre_eficiencia, float consumo_modelo, float emisiones_modelo) {
        this.nombre_modelo = nombre_modelo;
        this.nombre_marca = nombre_marca;
        this.nombre_eficiencia = nombre_eficiencia;
        this.consumo_modelo = consumo_modelo;
        this.emisiones_modelo = emisiones_modelo;
    }
    
    public int getId_modelo() {
        return id_modelo;
    }

    public void setId_modelo(int id_modelo) {
        this.id_modelo = id_modelo;
    }

    public String getNombre_modelo() {
        return nombre_modelo;
    }

    public void setNombre_modelo(String nombre_modelo) {
        this.nombre_modelo = nombre_modelo;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public int getId_eficiencia() {
        return id_eficiencia;
    }

    public void setId_eficiencia(int id_eficiencia) {
        this.id_eficiencia = id_eficiencia;
    }

    public float getConsumo_modelo() {
        return consumo_modelo;
    }

    public void setConsumo_modelo(float consumo_modelo) {
        this.consumo_modelo = consumo_modelo;
    }

    public float getEmisiones_modelo() {
        return emisiones_modelo;
    }

    public void setEmisiones_modelo(float emisiones_modelo) {
        this.emisiones_modelo = emisiones_modelo;
    }
    
    public String getNombre_marca() {
        return nombre_marca;
    }

    public void setNombre_marca(String nombre_marca) {
        this.nombre_marca = nombre_marca;
    }

    public String getNombre_eficiencia() {
        return nombre_eficiencia;
    }

    public void setNombre_eficiencia(String nombre_eficiencia) {
        this.nombre_eficiencia = nombre_eficiencia;
    }
}
