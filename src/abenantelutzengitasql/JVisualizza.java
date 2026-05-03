/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package abenantelutzengitasql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JVisualizza extends javax.swing.JFrame {

    private final Logica logica;

    // Rimossi i modelli globali modelStudenti e modelGite

    public JVisualizza(Logica logica) {
        this.logica = logica;
        initComponents();
        caricaTutto();
        setLocationRelativeTo(null);
    }

    private void caricaTutto() {
        DefaultComboBoxModel<String> modelCombo = new DefaultComboBoxModel<>();
        modelCombo.addElement("Tutte le classi");
        for (String c : logica.getElencoClassi()) {
            modelCombo.addElement(c);
        }
        cmbClasse.setModel(modelCombo);

        aggiornaTabellStudenti(-1); 
        aggiornaTabellGite();
    }

    private void aggiornaTabellStudenti(int idClasse) {
        Object[][] dati = (idClasse < 0)
                ? logica.getTuttiStudentiTabella()
                : logica.getStudentiPerClasseTabella(idClasse);

        tblStudente.setModel(new DefaultTableModel(
                dati,
                new String[]{"Matricola", "Nome", "Cognome", "Anno"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void aggiornaTabellGite() {
        Object[][] dati = logica.getGiteTabella();
        tblGita.setModel(new DefaultTableModel(
                dati,
                new String[]{"ID", "Durata (gg)", "Località"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cmbClasse = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudente = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGita = new javax.swing.JTable();
        btnRimuoviGita = new javax.swing.JButton();
        btnRimuoviClasse = new javax.swing.JButton();
        btnRimuoviStudente = new javax.swing.JButton();
        lblClasseFiltro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(770, 580));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(235, 245, 255));
        jPanel1.setLayout(null);

        lblClasseFiltro.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 13));
        lblClasseFiltro.setForeground(new java.awt.Color(0, 80, 160));
        lblClasseFiltro.setText("Filtra per classe:");
        jPanel1.add(lblClasseFiltro);
        lblClasseFiltro.setBounds(20, 15, 120, 22);

        cmbClasse.addActionListener(this::cmbClasseActionPerformed);
        jPanel1.add(cmbClasse);
        cmbClasse.setBounds(145, 13, 200, 24);

        tblStudente.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 13));
        jScrollPane1.setViewportView(tblStudente);
        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(150, 50, 505, 220);

        tblGita.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 13));
        jScrollPane2.setViewportView(tblGita);
        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(250, 295, 405, 210);

        btnRimuoviGita.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 13));
        btnRimuoviGita.setForeground(new java.awt.Color(200, 0, 0));
        btnRimuoviGita.setText("Rimuovi Gita");
        btnRimuoviGita.addActionListener(this::btnRimuoviGitaActionPerformed);
        jPanel1.add(btnRimuoviGita);
        btnRimuoviGita.setBounds(30, 300, 130, 45);

        btnRimuoviClasse.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 13));
        btnRimuoviClasse.setForeground(new java.awt.Color(200, 0, 0));
        btnRimuoviClasse.setText("Rimuovi Classe");
        btnRimuoviClasse.addActionListener(this::btnRimuoviClasseActionPerformed);
        jPanel1.add(btnRimuoviClasse);
        btnRimuoviClasse.setBounds(30, 370, 130, 45);

        btnRimuoviStudente.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 13));
        btnRimuoviStudente.setForeground(new java.awt.Color(200, 0, 0));
        btnRimuoviStudente.setText("Rimuovi Studente");
        btnRimuoviStudente.addActionListener(this::btnRimuoviStudenteActionPerformed);
        jPanel1.add(btnRimuoviStudente);
        btnRimuoviStudente.setBounds(30, 440, 140, 45);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 700, 540);

        pack();
    }// </editor-fold>                        

    private void cmbClasseActionPerformed(java.awt.event.ActionEvent evt) {
        String selezionata = (String) cmbClasse.getSelectedItem();
        if (selezionata == null || selezionata.equals("Tutte le classi")) {
            aggiornaTabellStudenti(-1);
        } else {
            int idClasse = logica.estraiIdDaStringa(selezionata);
            aggiornaTabellStudenti(idClasse);
        }
    }

    private void btnRimuoviGitaActionPerformed(java.awt.event.ActionEvent evt) {
        int rigaSelezionata = tblGita.getSelectedRow();
        if (rigaSelezionata < 0) {
            System.err.println("Nessuna gita selezionata dalla tabella.");
            return;
        }

        // Estrae il modello dinamicamente dalla tabella
        DefaultTableModel model = (DefaultTableModel) tblGita.getModel();
        int idGita = (int) model.getValueAt(rigaSelezionata, 0);

        String errore = logica.eliminaGita(idGita);
        if (!errore.isEmpty()) {
            System.err.println("Errore eliminazione gita: " + errore);
        } else {
            System.out.println("Successo: Gita eliminata.");
            aggiornaTabellGite();
        }
    }

    private void btnRimuoviClasseActionPerformed(java.awt.event.ActionEvent evt) {
        String classeSelezionata = (String) cmbClasse.getSelectedItem();
        if (classeSelezionata == null || classeSelezionata.equals("Tutte le classi")) {
            System.err.println("Seleziona una classe specifica dalla combobox prima di eliminare.");
            return;
        }

        int idClasse = logica.estraiIdDaStringa(classeSelezionata);
        String errore = logica.eliminaClasse(idClasse);
        if (!errore.isEmpty()) {
            System.err.println("Errore eliminazione classe: " + errore);
        } else {
            System.out.println("Successo: Classe eliminata.");
            caricaTutto();
        }
    }

    private void btnRimuoviStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        int rigaSelezionata = tblStudente.getSelectedRow();
        if (rigaSelezionata < 0) {
            System.err.println("Nessuno studente selezionato dalla tabella.");
            return;
        }

        // Estrae il modello dinamicamente
        DefaultTableModel model = (DefaultTableModel) tblStudente.getModel();
        int matricola = (int) model.getValueAt(rigaSelezionata, 0);

        String errore = logica.eliminaStudente(matricola);
        if (!errore.isEmpty()) {
            System.err.println("Errore eliminazione studente: " + errore);
        } else {
            System.out.println("Successo: Studente eliminato.");
            cmbClasseActionPerformed(null);
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnRimuoviClasse;
    private javax.swing.JButton btnRimuoviGita;
    private javax.swing.JButton btnRimuoviStudente;
    private javax.swing.JComboBox<String> cmbClasse;
    private javax.swing.JLabel lblClasseFiltro;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblGita;
    private javax.swing.JTable tblStudente;
    // End of variables declaration                   
}