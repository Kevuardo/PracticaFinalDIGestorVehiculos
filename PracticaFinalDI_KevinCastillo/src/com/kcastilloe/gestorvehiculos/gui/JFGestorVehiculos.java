package com.kcastilloe.gestorvehiculos.gui;

import com.kcastilloe.gestorvehiculos.modelo.Eficiencia;
import com.kcastilloe.gestorvehiculos.modelo.Marca;
import com.kcastilloe.gestorvehiculos.modelo.Modelo;
import com.kcastilloe.gestorvehiculos.persistencia.GestorBBDD;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
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
        this.setTitle("Gestor de Vehículos - Kevin Castillo");
        
        /* Icono de aplicación. */
        try {
            Image icono = ImageIO.read(new File("recursos/icono.png"));
            this.setIconImage(icono);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se ha podido poner icono de aplicación.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
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
        jtTablaConsultaModelos.setModel(dtmModelos);

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
                    reiniciarCamposConsultarModelo();
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
                    reiniciarCamposConsultarModelo();
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
            reiniciarCamposConsultarModelo();
            cargarMarcasModificables();
            cargarMarcasEficienciasModelo();
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

    /**
     * Método encargado de rellenar dinámicamente los JComboBox de Modelo.
     */
    private void cargarMarcasEficienciasModelo() {
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

            /* Combobox de marcas de Consultar Modelo. */
 /* Borra los elementos (si los hubiera) que no son "Seleccione una marca...". */
            for (int i = jcbMarcasConsultaModelos.getItemCount() - 1; i > 0; i--) {
                jcbMarcasConsultaModelos.removeItemAt(i);
            }
            /* Añade los elementos recogidos de la base de datos. */
            for (int i = 0; i < alMarcas.size(); i++) {
                jcbMarcasConsultaModelos.addItem(alMarcas.get(i).getNombre_marca());
            }

            /* Combobox de eficiencias de Consultar Modelo. */
 /* Borra los elementos (si los hubiera) que no son "Seleccione una eficiencia...". */
            for (int i = jcbEficienciasConsultaModelos.getItemCount() - 1; i > 0; i--) {
                jcbEficienciasConsultaModelos.removeItemAt(i);
            }
            /* Añade los elementos recogidos de la base de datos. */
            for (int i = 0; i < alEficiencias.size(); i++) {
                jcbEficienciasConsultaModelos.addItem(alEficiencias.get(i).getNombre_eficiencia());
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
    private void modificarModelo() {
        boolean yaExiste = false;

        /* Evalúa la opción seleccionada en el JCombobox de Modelos. */
        String modeloSeleccionado = (String) jcbModelosModificar.getSelectedItem();
        if (modeloSeleccionado.compareToIgnoreCase("Seleccione un modelo...") == 0) {
            jtfNuevoNombreMarcaModificar.setText("");
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un modelo válido en el selector desplegable.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String nuevoNombreModelo = jtfNuevoNombreModeloModificar.getText().toUpperCase();
            if (nuevoNombreModelo.trim().compareToIgnoreCase("") == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Introduzca un nombre válido para el modelo.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                String marcaSeleccionada = (String) jcbNuevaMarcaModelo.getSelectedItem();
                if (marcaSeleccionada.compareToIgnoreCase("Seleccione una marca...") == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Seleccione una marca válida en el selector desplegable.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String consumoNuevoModelo = jtfNuevoConsumoModelo.getText();
                    if (consumoNuevoModelo.trim().compareToIgnoreCase("") == 0) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Introduzca un consumo válido para el modelo.",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String emisionesNuevoModelo = jtfNuevasEmisionesModelo.getText();
                        if (emisionesNuevoModelo.trim().compareToIgnoreCase("") == 0) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Introduzca unas emisionas válidas para el modelo.",
                                    "Información",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            String eficienciaSeleccionada = (String) jcbNuevaEficienciaModelo.getSelectedItem();
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
                                    int idModeloModificable = jcbModelosModificar.getSelectedIndex();
                                    miModelo = new Modelo(idModeloModificable, nuevoNombreModelo, idMarca, idEficiencia, consumoNumerico, emisionesNumerico);
                                    yaExiste = ges.existeModelo(miModelo);
                                    if (!yaExiste) {
                                        ges.modificarModelo(miModelo);
                                        reiniciarCamposModificarModelo();
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Modelo modificado con éxito",
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
     * Método usado para buscar modelos según los parámetros que seleccione el
     * usuario.
     */
    private void consultarModelos() {
        String marcaBusqueda = (String) jcbMarcasConsultaModelos.getSelectedItem();
        if (marcaBusqueda.compareToIgnoreCase("Seleccione una marca...") == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una marca válida en el selector desplegable.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            int idMarca = jcbMarcasConsultaModelos.getSelectedIndex();
            if (jlConsumoConsultaModelos.getText().compareToIgnoreCase("Consumo máximo: --- L/100km") == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un consumo válido en el selector deslizable.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                float consumoMaximoSeleccionado = jslSelectorConsumo.getValue();
                String eficienciaBusqueda = (String) jcbEficienciasConsultaModelos.getSelectedItem();
                if (eficienciaBusqueda.compareToIgnoreCase("Seleccione una eficiencia...") == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Seleccione una eficiencia válida en el selector desplegable.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int idEficiencia = jcbEficienciasConsultaModelos.getSelectedIndex();
                    if (jlEmisionesConsultaModelos.getText().compareToIgnoreCase("Emisiones máximas: --- gCO2/km") == 0) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Seleccione unas emisiones válidas en el selector deslizable.",
                                "Información",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        float emisionesMaximasSeleccionadas = jslSelectorEmisiones.getValue();
                        miModelo = new Modelo(idMarca, consumoMaximoSeleccionado, emisionesMaximasSeleccionadas, idEficiencia);
                        try {
                            alModelos.clear();
                            limpiarTablas();
                            alModelos = ges.buscarModelosEspecificos(miModelo);
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
                                    jtTablaConsultaModelos.setValueAt(nombreMarca, i, 0);
                                    jtTablaConsultaModelos.setValueAt(alModelos.get(i).getNombre_modelo(), i, 1);
                                    jtTablaConsultaModelos.setValueAt(alModelos.get(i).getConsumo_modelo(), i, 2);
                                    jtTablaConsultaModelos.setValueAt(alModelos.get(i).getEmisiones_modelo(), i, 3);
                                    jtTablaConsultaModelos.setValueAt(nombreEficiencia, i, 4);
                                }
                            }
                            jbReiniciarConsulta.setEnabled(true);
                            jbExportarModelos.setEnabled(true);
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
            }
        }
    }

    /**
     * Método usado para exportar a un documento .csv (compatible con Microsoft
     * Excel) los resultados de la consulta realizada.
     *
     */
    private void exportarResultadosConsulta() {
        String nombre_marca = null;
        String nombre_modelo = null;
        float consumo_modelo = 0f;
        float emisiones_modelo = 0f;
        String eficiencia_modelo = null;
        String nombreFichero = null;
        boolean nombreCorrecto = false;
        alModelos.clear();
        /* Primero recoge los modelos que han resultado de la consulta y figuran en la tabla. */
        for (int i = 0; i < jtTablaConsultaModelos.getRowCount(); i++) {
            nombre_marca = jtTablaConsultaModelos.getValueAt(i, 0).toString();
            nombre_modelo = jtTablaConsultaModelos.getValueAt(i, 1).toString();
            consumo_modelo = Float.parseFloat(jtTablaConsultaModelos.getValueAt(i, 2).toString());
            emisiones_modelo = Float.parseFloat(jtTablaConsultaModelos.getValueAt(i, 3).toString());
            eficiencia_modelo = jtTablaConsultaModelos.getValueAt(i, 4).toString();
            /* Crea un nuevo objeto Modelo para cada fila de la tabla. */
            miModelo = new Modelo(nombre_marca, nombre_modelo, eficiencia_modelo, consumo_modelo, emisiones_modelo);
            /* Lo añade al ArrayList de Modelos. */
            alModelos.add(miModelo);
        }

        /* Pide nombre para el documento exportado. */
        while (!nombreCorrecto) {

            /* Lo siguiente es pedir al usuario un nombre para el fichero. */
            nombreFichero = JOptionPane.showInputDialog(null, "Seleccione un nombre para el fichero .csv:",
                    "Nombre del fichero", JOptionPane.INFORMATION_MESSAGE);
            if (nombreFichero == null) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún nombre de fichero.",
                            "Error", JOptionPane.ERROR_MESSAGE);
            } else if (nombreFichero.trim().compareToIgnoreCase("") == 0) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un nombre válido para el fichero.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                nombreCorrecto = true;
            }
        }
        /* Creación del documento. */
        if (nombreCorrecto) {
            /* Evalúa que de verdad el nombre sea correcto por si el usuario cierra el InputDialog.*/
            PrintWriter fichero = null;
            try {
                fichero = new PrintWriter(nombreFichero + ".csv");
                /* Nombre del fichero. */
                fichero.println("Marca;Modelo;Consumo;Emisiones;Eficiencia");
                /* Campos. */
                /* A continuación recorre el ArrayList e imprime cada registro en el documento. */
                for (int i = 0; i < alModelos.size(); i++) {
                    fichero.println("" + alModelos.get(i).getNombre_marca() + ";"
                            + alModelos.get(i).getNombre_modelo() + ";"
                            + alModelos.get(i).getConsumo_modelo() + ";"
                            + alModelos.get(i).getEmisiones_modelo() + ";"
                            + alModelos.get(i).getNombre_eficiencia() + ";");
                }
                fichero.flush();
                reiniciarCamposConsultarModelo();
                JOptionPane.showMessageDialog(null, "Se ha generado el informe con éxito.",
                        "Informe exportado", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "No se ha podido exportar el fichero." + "\nInténtelo de nuevo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (fichero != null) {
                    reiniciarCamposConsultarModelo();
                    fichero.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al cerrar el fichero." + "\nInténtelo de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
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
            cargarMarcasEficienciasModelo();
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
        jcbModelosModificar.setSelectedIndex(0);
        jcbNuevaMarcaModelo.setSelectedIndex(0);
        jcbNuevaEficienciaModelo.setSelectedIndex(0);

        jtfNuevoNombreModeloModificar.setText("");
        jtfNuevoConsumoModelo.setText("");
        jtfNuevasEmisionesModelo.setText("");

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
                    jtTablaModificarModelo.setValueAt(nombreMarca, i, 0);
                    jtTablaModificarModelo.setValueAt(alModelos.get(i).getNombre_modelo(), i, 1);
                    jtTablaModificarModelo.setValueAt(alModelos.get(i).getConsumo_modelo(), i, 2);
                    jtTablaModificarModelo.setValueAt(alModelos.get(i).getEmisiones_modelo(), i, 3);
                    jtTablaModificarModelo.setValueAt(nombreEficiencia, i, 4);
                }
            }
            cargarMarcasEficienciasModelo();
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
            cargarMarcasEficienciasModelo();
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
     * Método usado para resetear los campos de la sección Consultar Modelo de
     * cara a una nueva consulta.
     */
    private void reiniciarCamposConsultarModelo() {
        jcbMarcasConsultaModelos.setSelectedIndex(0);
        jslSelectorConsumo.setValue(50);
        jslSelectorEmisiones.setValue(50);
        jlConsumoConsultaModelos.setText("Consumo máximo: --- L/100km");
        jlEmisionesConsultaModelos.setText("Emisiones máximas: --- gCO2/km");
        jcbEficienciasConsultaModelos.setSelectedIndex(0);
        jbReiniciarConsulta.setEnabled(false);
        jbExportarModelos.setEnabled(false);
        limpiarTablas();
        cargarMarcasEficienciasModelo();
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
        jlUnidadesConsumoModificarModelo = new javax.swing.JLabel();
        jtfNuevoConsumoModelo = new javax.swing.JTextField();
        jlNuevasEmisionesModelo = new javax.swing.JLabel();
        jlUnidadesEmisionesModificarModelo = new javax.swing.JLabel();
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
        jpConsultarModelo = new javax.swing.JPanel();
        jlTituloConsultarModelo = new javax.swing.JLabel();
        jsSeparadorConsultaModelo = new javax.swing.JSeparator();
        jlMarcaConsultaModelos = new javax.swing.JLabel();
        jcbMarcasConsultaModelos = new javax.swing.JComboBox<>();
        jlConsumoConsultaModelos = new javax.swing.JLabel();
        jslSelectorConsumo = new javax.swing.JSlider();
        jlEficienciaConsultaModelos = new javax.swing.JLabel();
        jcbEficienciasConsultaModelos = new javax.swing.JComboBox<>();
        jlEmisionesConsultaModelos = new javax.swing.JLabel();
        jslSelectorEmisiones = new javax.swing.JSlider();
        jbConsultarModelos = new javax.swing.JButton();
        jbReiniciarConsulta = new javax.swing.JButton();
        jspTablaConsultaModelos = new javax.swing.JScrollPane();
        jtTablaConsultaModelos = new javax.swing.JTable();
        jbExportarModelos = new javax.swing.JButton();
        jmbBarraMenu = new javax.swing.JMenuBar();
        jmModulos = new javax.swing.JMenu();
        jmiConectar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                    .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
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
                .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
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
                            .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
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
                .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
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
                    .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
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
                .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
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

        jtfNombreNuevoModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNombreNuevoModeloActionPerformed(evt);
            }
        });

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
                    .addComponent(jspTablaCrearModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearModeloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                .addComponent(jlEficienciaNuevoModelo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbEficienciaNuevoModelo, 0, 438, Short.MAX_VALUE))
                            .addComponent(jbCrearModelo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlConsumoNuevoModelo)
                                    .addComponent(jlMarcaNuevoModelo)
                                    .addComponent(jlNombreNuevoModelo)
                                    .addComponent(jlEmisionesNuevoModelo))
                                .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtfEmisionesNuevoModelo)
                                            .addComponent(jtfConsumoNuevoModelo))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jlUnidadesConsumoNuevoModelo)
                                            .addComponent(jlUnidadesEmisionesNuevoModelo, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(jpCrearModeloLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtfNombreNuevoModelo, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jcbMarcaNuevoModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
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
                .addComponent(jspTablaCrearModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
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
                .addContainerGap())
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

        jlUnidadesConsumoModificarModelo.setText("L/100km");

        jlNuevasEmisionesModelo.setText("Nuevas emisiones del modelo:");

        jlUnidadesEmisionesModificarModelo.setText("gCO2/km");

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
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpModificarModeloLayout.createSequentialGroup()
                                .addComponent(jlNuevasEmisionesModelo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jpModificarModeloLayout.createSequentialGroup()
                                .addComponent(jlNuevoConsumoModelo)
                                .addGap(19, 19, 19)))
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jtfNuevoConsumoModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                            .addComponent(jtfNuevasEmisionesModelo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlUnidadesConsumoModificarModelo)
                            .addComponent(jlUnidadesEmisionesModificarModelo, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jpModificarModeloLayout.createSequentialGroup()
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlNuevaMarcaModelo)
                            .addComponent(jlNuevoNombreModeloModificar))
                        .addGap(28, 28, 28)
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbNuevaMarcaModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtfNuevoNombreModeloModificar)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpModificarModeloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpModificarModeloLayout.createSequentialGroup()
                                .addComponent(jlNuevaEficienciaModelo)
                                .addGap(18, 18, 18)
                                .addComponent(jcbNuevaEficienciaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbModificarModelo))))
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
                .addComponent(jspTablaModificarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevoNombreModeloModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevoNombreModeloModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbNuevaMarcaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevaMarcaModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevoConsumoModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevoConsumoModelo)
                    .addComponent(jlUnidadesConsumoModificarModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevasEmisionesModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevasEmisionesModelo)
                    .addComponent(jlUnidadesEmisionesModificarModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbNuevaEficienciaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevaEficienciaModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(jspTablaEliminarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
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
                .addComponent(jspTablaEliminarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNombreModeloEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNombreModeloEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEliminarModelo)
                .addContainerGap())
        );

        jtpSeccionesModelos.addTab("Eliminar modelo", jpEliminarModelo);

        jlTituloConsultarModelo.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloConsultarModelo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloConsultarModelo.setText("CONSULTAR MODELO");

        jlMarcaConsultaModelos.setText("Marca:");

        jcbMarcasConsultaModelos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una marca..." }));

        jlConsumoConsultaModelos.setText("Consumo máximo: --- L/100km");

        jslSelectorConsumo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslSelectorConsumoStateChanged(evt);
            }
        });

        jlEficienciaConsultaModelos.setText("Eficiencia:");

        jcbEficienciasConsultaModelos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una eficiencia..." }));

        jlEmisionesConsultaModelos.setText("Emisiones máximas: --- gCO2/km");

        jslSelectorEmisiones.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslSelectorEmisionesStateChanged(evt);
            }
        });

        jbConsultarModelos.setText("Consultar modelos");
        jbConsultarModelos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConsultarModelosActionPerformed(evt);
            }
        });

        jbReiniciarConsulta.setText("Reiniciar consulta");
        jbReiniciarConsulta.setEnabled(false);
        jbReiniciarConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReiniciarConsultaActionPerformed(evt);
            }
        });

        jtTablaConsultaModelos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jspTablaConsultaModelos.setViewportView(jtTablaConsultaModelos);

        jbExportarModelos.setText("Exportar informe");
        jbExportarModelos.setEnabled(false);
        jbExportarModelos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbExportarModelosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpConsultarModeloLayout = new javax.swing.GroupLayout(jpConsultarModelo);
        jpConsultarModelo.setLayout(jpConsultarModeloLayout);
        jpConsultarModeloLayout.setHorizontalGroup(
            jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpConsultarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaConsultaModelos, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(jpConsultarModeloLayout.createSequentialGroup()
                        .addComponent(jlMarcaConsultaModelos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlConsumoConsultaModelos))
                    .addComponent(jlTituloConsultarModelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorConsultaModelo)
                    .addGroup(jpConsultarModeloLayout.createSequentialGroup()
                        .addComponent(jlEficienciaConsultaModelos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlEmisionesConsultaModelos))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpConsultarModeloLayout.createSequentialGroup()
                        .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jcbEficienciasConsultaModelos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcbMarcasConsultaModelos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jslSelectorConsumo, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                            .addComponent(jslSelectorEmisiones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpConsultarModeloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbExportarModelos, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpConsultarModeloLayout.createSequentialGroup()
                                .addComponent(jbConsultarModelos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbReiniciarConsulta)))))
                .addContainerGap())
        );
        jpConsultarModeloLayout.setVerticalGroup(
            jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpConsultarModeloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloConsultarModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorConsultaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlMarcaConsultaModelos)
                    .addComponent(jlConsumoConsultaModelos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbMarcasConsultaModelos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jslSelectorConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlEficienciaConsultaModelos)
                    .addComponent(jlEmisionesConsultaModelos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jslSelectorEmisiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbEficienciasConsultaModelos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpConsultarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbConsultarModelos)
                    .addComponent(jbReiniciarConsulta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaConsultaModelos, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbExportarModelos)
                .addContainerGap())
        );

        jtpSeccionesModelos.addTab("Consultar modelo", jpConsultarModelo);

        javax.swing.GroupLayout jpModelosLayout = new javax.swing.GroupLayout(jpModelos);
        jpModelos.setLayout(jpModelosLayout);
        jpModelosLayout.setHorizontalGroup(
            jpModelosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesModelos)
        );
        jpModelosLayout.setVerticalGroup(
            jpModelosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpSeccionesModelos)
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

    private void jslSelectorConsumoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslSelectorConsumoStateChanged
        jlConsumoConsultaModelos.setText("Consumo máximo: " + jslSelectorConsumo.getValue() + " L/100km");
    }//GEN-LAST:event_jslSelectorConsumoStateChanged

    private void jslSelectorEmisionesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslSelectorEmisionesStateChanged
        jlEmisionesConsultaModelos.setText("Emisiones máximas: " + jslSelectorEmisiones.getValue() + " gCO2/km");
    }//GEN-LAST:event_jslSelectorEmisionesStateChanged

    private void jbConsultarModelosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConsultarModelosActionPerformed
        consultarModelos();
    }//GEN-LAST:event_jbConsultarModelosActionPerformed

    private void jtfNombreNuevoModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNombreNuevoModeloActionPerformed

    }//GEN-LAST:event_jtfNombreNuevoModeloActionPerformed

    private void jbExportarModelosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbExportarModelosActionPerformed
        exportarResultadosConsulta();
    }//GEN-LAST:event_jbExportarModelosActionPerformed

    private void jbReiniciarConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReiniciarConsultaActionPerformed
        reiniciarCamposConsultarModelo();
    }//GEN-LAST:event_jbReiniciarConsultaActionPerformed

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
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFGestorVehiculos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton jbConsultarModelos;
    private javax.swing.JButton jbCrearMarca;
    private javax.swing.JButton jbCrearModelo;
    private javax.swing.JButton jbEliminarMarca;
    private javax.swing.JButton jbEliminarModelo;
    private javax.swing.JButton jbExportarModelos;
    private javax.swing.JButton jbModificarMarca;
    private javax.swing.JButton jbModificarModelo;
    private javax.swing.JButton jbReiniciarConsulta;
    private javax.swing.JComboBox<String> jcbEficienciaNuevoModelo;
    private javax.swing.JComboBox<String> jcbEficienciasConsultaModelos;
    private javax.swing.JComboBox<String> jcbMarcaNuevoModelo;
    private javax.swing.JComboBox<String> jcbMarcasConsultaModelos;
    private javax.swing.JComboBox<String> jcbMarcasModificar;
    private javax.swing.JComboBox<String> jcbModelosModificar;
    private javax.swing.JComboBox<String> jcbNuevaEficienciaModelo;
    private javax.swing.JComboBox<String> jcbNuevaMarcaModelo;
    private javax.swing.JLabel jlConsumoConsultaModelos;
    private javax.swing.JLabel jlConsumoNuevoModelo;
    private javax.swing.JLabel jlEficienciaConsultaModelos;
    private javax.swing.JLabel jlEficienciaNuevoModelo;
    private javax.swing.JLabel jlEmisionesConsultaModelos;
    private javax.swing.JLabel jlEmisionesNuevoModelo;
    private javax.swing.JLabel jlMarcaConsultaModelos;
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
    private javax.swing.JLabel jlTituloConsultarModelo;
    private javax.swing.JLabel jlTituloCrearMarca;
    private javax.swing.JLabel jlTituloCrearModelo;
    private javax.swing.JLabel jlTituloEliminarMarca;
    private javax.swing.JLabel jlTituloEliminarModelo;
    private javax.swing.JLabel jlTituloModificarMarca;
    private javax.swing.JLabel jlTituloModificarModelo;
    private javax.swing.JLabel jlUnidadesConsumoModificarModelo;
    private javax.swing.JLabel jlUnidadesConsumoNuevoModelo;
    private javax.swing.JLabel jlUnidadesEmisionesModificarModelo;
    private javax.swing.JLabel jlUnidadesEmisionesNuevoModelo;
    private javax.swing.JMenu jmModulos;
    private javax.swing.JMenuBar jmbBarraMenu;
    private javax.swing.JMenuItem jmiConectar;
    private javax.swing.JPanel jpConsultarModelo;
    private javax.swing.JPanel jpCrearMarca;
    private javax.swing.JPanel jpCrearModelo;
    private javax.swing.JPanel jpEliminarMarca;
    private javax.swing.JPanel jpEliminarModelo;
    private javax.swing.JPanel jpMarcas;
    private javax.swing.JPanel jpModelos;
    private javax.swing.JPanel jpModificarMarca;
    private javax.swing.JPanel jpModificarModelo;
    private javax.swing.JSeparator jsSeparadorConsultaModelo;
    private javax.swing.JSeparator jsSeparadorCrearMarca;
    private javax.swing.JSeparator jsSeparadorCrearModelo;
    private javax.swing.JSeparator jsSeparadorEliminarMarca;
    private javax.swing.JSeparator jsSeparadorEliminarModelo;
    private javax.swing.JSeparator jsSeparadorModificarMarca;
    private javax.swing.JSeparator jsSeparadorModificarModelo;
    private javax.swing.JSlider jslSelectorConsumo;
    private javax.swing.JSlider jslSelectorEmisiones;
    private javax.swing.JScrollPane jspTablaConsultaModelos;
    private javax.swing.JScrollPane jspTablaCrearMarca;
    private javax.swing.JScrollPane jspTablaCrearModelo;
    private javax.swing.JScrollPane jspTablaEliminarMarca;
    private javax.swing.JScrollPane jspTablaEliminarModelo;
    private javax.swing.JScrollPane jspTablaModificarMarca;
    private javax.swing.JScrollPane jspTablaModificarModelo;
    private javax.swing.JTable jtTablaConsultaModelos;
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
