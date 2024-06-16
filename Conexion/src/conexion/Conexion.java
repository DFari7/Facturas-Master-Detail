package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    Connection con;
    String ur = "jdbc:mysql://localhost:3306/comercial2";
    String uss="root";
    String pas="root1234";

    public Connection establecerConection(){
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(ur,uss,pas);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectarse a la BD "+e);
        }
        return con;
    }
    /*public static void main(String[] args) {
        Conexion db = new Conexion();
        db.establecerConection();
    }*/
    
}
