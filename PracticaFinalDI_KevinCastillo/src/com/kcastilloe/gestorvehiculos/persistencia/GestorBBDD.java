package com.kcastilloe.gestorvehiculos.persistencia;

import com.kcastilloe.gestorvehiculos.gui.JFGestorVehiculos;
import com.kcastilloe.gestorvehiculos.modelo.Eficiencia;
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
    Eficiencia miEficiencia;
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
    
    /* Métodos relacionados con la tabla Marcas. */
    
    /**
     * Método que evalúa si existe la marca seleccionada por el usuario para evitar redundancia de datos.
     * @param marcaConsulta El nombre de la marca de la que se desea saber si existe.
     * @return Booleano para evaluar si existe (true) o no (false).
     * @throws SQLException
     * @throws Exception 
     */
    public boolean existeMarca(Marca marcaConsulta) throws SQLException, Exception{
        boolean yaExiste = false;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from marcas where nombre_marca = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, marcaConsulta.getNombre_marca());
            rs = ps.executeQuery();
            if (rs.next()) {
                yaExiste = true;
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return yaExiste;
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
            sql = "select * from marcas order by id_marca";
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
     * Método usado para devolver el id de la marca correspondiente al nombre de la marca facilitado para eliminar posteriormente por id_marca por ser clave primaria.
     * @param nombreBusqueda El nombre de la marca de la que se desea saber el id.
     * @return El id de la marca correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public int buscarMarcaPorNombre(String nombreBusqueda) throws SQLException, Exception{
        int idBusqueda = 0;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from marcas where nombre_marca = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                idBusqueda = rs.getInt("id_marca");
            }
            return idBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    /**
     * Método usado para devolver el nombre de la marca correspondiente al id de la marca facilitado.
     * @param idBusqueda El id de la marca de la que se desea sabes su nombre.
     * @return El nombre de la marca correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public String buscarMarcaPorId(int idBusqueda) throws SQLException, Exception{
        String nombreBusqueda = null;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from marcas where id_marca = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombreBusqueda = rs.getString("nombre_marca");
            }
            return nombreBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
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
    
    /**
     * Método usado para modificar registros de marca en la BD.
     * 
     * @param marcaModificada Objeto Marca del que recoger los datos para modificar en la BD.
     * @throws SQLException
     * @throws Exception 
     */
    public void modificarMarca(Marca marcaModificada) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "update marcas set nombre_marca = ? where id_marca = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, marcaModificada.getNombre_marca());
            ps.setInt(2, marcaModificada.getId_marca());
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
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
            return filasAfectadas;
        } catch (SQLException ex) {
            System.out.println("No se ha encontrado la base de datos.");
        }
        return 9;
    }
    
    /**
     * Método usado para resetear el autoincremental de id_marca por cuestiones de estética en caso de no haber registros.
     * @throws SQLException
     * @throws Exception 
     */
    public void resetearAutoincrementalMarca() throws SQLException, Exception{
         /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "alter table marcas auto_increment = 1";
            ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    /* Métodos relacionados con la tabla Modelos. */
    
    /**
     * Método que evalúa si existe el modelo seleccionado por el usuario para evitar redundancia de datos.
     * @param modeloConsulta El nombre del modelo del que se desea saber si existe.
     * @return Booleano para evaluar si existe (true) o no (false).
     * @throws SQLException
     * @throws Exception 
     */
    public boolean existeModelo(Modelo modeloConsulta) throws SQLException, Exception{
        boolean yaExiste = false;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from modelos where nombre_modelo = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, modeloConsulta.getNombre_modelo());
            rs = ps.executeQuery();
            if (rs.next()) {
                yaExiste = true;
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return yaExiste;
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
            sql = "select * from modelos order by id_modelo";
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
     * Método usado para devolver el id del modelo correspondiente al nombre del modelo facilitado para eliminar posteriormente por id_modelo por ser clave primaria.
     * @param nombreBusqueda El nombre del modelo de la que se desea saber el id.
     * @return El id del modelo correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public int buscarModeloPorNombre(String nombreBusqueda) throws SQLException, Exception{
        int idBusqueda = 0;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from modelos where nombre_modelo = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                idBusqueda = rs.getInt("id_modelo");
            }
            return idBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
     /**
     * Método usado para devolver el nombre del modelo correspondiente al id del modelo facilitado.
     * @param idBusqueda El id del modelo de la que se desea sabes su nombre.
     * @return El nombre del modelo correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public String buscarModeloPorId(int idBusqueda) throws SQLException, Exception{
        String nombreBusqueda = null;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from modelos where id_modelo = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombreBusqueda = rs.getString("nombre_modelo");
            }
            return nombreBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
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
            sql = "insert into modelos (nombre_modelo, id_marca, id_eficiencia, consumo_modelo, emisiones_modelo) values (?,?,?,?,?)";
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
    
    /**
     * Método usado para modificar registros de modelo en la BD.
     * 
     * @param modeloModificado Objeto Modelo del que recoger los datos para modificar en la BD.
     * @throws SQLException
     * @throws Exception 
     */
    public void modificarModelo(Modelo modeloModificado) throws SQLException, Exception {
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "update modelos set nombre_modelo = ?, id_marca = ?, id_eficiencia = ?, consumo_modelo = ?, emisiones_modelo = ? where id_modelo = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, modeloModificado.getNombre_modelo());
            ps.setInt(2, modeloModificado.getId_marca());
            ps.setInt(3, modeloModificado.getId_eficiencia());
            ps.setFloat(4, modeloModificado.getConsumo_modelo());
            ps.setFloat(5, modeloModificado.getEmisiones_modelo());
            ps.setInt(6, modeloModificado.getId_modelo());
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
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
            return filasAfectadas;
        } catch (SQLException ex) {
            System.out.println("No se ha encontrado la base de datos.");
        }
        return 9;
    }
    
    public ArrayList buscarModelosEspecificos(Modelo modeloBusqueda) throws SQLException, Exception{
        int id_modelo = 0;
        String nombre_modelo = null;
        float consumo_modelo = 0f;
        float emisiones_modelo = 0f;
        
        ArrayList<Modelo> alModelos = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            /* Selecciona los máximos consumos y emisiones para luego aplicarlos al rango de los JSliders. */
            sql = "select * from modelos where id_marca = ? and consumo_modelo <= ? and emisiones_modelo <= ? and id_eficiencia <= ? order by id_modelo";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, modeloBusqueda.getId_marca());
            ps.setFloat(2, modeloBusqueda.getConsumo_modelo());
            ps.setFloat(3, modeloBusqueda.getEmisiones_modelo());
            ps.setInt(1, modeloBusqueda.getId_eficiencia());
            rs = ps.executeQuery();
            while (rs.next()) {
                id_modelo = rs.getInt("id_modelo");
                nombre_modelo = rs.getString("nombre_modelo");
                consumo_modelo = rs.getFloat("consumo_modelo");
                emisiones_modelo = rs.getFloat("emisiones_modelo");
                miModelo = new Modelo(id_modelo, nombre_modelo, modeloBusqueda.getId_marca(), modeloBusqueda.getId_eficiencia(), consumo_modelo, emisiones_modelo);
                alModelos.add(miModelo);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return alModelos;
    }
    
    /**
     * Método usado para resetear el autoincremental de id_modelo por cuestiones de estética en caso de no haber registros.
     * @throws SQLException
     * @throws Exception 
     */
    public void resetearAutoincrementalModelo() throws SQLException, Exception{
         /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "alter table modelos auto_increment = 1";
            ps = conexion.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    /* Métodos relacionados con la tabla Eficiencias. */
    
    public ArrayList buscarEficiencias() throws SQLException, Exception{
        int id_eficiencia = 0;
        String nombre_eficiencia = null;
        ArrayList<Eficiencia> alEficiencias = new ArrayList();
        
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from eficiencias order by id_eficiencia";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id_eficiencia = rs.getInt("id_eficiencia");
                nombre_eficiencia = rs.getString("nombre_eficiencia");
                miEficiencia = new Eficiencia(id_eficiencia, nombre_eficiencia);
                alEficiencias.add(miEficiencia);
            }
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
        return alEficiencias;
    }
    
    /**
     * Método usado para recuperar el nombre de una eficiencia según su id.
     * @param idBusqueda El id de la eficiencia de la que se desea conocer el nombre.
     * @return El nombre de la eficiencia correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public String buscarEficienciasPorId(int idBusqueda) throws SQLException, Exception{
        String nombreBusqueda = null;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from eficiencias where id_eficiencia = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombreBusqueda = rs.getString("nombre_eficiencia");
            }
            return nombreBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
    
    /**
     * Método usado para recuperar el id de una eficiencia según su nombre.
     * @param nombreBusqueda El nombre de la eficiencia de la que se desea conocer el id.
     * @return El id de la eficiencia correspondiente.
     * @throws SQLException
     * @throws Exception 
     */
    public int buscarEficienciasPorNombre(String nombreBusqueda) throws SQLException, Exception{
        int idBusqueda = 0;
        /* Para cerciorarse de que no se ha cerrado la conexión antes de hacer la consulta. */
        if (conexion == null) {
            this.abrirConexion();
        }
        try {
            sql = "select * from eficiencias where nombre_eficiencia = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreBusqueda);
            rs = ps.executeQuery();
            if (rs.next()) {
                idBusqueda = rs.getInt("id_eficiencia");
            }
            return idBusqueda;
        } catch (SQLException sqlex) {
            throw new SQLException("Imposible conectar a la base de datos.");
        }
    }
}
