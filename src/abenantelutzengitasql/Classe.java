package abenantelutzengitasql;

/**
 * Classe che rappresenta una classe scolastica nel sistema.
 * Contiene l'anno (da 1 a 5), la sezione (una lettera come A, B, C)
 * e l'indirizzo di studio (es. "Informatica", "Elettronica").
 * Nota: questa classe al momento non ha costruttore, getter o setter espliciti,
 * ma i dati vengono gestiti direttamente tramite GestioneDatabase.
 *
 * @author lutzen.jacopo
 */
public class Classe {

    /** Anno scolastico della classe, deve essere compreso tra 1 e 5 */
    private int anno;

    /** Sezione della classe, deve essere un singolo carattere alfabetico (es. 'A') */
    private char sezione;

    /** Indirizzo di studio della classe (es. "Informatica") */
    private String indirizzo;
}
