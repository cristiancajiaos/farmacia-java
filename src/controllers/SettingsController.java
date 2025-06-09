package controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import views.SystemView;

public class SettingsController implements MouseListener {

    private SystemView views;

    public SettingsController(SystemView views) {
        this.views = views;
        this.views.jPanelProducts.addMouseListener(this);
        this.views.jPanelPurchases.addMouseListener(this);
        this.views.jPanelCustomers.addMouseListener(this);
        this.views.jPanelEmployees.addMouseListener(this);
        this.views.jPanelSuppliers.addMouseListener(this);
        this.views.jPanelCategories.addMouseListener(this);
        this.views.jPanelReports.addMouseListener(this);
        this.views.jPanelSettings.addMouseListener(this);
    }

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
        if (e.getSource() == this.views.jPanelProducts) {
            this.views.jPanelProducts.setBackground(new Color(152, 202, 63));
        } else if (e.getSource() == this.views.jPanelPurchases) {
            this.views.jPanelPurchases.setBackground(new Color(152, 202, 63));
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

}
