package abenantelutzengitasql;

import java.sql.*;
import java.util.*;

/**
 * Questa classe gestisce tutta la comunicazione con il database SQLite.
 * Contiene i metodi per creare le tabelle, inserire dati, eliminarli e leggerli.
 * Abbiamo usato SQLite perché non richiede un server separato ed è più comodo per un progetto scolastico.
 *
 * @author lutzen.jacopo
 */
public class GestioneDatabase {

    /** URL di connessione al database SQLite. foreign_keys=on serve per attivare i vincoli di chiave esterna */
    private final String url = "jdbc:sqlite:gestione_gite.db?foreign_keys=on";

    /**
     * Costruttore della classe GestioneDatabase.
     * Quando viene creato un oggetto di questa classe, vengono create automaticamente
     * tutte e quattro le tabelle del database (solo se non esistono già).
     * In questo modo i dati salvati in precedenza non vengono persi.
     */
    public GestioneDatabase() {
        creaTabellaClassi();
        creaTabellaGite();
        creaTabellaStudenti();
        creaTabellaPartecipazioni();
    }

    // ── CREAZIONE TABELLE ────────────────────────────────────────────────────

    /**
     * Crea la tabella "classi" nel database se non esiste già.
     * La tabella ha un ID autoincrementale, l'anno, la sezione e l'indirizzo.
     * Il vincolo UNIQUE su (anno, sezione, indirizzo) impedisce di inserire due classi uguali.
     *
     * @return true se la tabella è stata creata (o esisteva già), false in caso di errore
     */
    public final boolean creaTabellaClassi() {
        String sql = "CREATE TABLE IF NOT EXISTS classi ("
                + " cla_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " cla_anno INTEGER NOT NULL,"
                + " cla_sezione TEXT NOT NULL,"
                + " cla_indirizzo TEXT NOT NULL,"
                + " UNIQUE(cla_anno, cla_sezione, cla_indirizzo)"
                + ");";
        return eseguiSQL(sql, "creaTabellaClassi");
    }

    /**
     * Crea la tabella "gite" nel database se non esiste già.
     * Ogni gita ha un ID, una destinazione, una durata in giorni e un prezzo.
     *
     * @return true se tutto è andato bene, false se c'è stato un errore SQL
     */
    public final boolean creaTabellaGite() {
        String sql = "CREATE TABLE IF NOT EXISTS gite ("
                + " git_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " git_destinazione TEXT NOT NULL,"
                + " git_durata TEXT NOT NULL,"
                + " git_prezzo INTEGER NOT NULL"
                + ");";
        return eseguiSQL(sql, "creaTabellaGite");
    }

    /**
     * Crea la tabella "studenti" nel database se non esiste già.
     * Ogni studente è collegato a una classe tramite la chiave esterna stu_cla_id.
     *
     * @return true se tutto è andato bene, false se c'è stato un errore SQL
     */
    public final boolean creaTabellaStudenti() {
        String sql = "CREATE TABLE IF NOT EXISTS studenti ("
                + " stu_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " stu_nome TEXT NOT NULL,"
                + " stu_cognome TEXT NOT NULL,"
                + " stu_cla_id INTEGER NOT NULL,"
                + " FOREIGN KEY (stu_cla_id) REFERENCES classi(cla_id)"
                + ");";
        return eseguiSQL(sql, "creaTabellaStudenti");
    }

    /**
     * Crea la tabella "partecipazioni" nel database se non esiste già.
     * Questa tabella è una tabella di collegamento (N:N) tra studenti e gite.
     * La chiave primaria è composta da entrambi gli ID, così uno studente
     * non può essere iscritto due volte alla stessa gita.
     *
     * @return true se tutto è andato bene, false se c'è stato un errore SQL
     */
    public final boolean creaTabellaPartecipazioni() {
        String sql = "CREATE TABLE IF NOT EXISTS partecipazioni ("
                + " par_stu_id INTEGER NOT NULL,"
                + " par_git_id INTEGER NOT NULL,"
                + " PRIMARY KEY (par_stu_id, par_git_id),"
                + " FOREIGN KEY (par_stu_id) REFERENCES studenti(stu_id),"
                + " FOREIGN KEY (par_git_id) REFERENCES gite(git_id)"
                + ");";
        return eseguiSQL(sql, "creaTabellaPartecipazioni");
    }

