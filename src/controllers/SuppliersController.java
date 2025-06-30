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
import static models.EmployeesDAO.rol_user;
import models.Suppliers;
import models.SuppliersDAO;
import views.SystemView;

public class SuppliersController implements ActionListener, MouseListener, KeyListener {

    private Suppliers supplier;
    private SuppliersDAO supplierDAO;
    private SystemView views;

    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public SuppliersController(Suppliers supplier, SuppliersDAO supplierDAO, SystemView views) {
        this.supplier = supplier;
        this.supplierDAO = supplierDAO;
        this.views = views;

        // Botón de registrar proveedor
        this.views.btn_register_supplier.addActionListener(this);
        // Botón de modificar proveedor
        this.views.btn_update_supplier.addActionListener(this);
        // Botón de eliminar proveedor
        this.views.btn_delete_supplier.addActionListener(this);
        // Botón de cancelar proveedor
        this.views.btn_cancel_supplier.addActionListener(this);
        // Panel de proveedores en menú lateral
        this.views.jPanelSuppliers.addMouseListener(this);
        // Tabla de proveedores
        this.views.suppliers_table.addMouseListener(this);
        // Campo de búsqueda de proveedores
        this.views.txt_search_supplier.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_supplier) {
            if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                // Realizar la inserción
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                if (supplierDAO.registerSupplierQuery(supplier)) {
                    cleanTable();
                    listAllSuppliers();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el proveedor");
                }
            }
        } else if (e.getSource() == views.btn_update_supplier) {
            if (views.txt_supplier_id.equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor en la tabla para editarlo");
            } else {
                if (views.txt_supplier_name.getText().equals("")
                        || views.txt_supplier_address.getText().equals("")
                        || views.txt_supplier_telephone.getText().equals("")
                        || views.txt_supplier_email.getText().equals("")
                        || views.txt_supplier_description.getText().equals("")
                        || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    supplier.setId(Integer.parseInt(views.txt_supplier_id.getText()));
                    supplier.setName(views.txt_supplier_name.getText().trim());
                    supplier.setAddress(views.txt_supplier_address.getText().trim());
                    supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                    supplier.setEmail(views.txt_supplier_email.getText().trim());
                    supplier.setDescription(views.txt_supplier_description.getText().trim());
                    supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                    if (supplierDAO.updateSupplierQuery(supplier)) {
                        cleanTable();
                        cleanFields();
                        listAllSuppliers();
                        views.btn_register_supplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con éxito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar el proveedor");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_supplier) {
            int row = views.suppliers_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un proveedor para eliminar");
            } else {
                int id = Integer.parseInt(views.suppliers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad desea eliminar a este proveedor?");
                if (question == 0 && supplierDAO.deleteSupplierQuery(id)) {
                    cleanTable();
                    cleanFields();
                    listAllSuppliers();
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con éxito");
                }
            }
        } else if (e.getSource() == views.btn_cancel_supplier) {
            cleanFields();
            views.btn_register_supplier.setEnabled(true);
        }
    }

    // Listar proveedores
    public void listAllSuppliers() {
        if (rol.equals("Administrador")) {
            List<Suppliers> list = supplierDAO.listSuppliersQuery(views.txt_search_supplier.getText());
            model = (DefaultTableModel) views.suppliers_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();
                model.addRow(row);
            }
            views.suppliers_table.setModel(model);

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.suppliers_table) {
            int row = views.suppliers_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.suppliers_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.suppliers_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.suppliers_table.getValueAt(row, 2).toString());
            views.txt_supplier_address.setText(views.suppliers_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.suppliers_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.suppliers_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.employees_table.getValueAt(row, 6).toString());
            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEditable(false);
        } else if (e.getSource() == views.jPanelSuppliers) {
            if (rol.equals("Administrador")) {
                // Setear pestaña de proveedores
                views.jTabbedPane1.setSelectedIndex(4);
                // Limpiar tabla
                cleanTable();
                // Limpiar campos 
                cleanFields();
                // Lista proveedores
                listAllSuppliers();
            } else {
                views.jTabbedPane1.setEnabledAt(4, false);
                views.jLabelSuppliers.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes permisos de administrador para acceder a esta vista");
            }
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_supplier) {
            // Limpiar tabla
            cleanTable();
            // Listar todos los proveedores
            listAllSuppliers();
        }

    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields() {
        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEditable(true);
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);
    }

}
