package controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import static models.EmployeesDAO.rol_user;
import models.Products;
import models.ProductsDAO;
import models.Purchases;
import models.PurchasesDAO;
import views.SystemView;

public class PurchasesController implements KeyListener {
    
    private Purchases purchase;
    private PurchasesDAO purchaseDAO;
    private SystemView views;
    
    // Instanciar el modelo productos
    Products product = new Products();
    ProductsDAO productDAO = new ProductsDAO();
    
    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDAO purchaseDAO, SystemView views) {
        this.purchase = purchase;
        this.purchaseDAO = purchaseDAO;
        this.views = views;
        
        // Campo de código de compra
        this.views.txt_purchase_product_code.addKeyListener(this);
        // Campo de precio de venta de compra
        this.views.txt_purchase_price.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (views.txt_purchase_product_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Ingresa el código del producto a comprar");
                } else {
                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                    product = productDAO.searchCode(id);
                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_price) {
            int quantity;
            Double price = 0.0;
            
            if (views.txt_purchase_amount.getText().equals("")) {
                quantity = 1;
                views.txt_purchase_price.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText("" + (quantity * price));
            }
        }
        
    }
    
    
    
}
