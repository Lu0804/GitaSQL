package abenantelutzengitasql;

/**
 * Classe che rappresenta uno studente nel sistema.
 * Ogni studente ha un nome, un cognome, una matricola (che funge da ID univoco)
 * e l'anno scolastico della classe a cui appartiene.
 * Abbiamo implementato equals e hashCode basandoci sulla matricola
 * perché due studenti sono considerati uguali se hanno la stessa matricola.
 *
 * @author lutzen.jacopo
 */
public class Studente {

    /** Nome dello studente */
    private String nome;

    /** Cognome dello studente */
    private String cognome;

    /** Matricola univoca dello studente, usata come identificatore nel database */
    private int matricola;

    /** Anno scolastico della classe a cui appartiene lo studente */
    private int anno;

    /**
     * Costruttore che inizializza tutti i campi dello studente.
     *
     * @param nome      nome dello studente
     * @param cognome   cognome dello studente
     * @param matricola matricola univoca dello studente
     * @param anno      anno scolastico della classe dello studente
     */
    public Studente(String nome, String cognome, int matricola, int anno) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.anno = anno;
    }

    // ── GETTER E SETTER ──────────────────────────────────────────────────────

    /**
     * Restituisce il nome dello studente.
     * @return nome dello studente
     */
    public String getNome() { return nome; }

    /**
     * Imposta il nome dello studente.
     * @param nome nuovo nome dello studente
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce il cognome dello studente.
     * @return cognome dello studente
     */
    public String getCognome() { return cognome; }

    /**
     * Imposta il cognome dello studente.
     * @param cognome nuovo cognome dello studente
     */
    public void setCognome(String cognome) { this.cognome = cognome; }

    /**
     * Restituisce la matricola dello studente.
     * @return matricola dello studente
     */
    public int getMatricola() { return matricola; }

    /**
     * Imposta la matricola dello studente.
     * @param matricola nuova matricola dello studente
     */
    public void setMatricola(int matricola) { this.matricola = matricola; }

    /**
     * Restituisce l'anno scolastico dello studente.
     * @return anno della classe dello studente
     */
    public int getAnno() { return anno; }

    /**
     * Imposta l'anno scolastico dello studente.
     * @param anno nuovo anno della classe dello studente
     */
    public void setAnno(int anno) { this.anno = anno; }

    /**
     * Calcola l'hash code basandosi sulla matricola.
     * Due studenti con la stessa matricola avranno lo stesso hash code.
     * Questo è necessario perché abbiamo sovrascritto equals.
     *
     * @return hash code calcolato dalla matricola
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.matricola;
        return hash;
    }

    /**
     * Confronta due oggetti Studente in base alla matricola.
     * Due studenti sono uguali se e solo se hanno la stessa matricola.
     *
     * @param obj l'oggetto con cui confrontare questo studente
     * @return true se i due studenti hanno la stessa matricola, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Studente other = (Studente) obj;
        return this.matricola == other.matricola;
    }

    /**
     * Restituisce una rappresentazione testuale dello studente,
     * con nome, cognome e matricola.
     *
     * @return stringa descrittiva dello studente
     */
    @Override
    public String toString() {
        return " " + nome + " " + cognome + " con matricola " + matricola;
    }
}
