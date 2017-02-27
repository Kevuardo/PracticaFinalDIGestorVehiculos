package com.kcastilloe.gestorvehiculos.gui;

import com.kcastilloe.gestorvehiculos.persistencia.GestorBBDD;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * La ventana principal de la aplicación.
 * @author Kevin Castillo
 */
public class JFGestorVehiculos extends javax.swing.JFrame {

    /* Paneles de operaciones. */
    JPMarcas jpPanelMarcas = new JPMarcas(); /* Panel específico para Marcas. */
    JPanel jpPanelModelos = new JPanel(); /* Panel específico para Modelos. */
    JPanel jpPanelEficiencias = new JPanel(); /* Panel específico para Eficiencias. */
    
    GestorBBDD ges = new GestorBBDD(this);
    
    /**
     * Crea un nuevo JFGestorVehiculos.
     */
    public JFGestorVehiculos() {
        initComponents();
        this.setBounds(300, 150, 800, 600);
        this.setResizable(false); /* Se niega la redimensión de la ventana de la app. */
        iniciarVentana();
        jpPrincipal.add(jpPanelMarcas);
    }
    
    /**
     * Comprueba que existe conexión con la BD antes de poder realizar ninguna acción.
     * En caso negativo fuerza la detención del programa para evitar posibles errores.
     */
    private void iniciarVentana(){
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpPrincipal = new javax.swing.JPanel();
        jmbBarraMenu = new javax.swing.JMenuBar();
        jmModulos = new javax.swing.JMenu();
        jmiMarcas = new javax.swing.JMenuItem();
        jmiModelos = new javax.swing.JMenuItem();
        jmiEficiencias = new javax.swing.JMenuItem();
        jmInfo = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpPrincipal.setLayout(new java.awt.CardLayout());

        jmModulos.setText("Módulos");

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
            .addComponent(jpPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmiMarcasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMarcasActionPerformed
        jpPrincipal.add(jpPanelMarcas);
    }//GEN-LAST:event_jmiMarcasActionPerformed

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
    private javax.swing.JMenu jmInfo;
    private javax.swing.JMenu jmModulos;
    private javax.swing.JMenuBar jmbBarraMenu;
    private javax.swing.JMenuItem jmiEficiencias;
    private javax.swing.JMenuItem jmiMarcas;
    private javax.swing.JMenuItem jmiModelos;
    private javax.swing.JPanel jpPrincipal;
    // End of variables declaration//GEN-END:variables
}
