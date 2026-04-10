/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengitasql;

import java.sql.*;

/**
 *
 * @author lutzen.jacopo
 */
public class GestioneDatabase {
    
    // Mettiamo l'URL come variabile globale della classe.
    // L'aggiunta di "?foreign_keys=on" dice al driver JDBC di attivarle in automatico ad ogni connessione!
    private final String url = "jdbc:sqlite:gestione_gite.db?foreign_keys=on";

     public GestioneDatabase() {
        //creazione delle tabelle
        creaTabellaClassi();
        creaTabellaGite();
        creaTabellaStudenti();
        creaTabellaPartecipazioni();
    }

    /**
     * Metodo per creare la tabella delle classi
     * @return true se e' stata creata la tabella, false se non e' riuscito a creare la tabella
     */
    public boolean creaTabellaClassi() {
        String sql = "CREATE TABLE IF NOT EXISTS classi (\n"  
           + " cla_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
           + " cla_anno INTEGER NOT NULL,\n"           // Es: 3, 4
           + " cla_sezione TEXT NOT NULL,\n"           // Es: "A", "B"
           + " cla_indirizzo TEXT NOT NULL,\n"         // Es: "Informatica", "Linguistico"
           + " UNIQUE(cla_anno, cla_sezione, cla_indirizzo)\n" // Evita duplicati come due "3 A Informatica"
           + ");";
        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'classi' creata con successo!");
            return true;
           
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaClassi: " + e.getMessage());  
            return false;
        }
    }

    /**
     * Metodo per creare la tabella delle gite
     * @return true se e' stata creata la tabella, false se non e' riuscito a creare la tabella
     */
    public boolean creaTabellaGite() {
        String sql = "CREATE TABLE IF NOT EXISTS gite (\n"  
           + " git_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
           + " git_destinazione TEXT NOT NULL,\n"  
           + " git_durata TEXT NOT NULL,\n"          
           + " git_prezzo INTEGER NOT NULL\n"      
           + ");";

        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'gite' creata con successo!");
            return true;
           
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaGite: " + e.getMessage());  
            return false;
        }
    }

    /**
     * Metodo per creare la tabella degli studenti
     * @return true se e' stata creata la tabella, false se non e' riuscito a creare la tabella
     */
    public boolean creaTabellaStudenti() {
        String sql = "CREATE TABLE IF NOT EXISTS studenti (\n"  
           + " stu_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
           + " stu_nome TEXT NOT NULL,\n"  
           + " stu_cognome TEXT NOT NULL,\n"
           + " stu_cla_id INTEGER NOT NULL,\n"
           + " FOREIGN KEY (stu_cla_id) REFERENCES classi(cla_id)\n"
           + ");";

        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'studenti' creata con successo!");
            return true;
           
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaStudenti: " + e.getMessage());  
            return false;
        }
    }

    /**
     * Metodo per creare la tabella delle partecipazioni, lega gita a studente
     * @return true se e' stata creata la tabella, false se non e' riuscito a creare la tabella
     */
    public boolean creaTabellaPartecipazioni() {
        // "tabella ponte" o di congiunzione, perché serve solo a collegare gli studenti alle gite in una relazione "molti-a-molti"
        String sql = "CREATE TABLE IF NOT EXISTS partecipazioni (\n"  
           + " par_stu_id INTEGER NOT NULL,\n"  
           + " par_git_id INTEGER NOT NULL,\n"  
           + " PRIMARY KEY (par_stu_id, par_git_id),\n" // Uno studente non può iscriversi due volte alla stessa gita
           + " FOREIGN KEY (par_stu_id) REFERENCES studenti(stu_id),\n" // Collega allo studente
           + " FOREIGN KEY (par_git_id) REFERENCES gite(git_id)\n" // Collega alla gita
           + ");";

        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'partecipazioni' creata con successo!");
            return true;
           
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaPartecipazioni: " + e.getMessage());  
            return false;
        }
    }
   
    /**
     * Inserisce una nuova classe nel database
     * @param anno Es: 3, 4, 5
     * @param sezione Es: "A", "B"
     * @param indirizzo Es: "Informatica", "Linguistico"
     * @return true se l'inserimento è riuscito
     */
    public boolean inserisciClasse(int anno, String sezione, String indirizzo) {
        String sql = "INSERT INTO classi (cla_anno, cla_sezione, cla_indirizzo) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, anno);
            pstmt.setString(2, sezione);
            pstmt.setString(3, indirizzo);

            pstmt.executeUpdate();
            System.out.println("Classe inserita con successo!");
            return true;

        } catch (SQLException e) {
            System.out.println("Errore inserimento classe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce una nuova gita
     * @param destinazione Es: "Roma", "Parigi"
     * @param durata Es: "3 giorni", "1 giorno"
     * @param prezzo Prezzo in euro
     * @return true se l'inserimento è riuscito
     */
    public boolean inserisciGita(String destinazione, String durata, int prezzo) {
        String sql = "INSERT INTO gite (git_destinazione, git_durata, git_prezzo) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, destinazione);
            pstmt.setString(2, durata);
            pstmt.setInt(3, prezzo);

            pstmt.executeUpdate();
            System.out.println("Gita inserita con successo!");
            return true;

        } catch (SQLException e) {
            System.out.println("Errore inserimento gita: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserisce uno studente associandolo a una classe esistente
     * @param nome Nome studente
     * @param cognome Cognome studente
     * @param idClasse L'ID (cla_id) della classe a cui appartiene
     * @return true se l'inserimento è riuscito
     */
    public boolean inserisciStudente(String nome, String cognome, int idClasse) {
        String sql = "INSERT INTO studenti (stu_nome, stu_cognome, stu_cla_id) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setInt(3, idClasse);

            pstmt.executeUpdate();
            System.out.println("Studente inserito con successo!");
            return true;

        } catch (SQLException e) {
            System.out.println("Errore inserimento studente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra la partecipazione di uno studente a una gita
     * @param idStudente L'ID (stu_id) dello studente
     * @param idGita L'ID (git_id) della gita
     * @return true se l'inserimento è riuscito
     */
    public boolean inserisciPartecipazione(int idStudente, int idGita) {
        String sql = "INSERT INTO partecipazioni (par_stu_id, par_git_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idStudente);
            pstmt.setInt(2, idGita);

            pstmt.executeUpdate();
            System.out.println("Partecipazione registrata con successo!");
            return true;

        } catch (SQLException e) {
            System.out.println("Errore inserimento partecipazione: " + e.getMessage());
            // Nota: fallirà se lo studente o la gita non esistono (grazie ai Foreign Keys)
            // o se lo studente è già iscritto a quella gita (grazie alla Primary Key composta)
            return false;
        }
    }
}