package abenantelutzengitasql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Finestra principale dell'applicazione per la gestione delle gite scolastiche.
 * Questa è la prima finestra che viene aperta quando si avvia il programma.
 * Crea un'unica istanza di Logica che poi viene passata a tutte le finestre figlie,
 * così tutte usano la stessa connessione al database e gli stessi dati.
 *
 * La finestra mostra una tabella con tutte le partecipazioni (studente → gita)
 * e ha dei pulsanti per aprire le altre finestre e per rimuovere una partecipazione.
 * Ogni volta che una finestra figlia si chiude, la tabella viene aggiornata
 * automaticamente con i dati più recenti dal database.
 *
 * @author abenante.lucia
 */
public class JPartecipazione extends javax.swing.JFrame {

    /** Oggetto logica condiviso con tutte le finestre figlie */
    private final Logica logica;

    /**
     * Copia completa dei dati della tabella partecipazioni, con anche gli ID
     * che non vengono mostrati all'utente ma servono per l'eliminazione.
     * Ogni riga è: [matricola (int), nomeCompleto (String), idGita (int), localita (String)]
     */
    private Object[][] partecipazioniDati = new Object[0][4];

    /**
     * Costruttore della finestra principale.
     * Crea l'oggetto Logica, inizializza i componenti grafici e carica
     * la tabella delle partecipazioni dal database.
     */
    public JPartecipazione() {
        this.logica = new Logica();
        initComponents();
        setLocationRelativeTo(null);
        aggiornaTabella();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPnlSfondo               = new javax.swing.JPanel();
        jPanel1                  = new javax.swing.JPanel();
        jLabel1                  = new javax.swing.JLabel();
        jScrollPane3             = new javax.swing.JScrollPane();
        jTable2                  = new javax.swing.JTable();
        btnRimuoviPartecipazione = new javax.swing.JButton();
        btnCreaClasse            = new javax.swing.JButton();
        btnCreaStudente          = new javax.swing.JButton();
        btnCreaGita              = new javax.swing.JButton();
        btnVisualizzaTutto       = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(700, 660));
        setResizable(false);
        getContentPane().setLayout(null);

        jPnlSfondo.setBackground(new java.awt.Color(135, 197, 234));
        jPnlSfondo.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 126, 249));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 20));
        jLabel1.setForeground(new java.awt.Color(135, 197, 234));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Studente \u2192 Gita");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 30, 370, 30);

        jTable2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 0, 14));
        jTable2.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Studente", "Gita"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        });
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jScrollPane3.setViewportView(jTable2);
        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(45, 80, 300, 500);

        jPnlSfondo.add(jPanel1);
        jPanel1.setBounds(0, 0, 390, 630);

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

        btnRimuoviPartecipazione.setBackground(new java.awt.Color(135, 197, 234));
        btnRimuoviPartecipazione.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 14));
        btnRimuoviPartecipazione.setForeground(new java.awt.Color(0, 126, 249));
        btnRimuoviPartecipazione.setText("<html><center>Rimuovi<br>Partecipazione</center></html>");
        btnRimuoviPartecipazione.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 126, 249), 2, true));
        btnRimuoviPartecipazione.addActionListener(evt -> btnRimuoviPartecipazioneActionPerformed(evt));
        jPnlSfondo.add(btnRimuoviPartecipazione);
        btnRimuoviPartecipazione.setBounds(460, 460, 160, 50);

        getContentPane().add(jPnlSfondo);
        jPnlSfondo.setBounds(0, 0, 700, 630);

        pack();
    }

    /**
     * Ricarica la tabella delle partecipazioni dal database.
     * Salva anche i dati completi (con gli ID) in partecipazioniDati,
     * che servono quando l'utente vuole eliminare una riga.
     * Viene chiamato all'avvio e ogni volta che una finestra figlia viene chiusa.
     */
    public void aggiornaTabella() {
        partecipazioniDati = logica.getPartecipazioniTabellaCompleta();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        for (Object[] riga : partecipazioniDati) {
            model.addRow(new Object[]{ riga[1], riga[3] });
        }
    }

    /**
     * Apre la finestra per creare una nuova classe.
     * Quando la finestra viene chiusa, aggiorna la tabella principale.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnCreaClasseActionPerformed(java.awt.event.ActionEvent evt) {
        JClasse finestra = new JClasse(logica);
        finestra.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { aggiornaTabella(); }
        });
        finestra.setVisible(true);
    }

    /**
     * Apre la finestra per creare un nuovo studente.
     * Quando la finestra viene chiusa, aggiorna la tabella principale.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnCreaStudenteActionPerformed(java.awt.event.ActionEvent evt) {
        JStudente finestra = new JStudente(logica);
        finestra.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { aggiornaTabella(); }
        });
        finestra.setVisible(true);
    }

    /**
     * Apre la finestra per creare una nuova gita.
     * Quando la finestra viene chiusa, aggiorna la tabella principale.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnCreaGitaActionPerformed(java.awt.event.ActionEvent evt) {
        JGita finestra = new JGita(logica);
        finestra.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { aggiornaTabella(); }
        });
        finestra.setVisible(true);
    }

    /**
     * Apre la finestra JVisualizza che mostra studenti, gite e permette
     * di aggiungere o rimuovere partecipazioni, classi, studenti e gite.
     * Passa anche un Runnable (riferimento a aggiornaTabella) come callback,
     * così JVisualizza può aggiornare questa tabella anche senza chiudersi.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnVisualizzaTuttoActionPerformed(java.awt.event.ActionEvent evt) {
        JVisualizza finestra = new JVisualizza(logica, this::aggiornaTabella);
        finestra.addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent e) { aggiornaTabella(); }
        });
        finestra.setVisible(true);
    }

    /**
     * Rimuove la partecipazione selezionata nella tabella.
     * Prima controlla che l'utente abbia selezionato una riga, poi chiede conferma
     * con un JOptionPane, e infine chiama il metodo di Logica per l'eliminazione.
     * Usa partecipazioniDati per recuperare gli ID della riga selezionata.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void btnRimuoviPartecipazioneActionPerformed(java.awt.event.ActionEvent evt) {
        int riga = jTable2.getSelectedRow();
        if (riga < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona una riga dalla tabella prima di rimuoverla.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int matricola = (int) partecipazioniDati[riga][0];
        int idGita    = (int) partecipazioniDati[riga][2];
        String nomeStudente = (String) partecipazioniDati[riga][1];
        String localita     = (String) partecipazioniDati[riga][3];

        int scelta = JOptionPane.showConfirmDialog(this,
                "Rimuovere la partecipazione di \"" + nomeStudente + "\" alla gita \"" + localita + "\"?",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (scelta != JOptionPane.YES_OPTION) return;

        String errore = logica.eliminaPartecipazione(matricola, idGita);
        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Partecipazione rimossa con successo.",
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            aggiornaTabella();
        }
    }

    // Variables declaration
    private javax.swing.JButton btnCreaClasse;
    private javax.swing.JButton btnCreaGita;
    private javax.swing.JButton btnCreaStudente;
    private javax.swing.JButton btnVisualizzaTutto;
    private javax.swing.JButton btnRimuoviPartecipazione;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPnlSfondo;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
}
