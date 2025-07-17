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
import models.DynamicComboBox;
import static models.EmployeesDAO.rol_user;
import models.Products;
import models.ProductsDAO;
import views.SystemView;

public class ProductsController implements ActionListener, MouseListener, KeyListener {

    private Products product;
    private ProductsDAO productDAO;
    private SystemView views;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();

    public ProductsController(Products product, ProductsDAO productDAO, SystemView views) {
        this.product = product;
        this.productDAO = productDAO;
        this.views = views;

        // Pestaña de Productos
        // Botón Registrar (Producto)
        this.views.btn_register_product.addActionListener(this);
        // Botón Modificar (Producto) 
        this.views.btn_update_product.addActionListener(this);
        // Botón Eliminar (Producto)
        this.views.btn_delete_product.addActionListener(this);
        // Botón Cancelar (Producto)
        this.views.btn_cancel_product.addActionListener(this);

        // Tabla de Productos
        this.views.products_table.addMouseListener(this);
        // Panel de Productos en el menú lateral
        this.views.jPanelProducts.addMouseListener(this);

        // Campo de búsqueda de productos
        this.views.txt_search_product.addKeyListener(this);
    }

    // Función actionPerformed de ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_product) {
            registerProduct();
        } else if (e.getSource() == views.btn_update_product) {
            updateProduct();
        } else if (e.getSource() == views.btn_delete_product) {
            deleteProduct();
        } else if (e.getSource() == views.btn_cancel_product) {
            cancelOperationsProduct();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.products_table) {
            showProductInfo(e);
        } else if (e.getSource() == views.jPanelProducts) {
            goToProductsTab();
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
        if (e.getSource() == views.txt_search_product) {
            filterProductsByInput();
        }
    }

    // Funciones generales
    // Listar todos los productos
    public void listAllProducts() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Products> list = productDAO.listProductsQuery(views.txt_search_product.getText());
            model = (DefaultTableModel) views.products_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnit_price();
                row[5] = list.get(i).getProduct_quantity();
                row[6] = list.get(i).getCategory_name();
                model.addRow(row);
            }
            views.products_table.setModel(model);

            if (rol.equals("Auxiliar")) {
                views.btn_register_product.setEnabled(false);
                views.btn_update_product.setEnabled(false);
                views.btn_delete_product.setEnabled(false);
                views.btn_cancel_product.setEnabled(false);
                views.txt_product_code.setEditable(false);
                views.txt_product_description.setEditable(false);
                views.txt_product_name.setEditable(false);
                views.txt_product_unit_price.setEditable(false);
                views.txt_product_id.setEditable(false);
                views.cmb_product_category.setEditable(false);
            }
        }
    }

    // Limpiar campos de texto y select/combobox en la pestaña Productos
    public void cleanFields() {
        views.txt_product_code.setText("");
        views.txt_product_name.setText("");
        views.txt_product_unit_price.setText("");
        views.txt_product_description.setText("");
        views.txt_product_id.setText("");
        views.txt_product_id.setEnabled(true);
        views.txt_product_id.setEditable(false);
        views.cmb_product_category.setSelectedIndex(0);
        views.btn_register_product.setEnabled(true);
    }

    // Limpiar tabla de pestaña Productos
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Pestaña Productos, botón Registrar: Registrar producto
    public void registerProduct() {
        if (views.txt_product_code.getText().equals("")
                || views.txt_product_name.getText().equals("")
                || views.txt_product_description.getText().equals("")
                || views.txt_product_unit_price.getText().equals("")
                || views.cmb_product_category.getSelectedItem().toString().equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            product.setCode(Integer.parseInt(views.txt_product_code.getText()));
            product.setName(views.txt_product_name.getText().trim());
            product.setDescription(views.txt_product_description.getText().trim());
            product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
            DynamicComboBox category_id = (DynamicComboBox) views.cmb_product_category.getSelectedItem();
            product.setCategory_id(category_id.getId());
            if (productDAO.registerProductQuery(product)) {
                cleanTable();
                cleanFields();
                listAllProducts();
                JOptionPane.showMessageDialog(null, "El producto se ha registrado con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el producto");
            }
        }
    }

    // Pestaña Productos, botón Modificar: Modificar producto
    public void updateProduct() {
        if (views.txt_product_id.equals("")) {
            JOptionPane.showMessageDialog(null, "No hay ningún producto de la tabla seleccionado. Seleccione un producto de la tabla.");
        } else {
            if (views.txt_product_code.getText().equals("")
                    || views.txt_product_name.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("")
                    || views.txt_product_description.getText().equals("")
                    || views.txt_product_id.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setName(views.txt_product_name.getText().trim());
                product.setDescription(views.txt_product_description.getText().trim());
                product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
                // Obtener el ID de la categoría
                DynamicComboBox category_id = (DynamicComboBox) views.cmb_product_category.getSelectedItem();
                product.setCategory_id(category_id.getId());
                // Pasar ID al método
                product.setId(Integer.parseInt(views.txt_product_id.getText()));
                if (productDAO.updateProductQuery(product)) {
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Los datos del producto se han modificado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha habido un error al modificar los datos del producto");
                }
            }
        }
    }

    // Pestaña Productos, botón Eliminar: Eliminar producto
    public void deleteProduct() {
        int row = views.products_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No hay ningún producto de la tabla seleccionado. Seleccione un producto de la tabla.");
        } else {
            int id = Integer.parseInt(views.products_table.getValueAt(row, 0).toString());
            int question = JOptionPane.showConfirmDialog(null, "¿Confirma que desea eliminar este producto");
            if (question == 0 && productDAO.deleteProductQuery(id)) {
                cleanTable();
                cleanFields();
                views.btn_register_product.setEnabled(true);
                listAllProducts();
                JOptionPane.showMessageDialog(null, "El producto ha sido eliminado con éxito");
            }
        }
    }

    // Pestaña Productos, botón Cancelar: Cancelar operaciones sobre el producto seleccionado
    public void cancelOperationsProduct() {
        cleanFields();
        views.btn_register_product.setEnabled(true);
        views.products_table.clearSelection();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Tabla de Productos: Mostrar información de producto al hacer click en una fila de la tabla
    public void showProductInfo(MouseEvent e) {
        // Obtener la fila en la que se hizo click
        int row = views.products_table.rowAtPoint(e.getPoint());

        // Llenar los campos de la pestaña Productos en base a la fila seleccionada
        views.txt_product_id.setText(views.products_table.getValueAt(row, 0).toString());
        product = productDAO.searchProduct(Integer.parseInt(views.txt_product_id.getText()));
        views.txt_product_code.setText("" + product.getCode());
        views.txt_product_name.setText(product.getName());
        views.txt_product_description.setText(product.getDescription());
        views.txt_product_unit_price.setText("" + product.getUnit_price());
        views.cmb_product_category.setSelectedItem(new DynamicComboBox(product.getCategory_id(), product.getCategory_name()));

        views.txt_product_id.setEnabled(true);
        views.txt_product_id.setEditable(false);
        
        // Deshabilitar botones en la pestaña Productos 
        views.btn_register_product.setEnabled(false);
    }

    // Panel de Productos en menú lateral: Ir a la pestaña de Productos
    public void goToProductsTab() {
        // Setear pestaña de Productos
        views.jTabbedPane1.setSelectedIndex(0);
        cleanTable();
        cleanFields();
        listAllProducts();
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de búsqueda de Productos: Filtrar productos por campo de texto
    public void filterProductsByInput() {
        cleanTable();
        listAllProducts();
    }

}
