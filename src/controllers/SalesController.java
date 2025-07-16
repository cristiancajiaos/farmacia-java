package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Customers;
import models.CustomersDAO;
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
    
    // Instanciado del modelo Customers
    Customers customer = new Customers();
    CustomersDAO customerDAO = new CustomersDAO();

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

        // Campo de código del producto
        this.views.txt_sale_product_code.addKeyListener(this);
        // Campo de cédula/ID del cliente
        this.views.txt_sale_customer_id.addKeyListener(this);
        // Campo de cantidad del producto
        this.views.txt_sale_quantity.addKeyListener(this);
    }

    /* Nota: Para simplificar la legibilidad del controlador, se optó por,
             en lugar de dejar el código en los métodos implementados,
             crear un método individual para cada funcionalidad. 
             Esto es, un método independiente para cada acción de click, 
             mouse, y tecla en cada campo, botón, y tabla. 
             Esto no es exclusivo de esta clase. 
             En todos los controladores del sistema, se optó 
             por este tipo de separación para botones, campos, y tablas. */
    
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
        } else if (e.getSource() == views.txt_sale_customer_id) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                // Setear cliente por el ID ingresado
                setCustomerById();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_sale_quantity) {
            setSubtotal();
        }
    }

    // Funciones generales
    public void listAllSales() {
    }
    
    public void cleanFieldsSales() {
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_stock.setText("");
        views.txt_sale_customer_id.setText("");
        views.txt_sale_customer_name.setText("");
        views.txt_sale_total_to_pay.setText("");
        views.txt_sale_customer_id.setEnabled(false);
        views.txt_sale_customer_id.setEditable(false);
        views.txt_sale_quantity.setEnabled(false);
        views.txt_sale_quantity.setEditable(false);
        views.txt_sale_subtotal.setEnabled(false);
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
        // TODO: Limpiar tabla temporal
        cleanFieldsSales();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Panel de Ventas en menú lateral: Ir a la pestaña de Ventas
    public void goToSalesTab() {
        if (rol.equals("Administrador")) {
            views.jTabbedPane1.setSelectedIndex(2);
            // TODO: Limpiar tabla
            this.cleanFieldsSales();
        } else {
            views.jTabbedPane1.setEnabledAt(2, false);
            views.jLabelSales.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta pestaña");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de código del producto + tecla ENTER: Ingresar automáticamente producto por su código
    public void setProductToSaleByCode() {
        if (views.txt_sale_product_code.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese del código del producto a vender");
        } else {
            /* Nota: El código que se solicitó ingresar, involucra invocar
             el método searchCode de ProductsDAO que solo obtiene el nombre y 
             el ID del producto, pero no la cantidad de producto ni el precio 
             por unidad.
             Para mitigar esto, y para evitar realizar dos consultas a la DB, 
             en la clase ProductsDAO se creó un método nuevo para obtener el id 
             del producto, el nombre del producto, el precio, y la cantidad de 
             producto, todos los cuales se solicita introducir en sus
             respectivos campos.
             Además, se usa código adicional fuera del solicitado 
             para hacer más legibles los textos de los campos. */
            int code = Integer.parseInt(views.txt_sale_product_code.getText());
            Products productSearch = new Products();
            // A partir del código se obtienen los campos restantes
            productSearch = productDAO.searchProductQuantityCode(code);
            if (productSearch.getName() != null) {
                // Se llenan los campos de ID de producto, precio, y nombre de producto 
                views.txt_sale_product_id.setText("" + productSearch.getId());
                views.txt_sale_product_id.setEnabled(true);
                views.txt_sale_price.setText("" + productSearch.getUnit_price());
                views.txt_sale_price.setEnabled(true);
                views.txt_sale_product_name.setText(productSearch.getName());
                views.txt_sale_product_name.setEnabled(true);
                views.txt_sale_quantity.setText("" + productSearch.getProduct_quantity());
                // Se habilita campo de ID de cliente
                views.txt_sale_customer_id.setEnabled(true);
                views.txt_sale_customer_id.setEditable(true);
                // Se habilita y pone foco en campo de cantidad de producto
                views.txt_sale_quantity.setEnabled(true);
                views.txt_sale_quantity.setEditable(true);
                views.txt_sale_quantity.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "No existe ningún producto con ese código");
                cleanFieldsSales();
                views.txt_sale_product_code.requestFocus();
            } 
        }
    }
    
    // Campo de ID del cliente + tecla ENTER: Ingresar nombre del cliente por el ID
    public void setCustomerById() {
        /* Nota: Para este método, se creó en el customerDAO 
               el método para obtener nombre del cliente a partir del ID */
        if (views.txt_sale_customer_id.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese el código del cliente asociado a la venta");
            views.txt_sale_customer_id.setText("");
        } else {   
            int customerId = Integer.parseInt(views.txt_sale_customer_id.getText());
            Customers customerSearch = customerDAO.searchCustomerName(customerId);
            if (customerSearch.getFull_name() != null) {
                /* Si se obtiener nombre de cliente,
                   se llenan los campos de ID y nombre de cliente */
                views.txt_sale_customer_id.setText("" + customerSearch.getId());
                views.txt_sale_customer_name.setText(customerSearch.getFull_name());
                views.txt_sale_customer_name.setEnabled(true);
            } else {
                /* De lo contrario, se vacía el campo de ID del cliente,
                   y se avisa que el cliente no existe */
                JOptionPane.showMessageDialog(null, "El cliente no existe");
                views.txt_sale_customer_id.setText("");
                views.txt_sale_customer_id.requestFocus();
            }
        }
    }
    
    // Campo de cantidad de producto + Tecla soltada: Calcular subtotal
    public void setSubtotal() {
        int quantity;
        double price = Double.parseDouble(views.txt_sale_price.getText());
        if (views.txt_sale_quantity.getText().equals("")) {
            quantity = 1;
            views.txt_sale_price.setText("" + price);
        } else {
            quantity = Integer.parseInt(views.txt_sale_quantity.getText());
            price = Double.parseDouble(views.txt_sale_price.getText());
            views.txt_sale_subtotal.setEnabled(true);
            views.txt_sale_subtotal.setText("" + (quantity * price));
        }
    }

}
