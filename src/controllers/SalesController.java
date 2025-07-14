package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static models.EmployeesDAO.rol_user;
import models.Products;
import models.ProductsDAO;
import models.Sales;
import models.SalesDAO;
import views.SystemView;

public class SalesController implements ActionListener, MouseListener, KeyListener {

    private Sales sale;
    private SalesDAO saleDAO;
    private SystemView views;

    // Instanciado del modelo Productos
    Products product = new Products();
    ProductsDAO productDAO = new ProductsDAO();

    // Item
    private int item = 0;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp = new DefaultTableModel();

    public SalesController(Sales sale, SalesDAO saleDAO, SystemView views) {
        this.sale = sale;
        this.saleDAO = saleDAO;
        this.views = views;

        // Pestaña de ventas
        // Botón Agregar (Producto a la venta)
        this.views.btn_add_product_sale.addActionListener(this);
        // Botón Vender
        this.views.btn_confirm_sale.addActionListener(this);
        // Botón Eliminar (Producto de la venta)
        this.views.btn_remove_sale.addActionListener(this);
        // Botón Nuevo (Nueva venta)
        this.views.btn_new_sale.addActionListener(this);

        // Panel de ventas en menú lateral 
        this.views.jPanelSales.addMouseListener(this);

        // Campo de código de compra
        this.views.txt_sale_product_code.addKeyListener(this);
    }

    /* Nota: Para facilitar la lectura de las funcionalidades por botón o input,
             estas se han separado en sus propias funciones.
             Al mostrar las funciones implementadas se muestran las 
             invocaciones, mientras que más abajo se muestran sus
             correspondientes implementaciones */
    // Función actionPerformed de ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_add_product_sale) {
            // Añadir producto a la venta
            addProductToSale();
        } else if (e.getSource() == views.btn_confirm_sale) {
            // Confirmar la venta 
            insertSale();
        } else if (e.getSource() == views.btn_remove_sale) {
            // Remover producto de la venta
            removeProductInCurrentSale();
        } else if (e.getSource() == views.btn_new_sale) {
            // Iniciar una nueva venta
            newSale();
        }

    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.jPanelSales) {
            goToSalesTab();
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
        if (e.getSource() == views.txt_sale_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // Setear producto por el código ingresado
                setProductToSaleByCode();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // Funciones generales
    public void listAllSales() {
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Botón Agregar: Agregar producto a la venta
    public void addProductToSale() {
        JOptionPane.showMessageDialog(null, "views.btn_add_product_sale presionado");
    }

    // Botón Vender: Ingresar venta
    public void insertSale() {
        JOptionPane.showMessageDialog(null, "views.btn_confirm_sale presionado");
    }

    // Botón Eliminar: Eliminar producto actual en la venta
    public void removeProductInCurrentSale() {
        JOptionPane.showMessageDialog(null, "views.btn_remove_sale presionado");
    }

    // Botón Nuevo: Nueva venta
    public void newSale() {
        JOptionPane.showMessageDialog(null, "views.btn_new_sale presionado");
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Panel de Ventas en menú lateral: Ir a la pestaña de Ventas
    public void goToSalesTab() {
        if (rol.equals("Administrador")) {
            views.jTabbedPane1.setSelectedIndex(2);
            // Limpiar tabla
            // Limpiar campos de venta
        } else {
            views.jTabbedPane1.setEnabledAt(2, false);
            views.jLabelSales.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta pestaña");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de código del producto + tecla ENTER: Ingresar automáticamente producto por su código
    public void setProductToSaleByCode() {
        JOptionPane.showMessageDialog(null, "views.txt_sale_product_code, presionada la tecla ENTER");
    }

}
