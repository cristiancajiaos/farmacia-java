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
    
    // Registrar detalle de la venta
    public boolean registerSaleDetailQuery(int product_id, int sale_id, int sale_quantity, double sale_price, double sale_subtotal) {
        String query = "INSERT INTO sales (product_id, sale_id, sale_quantity, "
                     + "sale_price, sale_subtotal) VALUES (?, ?, ?, ?, ?)";
        /* Timestamp no se requiere para registrar el detalle de la venta */
        // Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product_id);
            pst.setInt(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al registrar el detalle de la venta: " + e.getMessage());
            System.err.println("Hubo un error al registrar el detalle de la venta: " + e.getMessage());
            return false;
        }
        
    }
    
    
}
