package com.kcastilloe.gestorvehiculos.persistencia;

import com.kcastilloe.gestorvehiculos.gui.JFGestorVehiculos;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Maneja todos los métodos de gestión de BBDD (creación, inserción,
 * consulta...).
 * 
 * @author Kevin Castillo
 */
public class GestorBBDD {
    
    private String url = "jdbc:mysql://localhost:3306/vehiculos?useServerPrepStmts=true";
    private String usuario = "dam2015";
    private String password = "dam2015";
    
    
    private Connection conexion = null; /* La conexión a establecer. */
    private String sql; /* La cadena que recogerá las sentencias SQL (creación, inserción, consulta... */
    private PreparedStatement ps = null; /* Para las operaciones sobre la BD (modificación y consulta). */
    private ResultSet rs = null;
    PrintWriter fichero = null;
    JFGestorVehiculos jfp;

    public GestorBBDD(JFGestorVehiculos jpp) {
        this.jfp = jpp;
    }
    
    /**
     * Establece la conexión con la BD.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception
     */
    public void abrirConexion() throws ClassNotFoundException, SQLException, Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection(url, usuario, password); /* La dirección del servidor local, y el nombre de la BD local. */
        } catch (ClassNotFoundException cnfex) {
            throw new Exception("Imposible conectar a la base de datos.");
        } catch (SQLException sqlex) {
            throw new Exception("No se ha encontrado la base de datos.");
        }
    }
    
    public ArrayList buscarMarcas() throws SQLException, Exception {
        String marca = null;
        ArrayList<String> almar = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select distinct nombre_marca from marcas order by nombre_marca";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                marca = rs.getString("nombre_marca");
                almar.add(marca);
                System.out.println(marca);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return almar;
    }
    
    public void crearMarca(String nombreMarcaNueva) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "insert into marcas (nombre_marca) values (?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreMarcaNueva);
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    public int borrarMarca (int id_marca) throws SQLException, Exception {
        int filasAfectadas = 0;
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "delete from marcas where id_marca = " + id_marca;
            ps = conexion.prepareStatement(sql);
            filasAfectadas = ps.executeUpdate();
            //System.out.println("Registros eliminados: " + filasAfectadas + "\n");
            return filasAfectadas;
        } catch (SQLException ex) {
            System.out.println("No se ha encontrado la base de datos.");
        }
        return 9;
    }
    
    public ArrayList buscarModelos() throws SQLException, Exception {
        String modelo = null;
        ArrayList<String> almod = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select distinct nombre_modelo from modelos order by nombre_modelo";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                modelo = rs.getString("nombre_modelo");
                almod.add(modelo);
                System.out.println(modelo);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return almod;
    }
    
    public void crearModelo(String nombreModeloNuevo, int id_marca, int id_eficiencia, float consumo_modelo, float emisiones_modelo) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "insert into modelos (nombre_modelo, id_marca, id_eficiencia, consumo_modelo, emisiones_modelo) VALUES (?,?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreModeloNuevo);
            ps.setInt(2, id_marca);
            ps.setInt(3, id_eficiencia);
            ps.setFloat(4, consumo_modelo);
            ps.setFloat(5, emisiones_modelo);
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    public int borrarModelo (int id_modelo) throws SQLException, Exception {
        int filasAfectadas = 0;
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "delete from modelos where id_modelo = " + id_modelo;
            ps = conexion.prepareStatement(sql);
            filasAfectadas = ps.executeUpdate();
            //System.out.println("Registros eliminados: " + filasAfectadas + "\n");
            return filasAfectadas;
        } catch (SQLException ex) {
            System.out.println("No se ha encontrado la base de datos.");
        }
        return 9;
    }
}
