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

    public Controllo getControllo() {
        return controllo;
    }

    // ── CLASSI ───────────────────────────────────────────────────────────────

    public String creaClasse(String annoStr, String sezione, String indirizzo) {
        String errVal = controllo.validaClasse(annoStr, sezione, indirizzo);
        if (!errVal.isEmpty()) return errVal;

        int anno = Integer.parseInt(annoStr.trim());
        boolean ok = db.inserisciClasse(anno, sezione.trim().toUpperCase(), indirizzo.trim());
        if (!ok) return "Errore: questa classe esiste già o si è verificato un problema con il database.";
        return "";
    }

    public List<String> getElencoClassi() {
        return db.getElencoClassiFormattate();
    }

    public int estraiIdDaStringa(String s) {
        if (s == null || s.isEmpty()) return -1;
        try {
            return Integer.parseInt(s.split(" - ")[0].trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String eliminaClasse(int idClasse) {
        boolean ok = db.eliminaClasse(idClasse);
        if (!ok) return "Impossibile eliminare la classe: potrebbe avere studenti associati o non esistere.";
        return "";
    }

    // ── GITE ─────────────────────────────────────────────────────────────────

    public String creaGita(String destinazione, String durataStr, String prezzoStr) {
        String errVal = controllo.validaGita(destinazione, durataStr, prezzoStr);
        if (!errVal.isEmpty()) return errVal;

        int durata = Integer.parseInt(durataStr.trim());
        int prezzo = Integer.parseInt(prezzoStr.trim());
        boolean ok = db.inserisciGita(destinazione.trim(), String.valueOf(durata), prezzo);
        if (!ok) return "Errore durante l'inserimento della gita nel database.";
        return "";
    }

    public List<String> getElencoGite() {
        return db.getElencoGiteFormattate();
    }

    public String eliminaGita(int idGita) {
        boolean ok = db.eliminaGita(idGita);
        if (!ok) return "Impossibile eliminare la gita: potrebbe avere partecipazioni associate o non esistere.";
        return "";
    }

    // ── STUDENTI ─────────────────────────────────────────────────────────────

    public String creaStudente(String nome, String cognome, String classeFormattata) {
        String errVal = controllo.validaStudente(nome, cognome, classeFormattata);
        if (!errVal.isEmpty()) return errVal;

        int idClasse = estraiIdDaStringa(classeFormattata);
        if (idClasse < 0) return "Seleziona una classe valida.";

        boolean ok = db.inserisciStudente(nome.trim(), cognome.trim(), idClasse);
        if (!ok) return "Errore durante l'inserimento dello studente nel database.";
        return "";
    }

    public String eliminaStudente(int idStudente) {
        boolean ok = db.eliminaStudente(idStudente);
        if (!ok) return "Impossibile eliminare lo studente.";
        return "";
    }

    // ── PARTECIPAZIONI ───────────────────────────────────────────────────────

    public String creaPartecipazione(int idStudente, int idGita) {
        if (idStudente <= 0) return "ID Studente non valido.";
        if (idGita <= 0) return "ID Gita non valido.";
        boolean ok = db.inserisciPartecipazione(idStudente, idGita);
        if (!ok) return "Errore: lo studente potrebbe essere già iscritto a questa gita, oppure gli ID non esistono.";
        return "";
    }

    /**
     * Rimuove una partecipazione dato matricola studente e id gita.
     */
    public String eliminaPartecipazione(int matricola, int idGita) {
        boolean ok = db.eliminaPartecipazione(matricola, idGita);
        if (!ok) return "Impossibile rimuovere la partecipazione.";
        return "";
    }

    // ── VISUALIZZAZIONE ───────────────────────────────────────────────────────

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

    /**
     * Restituisce tutte le partecipazioni come matrice per JTable (solo 2 colonne visibili).
     * Colonne: Studente (nome+cognome) | Gita (destinazione)
     * Usato dal vecchio codice – preferire getPartecipazioniTabellaCompleta().
     */
    public Object[][] getPartecipazioniTabella() {
        Object[][] completa = getPartecipazioniTabellaCompleta();
        Object[][] dati = new Object[completa.length][2];
        for (int i = 0; i < completa.length; i++) {
            dati[i][0] = completa[i][1]; // nome studente
            dati[i][1] = completa[i][3]; // localita gita
        }
        return dati;
    }

    /**
     * Restituisce tutte le partecipazioni con 4 colonne:
     * [0] matricola (int)  |  [1] nome+cognome (String)
     * [2] idGita (int)     |  [3] localita (String)
     *
     * Le colonne 0 e 2 servono per la cancellazione in JPartecipazione.
     */
    public Object[][] getPartecipazioniTabellaCompleta() {
        List<Object[]> lista = db.getPartecipazioni();
        Object[][] dati = new Object[lista.size()][4];
        for (int i = 0; i < lista.size(); i++) {
            Object[] riga = lista.get(i);
            // GestioneDatabase.getPartecipazioni() deve restituire:
            // riga[0] = matricola (int), riga[1] = nome+cognome (String),
            // riga[2] = idGita (int),    riga[3] = localita (String)
            dati[i][0] = riga[0]; // matricola
            dati[i][1] = riga[1]; // nome studente
            dati[i][2] = riga[2]; // id gita
            dati[i][3] = riga[3]; // localita
        }
        return dati;
    }
}