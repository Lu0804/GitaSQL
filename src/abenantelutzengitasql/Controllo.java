/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengitasql;

/**
 * Classe di controllo: valida i dati, NON mostra messaggi.
 * Restituisce stringhe di errore vuote se tutto ok, altrimenti il messaggio d'errore.
 * La grafica usa questi messaggi per mostrare dialog/label all'utente.
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class Controllo {

    /**
     * Controlla che il campo non sia vuoto.
     * @param valore il testo da controllare
     * @param nomeCampo nome del campo (es. "Nome", "Destinazione")
     * @return messaggio d'errore, oppure stringa vuota se ok
     */
    public String controllaVuoto(String valore, String nomeCampo) {
        if (valore == null || valore.trim().isEmpty()) {
            return "Il campo \"" + nomeCampo + "\" non può essere vuoto.";
        }
        return "";
    }

    /**
     * Controlla che il valore sia un intero positivo.
     * @param valore il testo da controllare
     * @param nomeCampo nome del campo (es. "Anno", "Prezzo")
     * @return messaggio d'errore, oppure stringa vuota se ok
     */
    public String controllaIntero(String valore, String nomeCampo) {
        if (valore == null || valore.trim().isEmpty()) {
            return "Il campo \"" + nomeCampo + "\" non può essere vuoto.";
        }
        try {
            int n = Integer.parseInt(valore.trim());
            if (n <= 0) {
                return "Il campo \"" + nomeCampo + "\" deve essere un numero positivo.";
            }
        } catch (NumberFormatException e) {
            return "Il campo \"" + nomeCampo + "\" deve essere un numero intero (es. 3, 150).";
        }
        return "";
    }

    /**
     * Controlla che il valore sia solo testo (non un numero).
     * @param valore il testo da controllare
     * @param nomeCampo nome del campo
     * @return messaggio d'errore, oppure stringa vuota se ok
     */
    public String controllaSoloTesto(String valore, String nomeCampo) {
        String err = controllaVuoto(valore, nomeCampo);
        if (!err.isEmpty()) return err;
        try {
            Integer.parseInt(valore.trim());
            return "Il campo \"" + nomeCampo + "\" non può essere un numero.";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * Controlla che la sezione sia un singolo carattere alfabetico.
     * @param valore il testo da controllare
     * @return messaggio d'errore, oppure stringa vuota se ok
     */
    public String controllaSezione(String valore) {
        if (valore == null || valore.trim().isEmpty()) {
            return "Il campo \"Sezione\" non può essere vuoto.";
        }
        if (valore.trim().length() != 1 || !Character.isLetter(valore.trim().charAt(0))) {
            return "Il campo \"Sezione\" deve essere una singola lettera (es. A, B, C).";
        }
        return "";
    }

    /**
     * Controlla che l'anno della classe sia tra 1 e 5.
     * @param valore il testo da controllare
     * @return messaggio d'errore, oppure stringa vuota se ok
     */
    public String controllaAnnoClasse(String valore) {
        String err = controllaIntero(valore, "Anno");
        if (!err.isEmpty()) return err;
        int anno = Integer.parseInt(valore.trim());
        if (anno < 1 || anno > 5) {
            return "Il campo \"Anno\" deve essere compreso tra 1 e 5.";
        }
        return "";
    }

    /**
     * Valida tutti i campi di una classe.
     * @return primo errore trovato, oppure stringa vuota se tutto ok
     */
    public String validaClasse(String anno, String sezione, String indirizzo) {
        String err;
        err = controllaAnnoClasse(anno);
        if (!err.isEmpty()) return err;
        err = controllaSezione(sezione);
        if (!err.isEmpty()) return err;
        err = controllaSoloTesto(indirizzo, "Indirizzo");
        if (!err.isEmpty()) return err;
        return "";
    }

    /**
     * Valida tutti i campi di una gita.
     * @return primo errore trovato, oppure stringa vuota se tutto ok
     */
    public String validaGita(String destinazione, String durata, String prezzo) {
        String err;
        err = controllaSoloTesto(destinazione, "Destinazione");
        if (!err.isEmpty()) return err;
        err = controllaIntero(durata, "Durata (giorni)");
        if (!err.isEmpty()) return err;
        err = controllaIntero(prezzo, "Prezzo (€)");
        if (!err.isEmpty()) return err;
        return "";
    }

    /**
     * Valida tutti i campi di uno studente.
     * @return primo errore trovato, oppure stringa vuota se tutto ok
     */
    public String validaStudente(String nome, String cognome, Object classeSelezionata) {
        String err;
        err = controllaSoloTesto(nome, "Nome");
        if (!err.isEmpty()) return err;
        err = controllaSoloTesto(cognome, "Cognome");
        if (!err.isEmpty()) return err;
        if (classeSelezionata == null) {
            return "Seleziona una classe per lo studente.";
        }
        return "";
    }
}