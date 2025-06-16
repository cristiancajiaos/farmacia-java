package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;

public class SalesDAO {
    
    // Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    // Registrar venta
    public boolean registerSaleQuery(int customer_id, int employee_id, double total) {
        String query = "INSERT INTO sales (customer_id, employee_id, total, "
                 + "sale_date) VALUES(?, ?, ?, ?);";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al registrar la venta: " + e.getMessage());
            System.err.println("Hubo un error al registrar la venta: " + e.getMessage());
            return false;
        }
    }
    
    
}
