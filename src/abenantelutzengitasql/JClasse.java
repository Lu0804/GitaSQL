package abenantelutzengitasql;
import javax.swing.JOptionPane;

/**
 * Finestra grafica per la creazione di una nuova classe scolastica.
 * L'utente inserisce l'anno, la sezione e l'indirizzo, poi preme "Crea".
 * I dati vengono validati e salvati nel database tramite l'oggetto Logica.
 * Se la creazione va a buon fine la finestra si chiude da sola (dispose),
 * così la finestra principale può aggiornare la tabella.
 * Abbiamo usato DISPOSE_ON_CLOSE invece di EXIT_ON_CLOSE per non chiudere
 * tutta l'applicazione quando si chiude solo questa finestra secondaria.
 *
 * @author abenante.lucia
 */
public class JClasse extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(JClasse.class.getName());

    /** Riferimento all'oggetto logica per la comunicazione con il database */
    private Logica l;

    /**
     * Costruttore della finestra JClasse.
     * Riceve l'oggetto Logica dalla finestra principale e inizializza i componenti.
     *
     * @param l istanza di Logica condivisa con le altre finestre
     */
    public JClasse(Logica l) {
        this.l = l;
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnlSfondo = new javax.swing.JPanel();
        jPanel1    = new javax.swing.JPanel();
        jLabel2    = new javax.swing.JLabel();
        jLabel3    = new javax.swing.JLabel();
        jLabel4    = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel5    = new javax.swing.JLabel();
        jButton1   = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPnlSfondo.setBackground(new java.awt.Color(102, 174, 33));
        jPnlSfondo.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel2.setForeground(new java.awt.Color(102, 174, 33));
        jLabel2.setText("Anno");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 80, 70, 30);

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel3.setForeground(new java.awt.Color(102, 174, 33));
        jLabel3.setText("Sezione");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 130, 100, 30);

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        jLabel4.setForeground(new java.awt.Color(102, 174, 33));
        jLabel4.setText("Indirizzo");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 180, 90, 30);

        jPanel1.add(jTextField2);
        jTextField2.setBounds(130, 90, 71, 22);
        jPanel1.add(jTextField3);
        jTextField3.setBounds(130, 140, 71, 22);
        jPanel1.add(jTextField4);
        jTextField4.setBounds(130, 190, 71, 22);

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 3, 24));
        jLabel5.setForeground(new java.awt.Color(102, 174, 33));
        jLabel5.setText("Crea una Classe");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(30, 10, 190, 30);

        jButton1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18));
        jButton1.setForeground(new java.awt.Color(102, 174, 33));
        jButton1.setText("Crea");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 174, 33), 2, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(70, 240, 90, 40);

        jPnlSfondo.add(jPanel1);
        jPanel1.setBounds(0, 0, 240, 300);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnlSfondo, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnlSfondo, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Gestisce il click sul pulsante "Crea".
     * Legge i dati dai campi di testo e li passa a Logica per la validazione e il salvataggio.
     * Se ci sono errori li mostra con un JOptionPane, altrimenti conferma e chiude la finestra.
     *
     * @param evt evento del pulsante (non usato direttamente)
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String anno      = jTextField2.getText();
        String sezione   = jTextField3.getText();
        String indirizzo = jTextField4.getText();

        String errore = l.creaClasse(anno, sezione, indirizzo);

        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Classe creata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
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
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new JClasse(null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPnlSfondo;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
