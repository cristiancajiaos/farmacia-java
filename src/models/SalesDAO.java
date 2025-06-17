package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class SalesDAO {

    // Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    // Registrar venta
    public boolean registerSaleQuery(int customer_id, int employee_id,
            double total) {
        String query = "INSERT INTO sales (customer_id, employee_id, total, "
                     + "sale_date) VALUES(?, ?, ?, ?)";
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
            String strError = "Hubo un error al obtener el ID máximo de la "
                    + "venta: " + e.getMessage();
            JOptionPane.showMessageDialog(null, strError);
            System.err.println(strError);
            return false;
        }
    }

    // Registrar detalle de la venta
    public boolean registerSaleDetailQuery(int product_id, int sale_id,
            int sale_quantity, double sale_price, double sale_subtotal) {
        String query = "INSERT INTO sales (product_id, sale_id, sale_quantity, "
                     + "sale_price, sale_subtotal) VALUES (?, ?, ?, ?, ?)";
        /* En la página de la actividad, en este método, se define el atributo
           datetime. Para efectos de este desarrollo, al no usarse en el
           método, se omite. */

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product_id);
            /* En la página de la actividad, en este método, aparece el 
               parámetro sale_id como tipo double. 
               Esto, el el contexto de la inserción en la tabla 
               sale_details, es incorrecto, puesto que la columna del mismo
               nombre en esa tabla, es de tipo int, y es clave foránea (FK)
               que referencia a la columna id de la tabla sales, también de 
               tipo int, clave principal (PK) y autoincremental de dicha tabla. 
               Para efectos de esta actividad, en este método, se cambia el 
               tipo sale_id de double a int, respetando la estructura 
               solicitada de las tablas sale_details y sales en el diagrama E-R
            */
            pst.setInt(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error "
                    + "al registrar el detalle de la venta: " + e.getMessage());
            System.err.println("Hubo un error al registrar el detalle "
                    + "de la venta: " + e.getMessage());
            return false;
        }

    }
    
    // Obtener ID máximo de la venta
    public int saleId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id from sales";
        
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            String strError = "Hubo un error al obtener el ID máximo de la "
                    + "venta: " + e.getMessage();
            JOptionPane.showMessageDialog(null, strError);
            System.err.println(strError);
        }
        return id;
    }
    
    // Listar todas las ventas realizadas
    public List listAllSalesQuery() {
        List<Sales> list_sales = new ArrayList();
        String query = "SELECT s.id AS invoice, c.full_name AS customer, "
                     + "e.full_name AS employee, s.total, s.sale_date "
                     + "FROM sales s "
                     + "INNER JOIN customers c ON s.customer_id = c.id "
                     + "INNER JOIN employees e ON s.employee_id = e.id "
                     + "ORDER BY s.id ASC";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt("invoice"));
                sale.setCustomer_name(rs.getString("customer"));
                sale.setEmployee_name(rs.getString("employee"));
                sale.setTotal_to_pay(rs.getDouble("total"));
                sale.setSale_date(rs.getString("sale_date"));
                list_sales.add(sale);
            }
        } catch (SQLException e) {
            String strError = "Hubo un error al obtener la lista de todas "
                    + "las ventas realizadas" + e.getMessage();
            JOptionPane.showMessageDialog(null, strError);
            System.err.println(strError);
        }
        return list_sales;
    }

}
