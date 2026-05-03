package abenantelutzengitasql;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 * Finestra grafica per la creazione di un nuovo studente.
 * L'utente inserisce nome e cognome, poi sceglie la classe da una JComboBox
 * che viene caricata dal database. Infine preme "Crea".
 * La validazione e il salvataggio vengono gestiti da Logica.
 * Se l'inserimento va a buon fine la finestra si chiude con dispose().
 *
 * @author abenante.lucia
 */
public class JStudente extends javax.swing.JFrame {

    /** Riferimento all'oggetto logica per la comunicazione con il database */
    private Logica logica;

    /**
     * Costruttore della finestra JStudente.
     * Riceve l'oggetto Logica, inizializza i componenti grafici e
     * carica le classi disponibili nella JComboBox.
     *
     * @param logica istanza di Logica condivisa con le altre finestre
     */
    public JStudente(Logica logica) {
        this.logica = logica;
        initComponents();
        caricaClassi();
        setLocationRelativeTo(null);
    }

    /**
     * Carica l'elenco delle classi disponibili nella JComboBox.
     * Le classi vengono recuperate dal database tramite Logica e inserite
     * nel modello della combo. La prima voce è sempre "-- Seleziona classe --"
     * come placeholder, per obbligare l'utente a fare una scelta esplicita.
     */
    private void caricaClassi() {
        List<String> classi = logica.getElencoClassi();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Seleziona classe --");
        for (String c : classi) {
            model.addElement(c);
        }
        cmbClasse.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1    = new javax.swing.JPanel();
        jPanel2    = new javax.swing.JPanel();
        jLabel1    = new javax.swing.JLabel();
        jLabel2    = new javax.swing.JLabel();
        jLabel3    = new javax.swing.JLabel();
        jLabel4    = new javax.swing.JLabel();
        txtNome    = new javax.swing.JTextField();
        txtCognome = new javax.swing.JTextField();
        cmbClasse  = new javax.swing.JComboBox<>();
        jButton1   = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 153, 0));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 3, 24));
        jLabel1.setForeground(new java.awt.Color(51, 153, 0));
        jLabel1.setText("Crea Studente");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(30, 20, 200, 32);

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel2.setForeground(new java.awt.Color(51, 153, 0));
        jLabel2.setText("Nome");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(30, 90, 80, 25);

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel3.setForeground(new java.awt.Color(51, 153, 0));
        jLabel3.setText("Cognome");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(30, 140, 100, 25);

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel4.setForeground(new java.awt.Color(51, 153, 0));
        jLabel4.setText("Classe");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(30, 190, 80, 25);

        jPanel2.add(txtNome);
        txtNome.setBounds(200, 92, 200, 22);
        jPanel2.add(txtCognome);
        txtCognome.setBounds(200, 142, 200, 22);

        cmbClasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // nessuna azione necessaria al cambio selezione
            }
        });
        jPanel2.add(cmbClasse);
        cmbClasse.setBounds(200, 192, 200, 24);

        jButton1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jButton1.setForeground(new java.awt.Color(51, 153, 0));
        jButton1.setText("Crea");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 0), 2, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(180, 253, 100, 40);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 466, 315);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Gestisce il click sul pulsante "Crea".
     * Controlla prima che l'utente abbia selezionato una classe valida
     * (non il placeholder "-- Seleziona classe --"), poi passa i dati a Logica.
     * Mostra un messaggio di errore se qualcosa va storto,
     * oppure conferma e chiude la finestra se tutto è ok.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String nome    = txtNome.getText();
        String cognome = txtCognome.getText();
        Object classeSelezionata = cmbClasse.getSelectedItem();

        if (classeSelezionata == null || classeSelezionata.toString().startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Seleziona una classe valida.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String errore = logica.creaStudente(nome, cognome, classeSelezionata.toString());

        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Studente creato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new JStudente(null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbClasse;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtCognome;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
