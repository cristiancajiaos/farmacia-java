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
import models.CategoriesDAO;
import models.DynamicComboBox;
import static models.EmployeesDAO.rol_user;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import views.SystemView;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDAO categoryDAO;
    private SystemView views;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDAO categoryDAO, SystemView views) {
        this.category = category;
        this.categoryDAO = categoryDAO;
        this.views = views;

        // Pestaña de Categorías
        // Botón Registrar (Categoría)
        this.views.btn_register_category.addActionListener(this);
        // Botón Modificar (Categoría)
        this.views.btn_update_category.addActionListener(this);
        // Botón Eliminar (Categoría)
        this.views.btn_delete_category.addActionListener(this);
        // Botón Cancelar (Operaciones sobre una categoría)
        this.views.btn_cancel_category.addActionListener(this);

        // Tabla de Categorías
        this.views.categories_table.addMouseListener(this);
        // Panel de Categorías en el menú lateral
        this.views.jPanelCategories.addMouseListener(this);

        // Campo de búsqueda de categorías
        this.views.txt_search_category.addKeyListener(this);

        getCategoryName();

        // Se agrega librería de Swing para el select/combobox Categorías de la pestaña productos
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    // Función actionPerformed de ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            registerCategory();
        } else if (e.getSource() == views.btn_update_category) {
            updateCategory();
        } else if (e.getSource() == views.btn_delete_category) {
            deleteCategory();
        } else if (e.getSource() == views.btn_cancel_category) {
            cancelOperationsCategory();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.categories_table) {
            showCategoryInfo(e);
        } else if (e.getSource() == views.jPanelCategories) {
            goToCategoriesTab();
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
        if (e.getSource() == views.txt_search_category) {
            filterCategoriesByInput();
        }
    }

    // Funciones generales
    // Listar todas las categorías
    public void listAllCategories() {
        // Se listan todas las categorías solo si el rol es administrador
        if (rol.equals("Administrador")) {
            List<Categories> list = categoryDAO.listCategoriesQuery(views.txt_search_category.getText());
            model = (DefaultTableModel) views.categories_table.getModel();
            Object[] row = new Object[2];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                model.addRow(row);
            }
            views.categories_table.setModel(model);
        }
    }

    // Limpiar campos de texto en la pestaña Categorías
    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_id.setEditable(true);
        views.txt_category_name.setText("");
    }

    // Limpiar tabla de pestaña Categorías
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Limpiar el campo de Categorías en la pestaña de Productos
    public void clearCategories() {
        views.cmb_product_category.removeAllItems();
    }

    // Mostrar el nombre de las categorías en el campo de Categorías en la pestaña de Productos
    public void getCategoryName() {
        List<Categories> list = categoryDAO.listCategoriesQuery(views.txt_search_category.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_product_category.addItem(new DynamicComboBox(id, name));
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Pestaña Categorías, botón Registrar: Registrar categoría
    public void registerCategory() {
        if (views.txt_category_name.getText().equals("")) {
            JOptionPane.showMessageDialog(views, "El campo de nombre es obligatorio");
        } else {
            category.setName(views.txt_category_name.getText().trim());
            if (categoryDAO.registerCategoryQuery(category)) {
                cleanTable();
                cleanFields();
                listAllCategories();
                clearCategories();
                getCategoryName();
                JOptionPane.showMessageDialog(null, "La categoría ha sido registrada con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoría");
            }
        }
    }

    // Pestaña Categorías, botón Modificar: Modificar categoría
    public void updateCategory() {
        if (views.txt_category_id.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No hay ninguna categoría seleccionada. Seleccione una categoría de la tabla.");
        } else {
            if (views.txt_category_id.getText().equals("")
                    || views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                category.setId(Integer.parseInt(views.txt_category_id.getText()));
                category.setName(views.txt_category_name.getText());
                if (categoryDAO.updateCategoryQuery(category)) {
                    cleanTable();
                    cleanFields();
                    views.btn_register_category.setEnabled(true);
                    listAllCategories();
                    clearCategories();
                    getCategoryName();
                    JOptionPane.showMessageDialog(null, "Los datos de la categoría se han modificado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Hubo un error al intentar modificar la categoría");
                }
            }
        }
    }

    // Pestaña Categorías, botón Eliminar: Eliminar categoría
    public void deleteCategory() {
        int row = views.categories_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(views, "No hay ninguna categoría seleccionada. Seleccione una categoría de la tabla.");
        } else {
            int id = Integer.parseInt(views.categories_table.getValueAt(row, 0).toString());
            int question = JOptionPane.showConfirmDialog(null, "¿Confirma que desea eliminar esta categoría?");
            if (question == 0 && categoryDAO.deleteCategoryQuery(id)) {
                cleanTable();
                cleanFields();
                views.btn_register_category.setEnabled(true);
                listAllCategories();
                clearCategories();
                getCategoryName();
                JOptionPane.showMessageDialog(null, "La categoría se ha eliminado con éxito");
            }
        }
    }

    // Pestaña Categorías, botón Cancelar: Cancelar operaciones sobre la categoría seleccionada
    public void cancelOperationsCategory() {
        cleanFields();
        views.btn_register_category.setEnabled(true);
        views.categories_table.clearSelection();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Tabla de Categorías: Mostrar información de categoría al hacer click en una fila de la tabla
    public void showCategoryInfo(MouseEvent e) {
        // Obtener la fila en la que se hizo click
        int row = views.categories_table.rowAtPoint(e.getPoint());

        // Llenar los campos de la pestaña Categorías en base a la fila seleccionada
        views.txt_category_id.setText(views.categories_table.getValueAt(row, 0).toString());
        views.txt_category_name.setText(views.categories_table.getValueAt(row, 1).toString());

        // Deshabilitar botones en la pestaña Categorías 
        views.btn_register_category.setEnabled(false);
    }

    // Panel de Categorías en menú lateral: Ir a la pestaña de Categorías
    public void goToCategoriesTab() {
        // Si el rol es administrador
        if (rol.equals("Administrador")) {
            // Setear pestaña de Categorías
            views.jTabbedPane1.setSelectedIndex(5);
            cleanTable();
            cleanFields();
            listAllCategories();
        } else {
            // Si no lo es, deshabilitar la pestaña de Categorías y el panel de Categorías en el menú lateral
            views.jTabbedPane1.setEnabledAt(5, false);
            views.jLabelCategories.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta vista");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de búsqueda de Categorías: Filtrar categorías por campo de texto
    public void filterCategoriesByInput() {
        cleanTable();
        listAllCategories();
    }

}
