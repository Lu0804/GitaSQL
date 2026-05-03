package abenantelutzengitasql;
import javax.swing.JOptionPane;

/**
 * @author abenante.lucia
 */
public class JGita extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JGita.class.getName());
    private Logica logica;

    public JGita(Logica logica) {
        this.logica = logica;
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1        = new javax.swing.JPanel();
        jPanel2        = new javax.swing.JPanel();
        lblTitolo      = new javax.swing.JLabel();
        lblPrezzo      = new javax.swing.JLabel();
        lblDurata      = new javax.swing.JLabel();
        lblLocalita    = new javax.swing.JLabel();
        txtPrezzo      = new javax.swing.JTextField();
        txtDurata      = new javax.swing.JTextField();
        txtDestinazione = new javax.swing.JTextField();
        btnCrea        = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Pannello esterno (colore di sfondo)
        jPanel1.setBackground(new java.awt.Color(66, 174, 203));
        jPanel1.setLayout(null);

        // FIX: jPanel2 (bianco) ora copre tutta la finestra (408 x 330)
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(null);

        lblTitolo.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 24));
        lblTitolo.setForeground(new java.awt.Color(66, 174, 203));
        lblTitolo.setText("Crea una Gita");
        jPanel2.add(lblTitolo);
        lblTitolo.setBounds(30, 10, 200, 32);

        lblPrezzo.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        lblPrezzo.setForeground(new java.awt.Color(66, 174, 203));
        lblPrezzo.setText("Prezzo (\u20ac)");
        jPanel2.add(lblPrezzo);
        lblPrezzo.setBounds(30, 90, 120, 25);

        lblDurata.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        lblDurata.setForeground(new java.awt.Color(66, 174, 203));
        lblDurata.setText("Durata (gg)");
        jPanel2.add(lblDurata);
        lblDurata.setBounds(30, 150, 120, 25);

        lblLocalita.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        lblLocalita.setForeground(new java.awt.Color(66, 174, 203));
        lblLocalita.setText("Destinazione");
        jPanel2.add(lblLocalita);
        lblLocalita.setBounds(30, 210, 130, 25);

        jPanel2.add(txtPrezzo);
        txtPrezzo.setBounds(200, 92, 140, 22);
        jPanel2.add(txtDurata);
        txtDurata.setBounds(200, 152, 140, 22);
        jPanel2.add(txtDestinazione);
        txtDestinazione.setBounds(200, 212, 140, 22);

        btnCrea.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18));
        btnCrea.setForeground(new java.awt.Color(66, 174, 203));
        btnCrea.setText("Crea");
        btnCrea.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(66, 174, 203), 2, true));
        btnCrea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreaActionPerformed(evt);
            }
        });
        jPanel2.add(btnCrea);
        btnCrea.setBounds(150, 270, 100, 40);

        // FIX: jPanel2 occupa tutta la larghezza di jPanel1 (408 x 330)
        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 408, 330);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreaActionPerformed(java.awt.event.ActionEvent evt) {
        String destinazione = txtDestinazione.getText();
        String durata       = txtDurata.getText();
        String prezzo       = txtPrezzo.getText();

        String errore = logica.creaGita(destinazione, durata, prezzo);

        if (!errore.isEmpty()) {
            JOptionPane.showMessageDialog(this, errore, "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gita creata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
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
        java.awt.EventQueue.invokeLater(() -> new JGita(null).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCrea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDurata;
    private javax.swing.JLabel lblPrezzo;
    private javax.swing.JLabel lblLocalita;
    private javax.swing.JLabel lblTitolo;
    private javax.swing.JTextField txtDestinazione;
    private javax.swing.JTextField txtDurata;
    private javax.swing.JTextField txtPrezzo;
    // End of variables declaration//GEN-END:variables
}
