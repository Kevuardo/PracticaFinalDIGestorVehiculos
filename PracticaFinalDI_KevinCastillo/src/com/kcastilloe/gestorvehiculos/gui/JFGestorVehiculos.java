package com.kcastilloe.gestorvehiculos.gui;

import com.kcastilloe.gestorvehiculos.modelo.Eficiencia;
import com.kcastilloe.gestorvehiculos.modelo.Marca;
import com.kcastilloe.gestorvehiculos.modelo.Modelo;
import com.kcastilloe.gestorvehiculos.persistencia.GestorBBDD;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 * La ventana principal de la aplicación.
 *
 * @author Kevin Castillo
 */
public class JFGestorVehiculos extends javax.swing.JFrame {

    Marca miMarca;
    ArrayList<Marca> alMarcas = new ArrayList();
    Modelo miModelo;
    ArrayList<Modelo> alModelos = new ArrayList();
    Eficiencia miEficiencia;
    ArrayList<Eficiencia> alEficiencias = new ArrayList();
    GestorBBDD ges = new GestorBBDD(this);

    /* Los Vectores servirán de estructura para las tablas: */
    private Vector vMarcas = new Vector();
    private Vector vModelos = new Vector();

    /* Elige el modelo de las tablas, con los campos del Vector y 0 columnas iniciales: */
    private DefaultTableModel dtmMarcas = new DefaultTableModel(vMarcas, 0);
    private DefaultTableModel dtmModelos = new DefaultTableModel(vModelos, 0);

