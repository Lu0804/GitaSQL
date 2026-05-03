package abenantelutzengitasql;

/**
 * Questa classe si occupa solo di validare i dati inseriti dall'utente.
 * Non mostra nessun messaggio a schermo (quello lo fa la grafica),
 * ma restituisce stringhe di errore che la grafica usa per i JOptionPane.
 * Se il dato è corretto, il metodo restituisce una stringa vuota ("").
 * In questo modo la grafica sa se ci sono stati errori o no semplicemente
 * controllando se la stringa è vuota.
 * Questa separazione tra controllo e grafica è una buona pratica di programmazione.
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class Controllo {

    /**
     * Controlla che il campo non sia null e non sia vuoto (o fatto solo di spazi).
     * Viene usato come base da quasi tutti gli altri metodi di controllo.
     *
     * @param valore    il testo da controllare
     * @param nomeCampo nome del campo da mostrare nel messaggio di errore (es. "Nome")
     * @return messaggio di errore se il campo è vuoto, stringa vuota se è ok
     */
    public String controllaVuoto(String valore, String nomeCampo) {
        if (valore == null || valore.trim().isEmpty()) {
            return "Il campo \"" + nomeCampo + "\" non può essere vuoto.";
        }
        return "";
    }

    /**
     * Controlla che il valore sia un numero intero e che sia positivo (maggiore di 0).
     * Prima controlla che non sia vuoto, poi prova a convertirlo con parseInt.
     * Se la conversione fallisce vuol dire che l'utente ha scritto qualcosa che non è un numero.
     *
     * @param valore    il testo da controllare
     * @param nomeCampo nome del campo da mostrare nel messaggio di errore (es. "Anno")
     * @return messaggio di errore se non è un intero positivo, stringa vuota se è ok
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
     * Controlla che il valore sia solo testo e non un numero.
     * Serve per campi come "Nome", "Cognome" o "Destinazione" dove non ha senso scrivere un numero.
     * Tecnicamente prova a fare il parseInt: se ci riesce vuol dire che è un numero e dà errore.
     *
     * @param valore    il testo da controllare
     * @param nomeCampo nome del campo da mostrare nel messaggio di errore
     * @return messaggio di errore se è un numero o è vuoto, stringa vuota se è ok
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
     * Controlla che la sezione sia esattamente un singolo carattere alfabetico.
     * Per esempio "A" è valida, "AB" no, "1" no, "" no.
     *
     * @param valore il testo da controllare (dovrebbe essere la sezione della classe)
     * @return messaggio di errore se non è una singola lettera, stringa vuota se è ok
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
     * Controlla che l'anno della classe sia un numero compreso tra 1 e 5.
     * Prima riutilizza controllaIntero per i controlli base, poi verifica il range.
     *
     * @param valore il testo da controllare (dovrebbe essere l'anno della classe)
     * @return messaggio di errore se non è tra 1 e 5, stringa vuota se è ok
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
     * Valida tutti i campi necessari per creare una classe.
     * I controlli vengono fatti in ordine: anno, sezione, indirizzo.
     * Al primo errore trovato il metodo si ferma e restituisce quel messaggio.
     *
     * @param anno      anno della classe come stringa
     * @param sezione   sezione della classe
     * @param indirizzo indirizzo di studio
     * @return primo messaggio di errore trovato, oppure stringa vuota se tutto è valido
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
     * Valida tutti i campi necessari per creare una gita.
     * I controlli vengono fatti in ordine: destinazione, durata, prezzo.
     *
     * @param destinazione nome della destinazione
     * @param durata       durata in giorni come stringa
     * @param prezzo       prezzo in euro come stringa
     * @return primo messaggio di errore trovato, oppure stringa vuota se tutto è valido
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
     * Valida tutti i campi necessari per creare uno studente.
     * Controlla nome, cognome e che sia stata selezionata una classe.
     * La classe viene passata come Object perché viene presa direttamente
     * dalla JComboBox, che restituisce Object.
     *
     * @param nome             nome dello studente
     * @param cognome          cognome dello studente
     * @param classeSelezionata oggetto selezionato nella JComboBox della classe (può essere null)
     * @return primo messaggio di errore trovato, oppure stringa vuota se tutto è valido
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
