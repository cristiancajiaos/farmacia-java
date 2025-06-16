package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import java.util.Date;

public class PurchasesDAO {

    // Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    // Registrar compra
    public boolean registerPurchaseQuery(int supplier_id, int employee_id, double total) {
        String query = "INSERT INTO purchases (supplier_id, employee_id, total, "
                + "created) "
                + "VALUES(?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, supplier_id);
            pst.setInt(2, employee_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la compra: " + e);
            return false;
        }
    }
    
    // Registrar detalles de la compra 
    public boolean registerPurchaseDetailQuery(int purchase_id, 
            double purchase_price, int purchase_amount, 
            double purchase_subtotal, int product_id) {
        String query = "INSERT INTO purchase_detail (purchase_id, purchase_price, "
                     + "purchase_amount, purchase_subtotal, purchase_date, "
                     + "product_id) VALUES (?, ?, ?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, purchase_id);
            pst.setDouble(2, purchase_price);
            pst.setInt(3, purchase_amount);
            pst.setDouble(4, purchase_subtotal);
            pst.setTimestamp(5, datetime);
            pst.setInt(6, product_id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al registrar el detalle de la compra");
            return false;
        }
    }
    
    // Obtener ID de la compra 
    public int purchaseId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id from purchases";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al obtener el ID de la compra: " + e.getMessage());
        }
        return id;
    }

}
