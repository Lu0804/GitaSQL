package abenantelutzengitasql;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Classe che gestisce le iscrizioni degli studenti alle gite in memoria.
 * Usa strutture dati come HashSet e HashMap per tenere traccia di:
 * - tutte le matricole degli studenti nel sistema
 * - tutti gli ID delle gite nel sistema
 * - l'associazione tra ogni studente e le gite a cui è iscritto
 *
 * Questa classe non comunica con il database: serve per gestire i dati
 * direttamente in memoria (RAM) durante l'esecuzione del programma.
 * Abbiamo usato HashSet perché garantisce che non ci siano duplicati
 * e le operazioni di ricerca sono molto veloci (O(1) in media).
 *
 * @author lutzen.jacopo
 */
public class Partecipazione {

    /** Insieme di tutte le matricole degli studenti presenti nel sistema */
    private HashSet<Integer> matricole = new HashSet<>();

    /** Insieme di tutti gli ID delle gite presenti nel sistema */
    private HashSet<Integer> idGite = new HashSet<>();

    /**
     * Mappa che associa ogni matricola studente all'insieme delle gite
     * a cui è iscritto. La chiave è la matricola, il valore è un HashSet di ID gite.
     */
    private HashMap<Integer, HashSet<Integer>> iscrizioniPerStudente = new HashMap<>();

    // ── GESTIONE STUDENTI ─────────────────────────────────────────────────────

    /**
     * Aggiunge una matricola al sistema.
     * Se la matricola non è ancora presente, viene anche creato un HashSet vuoto
     * nella mappa delle iscrizioni, pronto per ricevere le gite dello studente.
     *
     * @param matricola matricola dello studente da aggiungere
     * @return true se la matricola è stata aggiunta (era nuova), false se già esisteva
     */
    public boolean aggiungiIdStudente(int matricola) {
        boolean nuovo = matricole.add(matricola);
        if (nuovo) {
            iscrizioniPerStudente.put(matricola, new HashSet<>());
        }
        return nuovo;
    }

    /**
     * Rimuove una matricola e tutte le sue iscrizioni alle gite dal sistema.
     *
     * @param matricola matricola dello studente da rimuovere
     * @return true se la matricola esisteva ed è stata rimossa, false altrimenti
     */
    public boolean rimuoviIdStudente(int matricola) {
        iscrizioniPerStudente.remove(matricola);
        return matricole.remove(matricola);
    }

    /**
     * Controlla se una matricola è presente nel sistema.
     *
     * @param matricola matricola da cercare
     * @return true se la matricola esiste, false altrimenti
     */
    public boolean esisteIdStudente(int matricola) {
        return matricole.contains(matricola);
    }

    /**
     * Restituisce l'insieme di tutte le matricole degli studenti nel sistema.
     *
     * @return HashSet con tutte le matricole
     */
    public HashSet<Integer> getMatricole() {
        return matricole;
    }

    // ── GESTIONE GITE ─────────────────────────────────────────────────────────

    /**
     * Aggiunge un ID gita al sistema.
     *
     * @param idGita ID della gita da aggiungere
     * @return true se l'ID è stato aggiunto (era nuovo), false se già esisteva
     */
    public boolean aggiungiIdGita(int idGita) {
        return idGite.add(idGita);
    }

    /**
     * Rimuove un ID gita dal sistema.
     *
     * @param idGita ID della gita da rimuovere
     * @return true se l'ID esisteva ed è stato rimosso, false altrimenti
     */
    public boolean rimuoviIdGita(int idGita) {
        return idGite.remove(idGita);
    }

    /**
     * Controlla se un ID gita è presente nel sistema.
     *
     * @param idGita ID della gita da cercare
     * @return true se la gita esiste, false altrimenti
     */
    public boolean esisteIdGita(int idGita) {
        return idGite.contains(idGita);
    }

    /**
     * Restituisce l'insieme di tutti gli ID delle gite nel sistema.
     *
     * @return HashSet con tutti gli ID gite
     */
    public HashSet<Integer> getIdGite() {
        return idGite;
    }

    // ── GESTIONE ISCRIZIONI ───────────────────────────────────────────────────

    /**
     * Associa uno studente a una gita (crea un'iscrizione).
     * Se lo studente non è ancora nel sistema, viene aggiunto automaticamente.
     * Lo stesso vale per la gita.
     *
     * @param matricola matricola dello studente
     * @param idGita    ID della gita
     * @return true se l'iscrizione è nuova, false se lo studente era già iscritto a quella gita
     */
    public boolean aggiungiIscrizione(int matricola, int idGita) {
        aggiungiIdStudente(matricola);
        aggiungiIdGita(idGita);
        return iscrizioniPerStudente.get(matricola).add(idGita);
    }

    /**
     * Rimuove l'iscrizione di uno studente a una gita.
     *
     * @param matricola matricola dello studente
     * @param idGita    ID della gita
     * @return true se l'iscrizione esisteva ed è stata rimossa, false altrimenti
     */
    public boolean rimuoviIscrizione(int matricola, int idGita) {
        if (!iscrizioniPerStudente.containsKey(matricola)) {
            return false;
        }
        return iscrizioniPerStudente.get(matricola).remove(idGita);
    }

    /**
     * Controlla se uno studente è iscritto a una gita specifica.
     *
     * @param matricola matricola dello studente
     * @param idGita    ID della gita
     * @return true se l'iscrizione esiste, false altrimenti
     */
    public boolean esisteIscrizione(int matricola, int idGita) {
        if (!iscrizioniPerStudente.containsKey(matricola)) {
            return false;
        }
        return iscrizioniPerStudente.get(matricola).contains(idGita);
    }

    /**
     * Restituisce l'insieme delle gite a cui è iscritto uno studente.
     * Se lo studente non esiste nel sistema, restituisce un HashSet vuoto.
     *
     * @param matricola matricola dello studente
     * @return HashSet con gli ID delle gite a cui è iscritto lo studente
     */
    public HashSet<Integer> getGitePerStudente(int matricola) {
        return iscrizioniPerStudente.getOrDefault(matricola, new HashSet<>());
    }

    /**
     * Restituisce tutta la mappa delle iscrizioni.
     * La chiave è la matricola dello studente, il valore è il set degli ID gite.
     * Utile se si vuole scorrere tutte le iscrizioni in un colpo solo.
     *
     * @return HashMap completa con tutte le iscrizioni studente → gite
     */
    public HashMap<Integer, HashSet<Integer>> getIscrizioniPerStudente() {
        return iscrizioniPerStudente;
    }
}
