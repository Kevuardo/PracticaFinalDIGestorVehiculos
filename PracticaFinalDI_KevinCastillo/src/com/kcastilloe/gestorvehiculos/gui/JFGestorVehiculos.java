package com.kcastilloe.gestorvehiculos.gui;

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
    GestorBBDD ges = new GestorBBDD(this);

    /* Los Vectores servirán de estructura para las tablas: */
    private Vector vMarcas = new Vector();
    /* Elige el modelo de las tablas, con los campos del Vector y 0 columnas iniciales: */
    private DefaultTableModel dtmCrearMarcas = new DefaultTableModel(vMarcas, 0);
    private DefaultTableModel dtmModificarMarcas = new DefaultTableModel(vMarcas, 0);
    private DefaultTableModel dtmEliminarMarcas = new DefaultTableModel(vMarcas, 0);

    /**
     * Crea un nuevo JFGestorVehiculos.
     */
    public JFGestorVehiculos() {
        initComponents();
        this.setBounds(300, 150, 600, 400);
        bloquearElementosIniciales();
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
                    reiniciarCamposCrearModelo();
                    reiniciarCamposModificarModelo();
                    reiniciarCamposEliminarModelo();
                }
            }
        });
        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane de Modelos. */
        jtpSeccionesModelos.addChangeListener(new ChangeListener() {
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
            cargarMarcasModificables();
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(
                    null,
                    sqlex.getMessage() + "\nForzando cierre del programa.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (ClassNotFoundException cnfex) {
            JOptionPane.showMessageDialog(
                    null,
                    cnfex.getMessage() + "\nForzando cierre del programa.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage() + "\nForzando cierre del programa.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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

    private void limpiarTablas() {
        /* Las tablas de Marcas tendrán 2 campos: */
        vMarcas.clear();
        vMarcas.add("ID marca");
        vMarcas.add("Nombre marca");
        /* Se asigna el modelo a las tablas de Marcas: */
        jtTablaCrearMarca.setModel(dtmCrearMarcas);
        dtmCrearMarcas.setRowCount(0);
        
        jtTablaModificarMarca.setModel(dtmModificarMarcas);
        dtmModificarMarcas.setRowCount(0);
        
        jtTablaEliminarMarca.setModel(dtmEliminarMarcas);
        dtmEliminarMarcas.setRowCount(0);

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
        String nombreModificadoMarca = jtfNuevoNombreModificar.getText().toUpperCase();
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

    /**
     * Método usado para llamar a la BD y eliminar la marca que seleccione el
     * usuario con los datos introducidos.
     */
    private void eliminarMarca() {
        boolean yaExiste = false;
        String nombreEliminadoMarca = jtfIdMarcaEliminar.getText();
        if (nombreEliminadoMarca.trim().compareToIgnoreCase("") == 0) {
            reiniciarCamposEliminarMarca();
            JOptionPane.showMessageDialog(
                    null,
                    "Inserte un nuevo nombre válido para la marca a eliminar.",
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
                            "No existe ninguna marca con ese nombre.",
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
                    dtmCrearMarcas.setRowCount(dtmCrearMarcas.getRowCount() + 1);
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
        jtfNuevoNombreModificar.setText("");
        jtfNuevoNombreModificar.setEnabled(false);
        jbModificarMarca.setEnabled(false);
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
                    dtmModificarMarcas.setRowCount(dtmModificarMarcas.getRowCount() + 1);
                    jtTablaModificarMarca.setValueAt(alMarcas.get(i).getId_marca(), i, 0);
                    jtTablaModificarMarca.setValueAt(alMarcas.get(i).getNombre_marca(), i, 1);
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
     * Método usado para resetear los campos de la sección Eliminar Marca de
     * cara a una nueva eliminación.
     */
    private void reiniciarCamposEliminarMarca() {
        jtfIdMarcaEliminar.setText("");
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
                    dtmEliminarMarcas.setRowCount(dtmEliminarMarcas.getRowCount() + 1);
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
        jlNombreNuevaMarca = new javax.swing.JLabel();
        jtfNombreNuevaMarca = new javax.swing.JTextField();
        jbCrearMarca = new javax.swing.JButton();
        jspTablaCrearMarca = new javax.swing.JScrollPane();
        jtTablaCrearMarca = new javax.swing.JTable();
        jpModificarMarca = new javax.swing.JPanel();
        jlTituloModificarMarca = new javax.swing.JLabel();
        jsSeparadorModificarMarca = new javax.swing.JSeparator();
        jlNombreActualModificar = new javax.swing.JLabel();
        jcbMarcasModificar = new javax.swing.JComboBox<>();
        jlNuevoNombreModificar = new javax.swing.JLabel();
        jtfNuevoNombreModificar = new javax.swing.JTextField();
        jbModificarMarca = new javax.swing.JButton();
        jspTablaModificarMarca = new javax.swing.JScrollPane();
        jtTablaModificarMarca = new javax.swing.JTable();
        jpEliminarMarca = new javax.swing.JPanel();
        jlTituloEliminarMarca = new javax.swing.JLabel();
        jsSeparadorEliminarMarca = new javax.swing.JSeparator();
        jlIdMarcaEliminar = new javax.swing.JLabel();
        jtfIdMarcaEliminar = new javax.swing.JTextField();
        jbEliminarMarca = new javax.swing.JButton();
        jspTablaEliminarMarca = new javax.swing.JScrollPane();
        jtTablaEliminarMarca = new javax.swing.JTable();
        jpModelos = new javax.swing.JPanel();
        jtpSeccionesModelos = new javax.swing.JTabbedPane();
        jpCrearModelo = new javax.swing.JPanel();
        jpModificarModelo = new javax.swing.JPanel();
        jpEliminarModelo = new javax.swing.JPanel();
        jmbBarraMenu = new javax.swing.JMenuBar();
        jmModulos = new javax.swing.JMenu();
        jmiConectar = new javax.swing.JMenuItem();
        jmInfo = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 400));

        jlTituloCrearMarca.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloCrearMarca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloCrearMarca.setText("CREAR MARCA");

        jlNombreNuevaMarca.setText("Nombre de la nueva marca:");

        jbCrearMarca.setText("Crear nueva marca");
        jbCrearMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCrearMarcaActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jpCrearMarcaLayout = new javax.swing.GroupLayout(jpCrearMarca);
        jpCrearMarca.setLayout(jpCrearMarcaLayout);
        jpCrearMarcaLayout.setHorizontalGroup(
            jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
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
                .addComponent(jspTablaCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
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

        jlNombreActualModificar.setText("Seleccione la marca a modificar:");

        jcbMarcasModificar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una marca..." }));
        jcbMarcasModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMarcasModificarActionPerformed(evt);
            }
        });

        jlNuevoNombreModificar.setText("Nuevo nombre de la marca:");

        jtfNuevoNombreModificar.setEnabled(false);

        jbModificarMarca.setText("Modificar marca");
        jbModificarMarca.setEnabled(false);
        jbModificarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificarMarcaActionPerformed(evt);
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

        javax.swing.GroupLayout jpModificarMarcaLayout = new javax.swing.GroupLayout(jpModificarMarca);
        jpModificarMarca.setLayout(jpModificarMarcaLayout);
        jpModificarMarcaLayout.setHorizontalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlTituloModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorModificarMarca)
                    .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                        .addComponent(jlNombreActualModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbMarcasModificar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlNuevoNombreModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNuevoNombreModificar))
                    .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpModificarMarcaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbModificarMarca)))
                .addContainerGap())
        );
        jpModificarMarcaLayout.setVerticalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpModificarMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloModificarMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorModificarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreActualModificar)
                    .addComponent(jcbMarcasModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspTablaModificarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfNuevoNombreModificar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlNuevoNombreModificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbModificarMarca)
                .addContainerGap())
        );

        jtpSeccionesMarcas.addTab("Modificar marca", jpModificarMarca);

        jlTituloEliminarMarca.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlTituloEliminarMarca.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTituloEliminarMarca.setText("ELIMINAR MARCA");

        jlIdMarcaEliminar.setText("Nombre de la marca a eliminar:");

        jbEliminarMarca.setText("Eliminar marca");
        jbEliminarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarMarcaActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jpEliminarMarcaLayout = new javax.swing.GroupLayout(jpEliminarMarca);
        jpEliminarMarca.setLayout(jpEliminarMarcaLayout);
        jpEliminarMarcaLayout.setHorizontalGroup(
            jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEliminarMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                    .addComponent(jlTituloEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSeparadorEliminarMarca)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEliminarMarcaLayout.createSequentialGroup()
                        .addComponent(jlIdMarcaEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfIdMarcaEliminar))
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
                .addComponent(jspTablaEliminarMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlIdMarcaEliminar)
                    .addComponent(jtfIdMarcaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout jpCrearModeloLayout = new javax.swing.GroupLayout(jpCrearModelo);
        jpCrearModelo.setLayout(jpCrearModeloLayout);
        jpCrearModeloLayout.setHorizontalGroup(
            jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        jpCrearModeloLayout.setVerticalGroup(
            jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jtpSeccionesModelos.addTab("Crear modelo", jpCrearModelo);

        javax.swing.GroupLayout jpModificarModeloLayout = new javax.swing.GroupLayout(jpModificarModelo);
        jpModificarModelo.setLayout(jpModificarModeloLayout);
        jpModificarModeloLayout.setHorizontalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        jpModificarModeloLayout.setVerticalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jtpSeccionesModelos.addTab("Modificar modelo", jpModificarModelo);

        javax.swing.GroupLayout jpEliminarModeloLayout = new javax.swing.GroupLayout(jpEliminarModelo);
        jpEliminarModelo.setLayout(jpEliminarModeloLayout);
        jpEliminarModeloLayout.setHorizontalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );
        jpEliminarModeloLayout.setVerticalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jtpSeccionesModelos.addTab("Eliminar modelo", jpEliminarModelo);

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
        String marcaSeleccionada = (String) jcbMarcasModificar.getSelectedItem();
        if (marcaSeleccionada.compareToIgnoreCase("Seleccione una marca...") == 0) {
            jtfNuevoNombreModificar.setEnabled(false);
            jtfNuevoNombreModificar.setText("");
            jbModificarMarca.setEnabled(false);
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una marca válida en el selector desplegable.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            jtfNuevoNombreModificar.setEnabled(true);
            jbModificarMarca.setEnabled(true);
        }
    }//GEN-LAST:event_jcbMarcasModificarActionPerformed

    private void jbModificarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificarMarcaActionPerformed
        modificarMarca();
    }//GEN-LAST:event_jbModificarMarcaActionPerformed

    private void jbEliminarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarMarcaActionPerformed
        eliminarMarca();
    }//GEN-LAST:event_jbEliminarMarcaActionPerformed

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
    private javax.swing.JButton jbEliminarMarca;
    private javax.swing.JButton jbModificarMarca;
    private javax.swing.JComboBox<String> jcbMarcasModificar;
    private javax.swing.JLabel jlIdMarcaEliminar;
    private javax.swing.JLabel jlNombreActualModificar;
    private javax.swing.JLabel jlNombreNuevaMarca;
    private javax.swing.JLabel jlNuevoNombreModificar;
    private javax.swing.JLabel jlTituloCrearMarca;
    private javax.swing.JLabel jlTituloEliminarMarca;
    private javax.swing.JLabel jlTituloModificarMarca;
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
    private javax.swing.JSeparator jsSeparadorEliminarMarca;
    private javax.swing.JSeparator jsSeparadorModificarMarca;
    private javax.swing.JScrollPane jspTablaCrearMarca;
    private javax.swing.JScrollPane jspTablaEliminarMarca;
    private javax.swing.JScrollPane jspTablaModificarMarca;
    private javax.swing.JTable jtTablaCrearMarca;
    private javax.swing.JTable jtTablaEliminarMarca;
    private javax.swing.JTable jtTablaModificarMarca;
    private javax.swing.JTextField jtfIdMarcaEliminar;
    private javax.swing.JTextField jtfNombreNuevaMarca;
    private javax.swing.JTextField jtfNuevoNombreModificar;
    private javax.swing.JTabbedPane jtpSeccionesMarcas;
    private javax.swing.JTabbedPane jtpSeccionesModelos;
    private javax.swing.JTabbedPane jtpVentanasModulos;
    // End of variables declaration//GEN-END:variables
}
