/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abenantelutzengitasql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author lutzen.jacopo
 */
public class GestioneDatabase {
 private Connection conn; // Dichiarata fuori per usarla in altri metodi

    public GestioneDatabase() {
        // Sostituisci con il nome reale del tuo database
        String url = "jdbc:sqlite:SQLite?foreign_keys=true";
        
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connessione stabilita e Foreign Keys attive!");
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }
    
}