    /**
     * Metodo privato di utilità che esegue una query SQL generica (tipo CREATE o DROP).
     * Viene usato dai metodi di creazione delle tabelle per evitare codice duplicato.
     *
     * @param sql        la stringa con il comando SQL da eseguire
     * @param nomeMetodo nome del metodo chiamante, usato solo per stampare eventuali errori
     * @return true se l'esecuzione è andata a buon fine, false se si è verificato un errore
     */
    private boolean eseguiSQL(String sql, String nomeMetodo) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Errore in " + nomeMetodo + ": " + e.getMessage());
            return false;
        }
    }

    // ── INSERT ───────────────────────────────────────────────────────────────

    /**
     * Inserisce una nuova classe nel database.
     * Se esiste già una classe con gli stessi dati, l'inserimento fallisce
     * perché c'è il vincolo UNIQUE sulla tabella.
     *
     * @param anno      anno scolastico della classe (da 1 a 5)
     * @param sezione   sezione della classe (es. "A", "B")
     * @param indirizzo indirizzo di studio (es. "Informatica")
     * @return true se l'inserimento ha avuto successo, false altrimenti
     */
    public boolean inserisciClasse(int anno, String sezione, String indirizzo) {
        String sql = "INSERT INTO classi (cla_anno, cla_sezione, cla_indirizzo) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, anno);
            pstmt.setString(2, sezione);
            pstmt.setString(3, indirizzo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore inserimento classe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce una nuova gita nel database con destinazione, durata e prezzo.
     *
     * @param destinazione nome della destinazione della gita (es. "Roma")
     * @param durata       durata della gita in giorni, passata come stringa
     * @param prezzo       costo della gita in euro
     * @return true se l'inserimento ha avuto successo, false altrimenti
     */
    public boolean inserisciGita(String destinazione, String durata, int prezzo) {
        String sql = "INSERT INTO gite (git_destinazione, git_durata, git_prezzo) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, destinazione);
            pstmt.setString(2, durata);
            pstmt.setInt(3, prezzo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore inserimento gita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce un nuovo studente nel database e lo associa alla classe indicata.
     * L'ID della classe deve esistere già nel database, altrimenti la foreign key
     * genera un errore.
     *
     * @param nome     nome dello studente
     * @param cognome  cognome dello studente
     * @param idClasse ID della classe a cui appartiene lo studente
     * @return true se l'inserimento ha avuto successo, false altrimenti
     */
    public boolean inserisciStudente(String nome, String cognome, int idClasse) {
        String sql = "INSERT INTO studenti (stu_nome, stu_cognome, stu_cla_id) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setInt(3, idClasse);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore inserimento studente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce una partecipazione, cioè associa uno studente a una gita.
     * Se la coppia (studente, gita) esiste già, l'inserimento fallisce
     * perché la PRIMARY KEY composta lo impedisce.
     *
     * @param idStudente matricola dello studente
     * @param idGita     ID della gita
     * @return true se l'inserimento ha avuto successo, false se già esiste o errore
     */
    public boolean inserisciPartecipazione(int idStudente, int idGita) {
        String sql = "INSERT INTO partecipazioni (par_stu_id, par_git_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idStudente);
            pstmt.setInt(2, idGita);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore inserimento partecipazione: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Elimina una classe dal database tramite il suo ID.
     * L'eliminazione fallirà se alla classe sono ancora associati degli studenti,
     * perché la foreign key nella tabella studenti lo impedisce (questo è il comportamento voluto).
     *
     * @param idClasse ID della classe da eliminare
     * @return true se almeno una riga è stata eliminata, false altrimenti
     */
    public boolean eliminaClasse(int idClasse) {
        String sql = "DELETE FROM classi WHERE cla_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idClasse);
            int righe = pstmt.executeUpdate();
            return righe > 0;
        } catch (SQLException e) {
            System.out.println("Errore eliminazione classe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una gita e tutte le partecipazioni collegate ad essa.
     * Usa una transazione per garantire che le due operazioni vadano a buon fine insieme:
     * se una delle due fallisce, viene fatto il rollback e non viene modificato niente.
     *
     * @param idGita ID della gita da eliminare
     * @return true se la gita è stata eliminata con successo, false altrimenti
     */
    public boolean eliminaGita(int idGita) {
        String sqlPart = "DELETE FROM partecipazioni WHERE par_git_id = ?";
        String sqlGita = "DELETE FROM gite WHERE git_id = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlPart);
                 PreparedStatement p2 = conn.prepareStatement(sqlGita)) {
                p1.setInt(1, idGita);
                p1.executeUpdate();
                p2.setInt(1, idGita);
                int righe = p2.executeUpdate();
                conn.commit();
                return righe > 0;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Errore eliminazione gita: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Errore connessione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina uno studente e tutte le sue partecipazioni alle gite.
     * Come per eliminaGita, usa una transazione per essere sicuri che
     * entrambe le operazioni vadano a buon fine, oppure nessuna delle due.
     *
     * @param idStudente matricola dello studente da eliminare
     * @return true se lo studente è stato eliminato con successo, false altrimenti
     */
    public boolean eliminaStudente(int idStudente) {
        String sqlPart = "DELETE FROM partecipazioni WHERE par_stu_id = ?";
        String sqlStu  = "DELETE FROM studenti WHERE stu_id = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlPart);
                 PreparedStatement p2 = conn.prepareStatement(sqlStu)) {
                p1.setInt(1, idStudente);
                p1.executeUpdate();
                p2.setInt(1, idStudente);
                int righe = p2.executeUpdate();
                conn.commit();
                return righe > 0;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Errore eliminazione studente: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Errore connessione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una singola partecipazione identificata dalla coppia (studente, gita).
     *
     * @param idStudente matricola dello studente
     * @param idGita     ID della gita
     * @return true se la partecipazione è stata trovata e rimossa, false altrimenti
     */
    public boolean eliminaPartecipazione(int idStudente, int idGita) {
        String sql = "DELETE FROM partecipazioni WHERE par_stu_id = ? AND par_git_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idStudente);
            pstmt.setInt(2, idGita);
            int righe = pstmt.executeUpdate();
            return righe > 0;
        } catch (SQLException e) {
            System.out.println("Errore eliminazione partecipazione: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ───────────────────────────────────────────────────────────────

    /**
     * Restituisce l'elenco di tutte le classi presenti nel database,
     * già formattate come stringhe pronte per essere mostrate in una JComboBox.
     * Il formato di ogni stringa è: "ID - anno° sezione indirizzo" (es. "1 - 4° A Informatica").
     *
     * @return lista di stringhe formattate con i dati delle classi
     */
    public List<String> getElencoClassiFormattate() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT cla_id, cla_anno, cla_sezione, cla_indirizzo FROM classi ORDER BY cla_anno, cla_sezione";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id    = rs.getInt("cla_id");
                int anno  = rs.getInt("cla_anno");
                String sez = rs.getString("cla_sezione");
                String ind = rs.getString("cla_indirizzo");
                lista.add(id + " - " + anno + "° " + sez + " " + ind);
            }
        } catch (SQLException e) {
            System.out.println("Errore getElencoClassi: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce l'elenco di tutte le gite presenti nel database,
     * già formattate come stringhe per la visualizzazione in una JComboBox.
     * Il formato è: "ID - destinazione (durata giorni) €prezzo".
     *
     * @return lista di stringhe formattate con i dati delle gite
     */
    public List<String> getElencoGiteFormattate() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT git_id, git_destinazione, git_durata, git_prezzo FROM gite ORDER BY git_id";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int    id   = rs.getInt("git_id");
                String dest = rs.getString("git_destinazione");
                String dur  = rs.getString("git_durata");
                int    pr   = rs.getInt("git_prezzo");
                lista.add(id + " - " + dest + " (" + dur + " giorni) €" + pr);
            }
        } catch (SQLException e) {
            System.out.println("Errore getElencoGite: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cerca tutti gli studenti appartenenti a una determinata classe.
     * Fa una JOIN tra la tabella studenti e classi per recuperare anche l'anno.
     * I risultati sono ordinati per cognome e poi per nome.
     *
     * @param idClasse ID della classe di cui si vogliono vedere gli studenti
     * @return lista di oggetti Studente che appartengono alla classe indicata
     */
    public List<Studente> cercaStudentiPerClasse(int idClasse) {
        List<Studente> lista = new ArrayList<>();
        String sql = "SELECT s.stu_id, s.stu_nome, s.stu_cognome, c.cla_anno "
                + "FROM studenti s JOIN classi c ON s.stu_cla_id = c.cla_id "
                + "WHERE s.stu_cla_id = ? ORDER BY s.stu_cognome, s.stu_nome";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idClasse);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Studente(
                        rs.getString("stu_nome"),
                        rs.getString("stu_cognome"),
                        rs.getInt("stu_id"),
                        rs.getInt("cla_anno")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Errore cercaStudentiPerClasse: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce tutti gli studenti presenti nel database, di tutte le classi.
     * Anche qui viene fatta la JOIN con classi per avere l'anno.
     * I risultati sono ordinati per cognome e poi per nome.
     *
     * @return lista completa di tutti gli oggetti Studente nel database
     */
    public List<Studente> getTuttiGliStudenti() {
        List<Studente> lista = new ArrayList<>();
        String sql = "SELECT s.stu_id, s.stu_nome, s.stu_cognome, c.cla_anno "
                + "FROM studenti s JOIN classi c ON s.stu_cla_id = c.cla_id "
                + "ORDER BY s.stu_cognome, s.stu_nome";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Studente(
                        rs.getString("stu_nome"),
                        rs.getString("stu_cognome"),
                        rs.getInt("stu_id"),
                        rs.getInt("cla_anno")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Errore getTuttiGliStudenti: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce tutte le gite presenti nel database come lista di oggetti Gita.
     * La durata è salvata come stringa nel DB, quindi viene convertita in intero.
     * Se la conversione fallisce, la durata viene impostata a 0.
     *
     * @return lista di tutti gli oggetti Gita presenti nel database
     */
    public List<Gita> getTutteLeGite() {
        List<Gita> lista = new ArrayList<>();
        String sql = "SELECT git_id, git_destinazione, git_durata FROM gite ORDER BY git_id";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id  = rs.getInt("git_id");
                String loc = rs.getString("git_destinazione");
                int dur = 0;
                try { dur = Integer.parseInt(rs.getString("git_durata")); } catch (NumberFormatException ignored) {}
                lista.add(new Gita(id, loc, dur));
            }
        } catch (SQLException e) {
            System.out.println("Errore getTutteLeGite: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cerca tutti gli studenti iscritti a una gita specifica.
     * Usa una doppia JOIN tra studenti, partecipazioni e classi.
     *
     * @param idGita ID della gita di cui si vogliono vedere i partecipanti
     * @return lista di oggetti Studente iscritti alla gita
     */
    public List<Studente> cercaStudentiPerGita(int idGita) {
        List<Studente> lista = new ArrayList<>();
        String sql = "SELECT s.stu_id, s.stu_nome, s.stu_cognome, c.cla_anno "
                + "FROM studenti s "
                + "JOIN partecipazioni p ON s.stu_id = p.par_stu_id "
                + "JOIN classi c ON s.stu_cla_id = c.cla_id "
                + "WHERE p.par_git_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idGita);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Studente(
                        rs.getString("stu_nome"),
                        rs.getString("stu_cognome"),
                        rs.getInt("stu_id"),
                        rs.getInt("cla_anno")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Errore cercaStudentiPerGita: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cerca tutte le gite a cui è iscritto uno studente specifico.
     * Fa la JOIN tra gite e partecipazioni filtrando per matricola studente.
     *
     * @param idStudente matricola dello studente
     * @return lista di oggetti Gita a cui lo studente è iscritto
     */
    public List<Gita> cercaGitePerStudente(int idStudente) {
        List<Gita> lista = new ArrayList<>();
        String sql = "SELECT g.git_id, g.git_destinazione, g.git_durata "
                + "FROM gite g "
                + "JOIN partecipazioni p ON g.git_id = p.par_git_id "
                + "WHERE p.par_stu_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idStudente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int dur = 0;
                try { dur = Integer.parseInt(rs.getString("git_durata")); } catch (NumberFormatException ignored) {}
                lista.add(new Gita(rs.getInt("git_id"), rs.getString("git_destinazione"), dur));
            }
        } catch (SQLException e) {
            System.out.println("Errore cercaGitePerStudente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce tutte le partecipazioni presenti nel database.
     * Ogni elemento della lista è un array di 4 oggetti:
     * <ul>
     *   <li>[0] matricola studente (Integer)</li>
     *   <li>[1] nome e cognome studente (String, concatenati con spazio)</li>
     *   <li>[2] ID gita (Integer)</li>
     *   <li>[3] destinazione gita (String)</li>
     * </ul>
     * Questi array servono a Logica.java per mostrare la tabella in JPartecipazione
     * e per poter eliminare le righe conoscendo gli ID.
     *
     * @return lista di array Object[] con i dati di ogni partecipazione
     */
    public List<Object[]> getPartecipazioni() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT s.stu_id, s.stu_nome || ' ' || s.stu_cognome AS studente, "
                + "g.git_id, g.git_destinazione AS gita "
                + "FROM partecipazioni p "
                + "JOIN studenti s ON p.par_stu_id = s.stu_id "
                + "JOIN gite g ON p.par_git_id = g.git_id "
                + "ORDER BY s.stu_cognome, s.stu_nome";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("stu_id"),
                    rs.getString("studente"),
                    rs.getInt("git_id"),
                    rs.getString("gita")
                });
            }
        } catch (SQLException e) {
            System.out.println("Errore getPartecipazioni: " + e.getMessage());
        }
        return lista;
    }
}
