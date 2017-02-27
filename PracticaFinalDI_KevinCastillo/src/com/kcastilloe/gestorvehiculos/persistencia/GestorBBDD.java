package com.kcastilloe.gestorvehiculos.persistencia;

import com.kcastilloe.gestorvehiculos.gui.JFGestorVehiculos;
import com.kcastilloe.gestorvehiculos.modelo.Marca;
import com.kcastilloe.gestorvehiculos.modelo.Modelo;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    Marca miMarca;
    Modelo miModelo;
    JFGestorVehiculos jfp;
    
    private Connection conexion = null; /* La conexión a establecer. */
    private String sql; /* La cadena que recogerá las sentencias SQL (creación, inserción, consulta... */
    private PreparedStatement ps = null; /* Para las operaciones sobre la BD (modificación y consulta). */
    private ResultSet rs = null;
    PrintWriter fichero = null;

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
    
    /**
     * Método usado para listar todas las marcas registradas en la BD para volcarlas en la vista.
     * 
     * @return ArrayList con todas las marcas existentes en la BD.
     * @throws SQLException
     * @throws Exception 
     */
    public ArrayList buscarMarcas() throws SQLException, Exception {
        int id_marca;
        String nombre_marca = null;
        ArrayList<Marca> alMarcas = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from marcas order by nombre_marca";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id_marca = rs.getInt("id_marca");
                nombre_marca = rs.getString("nombre_marca");
                miMarca = new Marca(id_marca, nombre_marca);
                alMarcas.add(miMarca);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return alMarcas;
    }
    
    /**
     * Método usado para crear un nuevo registro de marca en la BD.
     * 
     * @param nuevaMarca Objeto Marca del que recoger los datos para guardar en BD.
     * @throws SQLException
     * @throws Exception 
     */
    public void crearMarca(Marca nuevaMarca) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "insert into marcas (nombre_marca) values (?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nuevaMarca.getNombre_marca());
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    public void modificarMarca(Marca marcaModificada) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        
    }
    
    /**
     * Método usado para eliminar un registro de marca a partir de su id, al ser clave primaria.
     * @param id_marca El id de la marca que se desea eliminar.
     * @return Las filas que se han visto afectadas por la operación de borrado.
     * @throws SQLException
     * @throws Exception 
     */
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
    
    /**
     * Método usado para listar todos los modelos registrados en la BD para volcarlos en la vista.
     * @return ArrayList con todos los modelos existentes en la BD.
     * @throws SQLException
     * @throws Exception 
     */
    public ArrayList buscarModelos() throws SQLException, Exception {
        int id_modelo = 0;
        String nombre_modelo = null;
        int id_marca = 0;
        int id_eficiencia = 0;
        float consumo_modelo = 0f;
        float emisiones_modelo = 0f;
        
        ArrayList<Modelo> alModelos = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from modelos order by nombre_modelo";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id_modelo = rs.getInt("id_modelo");
                nombre_modelo = rs.getString("nombre_modelo");
                id_marca = rs.getInt("id_marca");
                id_eficiencia = rs.getInt("id_eficiencia");
                consumo_modelo = rs.getFloat("consumo_modelo");
                emisiones_modelo = rs.getFloat("emisiones_modelo");
                miModelo = new Modelo(id_modelo, nombre_modelo, id_marca, id_eficiencia, consumo_modelo, emisiones_modelo);
                alModelos.add(miModelo);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return alModelos;
    }
    
    /**
     * Método usado para crear un nuevo registro de modelo en la BD.
     * 
     * @param nuevoModelo Objeto Modelo del que recoger los datos para guardar en BD.
     * @throws SQLException
     * @throws Exception 
     */
    public void crearModelo(Modelo nuevoModelo) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "insert into modelos (nombre_modelo, id_marca, id_eficiencia, consumo_modelo, emisiones_modelo) VALUES (?,?,?,?,?)";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nuevoModelo.getNombre_modelo());
            ps.setInt(2, nuevoModelo.getId_marca());
            ps.setInt(3, nuevoModelo.getId_eficiencia());
            ps.setFloat(4, nuevoModelo.getConsumo_modelo());
            ps.setFloat(5, nuevoModelo.getEmisiones_modelo());
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    public void modificarModelo(Modelo modeloModificado) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        
    }
    
    /**
     * Método usado para eliminar un registro de modelo a partir de su id, al ser clave primaria.
     * @param id_modelo El id del modelo que se desea eliminar.
     * @return Las filas que se han visto afectadas por la operación de borrado.
     * @throws SQLException
     * @throws Exception 
     */
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
