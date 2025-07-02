package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.DynamicComboBox;
import static models.EmployeesDAO.rol_user;
import models.Products;
import models.ProductsDAO;
import views.SystemView;

public class ProductsController implements ActionListener {
    
    private Products product;
    private ProductsDAO productDAO;
    private SystemView views;
    
    String rol = rol_user;

    public ProductsController(Products product, ProductsDAO productDAO, SystemView views) {
        this.product = product;
        this.productDAO = productDAO;
        this.views = views;
        
        // Botón de registrar producto
        this.views.btn_register_product.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_product) {
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
                    JOptionPane.showMessageDialog(null, "El producto se ha registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el producto");
                }
            }
        }
        
    }
    
    
    
}
