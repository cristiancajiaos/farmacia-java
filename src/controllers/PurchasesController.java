package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicComboBox;
import static models.EmployeesDAO.id_user;
import static models.EmployeesDAO.rol_user;
import models.Products;
import models.ProductsDAO;
import models.Purchases;
import models.PurchasesDAO;
import views.Print;
import views.SystemView;

public class PurchasesController implements KeyListener, ActionListener, MouseListener {

    private Purchases purchase;
    private PurchasesDAO purchaseDAO;
    private SystemView views;

    // Instanciado del modelo productos
    Products product = new Products();
    ProductsDAO productDAO = new ProductsDAO();

    // ID del Proveedor 
    private int getIdSupplier = 0;

    // Item
    private int item = 0;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas + Modelo temporal 
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp = new DefaultTableModel();

    public PurchasesController(Purchases purchase, PurchasesDAO purchaseDAO, SystemView views) {
        this.purchase = purchase;
        this.purchaseDAO = purchaseDAO;
        this.views = views;

        // Pestaña de Compras
        // Botón Agregar (Producto a la compra)
        this.views.btn_add_product_to_buy.addActionListener(this);
        // Botón Comprar
        this.views.btn_confirm_purchase.addActionListener(this);
        // Botón Eliminar (Producto en la compra)
        this.views.btn_remove_purchase.addActionListener(this);
        // Botón Nuevo (Nueva Compra)
        this.views.btn_new_purchase.addActionListener(this);

        // Panel de compras en menú lateral
        this.views.jPanelPurchases.addMouseListener(this);
        // Panel de reportes en menú lateral
        this.views.jPanelReports.addMouseListener(this);

        // Campo de código de compra
        this.views.txt_purchase_product_code.addKeyListener(this);
        // Campo de precio de venta de compra
        this.views.txt_purchase_price.addKeyListener(this);
    }

