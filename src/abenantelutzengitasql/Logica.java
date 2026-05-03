/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengitasql;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe Logica: ponte tra la grafica (JFrame) e il database (GestioneDatabase).
 * La grafica chiama solo metodi di questa classe, mai direttamente il database.
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class Logica {

    private final GestioneDatabase db;
    private final Controllo controllo;

    public Logica() {
        this.db = new GestioneDatabase();
        this.controllo = new Controllo();
    }

    // ── GETTER per usare il controllo dalla grafica ──────────────────────────

    public Controllo getControllo() {
        return controllo;
    }

    // ── CLASSI ───────────────────────────────────────────────────────────────

    /**
     * Crea una nuova classe nel database.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String creaClasse(String annoStr, String sezione, String indirizzo) {
        String errVal = controllo.validaClasse(annoStr, sezione, indirizzo);
        if (!errVal.isEmpty()) return errVal;

        int anno = Integer.parseInt(annoStr.trim());
        boolean ok = db.inserisciClasse(anno, sezione.trim().toUpperCase(), indirizzo.trim());
        if (!ok) return "Errore: questa classe esiste già o si è verificato un problema con il database.";
        return "";
    }

    /**
     * Restituisce tutte le classi come lista di stringhe leggibili "ID - Anno Sezione Indirizzo".
     */
    public List<String> getElencoClassi() {
        return db.getElencoClassiFormattate();
    }

    /**
     * Restituisce l'ID di una classe dalla stringa formattata "ID - ..."
     */
    public int estraiIdDaStringa(String s) {
        if (s == null || s.isEmpty()) return -1;
        try {
            return Integer.parseInt(s.split(" - ")[0].trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Elimina una classe per ID.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String eliminaClasse(int idClasse) {
        boolean ok = db.eliminaClasse(idClasse);
        if (!ok) return "Impossibile eliminare la classe: potrebbe avere studenti associati o non esistere.";
        return "";
    }

    // ── GITE ─────────────────────────────────────────────────────────────────

    /**
     * Crea una nuova gita nel database.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String creaGita(String destinazione, String durataStr, String prezzoStr) {
        String errVal = controllo.validaGita(destinazione, durataStr, prezzoStr);
        if (!errVal.isEmpty()) return errVal;

        int durata = Integer.parseInt(durataStr.trim());
        int prezzo = Integer.parseInt(prezzoStr.trim());
        boolean ok = db.inserisciGita(destinazione.trim(), String.valueOf(durata), prezzo);
        if (!ok) return "Errore durante l'inserimento della gita nel database.";
        return "";
    }

    /**
     * Restituisce tutte le gite come lista di stringhe "ID - Destinazione (X giorni) €Y"
     */
    public List<String> getElencoGite() {
        return db.getElencoGiteFormattate();
    }

    /**
     * Elimina una gita per ID.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String eliminaGita(int idGita) {
        boolean ok = db.eliminaGita(idGita);
        if (!ok) return "Impossibile eliminare la gita: potrebbe avere partecipazioni associate o non esistere.";
        return "";
    }

    // ── STUDENTI ─────────────────────────────────────────────────────────────

    /**
     * Crea un nuovo studente nel database.
     * @param classeFormattata la stringa dalla combobox "ID - Anno Sezione Indirizzo"
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String creaStudente(String nome, String cognome, String classeFormattata) {
        String errVal = controllo.validaStudente(nome, cognome, classeFormattata);
        if (!errVal.isEmpty()) return errVal;

        int idClasse = estraiIdDaStringa(classeFormattata);
        if (idClasse < 0) return "Seleziona una classe valida.";

        boolean ok = db.inserisciStudente(nome.trim(), cognome.trim(), idClasse);
        if (!ok) return "Errore durante l'inserimento dello studente nel database.";
        return "";
    }

    /**
     * Elimina uno studente per ID.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String eliminaStudente(int idStudente) {
        boolean ok = db.eliminaStudente(idStudente);
        if (!ok) return "Impossibile eliminare lo studente.";
        return "";
    }

    // ── PARTECIPAZIONI ───────────────────────────────────────────────────────

    /**
     * Registra una partecipazione studente-gita.
     * @return stringa vuota se ok, messaggio d'errore se fallisce
     */
    public String creaPartecipazione(int idStudente, int idGita) {
        if (idStudente <= 0) return "ID Studente non valido.";
        if (idGita <= 0) return "ID Gita non valido.";
        boolean ok = db.inserisciPartecipazione(idStudente, idGita);
        if (!ok) return "Errore: lo studente potrebbe essere già iscritto a questa gita, oppure gli ID non esistono.";
        return "";
    }

    // ── VISUALIZZAZIONE ───────────────────────────────────────────────────────

    /**
     * Restituisce tutti gli studenti di una classe come matrice per JTable.
     * Colonne: Matricola | Nome | Cognome | Anno classe
     */
    public Object[][] getStudentiPerClasseTabella(int idClasse) {
        List<Studente> lista = db.cercaStudentiPerClasse(idClasse);
        Object[][] dati = new Object[lista.size()][4];
        for (int i = 0; i < lista.size(); i++) {
            Studente s = lista.get(i);
            dati[i][0] = s.getMatricola();
            dati[i][1] = s.getNome();
            dati[i][2] = s.getCognome();
            dati[i][3] = s.getAnno();
        }
        return dati;
    }

    /**
     * Restituisce tutte le gite come matrice per JTable.
     * Colonne: ID | Durata | Località
     */
    public Object[][] getGiteTabella() {
        List<Gita> lista = db.getTutteLeGite();
        Object[][] dati = new Object[lista.size()][3];
        for (int i = 0; i < lista.size(); i++) {
            Gita g = lista.get(i);
            dati[i][0] = g.getId();
            dati[i][1] = g.getDurata();
            dati[i][2] = g.getLocalita();
        }
        return dati;
    }

    /**
     * Restituisce tutti gli studenti (tutte le classi) come matrice per JTable.
     */
    public Object[][] getTuttiStudentiTabella() {
        List<Studente> lista = db.getTuttiGliStudenti();
        Object[][] dati = new Object[lista.size()][4];
        for (int i = 0; i < lista.size(); i++) {
            Studente s = lista.get(i);
            dati[i][0] = s.getMatricola();
            dati[i][1] = s.getNome();
            dati[i][2] = s.getCognome();
            dati[i][3] = s.getAnno();
        }
        return dati;
    }
}