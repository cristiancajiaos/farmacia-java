package controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static models.EmployeesDAO.address_user;
import static models.EmployeesDAO.email_user;
import static models.EmployeesDAO.full_name_user;
import static models.EmployeesDAO.id_user;
import static models.EmployeesDAO.telephone_user;
import views.SystemView;

public class SettingsController implements MouseListener {

    private SystemView views;

    public SettingsController(SystemView views) {
        this.views = views;
        
        // Menú lateral del sistema 
        // Panel de Productos
        this.views.jPanelProducts.addMouseListener(this);
        // Panel de Compras
        this.views.jPanelPurchases.addMouseListener(this);
        // Panel de Ventas
        this.views.jPanelSales.addMouseListener(this);
        // Panel de Clientes
        this.views.jPanelCustomers.addMouseListener(this);
        // Panel de Empleados
        this.views.jPanelEmployees.addMouseListener(this);
        // Panel de Proveedores
        this.views.jPanelSuppliers.addMouseListener(this);
        // Panel de Categorías
        this.views.jPanelCategories.addMouseListener(this);
        // Panel de Reportes
        this.views.jPanelReports.addMouseListener(this);
        // Panel de Perfil (de empleado) 
        this.views.jPanelSettings.addMouseListener(this);
        
        profile();
    }
    
    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        /* Se colorea el panel en el menú lateral en el que se ha pasado el cursor del mouse */ 
        if (e.getSource() == this.views.jPanelProducts) {
            this.views.jPanelProducts.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelPurchases) {
            this.views.jPanelPurchases.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelSales){
            this.views.jPanelSales.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelCustomers) {
            this.views.jPanelCustomers.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelEmployees) {
            this.views.jPanelEmployees.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelSuppliers) {
            this.views.jPanelSuppliers.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelCategories) {
            this.views.jPanelCategories.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelReports) {
            this.views.jPanelReports.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelSettings) {
            this.views.jPanelSettings.setBackground(new Color(152, 202, 63));
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == this.views.jPanelProducts) {
            this.views.jPanelProducts.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelPurchases) {
            this.views.jPanelPurchases.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelSales) {
            this.views.jPanelSales.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelCustomers) {
            this.views.jPanelCustomers.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelEmployees) {
            this.views.jPanelEmployees.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelSuppliers) {
            this.views.jPanelSuppliers.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelCategories) {
            this.views.jPanelCategories.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelReports) {
            this.views.jPanelReports.setBackground(new Color(18, 45, 61));
        } else if (e.getSource() == this.views.jPanelSettings) {
            this.views.jPanelSettings.setBackground(new Color(18, 45, 61));
        }

    }
    
    // Funciones generales
    // Setear campos de perfil basados en la información del usuario logueado en el sistema
    public void profile() {
        this.views.txt_id_profile.setText("" + id_user);
        this.views.txt_name_profile.setText(full_name_user);
        this.views.txt_address_profile.setText(address_user);
        this.views.txt_phone_profile.setText(telephone_user);
        this.views.txt_email_profile.setText(email_user);
    }
}
