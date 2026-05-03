package abenantelutzengitasql;

import java.util.List;
import java.util.ArrayList;

/**
 * Questa è la classe "ponte" tra l'interfaccia grafica e il database.
 * Tutte le finestre (JFrame) comunicano solo con questa classe e non toccano
 * mai direttamente GestioneDatabase. Questo si chiama architettura a livelli,
 * e serve per tenere il codice più ordinato e meno complicato da modificare.
 * Questa classe si occupa anche di chiamare i controlli di validazione
 * prima di mandare i dati al database.
 *
 * @author abenante.lucia / lutzen.jacopo
 */
public class Logica {

    /** Oggetto per accedere al database */
    private final GestioneDatabase db;

    /** Oggetto per validare i dati inseriti dall'utente */
    private final Controllo controllo;

    /**
     * Costruttore della classe Logica.
     * Crea una nuova istanza del database e del controllo.
     * Viene chiamato una sola volta all'avvio dell'applicazione, in JPartecipazione.
     */
    public Logica() {
        this.db = new GestioneDatabase();
        this.controllo = new Controllo();
    }

    /**
     * Restituisce l'oggetto Controllo, nel caso in cui qualche finestra
     * avesse bisogno di usarlo direttamente.
     *
     * @return l'istanza di Controllo usata da questa classe
     */
    public Controllo getControllo() {
        return controllo;
    }

    // ── CLASSI ───────────────────────────────────────────────────────────────

    /**
     * Valida i dati di una classe e, se sono corretti, la inserisce nel database.
     * Prima chiama il metodo di validazione in Controllo, poi converte l'anno
     * da stringa a intero e passa tutto a GestioneDatabase.
     *
     * @param annoStr   anno della classe come stringa (es. "4")
     * @param sezione   sezione della classe (es. "A")
     * @param indirizzo indirizzo di studio (es. "Informatica")
     * @return stringa vuota se tutto è andato bene, altrimenti il messaggio di errore
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
     * Restituisce l'elenco di tutte le classi formattate per le JComboBox.
     * Delega direttamente a GestioneDatabase senza modifiche.
     *
     * @return lista di stringhe nel formato "ID - anno° sezione indirizzo"
     */
    public List<String> getElencoClassi() {
        return db.getElencoClassiFormattate();
    }

    /**
     * Estrae l'ID numerico da una stringa formattata come "ID - descrizione".
     * Viene usato quando l'utente sceglie una classe o una gita da una JComboBox:
     * la stringa contiene tutte le info, ma per il database serve solo l'ID.
     *
     * @param s stringa nel formato "ID - ..." (es. "3 - 4° A Informatica")
     * @return l'ID estratto come intero, oppure -1 se la stringa non è valida
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
     * Elimina una classe dal database tramite il suo ID.
     * L'eliminazione può fallire se la classe ha ancora studenti associati.
     *
     * @param idClasse ID della classe da eliminare
     * @return stringa vuota se l'eliminazione ha avuto successo, altrimenti messaggio di errore
     */
    public String eliminaClasse(int idClasse) {
        boolean ok = db.eliminaClasse(idClasse);
        if (!ok) return "Impossibile eliminare la classe: potrebbe avere studenti associati o non esistere.";
        return "";
    }

    // ── GITE ─────────────────────────────────────────────────────────────────

    /**
     * Valida i dati di una gita e, se sono corretti, la inserisce nel database.
     * I campi durata e prezzo vengono prima validati come interi, poi convertiti.
     *
     * @param destinazione nome della destinazione (es. "Venezia")
     * @param durataStr    durata in giorni come stringa (es. "3")
     * @param prezzoStr    prezzo in euro come stringa (es. "150")
     * @return stringa vuota se tutto è andato bene, altrimenti il messaggio di errore
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
     * Restituisce l'elenco di tutte le gite formattate per le JComboBox.
     *
     * @return lista di stringhe nel formato "ID - destinazione (durata giorni) €prezzo"
     */
    public List<String> getElencoGite() {
        return db.getElencoGiteFormattate();
    }

    /**
     * Elimina una gita e tutte le partecipazioni collegate.
     *
     * @param idGita ID della gita da eliminare
     * @return stringa vuota se l'eliminazione ha avuto successo, altrimenti messaggio di errore
     */
    public String eliminaGita(int idGita) {
        boolean ok = db.eliminaGita(idGita);
        if (!ok) return "Impossibile eliminare la gita: potrebbe avere partecipazioni associate o non esistere.";
        return "";
    }

    // ── STUDENTI ─────────────────────────────────────────────────────────────

