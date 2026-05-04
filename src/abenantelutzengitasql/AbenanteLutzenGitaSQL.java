package abenantelutzengitasql;

/**
 * Classe principale che contiene il metodo main dell'applicazione.
 * Avvia l'interfaccia grafica aprendo la finestra principale JPartecipazione
 * sull'Event Dispatch Thread (EDT), che è il thread dedicato alla grafica Swing.
 * Usare l'EDT è la pratica corretta con Swing per evitare problemi di concorrenza.
 * Da JPartecipazione poi vengono aperte tutte le altre finestre,
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class AbenanteLutzenGitaSQL {

    /**
     * Punto di ingresso dell'applicazione.
     * Usa invokeLater per avviare la finestra principale sull'EDT,
     * come consigliato dalla documentazione di Java Swing.
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JPartecipazione finestra = new JPartecipazione();
            finestra.setVisible(true);
        });
    }
}
