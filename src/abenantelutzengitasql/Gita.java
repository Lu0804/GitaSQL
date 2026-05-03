package abenantelutzengitasql;

/**
 * Classe che rappresenta una gita scolastica nel sistema.
 * Ogni gita ha un ID univoco generato dal database, una località di destinazione
 * e la durata in giorni.
 * Come per Studente, equals e hashCode sono basati sull'ID univoco.
 *
 * @author lutzen.jacopo
 */
public class Gita {

    /** ID univoco della gita, generato automaticamente dal database */
    private int id;

    /** Località di destinazione della gita (es. "Roma", "Venezia") */
    private String localita;

    /** Durata della gita espressa in giorni */
    private int durata;

    /**
     * Costruttore che inizializza tutti i campi della gita.
     *
     * @param id      ID univoco della gita
     * @param localita località di destinazione della gita
     * @param durata  durata della gita in giorni
     */
    public Gita(int id, String localita, int durata) {
        this.id = id;
        this.localita = localita;
        this.durata = durata;
    }

    // ── GETTER E SETTER ──────────────────────────────────────────────────────

    /**
     * Restituisce l'ID della gita.
     * @return ID univoco della gita
     */
    public int getId() { return id; }

    /**
     * Imposta l'ID della gita.
     * @param id nuovo ID della gita
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce la località di destinazione della gita.
     * @return nome della destinazione
     */
    public String getLocalita() { return localita; }

    /**
     * Imposta la località di destinazione della gita.
     * @param localita nuova destinazione
     */
    public void setLocalita(String localita) { this.localita = localita; }

    /**
     * Restituisce la durata della gita in giorni.
     * @return durata in giorni
     */
    public int getDurata() { return durata; }

    /**
     * Imposta la durata della gita in giorni.
     * @param durata nuova durata in giorni
     */
    public void setDurata(int durata) { this.durata = durata; }

    /**
     * Calcola l'hash code basandosi sull'ID della gita.
     * Necessario perché abbiamo sovrascritto equals.
     *
     * @return hash code calcolato dall'ID
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        return hash;
    }

    /**
     * Confronta due oggetti Gita in base all'ID.
     * Due gite sono uguali se e solo se hanno lo stesso ID.
     *
     * @param obj l'oggetto con cui confrontare questa gita
     * @return true se i due oggetti hanno lo stesso ID, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Gita other = (Gita) obj;
        return this.id == other.id;
    }

    /**
     * Restituisce una rappresentazione testuale della gita,
     * con ID, destinazione e durata.
     *
     * @return stringa descrittiva della gita
     */
    @Override
    public String toString() {
        return "Gita con id=" + id + " a " + localita + " di durata " + durata + " giorni";
    }
}