    /**
     * Valida i dati di uno studente e, se sono corretti, lo inserisce nel database.
     * La classe viene passata come stringa formattata dalla JComboBox,
     * e viene estratto l'ID prima di inserire nel DB.
     *
     * @param nome             nome dello studente
     * @param cognome          cognome dello studente
     * @param classeFormattata stringa formattata della classe (es. "2 - 4° A Informatica")
     * @return stringa vuota se tutto è andato bene, altrimenti il messaggio di errore
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
     * Elimina uno studente e tutte le sue partecipazioni alle gite.
     *
     * @param idStudente matricola dello studente da eliminare
     * @return stringa vuota se l'eliminazione ha avuto successo, altrimenti messaggio di errore
     */
    public String eliminaStudente(int idStudente) {
        boolean ok = db.eliminaStudente(idStudente);
        if (!ok) return "Impossibile eliminare lo studente.";
        return "";
    }

    // ── PARTECIPAZIONI ───────────────────────────────────────────────────────

    /**
     * Crea una nuova partecipazione tra uno studente e una gita.
     * Prima controlla che gli ID siano validi, poi delega al database.
     *
     * @param idStudente matricola dello studente
     * @param idGita     ID della gita
     * @return stringa vuota se tutto è andato bene, altrimenti il messaggio di errore
     */
    public String creaPartecipazione(int idStudente, int idGita) {
        if (idStudente <= 0) return "ID Studente non valido.";
        if (idGita <= 0) return "ID Gita non valido.";
        boolean ok = db.inserisciPartecipazione(idStudente, idGita);
        if (!ok) return "Errore: lo studente potrebbe essere già iscritto a questa gita, oppure gli ID non esistono.";
        return "";
    }

    /**
     * Rimuove una partecipazione dato la matricola dello studente e l'ID della gita.
     *
     * @param matricola matricola dello studente
     * @param idGita    ID della gita
     * @return stringa vuota se la rimozione ha avuto successo, altrimenti messaggio di errore
     */
    public String eliminaPartecipazione(int matricola, int idGita) {
        boolean ok = db.eliminaPartecipazione(matricola, idGita);
        if (!ok) return "Impossibile rimuovere la partecipazione.";
        return "";
    }

    // ── VISUALIZZAZIONE ───────────────────────────────────────────────────────

    /**
     * Restituisce gli studenti di una classe specifica come matrice per una JTable.
     * Ogni riga contiene: [matricola, nome, cognome, anno].
     *
     * @param idClasse ID della classe
     * @return matrice Object[][] con i dati degli studenti della classe
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
     * Restituisce tutte le gite come matrice per una JTable.
     * Le colonne sono: ID | Durata (giorni) | Località.
     *
     * @return matrice Object[][] con i dati di tutte le gite
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
     * Restituisce tutti gli studenti (di tutte le classi) come matrice per una JTable.
     * Colonne: [matricola, nome, cognome, anno].
     *
     * @return matrice Object[][] con i dati di tutti gli studenti
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
     * Restituisce le partecipazioni con solo 2 colonne visibili (nome studente, gita).
     * Metodo mantenuto per compatibilità, ma è preferibile usare
     * {@link #getPartecipazioniTabellaCompleta()} che include anche gli ID.
     *
     * @return matrice Object[][] con [nome studente, localita gita]
     */
    public Object[][] getPartecipazioniTabella() {
        Object[][] completa = getPartecipazioniTabellaCompleta();
        Object[][] dati = new Object[completa.length][2];
        for (int i = 0; i < completa.length; i++) {
            dati[i][0] = completa[i][1];
            dati[i][1] = completa[i][3];
        }
        return dati;
    }

    /**
     * Restituisce tutte le partecipazioni con 4 colonne, inclusi gli ID
     * necessari per poter eliminare le righe dalla tabella.
     * <ul>
     *   <li>[0] matricola (int)</li>
     *   <li>[1] nome e cognome dello studente (String)</li>
     *   <li>[2] ID gita (int)</li>
     *   <li>[3] destinazione della gita (String)</li>
     * </ul>
     * Le colonne 0 e 2 non vengono mostrate all'utente ma servono internamente
     * a JPartecipazione per sapere quale riga cancellare.
     *
     * @return matrice Object[][] completa con tutti i dati delle partecipazioni
     */
    public Object[][] getPartecipazioniTabellaCompleta() {
        List<Object[]> lista = db.getPartecipazioni();
        Object[][] dati = new Object[lista.size()][4];
        for (int i = 0; i < lista.size(); i++) {
            Object[] riga = lista.get(i);
            dati[i][0] = riga[0];
            dati[i][1] = riga[1];
            dati[i][2] = riga[2];
            dati[i][3] = riga[3];
        }
        return dati;
    }
}
