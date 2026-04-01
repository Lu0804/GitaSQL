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
        // L'ordine è importante! Prima le tabelle indipendenti (classe, gita), 
        // poi quelle che dipendono da esse tramite le chiavi esterne (studenti, partecipazioni).
        creaTabellaClasse();
        creaTabellaGita();
        creaTabellaStudenti();
        creaTabellaPartecipazioni();
    }

    public boolean creaTabellaClasse() {
        String sql = "CREATE TABLE IF NOT EXISTS classe (\n"  
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
                + " nome_classe TEXT NOT NULL UNIQUE\n"  // Es: "3A", "4B"
                + ");";  

        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'classe' creata con successo!");
            return true;
            
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaClasse: " + e.getMessage());  
            return false;
        }
    }

    public boolean creaTabellaGita() {
        String sql = "CREATE TABLE IF NOT EXISTS gita (\n"  
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
                + " destinazione TEXT NOT NULL,\n"  
                + " data TEXT NOT NULL\n"  
                + ");";  

        try (Connection conn = DriverManager.getConnection(url);  
             Statement stmt = conn.createStatement()) {  
             
            stmt.execute(sql);  
            System.out.println("Tabella 'gita' creata con successo!");
            return true;
            
        } catch (SQLException e) {  
            System.out.println("Errore in creaTabellaGita: " + e.getMessage());  
            return false;
        }
    }

    public boolean creaTabellaStudenti() {
        String sql = "CREATE TABLE IF NOT EXISTS studenti (\n"  
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"  
                + " nome TEXT NOT NULL,\n"  
                + " cognome TEXT NOT NULL,\n"
                + " id_classe INTEGER,\n" // Questa è la colonna per la chiave esterna
                + " FOREIGN KEY (id_classe) REFERENCES classe(id)\n" // Collega lo studente alla sua classe
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

    public boolean creaTabellaPartecipazioni() {
        // Tabella ponte per collegare gli studenti alle gite a cui partecipano
        String sql = "CREATE TABLE IF NOT EXISTS partecipazioni (\n"  
                + " id_studente INTEGER,\n"  
                + " id_gita INTEGER,\n"  
                + " PRIMARY KEY (id_studente, id_gita),\n" // Uno studente non può iscriversi due volte alla stessa gita
                + " FOREIGN KEY (id_studente) REFERENCES studenti(id),\n" // Collega allo studente
                + " FOREIGN KEY (id_gita) REFERENCES gita(id)\n" // Collega alla gita
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
}