/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengitasql;

import java.sql.*;
import java.util.*;

/**
 * Gestisce tutta la comunicazione con il database SQLite.
 *
 * @author lutzen.jacopo
 */
public class GestioneDatabase {

    private final String url = "jdbc:sqlite:gestione_gite.db?foreign_keys=on";

   public GestioneDatabase() {
        //
        eseguiSQL("DROP TABLE IF EXISTS partecipazioni;", "dropPartecipazioni");
        eseguiSQL("DROP TABLE IF EXISTS studenti;", "dropStudenti");
        eseguiSQL("DROP TABLE IF EXISTS gite;", "dropGite");
        eseguiSQL("DROP TABLE IF EXISTS classi;", "dropClassi");
        // -----------------------------------------------------------------

        // Questo è il tuo codice originale che le ricrea corrette
        creaTabellaClassi();
        creaTabellaGite();
        creaTabellaStudenti();
        creaTabellaPartecipazioni();
    }

    // ── CREAZIONE TABELLE ────────────────────────────────────────────────────

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

    public final boolean creaTabellaGite() {
        String sql = "CREATE TABLE IF NOT EXISTS gite ("
                + " git_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " git_destinazione TEXT NOT NULL,"
                + " git_durata TEXT NOT NULL,"
                + " git_prezzo INTEGER NOT NULL"
                + ");";
        return eseguiSQL(sql, "creaTabellaGite");
    }

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
     * Elimina una classe solo se non ha studenti associati.
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
     * Elimina una gita e le sue partecipazioni collegate.
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
     * Elimina uno studente e le sue partecipazioni collegate.
     */
    public boolean eliminaStudente(int idStudente) {
        String sqlPart = "DELETE FROM partecipazioni WHERE par_stu_id = ?";
        String sqlStu = "DELETE FROM studenti WHERE stu_id = ?";
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

    // ── SELECT / QUERY ───────────────────────────────────────────────────────

    /**
     * Restituisce tutte le classi come stringhe "ID - Anno Sezione Indirizzo"
     */
    public List<String> getElencoClassiFormattate() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT cla_id, cla_anno, cla_sezione, cla_indirizzo FROM classi ORDER BY cla_anno, cla_sezione";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("cla_id");
                int anno = rs.getInt("cla_anno");
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
     * Restituisce tutte le gite come stringhe "ID - Destinazione (X giorni) €Y"
     */
    public List<String> getElencoGiteFormattate() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT git_id, git_destinazione, git_durata, git_prezzo FROM gite ORDER BY git_id";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("git_id");
                String dest = rs.getString("git_destinazione");
                String dur = rs.getString("git_durata");
                int pr = rs.getInt("git_prezzo");
                lista.add(id + " - " + dest + " (" + dur + " giorni) €" + pr);
            }
        } catch (SQLException e) {
            System.out.println("Errore getElencoGite: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Restituisce tutti gli studenti di una classe.
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
     * Restituisce tutti gli studenti (tutte le classi).
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
     * Restituisce tutte le gite.
     */
    public List<Gita> getTutteLeGite() {
        List<Gita> lista = new ArrayList<>();
        String sql = "SELECT git_id, git_destinazione, git_durata FROM gite ORDER BY git_id";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("git_id");
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
}