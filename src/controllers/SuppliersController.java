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
import models.Categories;
import models.DynamicComboBox;
import static models.EmployeesDAO.rol_user;
import models.Suppliers;
import models.SuppliersDAO;
import views.SystemView;

public class SuppliersController implements ActionListener, MouseListener, KeyListener {

    private Suppliers supplier;
    private SuppliersDAO supplierDAO;
    private SystemView views;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();

    public SuppliersController(Suppliers supplier, SuppliersDAO supplierDAO, SystemView views) {
        this.supplier = supplier;
        this.supplierDAO = supplierDAO;
        this.views = views;

        // Pestaña de Proveedores
        // Botón Registrar (Proveedor)
        this.views.btn_register_supplier.addActionListener(this);
        // Botón Modificar (Proveedor)
        this.views.btn_update_supplier.addActionListener(this);
        // Botón Eliminar (Proveedor)
        this.views.btn_delete_supplier.addActionListener(this);
        // Botón Cancelar (Operaciones sobre un proveedor)
        this.views.btn_cancel_supplier.addActionListener(this);

        // Tabla de proveedores
        this.views.suppliers_table.addMouseListener(this);
        // Panel de proveedores en menú lateral
        this.views.jPanelSuppliers.addMouseListener(this);
        // Campo de búsqueda de proveedores

        this.views.txt_search_supplier.addKeyListener(this);

        getSuppliersName();
    }

    // Función actionPerformed de ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_supplier) {
            registerSupplier();
        } else if (e.getSource() == views.btn_update_supplier) {
            updateSupplier();
        } else if (e.getSource() == views.btn_delete_supplier) {
            deleteSupplier();
        } else if (e.getSource() == views.btn_cancel_supplier) {
            cancelOperationsSupplier();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.suppliers_table) {
            showSupplierInfo(e);
        } else if (e.getSource() == views.jPanelSuppliers) {
            goToSuppliersTab();
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
        if (e.getSource() == views.txt_search_supplier) {
            filterSuppliersByInput();
        }
    }

    // Funciones generales
    // Listar todos los proveedores
    public void listAllSuppliers() {
        // Se listan todos los proveedores solo si el rol es administrador
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

    // Limpiar campos de texto en la pestaña Proveedores
    public void cleanFields() {
        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEnabled(true);
        views.txt_supplier_id.setEditable(false);
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);
    }

    // Limpiar tabla de pestaña Proveedores
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Mostrar el nombre de las categorías en el campo de Categorías en la pestaña de Compras
    public void getSuppliersName() {
        List<Suppliers> list = supplierDAO.listSuppliersQuery(views.txt_search_supplier.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_purchase_supplier.addItem(new DynamicComboBox(id, name));
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Pestaña Proveedores, botón Registrar: Registrar proveedor
    public void registerSupplier() {
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
                cleanFields();
                listAllSuppliers();
                JOptionPane.showMessageDialog(null, "El proveedor ha sido registrado con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el proveedor");
            }
        }
    }

    // Pestaña Proveedores, botón Modificar: Modificar proveedor
    public void updateSupplier() {
        if (views.txt_supplier_id.equals("")) {
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor en la tabla para editarlo");
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
                    JOptionPane.showMessageDialog(null, "Los datos del proveedor modificados con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar el proveedor");
                }
            }
        }
    }

    // Pestaña Proveedores, botón Eliminar: Eliminar proveedor
    public void deleteSupplier() {
        int row = views.suppliers_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No hay ningún proveedor seleccionado. Seleccione un proveedor de la tabla.");
        } else {
            int id = Integer.parseInt(views.suppliers_table.getValueAt(row, 0).toString());
            int question = JOptionPane.showConfirmDialog(null, "¿Confirma que desea eliminar a este proveedor?");
            if (question == 0 && supplierDAO.deleteSupplierQuery(id)) {
                cleanTable();
                cleanFields();
                listAllSuppliers();
                JOptionPane.showMessageDialog(null, "Proveedor eliminado con éxito");
            }
        }
    }

    // Pestaña Proveedores, botón Cancelar: Cancelar operaciones sobre el proveedor seleccionado
    public void cancelOperationsSupplier() {
        cleanFields();
        views.btn_register_supplier.setEnabled(true);
        views.suppliers_table.clearSelection();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Tabla de Proveedores: Mostrar información de proveedor al hacer click en una fila de la tabla
    public void showSupplierInfo(MouseEvent e) {
        // Obtener la fila en la que se hizo click
        int row = views.suppliers_table.rowAtPoint(e.getPoint());

        // Llenar los campos de la pestaña Proveedores en base a la fila seleccionada
        views.txt_supplier_id.setText(views.suppliers_table.getValueAt(row, 0).toString());
        views.txt_supplier_name.setText(views.suppliers_table.getValueAt(row, 1).toString());
        views.txt_supplier_description.setText(views.suppliers_table.getValueAt(row, 2).toString());
        views.txt_supplier_address.setText(views.suppliers_table.getValueAt(row, 3).toString());
        views.txt_supplier_telephone.setText(views.suppliers_table.getValueAt(row, 4).toString());
        views.txt_supplier_email.setText(views.suppliers_table.getValueAt(row, 5).toString());
        views.cmb_supplier_city.setSelectedItem(views.suppliers_table.getValueAt(row, 6).toString());

        views.txt_supplier_id.setEnabled(true);
        views.txt_supplier_id.setEditable(false);
        
        // Deshabilitar campos y botones en la pestaña Proveedores
        views.btn_register_supplier.setEnabled(false);
        views.txt_supplier_id.setEditable(false);
    }

    // Panel de Proveedores en menú lateral: Ir a la pestaña de Proveedores
    public void goToSuppliersTab() {
        // Si el rol es administrador
        if (rol.equals("Administrador")) {
            // Setear pestaña de Proveedores
            views.jTabbedPane1.setSelectedIndex(5);
            cleanTable();
            cleanFields();
            listAllSuppliers();
        } else {
            // Si no lo es, deshabilitar la pestaña de Proveedores y el panel de Proveedores en el menú lateral
            views.jTabbedPane1.setEnabledAt(5, false);
            views.jLabelSuppliers.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta pestaña");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de búsqueda de Proveedores: Filtrar proveedores por campo de texto
    public void filterSuppliersByInput() {
        cleanTable();
        listAllSuppliers();
    }

}