    // Función actionPerformed de ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_add_product_to_buy) {
            addProductToPurchase();
        } else if (e.getSource() == views.btn_confirm_purchase) {
            insertPurchase();
        } else if (e.getSource() == views.btn_remove_purchase) {
            removeProductInCurrentPurchase();
        } else if (e.getSource() == views.btn_new_purchase) {
            newPurchase();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.jPanelPurchases) {
            goToPurchasesTab();
        } else if (e.getSource() == views.jPanelReports) {
            goToReportsTab();
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
        if (e.getSource() == views.txt_purchase_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                setProductToPurchaseByCode();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_price) {
            setSubtotalPurchase();
        }
    }

    // Funciones generales
    // Listar las compras realizadas
    public void listAllPurchases() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Purchases> list = purchaseDAO.listAllPurchasesQuery();
            model = (DefaultTableModel) views.table_all_purchases.getModel();
            Object[] row = new Object[4];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                model.addRow(row);
            }
            views.table_all_purchases.setModel(model);
        }
    }

    // Limpiar campos de texto en la pestaña de Compras
    public void cleanFieldsPurchase() {
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_product_name.setText("");
        views.txt_purchase_amount.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_id.setEnabled(true);
        views.txt_purchase_id.setEditable(false);
        views.txt_purchase_total_to_pay.setText("");
        views.cmb_purchase_supplier.setSelectedItem(0);
    }

    // Calcular total a pagar
    public void calculatePurchase() {
        double total = 0.0;
        int numRow = views.purchases_table.getRowCount();

        for (int i = 0; i < numRow; i++) {
            // Se pasa el indice de la columna
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i, 4)));
        }
        views.txt_purchase_total_to_pay.setText("" + total);
    }

    // Limpiar tabla temporal
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    // Limpiar tabla de pestaña Compras
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Botón Agregar: Agregar producto a la compra
    public void addProductToPurchase() {
        DynamicComboBox supplier_cmb = (DynamicComboBox) views.cmb_purchase_supplier.getSelectedItem();
        int supplier_id = supplier_cmb.getId();

        if (getIdSupplier == 0) {
            getIdSupplier = supplier_id;
        } else {
            if (getIdSupplier != supplier_id) {
                JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a varios proveedores");
            } else {
                int amount = Integer.parseInt(views.txt_purchase_amount.getText());
                String product_name = views.txt_purchase_product_name.getText();
                double price = Double.parseDouble(views.txt_purchase_price.getText());
                int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                String supplier_name = views.cmb_purchase_supplier.getSelectedItem().toString();

                if (amount > 0) {
                    temp = (DefaultTableModel) views.purchases_table.getModel();
                    for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                        if (views.purchases_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText())) {
                            JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de compras");
                            return;
                        }
                    }

                    ArrayList list = new ArrayList();
                    item = 1;
                    list.add(item);
                    list.add(purchase_id);
                    list.add(product_name);
                    list.add(amount);
                    list.add(price);
                    list.add(amount * price);
                    list.add(supplier_name);

                    Object[] obj = new Object[6];
                    obj[0] = list.get(1);
                    obj[1] = list.get(2);
                    obj[2] = list.get(3);
                    obj[3] = list.get(4);
                    obj[4] = list.get(5);
                    obj[5] = list.get(6);
                    temp.addRow(obj);

                    views.purchases_table.setModel(temp);
                    cleanFieldsPurchase();
                    views.cmb_purchase_supplier.setEditable(false);
                    views.txt_purchase_product_code.requestFocus();
                    calculatePurchase();
                }
            }
        }
    }

    // Botón Comprar: Ingresar compra 
    private void insertPurchase() {
        if (views.purchases_table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay ninguna compra enlistada. Agregue una compra en la lista de compras para poder generar su ingreso.");
        } else {
            double total = Double.parseDouble(views.txt_purchase_total_to_pay.getText());
            int employee_id = id_user;

            if (purchaseDAO.registerPurchaseQuery(getIdSupplier, employee_id, total)) {
                int purchase_id = purchaseDAO.purchaseId();
                for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                    int product_id = Integer.parseInt(views.purchases_table.getValueAt(i, 0).toString());
                    int purchase_amount = Integer.parseInt(views.purchases_table.getValueAt(i, 2).toString());
                    double purchase_price = Double.parseDouble(views.purchases_table.getValueAt(i, 3).toString());
                    double purchase_subtotal = purchase_price * purchase_amount;

                    // Registrar detalles de la compra
                    purchaseDAO.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);

                    // Traer la cantidad de productos
                    product = productDAO.searchId(product_id);
                    int amount = product.getProduct_quantity() + purchase_amount;

                    productDAO.updateStockQuery(amount, product_id);
                }

                cleanTableTemp();
                cleanFieldsPurchase();
                JOptionPane.showMessageDialog(null, "La compra ha sido generada con éxito");
                Print print = new Print(purchase_id);
                print.setVisible(true);
                listAllPurchases();
            }
        }
    }

    // Botón Eliminar: Eliminar producto actual en la compra
    public void removeProductInCurrentPurchase() {
        model = (DefaultTableModel) views.purchases_table.getModel();
        int row = views.purchases_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No hay ninguna compra seleccionada. Seleccione una compra en la tabla.");
        } else {
            model.removeRow(row);
            JOptionPane.showMessageDialog(null, "El producto seleccionado ha sido eliminado de la compra actual.");
            calculatePurchase();
            views.txt_purchase_product_code.requestFocus();
        }
    }

    // Botón nuevo: Nueva compra
    public void newPurchase() {
        cleanTableTemp();
        cleanFieldsPurchase();
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Panel de Compras en menú lateral: Ir a la pestaña de Compras
    public void goToPurchasesTab() {
        // Si el rol es administrador
        if (rol.equals("Administrador")) {
            // Setear pestaña de Compras
            views.jTabbedPane1.setSelectedIndex(1);
            cleanTable();
            cleanFieldsPurchase();
        } else {
            // Si no lo es, deshabilitar la pestaña de Compras y el panel de Compras en el menú lateral
            views.jTabbedPane1.setEnabledAt(1, false);
            views.jLabelPurchases.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta pestaña");
        }
    }

    // Panel de Reportes en menú lateral: Ir a la pestaña de Reportes
    public void goToReportsTab() {
        if (rol.equals("Administrador")) {
            views.jTabbedPane1.setSelectedIndex(7);
            cleanTable();
            listAllPurchases();
        } else {
            views.jTabbedPane1.setEnabledAt(7, false);
            views.jLabelReports.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tiene permisos de administrador para acceder a esta pestaña");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de código del producto + tecla ENTER: Ingresar automáticamente producto por su código
    public void setProductToPurchaseByCode() {
        if (views.txt_purchase_product_code.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Ingrese el código del producto a comprar");
        } else {
            int id = Integer.parseInt(views.txt_purchase_product_code.getText());
            product = productDAO.searchCode(id);
            views.txt_purchase_product_name.setText(product.getName());
            views.txt_purchase_id.setText("" + product.getId());
            views.txt_purchase_id.setEnabled(true);
            views.txt_purchase_id.setEditable(false);
            views.txt_purchase_amount.requestFocus();
        }
    }

    // Campo de cantidad de producto + Tecla soltada: Calcular subtotal
    public void setSubtotalPurchase() {
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
