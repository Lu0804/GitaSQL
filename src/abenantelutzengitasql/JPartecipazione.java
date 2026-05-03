/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package abenantelutzengitasql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
 
/**
 * Finestra principale dell'applicazione.
 * Crea un'unica istanza di Logica e la passa a tutte le finestre figlie.
 * Mostra anche la tabella delle partecipazioni (studente → gita).
 *
 * @author abenante.lucia
 */

public class JPartecipazione extends javax.swing.JFrame {

    private final Logica logica;

    // Rimosso: private DefaultTableModel modelPartecipazioni;

    public JPartecipazione() {
        this.logica = new Logica(); 
        initComponents();
        setLocationRelativeTo(null);
        
        aggiornaTabella(); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPnlSfondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        
        btnCreaClasse = new javax.swing.JButton();
        btnCreaStudente = new javax.swing.JButton();
        btnCreaGita = new javax.swing.JButton();
        btnVisualizzaTutto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(700, 560));
        setResizable(false);
        getContentPane().setLayout(null);

        jPnlSfondo.setBackground(new java.awt.Color(135, 197, 234));
        jPnlSfondo.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 126, 249));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 20));
        jLabel1.setForeground(new java.awt.Color(135, 197, 234));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ID dello Studente e delle Gite");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 30, 370, 30);

        // Inizializzazione diretta della tabella
        jTable2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 14));
        jTable2.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Studente", "Gita"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        });
        
        jScrollPane3.setViewportView(jTable2);
        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(45, 80, 300, 400);

        jPnlSfondo.add(jPanel1);
        jPanel1.setBounds(0, 0, 390, 530);

        btnCreaClasse.setBackground(new java.awt.Color(135, 197, 234));
        btnCreaClasse.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14));
        btnCreaClasse.setForeground(new java.awt.Color(0, 126, 249));
        btnCreaClasse.setText("Crea Classe");
        btnCreaClasse.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 126, 249), 2, true));
        btnCreaClasse.addActionListener(evt -> btnCreaClasseActionPerformed(evt));
        jPnlSfondo.add(btnCreaClasse);
        btnCreaClasse.setBounds(460, 60, 160, 50);

        btnCreaStudente.setBackground(new java.awt.Color(135, 197, 234));
        btnCreaStudente.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14));
        btnCreaStudente.setForeground(new java.awt.Color(0, 126, 249));
        btnCreaStudente.setText("Crea Studente");
        btnCreaStudente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 126, 249), 2, true));
        btnCreaStudente.addActionListener(evt -> btnCreaStudenteActionPerformed(evt));
        jPnlSfondo.add(btnCreaStudente);
        btnCreaStudente.setBounds(460, 160, 160, 50);

        btnCreaGita.setBackground(new java.awt.Color(135, 197, 234));
        btnCreaGita.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14));
        btnCreaGita.setForeground(new java.awt.Color(0, 126, 249));
        btnCreaGita.setText("Crea Gita");
        btnCreaGita.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 126, 249), 2, true));
        btnCreaGita.addActionListener(evt -> btnCreaGitaActionPerformed(evt));
        jPnlSfondo.add(btnCreaGita);
        btnCreaGita.setBounds(460, 260, 160, 50);

        btnVisualizzaTutto.setBackground(new java.awt.Color(135, 197, 234));
        btnVisualizzaTutto.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14));
        btnVisualizzaTutto.setForeground(new java.awt.Color(0, 126, 249));
        btnVisualizzaTutto.setText("Visualizza Tutto");
        btnVisualizzaTutto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 126, 249), 2, true));
        btnVisualizzaTutto.addActionListener(evt -> btnVisualizzaTuttoActionPerformed(evt));
        jPnlSfondo.add(btnVisualizzaTutto);
        btnVisualizzaTutto.setBounds(460, 360, 160, 50);

        getContentPane().add(jPnlSfondo);
        jPnlSfondo.setBounds(0, 0, 700, 530);

        pack();
    }// </editor-fold>                        

    public void aggiornaTabella() {
        // Recuperiamo il modello direttamente dalla tabella
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); // Svuota la tabella
        
        // TODO: Aggiungi le righe al 'model'. Esempio:
        // List<Object[]> dati = logica.getDati();
        // for(Object[] riga : dati) { model.addRow(riga); }
    }

    private void btnCreaClasseActionPerformed(java.awt.event.ActionEvent evt) {
        new JClasse(logica).setVisible(true);
    }

    private void btnCreaStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        new JStudente(logica).setVisible(true);
		aggiornaTabella();
    }

    private void btnCreaGitaActionPerformed(java.awt.event.ActionEvent evt) {
        new JGita(logica).setVisible(true);
    }

    private void btnVisualizzaTuttoActionPerformed(java.awt.event.ActionEvent evt) {
        new JVisualizza(logica).setVisible(true);
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCreaClasse;
    private javax.swing.JButton btnCreaGita;
    private javax.swing.JButton btnCreaStudente;
    private javax.swing.JButton btnVisualizzaTutto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPnlSfondo;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    // End of variables declaration                   
}