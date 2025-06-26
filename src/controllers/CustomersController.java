package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Customers;
import models.CustomersDAO;
import views.SystemView;

public class CustomersController implements ActionListener {

    private Customers customer;
    private CustomersDAO customerDAO;
    private SystemView views;

    public CustomersController(Customers customer, CustomersDAO customerDAO, SystemView views) {
        this.customer = customer;
        this.customerDAO = customerDAO;
        this.views = views;
        // Botón de registrar cliente
        this.views.btn_register_customer.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_customer) {
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
                    JOptionPane.showMessageDialog(null, "Cliente registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el cliente");
                }
            }
        }

    }

}
