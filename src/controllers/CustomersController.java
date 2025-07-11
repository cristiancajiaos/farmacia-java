package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Customers;
import models.CustomersDAO;
import views.SystemView;

public class CustomersController implements ActionListener, MouseListener, KeyListener {

    private Customers customer;
    private CustomersDAO customerDAO;
    private SystemView views;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();

    public CustomersController(Customers customer, CustomersDAO customerDAO, SystemView views) {
        this.customer = customer;
        this.customerDAO = customerDAO;
        this.views = views;

        // Pestaña de Clientes
        // Botón Registrar (Cliente)
        this.views.btn_register_customer.addActionListener(this);
        // Botón Modificar (Cliente)
        this.views.btn_update_customer.addActionListener(this);
        // Botón Eliminar (Cliente)
        this.views.btn_delete_customer.addActionListener(this);
        // Botón Cancelar (Operaciones sobre un cliente)
        this.views.btn_cancel_customer.addActionListener(this);

        // Tabla de pestaña Clientes
        this.views.customers_table.addMouseListener(this);
        // Panel de Clientes en menú lateral
        this.views.jPanelCustomers.addMouseListener(this);

        // Campo de búsqueda de Clientes
        this.views.txt_search_customer.addKeyListener(this);
    }

    // Función actionPerformed de ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_customer) {
            registerCustomer();
        } else if (e.getSource() == views.btn_update_customer) {
            updateCustomer();
        } else if (e.getSource() == views.btn_delete_customer) {
            deleteCustomer();
        } else if (e.getSource() == views.btn_cancel_customer) {
            cancelOperationsCustomer();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.customers_table) {
            showCustomerInfo(e);
        } else if (e.getSource() == views.jPanelCustomers) {
            goToCustomersTab();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Funciones de KeyListener
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_customer) {
            filterCustomersByInput();
        }
    }

    // Funciones generales
    // Listar todos los clientes
    public void listAllCustomers() {
        List<Customers> list = customerDAO.listCustomersQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
        views.customers_table.setModel(model);
    }

    // Limpiar campos de texto pestaña Clientes
    public void cleanFields() {
        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");
    }

    // Limpiar tabla de pestaña Clientes
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Pestaña Clientes, botón Registrar: Registrar el cliente ingresado
    public void registerCustomer() {
        // Verificar si los campos estan vacíos
        if (views.txt_customer_id.getText().equals("")
                || views.txt_customer_fullname.getText().equals("")
                || views.txt_customer_address.getText().equals("")
                || views.txt_customer_telephone.getText().equals("")
                || views.txt_customer_email.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
            customer.setFull_name(views.txt_customer_fullname.getText().trim());
            customer.setAddress(views.txt_customer_address.getText().trim());
            customer.setTelephone(views.txt_customer_telephone.getText().trim());
            customer.setEmail(views.txt_customer_email.getText().trim());
            if (customerDAO.registerCustomerQuery(customer)) {
                cleanTable();
                cleanFields();
                listAllCustomers();
                JOptionPane.showMessageDialog(null, "Cliente registrado con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el cliente");
            }
        }
    }

    // Pestaña Clientes, botón Modificar: Modificar el cliente seleccionado 
    public void updateCustomer() {
        if (views.txt_customer_id.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
        } else {
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_fullname.getText().equals("")
                    || views.txt_customer_address.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_fullname.getText().trim());
                customer.setAddress(views.txt_customer_address.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());
                System.out.println("Update customer");
                System.out.println(customer.getId());
                System.out.println(customer.getFull_name());
                System.out.println(customer.getAddress());
                System.out.println(customer.getTelephone());
                System.out.println(customer.getEmail());
                if (customerDAO.updateCustomerQuery(customer)) {
                    cleanTable();
                    cleanFields();
                    listAllCustomers();
                    views.btn_register_customer.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Datos del cliente modificados con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actualizar el cliente");

                }
            }
        }
    }

    // Pestaña Clientes, botón Eliminar: Eliminar el cliente seleccionado
    public void deleteCustomer() {
        int row = views.customers_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente para eliminar");
        } else {
            int id = Integer.parseInt(views.customers_table.getValueAt(row, 0).toString());
            int question = JOptionPane.showConfirmDialog(null, "¿En realidad desea eliminar a este cliente?");
            if (question == 0 && customerDAO.deleteCustomerQuery(id) != false) {
                cleanTable();
                cleanFields();
                views.btn_register_customer.setEnabled(true);
                listAllCustomers();
                JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito");
            }
        }
    }

    // Pestaña Clientes, botón Cancelar: Cancelar operaciones sobre el cliente seleccionado
    public void cancelOperationsCustomer() {
        views.btn_register_customer.setEnabled(true);
        cleanFields();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Tabla de Clientes: Mostrar información de cliente al hacer click en una fila de la tabla
    public void showCustomerInfo(MouseEvent e) {
        // Obtener la fila en la que se hizo click
        int row = views.customers_table.rowAtPoint(e.getPoint());

        // Llenar los campos de la pestaña Clientes en base a la fila seleccionada
        views.txt_customer_id.setText(views.customers_table.getValueAt(row, 0).toString());
        views.txt_customer_fullname.setText(views.customers_table.getValueAt(row, 1).toString());
        views.txt_customer_address.setText(views.customers_table.getValueAt(row, 2).toString());
        views.txt_customer_telephone.setText(views.customers_table.getValueAt(row, 3).toString());
        views.txt_customer_email.setText(views.customers_table.getValueAt(row, 4).toString());

        // Deshabilitar campos y botones en la pestaña Categorías 
        views.btn_register_customer.setEnabled(false);
        views.txt_customer_id.setEditable(false);
    }

    // Ir a la pestaña de Clientes al presionar el panel de Clientes en el menú lateral
    public void goToCustomersTab() {
        views.jTabbedPane1.setSelectedIndex(2);
        cleanTable();
        cleanFields();
        listAllCustomers();
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de búsqueda de Clientes: Filtrar clientes
    public void filterCustomersByInput() {
        cleanTable();
        listAllCustomers();
    }

}
