package models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomersDAO {

    // Instanciar la conexión
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    // Registrar cliente 
    public boolean registerCustomerQuery(Customers customer) {
        String query = "INSERT INTO customers(id, full_name, address, "
                + "telephone, email, created, updated) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer.getId());
            pst.setString(2, customer.getFull_name());
            pst.setString(3, customer.getAddress());
            pst.setString(4, customer.getTelephone());
            pst.setString(5, customer.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un "
                    + "error al registrar al cliente: " + e.getMessage());
            System.err.println("Ha ocurrido un error al registrar al "
                    + "cliente:" + e.getMessage());
            return false;
        }
    }

    // Listar clientes
    public List listCustomersQuery(String value) {
        List<Customers> list_customers = new ArrayList();
        String query = "SELECT * FROM customers";
        String query_search_customer = "SELECT * FROM customers "
                + "WHERE id LIKE '%" + value + "%'";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(value.equalsIgnoreCase("") ? query : query_search_customer);
            rs = pst.executeQuery();
            while (rs.next()) {
                Customers customer = new Customers();
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                list_customers.add(customer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido "
                    + "un error al obtener la lista de "
                    + "clientes: " + e.getMessage());
            System.err.println("Ha ocurrido un error al obtener la lista de "
                    + "clientes:" + e.getMessage());
        }
        return list_customers;
    }
    
    // Buscar cliente por ID
    public Customers searchCustomerName(int id) {
        /* Nota: Este método se creó para complementar la funcionalidad
           del controlador de ventas, buscando cliente por ID,
           y obteniendo tanto la ID como el nombre */
        String query = "SELECT cus.id, cus.full_name FROM customers cus "
                + "WHERE id = ?";
        Customers customer = new Customers();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al "
                    + "buscar el cliente: " + e.getMessage());
            System.err.println("Hubo un error al buscar el "
                    + "cliente: " + e.getMessage());
        }
        return customer;
    }

    // Modificar cliente
    public boolean updateCustomerQuery(Customers customer) {
        String query = "UPDATE customers SET full_name = ?, address = ?, "
                + "telephone = ?, email = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());

        System.out.println("Update customer query");
        System.out.println(customer.getId());
        System.out.println(customer.getFull_name());
        System.out.println(customer.getAddress());
        System.out.println(customer.getTelephone());
        System.out.println(customer.getEmail());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, customer.getFull_name());
            pst.setString(2, customer.getAddress());
            pst.setString(3, customer.getTelephone());
            pst.setString(4, customer.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customer.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido "
                    + "un error al modificar los datos del "
                    + "cliente:" + e.getMessage());
            System.err.println("Ha ocurrido un error al modificar los datos "
                    + "del cliente:" + e.getMessage());
            return false;
        }
    }

    // Eliminar cliente
    public boolean deleteCustomerQuery(int id) {
        String query = "DELETE FROM customers WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede eliminar "
                    + "un cliente que tenga relación con otra "
                    + "tabla: " + e.getMessage());
            System.err.println("No puede eliminar un cliente que tenga relación "
                    + "con otra tabla: " + e.getMessage());
            return false;
        }
    }

}
