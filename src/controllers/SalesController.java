package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
            // Ir a la pestaña de Ventas
            goToSalesTab();
        }
        
        /* Nota: La función de MouseListener sobre el panel de reportes en el
           menú lateral fue implementada en el controlador PurchasesController */
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
            // Setear subtotal de la venta
            setSubtotalSale();
        }
    }

    // Funciones generales
    public void listAllSales() {
    }
    
    // Limpiar algunos de los campos de texto en la pestaña de Ventas
    public void cleanSomeFieldsSales() {
        // Limpieza de campos
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_stock.setText("");
        
        // Seteo de características de campos
        views.txt_sale_quantity.setEnabled(false);
        views.txt_sale_quantity.setEditable(false);
        views.txt_sale_subtotal.setEnabled(false);
        views.txt_sale_product_code.requestFocus();
    }

    // Limpiar todos los campos de texto en la pestaña de Ventas
    public void cleanAllFieldsSales() {
        // Limpieza de campos
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
        
        // Seteo de características de campos
        views.txt_sale_customer_id.setEnabled(false);
        views.txt_sale_customer_id.setEditable(false);
        views.txt_sale_quantity.setEnabled(false);
        views.txt_sale_quantity.setEditable(false);
        views.txt_sale_subtotal.setEnabled(false);
        views.txt_sale_product_code.requestFocus();
    }
    
    // Limpiar tabla temporal
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    // Limpiar tabla de pestaña Ventas
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    
    // Calcular total de la venta 
    public void calculateSale() {
        double total = 0.0;
        int numRow = views.sales_table.getRowCount();
        
        for (int i = 0; i < numRow; i++) {
            total += Double.parseDouble(String.valueOf(views.sales_table.getValueAt(i, 4)));
        }
        
        views.txt_sale_total_to_pay.setEnabled(true);
        views.txt_sale_total_to_pay.setText("" + total);
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Botón Agregar: Agregar producto a la venta
    public void addProductToSale() {
        // Se obtienen los parámetros para llenar los registros de la tabla
        int product_id, amount;
        String product_name, customer_full_name;
        double unit_price, subtotal;
        
        if (views.txt_sale_product_code.getText().equals("")) {
            /* Se chequea si está ingresado el código del producto */
            JOptionPane.showMessageDialog(null, "Debe ingresar el código del producto");
            return;
        } else if (views.txt_sale_quantity.getText().equals("")) {
            /* Se chequea si hay ingresada cantidad de producto */
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad de producto para cursar la venta");
            return;
        } else if (Integer.parseInt(views.txt_sale_quantity.getText()) == 0) {
            /* Se chequea si la cantidad de producto es superior a 0 para la venta */
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad de producto mayor a 0 para cursar la venta");
            return;
        } else if (Integer.parseInt(views.txt_sale_quantity.getText()) > Integer.parseInt(views.txt_sale_stock.getText())) {
            /* Se chequea si el stock es mayor o igual a la cantidad de producto para la venta */
            JOptionPane.showMessageDialog(null, "La cantidad de producto ingresada debe ser menor o igual al stock disponible");
            return;
        } else if (views.txt_sale_customer_id.getText().equals("") || views.txt_sale_customer_name.getText().equals("")) {
            /* Se chequea si hay un usuario al cual se le cursa la venta, para ambos campos de ID y nombre */
            JOptionPane.showMessageDialog(null, "Debe ingresar un usuario al cual se cursa la venta");
            return;
        } else {
            /* Se chequea si el producto no estaba previamente registrado en la lista de productos para la venta */
            for (int i = 0; i < views.sales_table.getRowCount(); i++) {
                if (views.sales_table.getValueAt(i, 1).equals(views.txt_sale_product_name.getText())) {
                    JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de ventas");
                    return;
                }
            }

            // Si se cumplen las anteriores condiciones, se procede a registrar la venta 
            product_id = Integer.parseInt(views.txt_sale_product_id.getText());
            product_name = views.txt_sale_product_name.getText();
            amount = Integer.parseInt(views.txt_sale_quantity.getText());
            unit_price = Double.parseDouble(views.txt_sale_price.getText());
            subtotal = amount * unit_price;
            customer_full_name = views.txt_sale_customer_name.getText();
            
            item++;
            
            // Se crear la lista y la fila 
            temp = (DefaultTableModel) views.sales_table.getModel();
            ArrayList list = new ArrayList();
            list.add(product_id);
            list.add(product_name);
            list.add(amount);
            list.add(unit_price);
            list.add(subtotal);
            list.add(customer_full_name);

            Object[] obj = new Object[6];
            obj[0] = list.get(0);
            obj[1] = list.get(1);
            obj[2] = list.get(2);
            obj[3] = list.get(3);
            obj[4] = list.get(4);
            obj[5] = list.get(5);
            temp.addRow(obj);

            views.sales_table.setModel(temp);
            // Se limpian los campos para un nuevo ingreso
            cleanAllFieldsSales();
            // Se calcula el total a pagar de la venta
            calculateSale();
        }
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
        cleanAllFieldsSales();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Panel de Ventas en menú lateral: Ir a la pestaña de Ventas
    public void goToSalesTab() {
        if (rol.equals("Administrador")) {
            views.jTabbedPane1.setSelectedIndex(2);
            // TODO: Limpiar tabla
            this.cleanAllFieldsSales();
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
                views.txt_sale_stock.setText("" + productSearch.getProduct_quantity());
                views.txt_sale_stock.setEnabled(true);
                // Se habilita campo de ID de cliente
                views.txt_sale_customer_id.setEnabled(true);
                views.txt_sale_customer_id.setEditable(true);
                // Se habilita y pone foco en campo de cantidad de producto
                views.txt_sale_quantity.setEnabled(true);
                views.txt_sale_quantity.setEditable(true);
                views.txt_sale_quantity.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "No existe ningún producto con ese código");
                cleanAllFieldsSales();
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
    public void setSubtotalSale() {
        /* Nota: Se usa código adicional fuera del solicitado 
                 para hacer más legibles los textos de los campos. */
        int quantity;
        double price = Double.parseDouble(views.txt_sale_price.getText());
        if (views.txt_sale_quantity.getText().equals("")) {
            /* Si no hay texto en el campo de cantidad, la cantidad es 1 */
            quantity = 1;
            views.txt_sale_price.setText("" + price);
        } else {
            /* Si lo hay, se calcula cantidad * precio y se deja en el campo de subtotal */
            quantity = Integer.parseInt(views.txt_sale_quantity.getText());
            price = Double.parseDouble(views.txt_sale_price.getText());
            views.txt_sale_subtotal.setEnabled(true);
            views.txt_sale_subtotal.setText("" + (quantity * price));
        }
    }

}