    /**
     * Crea un nuevo JFGestorVehiculos.
     */
    public JFGestorVehiculos() {
        initComponents();
        this.setBounds(300, 150, 800, 600);
        bloquearElementosIniciales();
        /* Columnas de las tablas de Marcas: */
        vMarcas.clear();
        vMarcas.add("ID marca");
        vMarcas.add("Nombre marca");
        jtTablaCrearMarca.setModel(dtmMarcas);
        jtTablaModificarMarca.setModel(dtmMarcas);
        jtTablaEliminarMarca.setModel(dtmMarcas);

        /* Columnas de las tablas de Modelos: */
        vModelos.clear();
        vModelos.add("Nombre marca");
        vModelos.add("Nombre modelo");
        vModelos.add("Consumo (L/100km)");
        vModelos.add("Emisiones (gCO2/km)");
        vModelos.add("Eficiencia");
        jtTablaCrearModelo.setModel(dtmModelos);
        jtTablaModificarModelo.setModel(dtmModelos);
        jtTablaEliminarModelo.setModel(dtmModelos);

        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane general. */
        jtpVentanasModulos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                    reiniciarCamposCrearMarca();
                    reiniciarCamposModificarMarca();
                    reiniciarCamposEliminarMarca();
                    reiniciarCamposCrearModelo();
                    reiniciarCamposModificarModelo();
                    reiniciarCamposEliminarModelo();
                }
            }
        });
        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane de Marcas. */
        jtpSeccionesMarcas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                    reiniciarCamposCrearMarca();
                    reiniciarCamposModificarMarca();
                    reiniciarCamposEliminarMarca();
                }
            }
        });
        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane de Modelos. */
        jtpSeccionesModelos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                    reiniciarCamposCrearModelo();
                    reiniciarCamposModificarModelo();
                    reiniciarCamposEliminarModelo();
                }
            }
        });
    }

    /**
     * Método usado para evitar que el usuario acceda a funciones relacionadas
     * con BD antes de que se haya realizado la conexión con ésta.
     */
    private void bloquearElementosIniciales() {
        jtpVentanasModulos.setVisible(false);
    }

    /**
     * Comprueba que existe conexión con la BD antes de poder realizar ninguna
     * acción. En caso negativo fuerza la detención del programa para evitar
     * posibles errores.
     */
    private void iniciarElementos() {
        try {
            ges.abrirConexion();
            JOptionPane.showMessageDialog(
                    null,
                    "Conectado con éxito a la Base de Datos.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            jtpVentanasModulos.setVisible(true);
            reiniciarCamposCrearMarca();
            reiniciarCamposModificarMarca();
            reiniciarCamposEliminarMarca();
            reiniciarCamposCrearModelo();
            reiniciarCamposModificarModelo();
            reiniciarCamposEliminarModelo();
            cargarMarcasModificables();
            cargarMarcasEficienciasNuevoModelo();
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage() + "\nPruebe de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException cnfex) {
            JOptionPane.showMessageDialog(
                    null,
                    cnfex.getMessage() + "\nPruebe de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage() + "\nPruebe de nuevo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMarcasModificables() {
        try {
            alMarcas = ges.buscarMarcas();
            /* Borra los elementos (si los hubiera) que no son "Seleccione una marca...". */
            for (int i = jcbMarcasModificar.getItemCount() - 1; i > 0; i--) {
                jcbMarcasModificar.removeItemAt(i);
            }
            /* Añade los elementos recogidos de la base de datos. */
            for (int i = 0; i < alMarcas.size(); i++) {
                jcbMarcasModificar.addItem(alMarcas.get(i).getNombre_marca());
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMarcasEficienciasNuevoModelo(){
        try {
            /* Buscar todos los modelos, marcas y eficiencias. */
            alMarcas = ges.buscarMarcas();
            alModelos = ges.buscarModelos();
            alEficiencias = ges.buscarEficiencias();
            
            /* Combobox de marcas de Crear Modelo.*/
                /* Borra los elementos (si los hubiera) que no son "Seleccione una marca...". */
                for (int i = jcbMarcaNuevoModelo.getItemCount() - 1; i > 0; i--) {
                    jcbMarcaNuevoModelo.removeItemAt(i);
                }
                /* Añade los elementos recogidos de la base de datos. */
                for (int i = 0; i < alMarcas.size(); i++) {
                    jcbMarcaNuevoModelo.addItem(alMarcas.get(i).getNombre_marca());
                }
            
            /* Combobox de eficiencias de Crear Modelo.*/
                /* Borra los elementos (si los hubiera) que no son "Seleccione una eficiencia...". */
                for (int i = jcbEficienciaNuevoModelo.getItemCount() - 1; i > 0; i--) {
                    jcbEficienciaNuevoModelo.removeItemAt(i);
                }
                /* Añade los elementos recogidos de la base de datos. */
                for (int i = 0; i < alEficiencias.size(); i++) {
                    jcbEficienciaNuevoModelo.addItem(alEficiencias.get(i).getNombre_eficiencia());
                }
            
            /* Combobox de modelos de Modificar Modelo. */
                /* Borra los elementos (si los hubiera) que no son "Seleccione un modelo...". */
                for (int i = jcbModelosModificar.getItemCount() - 1; i > 0; i--) {
                    jcbModelosModificar.removeItemAt(i);
                }
                /* Añade los elementos recogidos de la base de datos. */
                for (int i = 0; i < alModelos.size(); i++) {
                    jcbModelosModificar.addItem(alModelos.get(i).getNombre_modelo());
                }
            
            /* Combobox de marcas de Modificar Modelo. */
                /* Borra los elementos (si los hubiera) que no son "Seleccione una marca...". */
                for (int i = jcbNuevaMarcaModelo.getItemCount() - 1; i > 0; i--) {
                    jcbNuevaMarcaModelo.removeItemAt(i);
                }
                /* Añade los elementos recogidos de la base de datos. */
                for (int i = 0; i < alMarcas.size(); i++) {
                    jcbNuevaMarcaModelo.addItem(alMarcas.get(i).getNombre_marca());
                }
            
            /* Combobox de eficiencias de Modificar Modelo.*/
                /* Borra los elementos (si los hubiera) que no son "Seleccione una eficiencia...". */
                for (int i = jcbNuevaEficienciaModelo.getItemCount() - 1; i > 0; i--) {
                    jcbNuevaEficienciaModelo.removeItemAt(i);
                }
                /* Añade los elementos recogidos de la base de datos. */
                for (int i = 0; i < alEficiencias.size(); i++) {
                    jcbNuevaEficienciaModelo.addItem(alEficiencias.get(i).getNombre_eficiencia());
                }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarTablas() {
        /* Se resetean las filas de las tablas: */
        dtmMarcas.setRowCount(0);
        dtmModelos.setRowCount(0);
    }

    /**
     * Método usado para llamar a la BD e insertar una nueva marca con los datos
     * que el usuario introduzca.
     */
    private void crearMarca() {
        boolean yaExiste = false;
        String nombreNuevaMarca = jtfNombreNuevaMarca.getText().toUpperCase();
        if (nombreNuevaMarca.trim().compareToIgnoreCase("") == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Introduzca un nombre válido para la marca.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            reiniciarCamposCrearMarca();
        } else {
            miMarca = new Marca(nombreNuevaMarca);
            try {
                yaExiste = ges.existeMarca(miMarca);
                if (!yaExiste) {
                    ges.crearMarca(miMarca);
                    reiniciarCamposCrearMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            "Marca creada con éxito",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    reiniciarCamposCrearMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            "Ya existe una marca con ese nombre.\nPor favor, introduzca otro nombre.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException sqlex) {
                reiniciarCamposCrearMarca();
                JOptionPane.showMessageDialog(
                        null,
                        sqlex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                reiniciarCamposCrearMarca();
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método usado para llamar a la BD y modificar la marca que seleccione el
     * usuario con los datos introducidos.
     */
    private void modificarMarca() {
        boolean yaExiste = false;

        /* Evalúa la opción seleccionada en el JCombobox. */
        String marcaSeleccionada = (String) jcbMarcasModificar.getSelectedItem();
        if (marcaSeleccionada.compareToIgnoreCase("Seleccione una marca...") == 0) {
            jtfNuevoNombreMarcaModificar.setText("");
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una marca válida en el selector desplegable.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String nombreModificadoMarca = jtfNuevoNombreMarcaModificar.getText().toUpperCase();
            int idMarcaModificable = 0;
            if (nombreModificadoMarca.trim().compareToIgnoreCase("") == 0) {
                reiniciarCamposModificarMarca();
                JOptionPane.showMessageDialog(
                        null,
                        "Inserte un nuevo nombre válido para la marca.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                idMarcaModificable = jcbMarcasModificar.getSelectedIndex();
                try {
                    miMarca = new Marca(idMarcaModificable, nombreModificadoMarca);
                    yaExiste = ges.existeMarca(miMarca);
                    if (!yaExiste) {
                        ges.modificarMarca(miMarca);
                        reiniciarCamposModificarMarca();
                        JOptionPane.showMessageDialog(
                                null,
                                "Marca modificada con éxito.",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        reiniciarCamposModificarMarca();
                        JOptionPane.showMessageDialog(
                                null,
                                "Ya existe una marca con ese nombre.\nPor favor, introduzca otro nombre.",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException sqlex) {
                    reiniciarCamposModificarMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            sqlex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    reiniciarCamposModificarMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Método usado para llamar a la BD y eliminar la marca que seleccione el
     * usuario con los datos introducidos.
     */
    private void eliminarMarca() {
        boolean yaExiste = false;
        String nombreEliminadoMarca = jtfNombreMarcaEliminar.getText();
        if (nombreEliminadoMarca.trim().compareToIgnoreCase("") == 0) {
            reiniciarCamposEliminarMarca();
            JOptionPane.showMessageDialog(
                    null,
                    "Inserte un nuevo ID válido para la marca a eliminar.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                miMarca = new Marca(nombreEliminadoMarca);
                yaExiste = ges.existeMarca(miMarca);
                if (!yaExiste) {
                    reiniciarCamposEliminarMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            "No existe ninguna marca con ese ID.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int idEliminadoMarca = ges.buscarMarcaPorNombre(nombreEliminadoMarca);
                    miMarca = new Marca(idEliminadoMarca, nombreEliminadoMarca);
                    ges.borrarMarca(miMarca.getId_marca());
                    reiniciarCamposEliminarMarca();
                    JOptionPane.showMessageDialog(
                            null,
                            "Marca eliminada con éxito.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException sqlex) {
                reiniciarCamposEliminarMarca();
                JOptionPane.showMessageDialog(
                        null,
                        sqlex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                reiniciarCamposEliminarMarca();
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método usado para llamar a la BD e insertar un nuevo modelo con los datos
     * que el usuario introduzca.
     */
    private void crearModelo() {
        boolean yaExiste = false;
        String nombreNuevoModelo = jtfNombreNuevoModelo.getText().toUpperCase();
        if (nombreNuevoModelo.trim().compareToIgnoreCase("") == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Introduzca un nombre válido para el modelo.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String marcaSeleccionada = (String) jcbMarcaNuevoModelo.getSelectedItem();
            if (marcaSeleccionada.compareToIgnoreCase("Seleccione una marca...") == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione una marca válida en el selector desplegable.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                String consumoNuevoModelo = jtfConsumoNuevoModelo.getText();
                if (consumoNuevoModelo.trim().compareToIgnoreCase("") == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Introduzca un consumo válido para el modelo.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String emisionesNuevoModelo = jtfEmisionesNuevoModelo.getText();
                    if (emisionesNuevoModelo.trim().compareToIgnoreCase("") == 0) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Introduzca unas emisionas válidas para el modelo.",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String eficienciaSeleccionada = (String) jcbEficienciaNuevoModelo.getSelectedItem();
                        if (eficienciaSeleccionada.compareToIgnoreCase("Seleccione una eficiencia...") == 0) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Seleccione una eficiencia válida en el selector desplegable.",
                                    "Información",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            try {
                                int idMarca = ges.buscarMarcaPorNombre(marcaSeleccionada);
                                int idEficiencia = ges.buscarEficienciasPorNombre(eficienciaSeleccionada);
                                float consumoNumerico = Float.parseFloat(consumoNuevoModelo);
                                float emisionesNumerico = Float.parseFloat(emisionesNuevoModelo);
                                miModelo = new Modelo(nombreNuevoModelo, idMarca, idEficiencia, consumoNumerico, emisionesNumerico);
                                yaExiste = ges.existeModelo(miModelo);
                                if (!yaExiste) {
                                    ges.crearModelo(miModelo);
                                    reiniciarCamposCrearModelo();
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Modelo creado con éxito",
                                            "Información",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    reiniciarCamposCrearModelo();
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Ya existe un modelo con ese nombre.\nPor favor, introduzca otro nombre.",
                                            "Información",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (SQLException sqlex) {
                                reiniciarCamposCrearMarca();
                                JOptionPane.showMessageDialog(
                                        null,
                                        sqlex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (Exception ex) {
                                reiniciarCamposCrearMarca();
                                JOptionPane.showMessageDialog(
                                        null,
                                        ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Método usado para llamar a la BD y modificar el modelo que seleccione el
     * usuario con los datos introducidos.
     */
    private void modificarModelo(){
            
    }
    
    /**
     * Método usado para llamar a la BD y eliminar el modelo que seleccione el
     * usuario con los datos introducidos.
     */
    private void eliminarModelo() {
        boolean yaExiste = false;
        String nombreEliminadoModelo = jtfNombreModeloEliminar.getText();
        if (nombreEliminadoModelo.trim().compareToIgnoreCase("") == 0) {
            reiniciarCamposEliminarModelo();
            JOptionPane.showMessageDialog(
                    null,
                    "Inserte un nuevo nombre válido para el modelo a eliminar.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                miModelo = new Modelo(nombreEliminadoModelo);
                yaExiste = ges.existeModelo(miModelo);
                if (!yaExiste) {
                    reiniciarCamposEliminarModelo();
                    JOptionPane.showMessageDialog(
                            null,
                            "No existe ningún modelo con ese nombre.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int idEliminadoModelo = ges.buscarModeloPorNombre(nombreEliminadoModelo);
                    miModelo = new Modelo(idEliminadoModelo, nombreEliminadoModelo);
                    ges.borrarModelo(miModelo.getId_modelo());
                    reiniciarCamposEliminarModelo();
                    JOptionPane.showMessageDialog(
                            null,
                            "Modelo eliminado con éxito.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException sqlex) {
                reiniciarCamposEliminarModelo();
                JOptionPane.showMessageDialog(
                        null,
                        sqlex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                reiniciarCamposEliminarModelo();
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    /**
     * Método usado para resetear los campos de la sección Crear Marca de cara a
     * una nueva inserción.
     */
    private void reiniciarCamposCrearMarca() {
        jtfNombreNuevaMarca.setText("");
        alMarcas.clear();
        limpiarTablas();
        try {
            alMarcas = ges.buscarMarcas();
            if (alMarcas.size() == 0) {
                /* Por cuestiones de estética, si no hubiese registros reseta el autoincremental para los futuros registros. */
                ges.resetearAutoincrementalMarca();
            } else {
                for (int i = 0; i < alMarcas.size(); i++) {
                    /* Incrementa filas: */
                    dtmMarcas.setRowCount(dtmMarcas.getRowCount() + 1);
                    jtTablaCrearMarca.setValueAt(alMarcas.get(i).getId_marca(), i, 0);
                    jtTablaCrearMarca.setValueAt(alMarcas.get(i).getNombre_marca(), i, 1);
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método usado para resetear los campos de la sección Modificar Marca de
     * cara a una nueva modificación.
     */
    private void reiniciarCamposModificarMarca() {
        jcbMarcasModificar.setSelectedIndex(0);
        jtfNuevoNombreMarcaModificar.setText("");
        alMarcas.clear();
        limpiarTablas();
        try {
            alMarcas = ges.buscarMarcas();
            if (alMarcas.size() == 0) {
                /* Por cuestiones de estética, si no hubiese registros reseta el autoincremental para los futuros registros. */
                ges.resetearAutoincrementalMarca();
            } else {
                for (int i = 0; i < alMarcas.size(); i++) {
                    /* Incrementa filas: */
                    dtmMarcas.setRowCount(dtmMarcas.getRowCount() + 1);
                    jtTablaModificarMarca.setValueAt(alMarcas.get(i).getId_marca(), i, 0);
                    jtTablaModificarMarca.setValueAt(alMarcas.get(i).getNombre_marca(), i, 1);
                }
            }
            cargarMarcasModificables();
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método usado para resetear los campos de la sección Eliminar Marca de
     * cara a una nueva eliminación.
     */
    private void reiniciarCamposEliminarMarca() {
        jtfNombreMarcaEliminar.setText("");
        alMarcas.clear();
        limpiarTablas();
        try {
            alMarcas = ges.buscarMarcas();
            if (alMarcas.size() == 0) {
                /* Por cuestiones de estética, si no hubiese registros reseta el autoincremental para los futuros registros. */
                ges.resetearAutoincrementalMarca();
            } else {
                for (int i = 0; i < alMarcas.size(); i++) {
                    /* Incrementa filas: */
                    dtmMarcas.setRowCount(dtmMarcas.getRowCount() + 1);
                    jtTablaEliminarMarca.setValueAt(alMarcas.get(i).getId_marca(), i, 0);
                    jtTablaEliminarMarca.setValueAt(alMarcas.get(i).getNombre_marca(), i, 1);
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método usado para resetear los campos de la sección Crear Modelo de cara
     * a una nueva inserción.
     */
    private void reiniciarCamposCrearModelo() {
        jtfNombreNuevoModelo.setText("");
        jtfConsumoNuevoModelo.setText("");
        jtfEmisionesNuevoModelo.setText("");
        jcbMarcaNuevoModelo.setSelectedIndex(0);
        jcbEficienciaNuevoModelo.setSelectedIndex(0);
        alModelos.clear();
        limpiarTablas();
        try {
            alModelos = ges.buscarModelos();
            if (alModelos.size() == 0) {
                /* Por cuestiones de estética, si no hubiese registros reseta el autoincremental para los futuros registros. */
                ges.resetearAutoincrementalModelo();
            } else {
                for (int i = 0; i < alModelos.size(); i++) {
                    /* Incrementa filas: */
                    dtmModelos.setRowCount(dtmModelos.getRowCount() + 1);
                    /* Recupera el nombre de la marca y la eficiencia según sus ID's: */
                    String nombreMarca = ges.buscarMarcaPorId(alModelos.get(i).getId_marca());
                    String nombreEficiencia = ges.buscarEficienciasPorId(alModelos.get(i).getId_eficiencia());
                    jtTablaCrearModelo.setValueAt(nombreMarca, i, 0);
                    jtTablaCrearModelo.setValueAt(alModelos.get(i).getNombre_modelo(), i, 1);
                    jtTablaCrearModelo.setValueAt(alModelos.get(i).getConsumo_modelo(), i, 2);
                    jtTablaCrearModelo.setValueAt(alModelos.get(i).getEmisiones_modelo(), i, 3);
                    jtTablaCrearModelo.setValueAt(nombreEficiencia, i, 4);
                }
            }
            cargarMarcasEficienciasNuevoModelo();
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método usado para resetear los campos de la sección Modificar Modelo de
     * cara a una nueva modificación.
     */
    private void reiniciarCamposModificarModelo() {

    }

    /**
     * Método usado para resetear los campos de la sección Eliminar Modelo de
     * cara a una nueva eliminación.
     */
    private void reiniciarCamposEliminarModelo() {
        jtfNombreModeloEliminar.setText("");
        alModelos.clear();
        limpiarTablas();
        try {
            alModelos = ges.buscarModelos();
            if (alModelos.size() == 0) {
                /* Por cuestiones de estética, si no hubiese registros reseta el autoincremental para los futuros registros. */
                ges.resetearAutoincrementalModelo();
            } else {
                for (int i = 0; i < alModelos.size(); i++) {
                    /* Incrementa filas: */
                    dtmModelos.setRowCount(dtmModelos.getRowCount() + 1);
                    /* Recupera el nombre de la marca y la eficiencia según sus ID's: */
                    String nombreMarca = ges.buscarMarcaPorId(alModelos.get(i).getId_marca());
                    String nombreEficiencia = ges.buscarEficienciasPorId(alModelos.get(i).getId_eficiencia());
                    jtTablaEliminarModelo.setValueAt(nombreMarca, i, 0);
                    jtTablaEliminarModelo.setValueAt(alModelos.get(i).getNombre_modelo(), i, 1);
                    jtTablaEliminarModelo.setValueAt(alModelos.get(i).getConsumo_modelo(), i, 2);
                    jtTablaEliminarModelo.setValueAt(alModelos.get(i).getEmisiones_modelo(), i, 3);
                    jtTablaEliminarModelo.setValueAt(nombreEficiencia, i, 4);
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtpVentanasModulos = new javax.swing.JTabbedPane();
        jpMarcas = new javax.swing.JPanel();
        jtpSeccionesMarcas = new javax.swing.JTabbedPane();
        jpCrearMarca = new javax.swing.JPanel();
        jlTituloCrearMarca = new javax.swing.JLabel();
        jsSeparadorCrearMarca = new javax.swing.JSeparator();
        jspTablaCrearMarca = new javax.swing.JScrollPane();
        jtTablaCrearMarca = new javax.swing.JTable();
        jlNombreNuevaMarca = new javax.swing.JLabel();
        jtfNombreNuevaMarca = new javax.swing.JTextField();
        jbCrearMarca = new javax.swing.JButton();
        jpModificarMarca = new javax.swing.JPanel();
        jlTituloModificarMarca = new javax.swing.JLabel();
        jsSeparadorModificarMarca = new javax.swing.JSeparator();
        jlNombreMarcaActualModificar = new javax.swing.JLabel();
        jcbMarcasModificar = new javax.swing.JComboBox<>();
        jspTablaModificarMarca = new javax.swing.JScrollPane();
        jtTablaModificarMarca = new javax.swing.JTable();
        jlNuevoNombreMarcaModificar = new javax.swing.JLabel();
        jtfNuevoNombreMarcaModificar = new javax.swing.JTextField();
        jbModificarMarca = new javax.swing.JButton();
        jpEliminarMarca = new javax.swing.JPanel();
        jlTituloEliminarMarca = new javax.swing.JLabel();
        jsSeparadorEliminarMarca = new javax.swing.JSeparator();
        jspTablaEliminarMarca = new javax.swing.JScrollPane();
        jtTablaEliminarMarca = new javax.swing.JTable();
        jlNombreMarcaEliminar = new javax.swing.JLabel();
        jtfNombreMarcaEliminar = new javax.swing.JTextField();
        jbEliminarMarca = new javax.swing.JButton();
        jpModelos = new javax.swing.JPanel();
        jtpSeccionesModelos = new javax.swing.JTabbedPane();
        jpCrearModelo = new javax.swing.JPanel();
        jlTituloCrearModelo = new javax.swing.JLabel();
        jsSeparadorCrearModelo = new javax.swing.JSeparator();
        jspTablaCrearModelo = new javax.swing.JScrollPane();
        jtTablaCrearModelo = new javax.swing.JTable();
        jlNombreNuevoModelo = new javax.swing.JLabel();
        jtfNombreNuevoModelo = new javax.swing.JTextField();
        jlMarcaNuevoModelo = new javax.swing.JLabel();
        jcbMarcaNuevoModelo = new javax.swing.JComboBox<>();
        jlConsumoNuevoModelo = new javax.swing.JLabel();
        jtfConsumoNuevoModelo = new javax.swing.JTextField();
        jlUnidadesConsumoNuevoModelo = new javax.swing.JLabel();
        jlEmisionesNuevoModelo = new javax.swing.JLabel();
        jtfEmisionesNuevoModelo = new javax.swing.JTextField();
        jlUnidadesEmisionesNuevoModelo = new javax.swing.JLabel();
        jlEficienciaNuevoModelo = new javax.swing.JLabel();
        jcbEficienciaNuevoModelo = new javax.swing.JComboBox<>();
        jbCrearModelo = new javax.swing.JButton();
        jpModificarModelo = new javax.swing.JPanel();
        jlTituloModificarModelo = new javax.swing.JLabel();
        jsSeparadorModificarModelo = new javax.swing.JSeparator();
        jlNombreModeloActualModificar = new javax.swing.JLabel();
        jcbModelosModificar = new javax.swing.JComboBox<>();
        jspTablaModificarModelo = new javax.swing.JScrollPane();
        jtTablaModificarModelo = new javax.swing.JTable();
        jlNuevoNombreModeloModificar = new javax.swing.JLabel();
        jtfNuevoNombreModeloModificar = new javax.swing.JTextField();
        jlNuevaMarcaModelo = new javax.swing.JLabel();
        jcbNuevaMarcaModelo = new javax.swing.JComboBox<>();
        jlNuevoConsumoModelo = new javax.swing.JLabel();
        jtfNuevoConsumoModelo = new javax.swing.JTextField();
        jlNuevasEmisionesModelo = new javax.swing.JLabel();
        jtfNuevasEmisionesModelo = new javax.swing.JTextField();
        jlNuevaEficienciaModelo = new javax.swing.JLabel();
        jcbNuevaEficienciaModelo = new javax.swing.JComboBox<>();
        jbModificarModelo = new javax.swing.JButton();
        jpEliminarModelo = new javax.swing.JPanel();
        jlTituloEliminarModelo = new javax.swing.JLabel();
        jsSeparadorEliminarModelo = new javax.swing.JSeparator();
        jspTablaEliminarModelo = new javax.swing.JScrollPane();
        jtTablaEliminarModelo = new javax.swing.JTable();
        jlNombreModeloEliminar = new javax.swing.JLabel();
        jtfNombreModeloEliminar = new javax.swing.JTextField();
        jbEliminarModelo = new javax.swing.JButton();
        jmbBarraMenu = new javax.swing.JMenuBar();
        jmModulos = new javax.swing.JMenu();
        jmiConectar = new javax.swing.JMenuItem();
        jmInfo = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 400));

        jlTituloCrearMarca.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloCrearMarca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloCrearMarca.setText("CREAR MARCA");

        jtTablaCrearMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaCrearMarca.setViewportView(jtTablaCrearMarca);

        jlNombreNuevaMarca.setText("Nombre de la nueva marca:");

        jbCrearMarca.setText("Crear nueva marca");
        jbCrearMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCrearMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCrearMarcaLayout = new javax.swing.GroupLayout(jpCrearMarca);
        jpCrearMarca.setLayout(jpCrearMarcaLayout);
        jpCrearMarcaLayout.setHorizontalGroup(
            jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(jsSeparadorCrearMarca)
                    .addComponent(jlTituloCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearMarcaLayout.createSequentialGroup()
                        .addComponent(jlNombreNuevaMarca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNombreNuevaMarca))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearMarcaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbCrearMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jpCrearMarcaLayout.setVerticalGroup(
            jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloCrearMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorCrearMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreNuevaMarca)
                    .addComponent(jtfNombreNuevaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCrearMarca)
                .addContainerGap())
        );

        jtpSeccionesMarcas.addTab("Crear marca", jpCrearMarca);

        jlTituloModificarMarca.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloModificarMarca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloModificarMarca.setText("MODIFICAR MARCA");

        jlNombreMarcaActualModificar.setText("Seleccione la marca a modificar:");

        jcbMarcasModificar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una marca..." }));
        jcbMarcasModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMarcasModificarActionPerformed(evt);
            }
        });

        jtTablaModificarMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaModificarMarca.setViewportView(jtTablaModificarMarca);

        jlNuevoNombreMarcaModificar.setText("Nuevo nombre de la marca:");

        jbModificarMarca.setText("Modificar marca");
        jbModificarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificarMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpModificarMarcaLayout = new javax.swing.GroupLayout(jpModificarMarca);
        jpModificarMarca.setLayout(jpModificarMarcaLayout);
        jpModificarMarcaLayout.setHorizontalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpModificarMarcaLayout.createSequentialGroup()
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jsSeparadorModificarMarca))
                    .addComponent(jlTituloModificarMarca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbModificarMarca))
                    .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                                .addComponent(jlNombreMarcaActualModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcbMarcasModificar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpModificarMarcaLayout.createSequentialGroup()
                                .addComponent(jlNuevoNombreMarcaModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfNuevoNombreMarcaModificar)))))
                .addContainerGap())
        );
        jpModificarMarcaLayout.setVerticalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloModificarMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorModificarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreMarcaActualModificar)
                    .addComponent(jcbMarcasModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevoNombreMarcaModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevoNombreMarcaModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbModificarMarca)
                .addContainerGap())
        );

        jtpSeccionesMarcas.addTab("Modificar marca", jpModificarMarca);

        jlTituloEliminarMarca.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloEliminarMarca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloEliminarMarca.setText("ELIMINAR MARCA");

        jtTablaEliminarMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaEliminarMarca.setViewportView(jtTablaEliminarMarca);

        jlNombreMarcaEliminar.setText("Nombre de la marca a eliminar:");

        jbEliminarMarca.setText("Eliminar marca");
        jbEliminarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarMarcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpEliminarMarcaLayout = new javax.swing.GroupLayout(jpEliminarMarca);
        jpEliminarMarca.setLayout(jpEliminarMarcaLayout);
        jpEliminarMarcaLayout.setHorizontalGroup(
            jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEliminarMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(jlTituloEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorEliminarMarca)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEliminarMarcaLayout.createSequentialGroup()
                        .addComponent(jlNombreMarcaEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNombreMarcaEliminar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEliminarMarcaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbEliminarMarca)))
                .addContainerGap())
        );
        jpEliminarMarcaLayout.setVerticalGroup(
            jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEliminarMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloEliminarMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorEliminarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreMarcaEliminar)
                    .addComponent(jtfNombreMarcaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEliminarMarca)
                .addContainerGap())
        );

        jtpSeccionesMarcas.addTab("Eliminar marca", jpEliminarMarca);

        javax.swing.GroupLayout jpMarcasLayout = new javax.swing.GroupLayout(jpMarcas);
        jpMarcas.setLayout(jpMarcasLayout);
        jpMarcasLayout.setHorizontalGroup(
            jpMarcasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesMarcas)
        );
        jpMarcasLayout.setVerticalGroup(
            jpMarcasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesMarcas)
        );

        jtpVentanasModulos.addTab("Marcas", jpMarcas);

        jlTituloCrearModelo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloCrearModelo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloCrearModelo.setText("CREAR MODELO");

        jtTablaCrearModelo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaCrearModelo.setViewportView(jtTablaCrearModelo);

        jlNombreNuevoModelo.setText("Nombre del nuevo modelo:");

        jlMarcaNuevoModelo.setText("Marca del nuevo modelo:");

        jcbMarcaNuevoModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una marca..." }));

        jlConsumoNuevoModelo.setText("Consumo del nuevo modelo:");

        jlUnidadesConsumoNuevoModelo.setText("L/100km");

        jlEmisionesNuevoModelo.setText("Emisiones del nuevo modelo:");

        jlUnidadesEmisionesNuevoModelo.setText("gCO2/km");

        jlEficienciaNuevoModelo.setText("Eficiencia del nuevo modelo:");

        jcbEficienciaNuevoModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una eficiencia..." }));

        jbCrearModelo.setText("Crear modelo");
        jbCrearModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCrearModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpCrearModeloLayout = new javax.swing.GroupLayout(jpCrearModelo);
        jpCrearModelo.setLayout(jpCrearModeloLayout);
        jpCrearModeloLayout.setHorizontalGroup(
            jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlTituloCrearModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorCrearModelo)
                    .addComponent(jspTablaCrearModelo)
                    .addGroup(jpCrearModeloLayout.createSequentialGroup()
                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlConsumoNuevoModelo)
                            .addComponent(jlEmisionesNuevoModelo)
                            .addComponent(jlMarcaNuevoModelo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtfEmisionesNuevoModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                                    .addComponent(jtfConsumoNuevoModelo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlUnidadesConsumoNuevoModelo)
                                    .addComponent(jlUnidadesEmisionesNuevoModelo, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(jcbMarcaNuevoModelo, 0, 432, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearModeloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearModeloLayout.createSequentialGroup()
                                .addComponent(jlEficienciaNuevoModelo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbEficienciaNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbCrearModelo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                .addComponent(jlNombreNuevoModelo)
                                .addGap(22, 22, 22)
                                .addComponent(jtfNombreNuevoModelo)))))
                .addContainerGap())
        );
        jpCrearModeloLayout.setVerticalGroup(
            jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloCrearModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorCrearModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaCrearModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNombreNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNombreNuevoModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlMarcaNuevoModelo)
                    .addComponent(jcbMarcaNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfConsumoNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlConsumoNuevoModelo)
                    .addComponent(jlUnidadesConsumoNuevoModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfEmisionesNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlEmisionesNuevoModelo)
                    .addComponent(jlUnidadesEmisionesNuevoModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbEficienciaNuevoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlEficienciaNuevoModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCrearModelo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpSeccionesModelos.addTab("Crear modelo", jpCrearModelo);

        jlTituloModificarModelo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloModificarModelo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloModificarModelo.setText("MODIFICAR MODELO");

        jlNombreModeloActualModificar.setText("Seleccione el modelo a modificar:");

        jcbModelosModificar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un modelo..." }));

        jtTablaModificarModelo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaModificarModelo.setViewportView(jtTablaModificarModelo);

        jlNuevoNombreModeloModificar.setText("Nuevo nombre del modelo:");

        jlNuevaMarcaModelo.setText("Nueva marca del modelo:");

        jcbNuevaMarcaModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una marca..." }));

        jlNuevoConsumoModelo.setText("Nuevo consumo del modelo:");

        jlNuevasEmisionesModelo.setText("Nuevas emisiones del modelo:");

        jlNuevaEficienciaModelo.setText("Nueva eficiencia del modelo:");

        jcbNuevaEficienciaModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una eficiencia..." }));

        jbModificarModelo.setText("Modificar modelo");
        jbModificarModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificarModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpModificarModeloLayout = new javax.swing.GroupLayout(jpModificarModelo);
        jpModificarModelo.setLayout(jpModificarModeloLayout);
        jpModificarModeloLayout.setHorizontalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModificarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaModificarModelo)
                    .addComponent(jlTituloModificarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorModificarModelo)
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNombreModeloActualModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbModelosModificar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNuevoNombreModeloModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpModificarModeloLayout.createSequentialGroup()
                                .addGap(0, 320, Short.MAX_VALUE)
                                .addComponent(jbModificarModelo))
                            .addComponent(jtfNuevoNombreModeloModificar)))
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNuevaEficienciaModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbNuevaEficienciaModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNuevasEmisionesModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNuevasEmisionesModelo))
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNuevoConsumoModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNuevoConsumoModelo))
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addComponent(jlNuevaMarcaModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbNuevaMarcaModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpModificarModeloLayout.setVerticalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModificarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloModificarModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorModificarModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbModelosModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNombreModeloActualModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaModificarModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevoNombreModeloModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevoNombreModeloModificar))
                .addGap(13, 13, 13)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNuevaMarcaModelo)
                    .addComponent(jcbNuevaMarcaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNuevoConsumoModelo)
                    .addComponent(jtfNuevoConsumoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevasEmisionesModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevasEmisionesModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbNuevaEficienciaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevaEficienciaModelo))
                .addGap(28, 28, 28)
                .addComponent(jbModificarModelo)
                .addContainerGap())
        );

        jtpSeccionesModelos.addTab("Modificar modelo", jpModificarModelo);

        jlTituloEliminarModelo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloEliminarModelo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloEliminarModelo.setText("ELIMINAR MODELO");

        jtTablaEliminarModelo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaEliminarModelo.setViewportView(jtTablaEliminarModelo);

        jlNombreModeloEliminar.setText("Nombre del modelo a eliminar:");

        jbEliminarModelo.setText("Eliminar modelo");
        jbEliminarModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpEliminarModeloLayout = new javax.swing.GroupLayout(jpEliminarModelo);
        jpEliminarModelo.setLayout(jpEliminarModeloLayout);
        jpEliminarModeloLayout.setHorizontalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEliminarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaEliminarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(jlTituloEliminarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorEliminarModelo)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEliminarModeloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbEliminarModelo))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEliminarModeloLayout.createSequentialGroup()
                        .addComponent(jlNombreModeloEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNombreModeloEliminar)))
                .addContainerGap())
        );
        jpEliminarModeloLayout.setVerticalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEliminarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloEliminarModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorEliminarModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaEliminarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNombreModeloEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNombreModeloEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEliminarModelo)
                .addContainerGap())
        );

        jtpSeccionesModelos.addTab("Eliminar modelo", jpEliminarModelo);

        javax.swing.GroupLayout jpModelosLayout = new javax.swing.GroupLayout(jpModelos);
        jpModelos.setLayout(jpModelosLayout);
        jpModelosLayout.setHorizontalGroup(
            jpModelosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesModelos, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        );
        jpModelosLayout.setVerticalGroup(
            jpModelosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesModelos, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );

        jtpVentanasModulos.addTab("Modelos", jpModelos);

        jmModulos.setText("Opciones");

        jmiConectar.setText("Conectar con BD");
        jmiConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiConectarActionPerformed(evt);
            }
        });
        jmModulos.add(jmiConectar);

        jmbBarraMenu.add(jmModulos);

        jmInfo.setText("Info");
        jmbBarraMenu.add(jmInfo);

        setJMenuBar(jmbBarraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpVentanasModulos)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpVentanasModulos)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmiConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiConectarActionPerformed
        iniciarElementos();
    }//GEN-LAST:event_jmiConectarActionPerformed

    private void jbCrearMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCrearMarcaActionPerformed
        crearMarca();
    }//GEN-LAST:event_jbCrearMarcaActionPerformed

    private void jcbMarcasModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMarcasModificarActionPerformed
//        String marcaSeleccionada = (String) jcbMarcasModificar.getSelectedItem();
//        if (marcaSeleccionada.compareToIgnoreCase("Seleccione una marca...") == 0) {
//            jtfNuevoNombreModificar.setEnabled(false);
//            jtfNuevoNombreModificar.setText("");
//            jbModificarMarca.setEnabled(false);
//            JOptionPane.showMessageDialog(
//                    null,
//                    "Seleccione una marca válida en el selector desplegable.",
//                    "Información",
//                    JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            jtfNuevoNombreModificar.setEnabled(true);
//            jbModificarMarca.setEnabled(true);
//        }
    }//GEN-LAST:event_jcbMarcasModificarActionPerformed

    private void jbModificarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificarMarcaActionPerformed
        modificarMarca();
    }//GEN-LAST:event_jbModificarMarcaActionPerformed

    private void jbEliminarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarMarcaActionPerformed
        eliminarMarca();
    }//GEN-LAST:event_jbEliminarMarcaActionPerformed

    private void jbCrearModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCrearModeloActionPerformed
        crearModelo();
    }//GEN-LAST:event_jbCrearModeloActionPerformed

    private void jbModificarModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificarModeloActionPerformed
        modificarModelo();
    }//GEN-LAST:event_jbModificarModeloActionPerformed

    private void jbEliminarModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarModeloActionPerformed
        eliminarModelo();
    }//GEN-LAST:event_jbEliminarModeloActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFGestorVehiculos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbCrearMarca;
    private javax.swing.JButton jbCrearModelo;
    private javax.swing.JButton jbEliminarMarca;
    private javax.swing.JButton jbEliminarModelo;
    private javax.swing.JButton jbModificarMarca;
    private javax.swing.JButton jbModificarModelo;
    private javax.swing.JComboBox<String> jcbEficienciaNuevoModelo;
    private javax.swing.JComboBox<String> jcbMarcaNuevoModelo;
    private javax.swing.JComboBox<String> jcbMarcasModificar;
    private javax.swing.JComboBox<String> jcbModelosModificar;
    private javax.swing.JComboBox<String> jcbNuevaEficienciaModelo;
    private javax.swing.JComboBox<String> jcbNuevaMarcaModelo;
    private javax.swing.JLabel jlConsumoNuevoModelo;
    private javax.swing.JLabel jlEficienciaNuevoModelo;
    private javax.swing.JLabel jlEmisionesNuevoModelo;
    private javax.swing.JLabel jlMarcaNuevoModelo;
    private javax.swing.JLabel jlNombreMarcaActualModificar;
    private javax.swing.JLabel jlNombreMarcaEliminar;
    private javax.swing.JLabel jlNombreModeloActualModificar;
    private javax.swing.JLabel jlNombreModeloEliminar;
    private javax.swing.JLabel jlNombreNuevaMarca;
    private javax.swing.JLabel jlNombreNuevoModelo;
    private javax.swing.JLabel jlNuevaEficienciaModelo;
    private javax.swing.JLabel jlNuevaMarcaModelo;
    private javax.swing.JLabel jlNuevasEmisionesModelo;
    private javax.swing.JLabel jlNuevoConsumoModelo;
    private javax.swing.JLabel jlNuevoNombreMarcaModificar;
    private javax.swing.JLabel jlNuevoNombreModeloModificar;
    private javax.swing.JLabel jlTituloCrearMarca;
    private javax.swing.JLabel jlTituloCrearModelo;
    private javax.swing.JLabel jlTituloEliminarMarca;
    private javax.swing.JLabel jlTituloEliminarModelo;
    private javax.swing.JLabel jlTituloModificarMarca;
    private javax.swing.JLabel jlTituloModificarModelo;
    private javax.swing.JLabel jlUnidadesConsumoNuevoModelo;
    private javax.swing.JLabel jlUnidadesEmisionesNuevoModelo;
    private javax.swing.JMenu jmInfo;
    private javax.swing.JMenu jmModulos;
    private javax.swing.JMenuBar jmbBarraMenu;
    private javax.swing.JMenuItem jmiConectar;
    private javax.swing.JPanel jpCrearMarca;
    private javax.swing.JPanel jpCrearModelo;
    private javax.swing.JPanel jpEliminarMarca;
    private javax.swing.JPanel jpEliminarModelo;
    private javax.swing.JPanel jpMarcas;
    private javax.swing.JPanel jpModelos;
    private javax.swing.JPanel jpModificarMarca;
    private javax.swing.JPanel jpModificarModelo;
    private javax.swing.JSeparator jsSeparadorCrearMarca;
    private javax.swing.JSeparator jsSeparadorCrearModelo;
    private javax.swing.JSeparator jsSeparadorEliminarMarca;
    private javax.swing.JSeparator jsSeparadorEliminarModelo;
    private javax.swing.JSeparator jsSeparadorModificarMarca;
    private javax.swing.JSeparator jsSeparadorModificarModelo;
    private javax.swing.JScrollPane jspTablaCrearMarca;
    private javax.swing.JScrollPane jspTablaCrearModelo;
    private javax.swing.JScrollPane jspTablaEliminarMarca;
    private javax.swing.JScrollPane jspTablaEliminarModelo;
    private javax.swing.JScrollPane jspTablaModificarMarca;
    private javax.swing.JScrollPane jspTablaModificarModelo;
    private javax.swing.JTable jtTablaCrearMarca;
    private javax.swing.JTable jtTablaCrearModelo;
    private javax.swing.JTable jtTablaEliminarMarca;
    private javax.swing.JTable jtTablaEliminarModelo;
    private javax.swing.JTable jtTablaModificarMarca;
    private javax.swing.JTable jtTablaModificarModelo;
    private javax.swing.JTextField jtfConsumoNuevoModelo;
    private javax.swing.JTextField jtfEmisionesNuevoModelo;
    private javax.swing.JTextField jtfNombreMarcaEliminar;
    private javax.swing.JTextField jtfNombreModeloEliminar;
    private javax.swing.JTextField jtfNombreNuevaMarca;
    private javax.swing.JTextField jtfNombreNuevoModelo;
    private javax.swing.JTextField jtfNuevasEmisionesModelo;
    private javax.swing.JTextField jtfNuevoConsumoModelo;
    private javax.swing.JTextField jtfNuevoNombreMarcaModificar;
    private javax.swing.JTextField jtfNuevoNombreModeloModificar;
    private javax.swing.JTabbedPane jtpSeccionesMarcas;
    private javax.swing.JTabbedPane jtpSeccionesModelos;
    private javax.swing.JTabbedPane jtpVentanasModulos;
    // End of variables declaration//GEN-END:variables
}
