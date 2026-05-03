/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package abenantelutzengitasql;

/**
 * Punto di ingresso dell'applicazione Gestione Gite.
 * Avvia solo la finestra principale JPartecipazione,
 * che crea l'unica istanza di Logica e la condivide con le finestre figlie.
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class AbenanteLutzenGitaSQL {

    public static void main(String[] args) {
        // Avvio sull'Event Dispatch Thread (buona pratica Swing)
        java.awt.EventQueue.invokeLater(() -> {
            JPartecipazione finestra = new JPartecipazione();
            finestra.setVisible(true);
        });
    }
}
