package abenantelutzengitasql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Finestra che mostra tutti i dati del sistema in modo completo.
 * Contiene due tabelle: una per gli studenti (con filtro per classe)
 * e una per le gite. Da questa finestra è possibile:
 * - filtrare gli studenti per classe usando la JComboBox
 * - aggiungere una o più partecipazioni selezionando studenti e gite con CTRL/SHIFT
 * - rimuovere una gita (con le sue partecipazioni collegate)
 * - rimuovere una classe (solo se non ha studenti associati)
 * - rimuovere uno studente (con le sue partecipazioni collegate)
 *
 * La finestra riceve un callback (Runnable) da JPartecipazione: ogni volta che
 * si aggiunge o rimuove una partecipazione, viene chiamato questo callback
 * per aggiornare automaticamente la tabella nella finestra principale.
 *
 * @author abenante.lucia
 */
public class JVisualizza extends javax.swing.JFrame {

    /** Riferimento all'oggetto logica per tutte le operazioni sul database */
    private final Logica logica;

    /**
     * Callback da eseguire dopo ogni modifica alle partecipazioni.
     * Punta al metodo aggiornaTabella() di JPartecipazione.
     * Usiamo Runnable perché è il modo più semplice per passare un riferimento
     * a un metodo senza creare un'interfaccia apposita.
     */
    private final Runnable onPartecipazioneAggiunta;

    /**
     * Costruttore della finestra JVisualizza.
     * Riceve l'oggetto Logica e il callback, inizializza i componenti
     * e carica tutti i dati nelle tabelle.
     *
     * @param logica                  istanza di Logica condivisa con le altre finestre
     * @param onPartecipazioneAggiunta callback da chiamare dopo ogni modifica alle partecipazioni
     */
    public JVisualizza(Logica logica, Runnable onPartecipazioneAggiunta) {
        this.logica = logica;
        this.onPartecipazioneAggiunta = onPartecipazioneAggiunta;
        initComponents();
        caricaTutto();
        setLocationRelativeTo(null);
    }

    /**
     * Carica tutti i dati nella finestra: riempie la JComboBox delle classi,
     * la tabella degli studenti (inizialmente con tutti) e la tabella delle gite.
     */
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

    /**
     * Aggiorna la tabella degli studenti.
     * Se idClasse è -1 vengono mostrati tutti gli studenti,
     * altrimenti solo quelli della classe specificata.
     *
     * @param idClasse ID della classe da filtrare, oppure -1 per mostrare tutti
     */
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

