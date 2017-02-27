package com.kcastilloe.gestorvehiculos.gui;

import com.kcastilloe.gestorvehiculos.modelo.Marca;
import com.kcastilloe.gestorvehiculos.modelo.Modelo;
import com.kcastilloe.gestorvehiculos.persistencia.GestorBBDD;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * La ventana principal de la aplicación.
 *
 * @author Kevin Castillo
 */
public class JFGestorVehiculos extends javax.swing.JFrame {

    Marca miMarca;
    Modelo miModelo;
    GestorBBDD ges = new GestorBBDD(this);

    /**
     * Crea un nuevo JFGestorVehiculos.
     */
    public JFGestorVehiculos() {
        initComponents();
        this.setBounds(300, 150, 400, 300);
        this.setResizable(false);
        /* Se niega la redimensión de la ventana de la app. */
 /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane general. */
        jtpVentanasModulos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                }
            }
        });
        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane de Marcas. */
        jtpSeccionesMarcas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                }
            }
        });
        /* Se añade un Listener para analizar cuándo se recibe algún cambio de ventana en el TabbedPane de Modelos. */
        jtpSeccionesModelos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    /* Métodos de reseteo de ventanas. */
                }
            }
        });
        iniciarVentana();
    }

    /**
     * Comprueba que existe conexión con la BD antes de poder realizar ninguna
     * acción. En caso negativo fuerza la detención del programa para evitar
     * posibles errores.
     */
    private void iniciarVentana() {
        try {
            ges.abrirConexion();
            JOptionPane.showMessageDialog(
                    null,
                    "Conectado con éxito a la Base de Datos.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
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

    /**
     * Método usado para resetear los campos de la sección Crear Marca de cara a
     * una nueva inserción.
     */
    private void reiniciarCamposCrearMarca() {

    }

    /**
     * Método usado para resetear los campos de la sección Modificar Marca de
     * cara a una nueva modificación.
     */
    private void reiniciarCamposModificarMarca() {

    }

    /**
     * Método usado para resetear los campos de la sección Eliminar Marca de
     * cara a una nueva eliminación.
     */
    private void reiniciarCamposEliminarMarca() {

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
        jpModificarMarca = new javax.swing.JPanel();
        jpEliminarMarca = new javax.swing.JPanel();
        jpModelos = new javax.swing.JPanel();
        jtpSeccionesModelos = new javax.swing.JTabbedPane();
        jpCrearModelo = new javax.swing.JPanel();
        jpModificarModelo = new javax.swing.JPanel();
        jpEliminarModelo = new javax.swing.JPanel();
        jmbBarraMenu = new javax.swing.JMenuBar();
        jmModulos = new javax.swing.JMenu();
        jmiMarcas = new javax.swing.JMenuItem();
        jmiModelos = new javax.swing.JMenuItem();
        jmiEficiencias = new javax.swing.JMenuItem();
        jmInfo = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(400, 300));

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

        javax.swing.GroupLayout jpCrearMarcaLayout = new javax.swing.GroupLayout(jpCrearMarca);
        jpCrearMarca.setLayout(jpCrearMarcaLayout);
        jpCrearMarcaLayout.setHorizontalGroup(
            jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsSeparadorCrearMarca)
                    .addComponent(jlTituloCrearMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                        .addComponent(jlNombreNuevaMarca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNombreNuevaMarca, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpCrearMarcaLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbCrearMarca)))
                .addContainerGap())
        );
        jpCrearMarcaLayout.setVerticalGroup(
            jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCrearMarcaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloCrearMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSeparadorCrearMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(jpCrearMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlNombreNuevaMarca)
                    .addComponent(jtfNombreNuevaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCrearMarca)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jtpSeccionesMarcas.addTab("Crear marca", jpCrearMarca);

        javax.swing.GroupLayout jpModificarMarcaLayout = new javax.swing.GroupLayout(jpModificarMarca);
        jpModificarMarca.setLayout(jpModificarMarcaLayout);
        jpModificarMarcaLayout.setHorizontalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jpModificarMarcaLayout.setVerticalGroup(
            jpModificarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        jtpSeccionesMarcas.addTab("Modificar marca", jpModificarMarca);

        javax.swing.GroupLayout jpEliminarMarcaLayout = new javax.swing.GroupLayout(jpEliminarMarca);
        jpEliminarMarca.setLayout(jpEliminarMarcaLayout);
        jpEliminarMarcaLayout.setHorizontalGroup(
            jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jpEliminarMarcaLayout.setVerticalGroup(
            jpEliminarMarcaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
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
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jpCrearModeloLayout.setVerticalGroup(
            jpCrearModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        jtpSeccionesModelos.addTab("Crear modelo", jpCrearModelo);

        javax.swing.GroupLayout jpModificarModeloLayout = new javax.swing.GroupLayout(jpModificarModelo);
        jpModificarModelo.setLayout(jpModificarModeloLayout);
        jpModificarModeloLayout.setHorizontalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jpModificarModeloLayout.setVerticalGroup(
            jpModificarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        jtpSeccionesModelos.addTab("Modificar modelo", jpModificarModelo);

        javax.swing.GroupLayout jpEliminarModeloLayout = new javax.swing.GroupLayout(jpEliminarModelo);
        jpEliminarModelo.setLayout(jpEliminarModeloLayout);
        jpEliminarModeloLayout.setHorizontalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        jpEliminarModeloLayout.setVerticalGroup(
            jpEliminarModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
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

        jmiMarcas.setText("Marcas");
        jmiMarcas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMarcasActionPerformed(evt);
            }
        });
        jmModulos.add(jmiMarcas);

        jmiModelos.setText("Modelos");
        jmModulos.add(jmiModelos);

        jmiEficiencias.setText("Eficiencias");
        jmModulos.add(jmiEficiencias);

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

    private void jmiMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMarcasActionPerformed

    }//GEN-LAST:event_jmiMarcasActionPerformed

    private void jbCrearMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCrearMarcaActionPerformed
        boolean yaExiste = false;
        String nombreNuevaMarca = jtfNombreNuevaMarca.getText();
        if (nombreNuevaMarca.compareToIgnoreCase("") == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Introduzca un nombre válido para la marca.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            miMarca = new Marca(nombreNuevaMarca);
            try {
                yaExiste = ges.existeMarca(miMarca);
                if (!yaExiste) {
                    ges.crearMarca(miMarca);
                    JOptionPane.showMessageDialog(
                            null,
                            "Marca creada con éxito",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ya existe una marca con ese nombre.\nPor favor, intriduzca otro nombre.",
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE);
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
    }//GEN-LAST:event_jbCrearMarcaActionPerformed

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
    private javax.swing.JLabel jlNombreNuevaMarca;
    private javax.swing.JLabel jlTituloCrearMarca;
    private javax.swing.JMenu jmInfo;
    private javax.swing.JMenu jmModulos;
    private javax.swing.JMenuBar jmbBarraMenu;
    private javax.swing.JMenuItem jmiEficiencias;
    private javax.swing.JMenuItem jmiMarcas;
    private javax.swing.JMenuItem jmiModelos;
    private javax.swing.JPanel jpCrearMarca;
    private javax.swing.JPanel jpCrearModelo;
    private javax.swing.JPanel jpEliminarMarca;
    private javax.swing.JPanel jpEliminarModelo;
    private javax.swing.JPanel jpMarcas;
    private javax.swing.JPanel jpModelos;
    private javax.swing.JPanel jpModificarMarca;
    private javax.swing.JPanel jpModificarModelo;
    private javax.swing.JSeparator jsSeparadorCrearMarca;
    private javax.swing.JTextField jtfNombreNuevaMarca;
    private javax.swing.JTabbedPane jtpSeccionesMarcas;
    private javax.swing.JTabbedPane jtpSeccionesModelos;
    private javax.swing.JTabbedPane jtpVentanasModulos;
    // End of variables declaration//GEN-END:variables
}
