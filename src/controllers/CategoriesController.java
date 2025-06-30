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
import static models.EmployeesDAO.rol_user;
import views.SystemView;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDAO categoryDAO;
    private SystemView views;

    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDAO categoryDAO, SystemView views) {
        this.category = category;
        this.categoryDAO = categoryDAO;
        this.views = views;

        // Botón de registrar categoría
        this.views.btn_register_category.addActionListener(this);
        // Botón de modificar categoría
        this.views.btn_update_category.addActionListener(this);
        // Botón de eliminar categoría
        this.views.btn_delete_category.addActionListener(this);
        // Botón de cancelar categoría
        this.views.btn_cancel_category.addActionListener(this);
        // Tabla de categorías
        this.views.categories_table.addMouseListener(this);
        // Campo de búsqueda de categorías
        this.views.txt_search_category.addKeyListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(views, "El campo de nombre es obligatorio");
            } else {
                category.setName(views.txt_category_name.getText().trim());
                if (categoryDAO.registerCategoryQuery(category)) {
                    cleanTable();
                    cleanFields();
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "Categoría registrada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoría");
                }
            }
        } else if (e.getSource() == views.btn_update_category) {
            if (views.txt_category_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
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
                        JOptionPane.showMessageDialog(null, "Los datos de la categoría se han modificado exitosamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "Hubo un error al intentar modificar la categoría");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_category) {
            int row = views.categories_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(views, "seleccione una fila de la tabla para continuar");
            } else{
                int id = Integer.parseInt(views.categories_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En verdad desea eliminar esta categoría?");
                if (question == 0 && categoryDAO.deleteCategoryQuery(id)) {
                    cleanTable();
                    cleanFields();
                    views.btn_register_category.setEnabled(true);
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "La categoría se ha eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al intentar eliminar la categoría");
                }
            }
        } else if (e.getSource() == views.btn_cancel_category) {
            cleanFields();
            views.btn_register_category.setEnabled(true);
        }

    }

    // Listar todas las categorías
    public void listAllCategories() {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.categories_table) {
            int row = views.categories_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.categories_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.categories_table.getValueAt(row, 1).toString());
            views.btn_register_category.setEnabled(false);
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
        if (e.getSource() == views.txt_search_category) {
            // Limpiar tabla
            cleanTable();
            // Listar todas las categorías 
            listAllCategories();
        }
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    
    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_id.setEditable(true);
        views.txt_category_name.setText("");
    }

}