    /**
     * Aggiorna la tabella delle gite con i dati più recenti dal database.
     * Le colonne mostrate sono: ID, Durata (giorni), Località.
     */
    private void aggiornaTabellGite() {
        Object[][] dati = logica.getGiteTabella();
        tblGita.setModel(new DefaultTableModel(
                dati,
                new String[]{"ID", "Durata (gg)", "Localit\u00e0"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1                   = new javax.swing.JPanel();
        cmbClasse                 = new javax.swing.JComboBox<>();
        jScrollPane1              = new javax.swing.JScrollPane();
        tblStudente               = new javax.swing.JTable();
        jScrollPane2              = new javax.swing.JScrollPane();
        tblGita                   = new javax.swing.JTable();
        btnAggiungiPartecipazione = new javax.swing.JButton();
        btnRimuoviGita            = new javax.swing.JButton();
        btnRimuoviClasse          = new javax.swing.JButton();
        btnRimuoviStudente        = new javax.swing.JButton();
        lblClasseFiltro           = new javax.swing.JLabel();

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
        tblStudente.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(tblStudente);
        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(150, 50, 505, 220);

        tblGita.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 13));
        tblGita.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane2.setViewportView(tblGita);
        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(250, 295, 405, 210);

        btnAggiungiPartecipazione.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 13));
        btnAggiungiPartecipazione.setForeground(new java.awt.Color(0, 120, 0));
        btnAggiungiPartecipazione.setBackground(new java.awt.Color(220, 255, 220));
        btnAggiungiPartecipazione.setText("<html><center>Aggiungi<br>Partecipazione</center></html>");
        btnAggiungiPartecipazione.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 120, 0), 2, true));
        btnAggiungiPartecipazione.addActionListener(this::btnAggiungiPartecipazioneActionPerformed);
        jPanel1.add(btnAggiungiPartecipazione);
        btnAggiungiPartecipazione.setBounds(30, 160, 110, 55);

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
    }

    // ── GESTORI DEGLI EVENTI ─────────────────────────────────────────────────

    /**
     * Gestisce il cambio di selezione nella JComboBox delle classi.
     * Se viene selezionata "Tutte le classi" mostra tutti gli studenti,
     * altrimenti filtra per la classe scelta estraendo l'ID dalla stringa formattata.
     *
     * @param evt evento della JComboBox (non usato direttamente)
     */
    private void cmbClasseActionPerformed(java.awt.event.ActionEvent evt) {
        String selezionata = (String) cmbClasse.getSelectedItem();
        if (selezionata == null || selezionata.equals("Tutte le classi")) {
            aggiornaTabellStudenti(-1);
        } else {
            aggiornaTabellStudenti(logica.estraiIdDaStringa(selezionata));
        }
    }

    /**
     * Aggiunge le partecipazioni per tutte le combinazioni di studenti e gite selezionati.
     * L'utente può selezionare più righe con CTRL o SHIFT in entrambe le tabelle.
     * Vengono tentate tutte le combinazioni, e alla fine viene mostrato un riepilogo
     * con quante iscrizioni sono andate a buon fine e quante già esistevano o hanno dato errore.
     * Se almeno una partecipazione è stata aggiunta, viene chiamato il callback
     * per aggiornare la tabella in JPartecipazione.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnAggiungiPartecipazioneActionPerformed(java.awt.event.ActionEvent evt) {
        int[] righeStudenti = tblStudente.getSelectedRows();
        int[] righeGite     = tblGita.getSelectedRows();

        if (righeStudenti.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona almeno uno studente dalla tabella in alto (CTRL per più righe).",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (righeGite.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona almeno una gita dalla tabella in basso (CTRL per più righe).",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel modelS = (DefaultTableModel) tblStudente.getModel();
        DefaultTableModel modelG = (DefaultTableModel) tblGita.getModel();

        int successi = 0;
        int errori   = 0;
        StringBuilder messaggiErrore = new StringBuilder();

        for (int rs : righeStudenti) {
            int matricola       = (int)    modelS.getValueAt(rs, 0);
            String nomeStudente = modelS.getValueAt(rs, 1) + " " + modelS.getValueAt(rs, 2);

            for (int rg : righeGite) {
                int idGita      = (int)    modelG.getValueAt(rg, 0);
                String localita = (String) modelG.getValueAt(rg, 2);

                String errore = logica.creaPartecipazione(matricola, idGita);
                if (errore.isEmpty()) {
                    successi++;
                } else {
                    errori++;
                    messaggiErrore.append("• ").append(nomeStudente)
                                  .append(" \u2192 ").append(localita)
                                  .append(": gi\u00e0 iscritto o errore DB\n");
                }
            }
        }

        if (successi > 0 && onPartecipazioneAggiunta != null) {
            onPartecipazioneAggiunta.run();
        }

        StringBuilder msg = new StringBuilder();
        if (successi > 0) msg.append(successi).append(" partecipazione/i aggiunta/e con successo.\n");
        if (errori   > 0) msg.append(errori).append(" gi\u00e0 esistenti o non inserite:\n").append(messaggiErrore);

        int tipo = (errori == 0) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
        JOptionPane.showMessageDialog(this, msg.toString().trim(),
                errori == 0 ? "Successo" : "Operazione parziale", tipo);
    }

    /**
     * Rimuove la gita selezionata nella tabella e tutte le sue partecipazioni.
     * Chiede conferma prima di procedere perché l'operazione non è reversibile.
     * Dopo l'eliminazione aggiorna sia la tabella delle gite che quella in JPartecipazione.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnRimuoviGitaActionPerformed(java.awt.event.ActionEvent evt) {
        int riga = tblGita.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona una gita dalla tabella prima di rimuoverla.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblGita.getModel();
        int    idGita = (int)    model.getValueAt(riga, 0);
        String dest   = (String) model.getValueAt(riga, 2);

        int scelta = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare la gita \"" + dest + "\"?\n"
                + "Verranno rimosse anche tutte le partecipazioni collegate.",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (scelta != JOptionPane.YES_OPTION) return;

        String errore = logica.eliminaGita(idGita);
        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gita \"" + dest + "\" eliminata con successo.",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            aggiornaTabellGite();
            if (onPartecipazioneAggiunta != null) onPartecipazioneAggiunta.run();
        }
    }

    /**
     * Rimuove la classe selezionata nella JComboBox.
     * L'operazione fallirà se ci sono ancora studenti associati a quella classe,
     * perché il database blocca l'eliminazione tramite la foreign key.
     * Chiede conferma prima di procedere.
     * Se l'eliminazione va a buon fine, ricarica tutto (combo + tabelle).
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnRimuoviClasseActionPerformed(java.awt.event.ActionEvent evt) {
        String classeSelezionata = (String) cmbClasse.getSelectedItem();
        if (classeSelezionata == null || classeSelezionata.equals("Tutte le classi")) {
            JOptionPane.showMessageDialog(this, "Seleziona una classe specifica dalla combobox prima di eliminarla.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int scelta = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare la classe \"" + classeSelezionata + "\"?\n"
                + "L'operazione fallir\u00e0 se ci sono studenti ancora associati.",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (scelta != JOptionPane.YES_OPTION) return;

        int idClasse = logica.estraiIdDaStringa(classeSelezionata);
        String errore = logica.eliminaClasse(idClasse);
        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Classe eliminata con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            caricaTutto();
        }
    }

    /**
     * Rimuove lo studente selezionato nella tabella studenti e tutte le sue partecipazioni.
     * Chiede conferma prima di procedere.
     * Dopo l'eliminazione aggiorna la tabella studenti e quella in JPartecipazione.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnRimuoviStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        int riga = tblStudente.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this, "Seleziona uno studente dalla tabella prima di rimuoverlo.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblStudente.getModel();
        int    matricola = (int)    model.getValueAt(riga, 0);
        String nome      = (String) model.getValueAt(riga, 1);
        String cognome   = (String) model.getValueAt(riga, 2);

        int scelta = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare lo studente " + nome + " " + cognome + "?\n"
                + "Verranno rimosse anche tutte le sue partecipazioni.",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (scelta != JOptionPane.YES_OPTION) return;

        String errore = logica.eliminaStudente(matricola);
        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Studente " + nome + " " + cognome + " eliminato con successo.",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            cmbClasseActionPerformed(null);
            if (onPartecipazioneAggiunta != null) onPartecipazioneAggiunta.run();
        }
    }

    // Variables declaration
    private javax.swing.JButton btnAggiungiPartecipazione;
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
}
