package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Categories;
import models.CategoriesDAO;
import static models.EmployeesDAO.rol_user;
import views.SystemView;

public class CategoriesController implements ActionListener {

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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(views, "El campo de nombre es obligatorio");
            } else {
                category.setName(views.txt_category_name.getText().trim());
                if (categoryDAO.registerCategoryQuery(category)) {
                    JOptionPane.showMessageDialog(null, "Categoría registrada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoría");
                }
            }
        }

    }
    
    // Listar todas las categorías
    public void listAllCategories() {
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
