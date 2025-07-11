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
import models.Employees;
import models.EmployeesDAO;
import static models.EmployeesDAO.id_user;
import static models.EmployeesDAO.rol_user;
import views.SystemView;

public class EmployeesController implements ActionListener, MouseListener, KeyListener {

    private Employees employee;
    private EmployeesDAO employeeDao;
    private SystemView views;

    // Rol
    String rol = rol_user;

    // Modelo para las tablas
    DefaultTableModel model = new DefaultTableModel();

    public EmployeesController(Employees employee, EmployeesDAO employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;

        // Pestañas de Empleados y Perfil (de Empleado)
        // Pestaña Empleados, botón Registrar
        this.views.btn_register_employee.addActionListener(this);
        // Pestaña Empleados, botón Modificar
        this.views.btn_update_employee.addActionListener(this);
        // Pestaña Empleados, botón Eliminar
        this.views.btn_delete_employee.addActionListener(this);
        // Pestaña Empleados, botón Cancelar
        this.views.btn_cancel_employee.addActionListener(this);
        // Pestaña Perfil, botón Modificar (Contraseña) 
        this.views.btn_modify_data.addActionListener(this);

        // Tabla de pestaña Empleados
        this.views.employees_table.addMouseListener(this);
        // Panel de Empleados en menú lateral
        this.views.jPanelEmployees.addMouseListener(this);

        // Campo de búsqueda de Empleados
        this.views.txt_search_employee.addKeyListener(this);
    }

    // Función actionPerformed de ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_employee) {
            registerEmployee();
        } else if (e.getSource() == views.btn_update_employee) {
            updateEmployee();
        } else if (e.getSource() == views.btn_delete_employee) {
            deleteEmployee();
        } else if (e.getSource() == views.btn_cancel_employee) {
            cancelOperationsEmployee();
        } else if (e.getSource() == views.btn_modify_data) {
            modifyDataEmployee();
        }
    }

    // Funciones de MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.employees_table) {
            showEmployeeInfo(e);
        } else if (e.getSource() == views.jPanelEmployees) {
            goToEmployeesTab();
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
        if (e.getSource() == views.txt_search_employee) {
            filterEmployeesByInput();
        }
    }

    // Funciones generales
    // Listar todos los empleados 
    public void listAllEmployees() {
        if (rol.equals("Administrador")) {
            List<Employees> list = employeeDao.listEmployeesQuery(views.txt_search_employee.getText());
            model = (DefaultTableModel) views.employees_table.getModel();
            Object[] row = new Object[7]; // Columnas de la tabla
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                model.addRow(row);
            }
            views.employees_table.setModel(model);
        } else {
        }
    }

    // Limpiar campos de texto de pestaña Empleados
    public void cleanFields() {
        views.txt_employee_id.setText("");
        views.txt_employee_id.setEditable(true);
        views.txt_employee_fullname.setText("");
        views.txt_employee_username.setText("");
        views.txt_employee_address.setText("");
        views.txt_employee_telephone.setText("");
        views.txt_employee_email.setText("");
        views.txt_employee_password.setText("");
        views.cmb_rol.setSelectedIndex(0);
    }

    // Limpiar tabla de pestaña Empleados 
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Pestaña Empleados, botón Registrar: Registrar el empleado ingresado 
    public void registerEmployee() {
        // Verificar si los campos no están vacíos
        if (views.txt_employee_id.getText().equals("")
                || views.txt_employee_fullname.getText().equals("")
                || views.txt_employee_username.getText().equals("")
                || views.txt_employee_address.getText().equals("")
                || views.txt_employee_telephone.getText().equals("")
                || views.txt_employee_email.getText().equals("")
                || views.cmb_rol.getSelectedItem().toString().equals("")
                || String.valueOf(views.txt_employee_password.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        } else {
            // Realizar la inserción
            employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
            employee.setFull_name(views.txt_employee_fullname.getText().trim());
            employee.setUsername(views.txt_employee_username.getText().trim());
            employee.setAddress(views.txt_employee_address.getText().trim());
            employee.setTelephone(views.txt_employee_telephone.getText().trim());
            employee.setEmail(views.txt_employee_email.getText().trim());
            employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
            employee.setRol(views.cmb_rol.getSelectedItem().toString());

            if (employeeDao.registerEmployeeQuery(employee)) {
                cleanTable();
                cleanFields();
                listAllEmployees();
                JOptionPane.showMessageDialog(null, "Empleado registrado con éxito");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el empleado");
            }
        }
    }

    // Pestaña Empleados, botón Modificar: Actualizar el empleado seleccionado
    public void updateEmployee() {
        if (views.txt_employee_id.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
        } else {
            // Verificar si los campos están vacíos
            if (views.txt_employee_id.getText().equals("")
                    || views.txt_employee_fullname.getText().equals("")
                    || views.cmb_rol.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                // Realizar la edición
                employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                employee.setFull_name(views.txt_employee_fullname.getText().trim());
                employee.setUsername(views.txt_employee_username.getText().trim());
                employee.setAddress(views.txt_employee_address.getText().trim());
                employee.setTelephone(views.txt_employee_telephone.getText().trim());
                employee.setEmail(views.txt_employee_email.getText().trim());
                employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                employee.setRol(views.cmb_rol.getSelectedItem().toString());
                if (employeeDao.updateEmployeeQuery(employee)) {
                    cleanTable();
                    cleanFields();
                    listAllEmployees();
                    views.btn_register_employee.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Datos del empleado modificados exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar el empleado");
                }

            }
        }
    }

    // Pestaña Empleados, botón Eliminar: Eliminar el empleado seleccionado 
    public void deleteEmployee() {
        int row = views.employees_table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Debes selecciona un empleado para eliminar");
        } else if (views.employees_table.getValueAt(row, 0).equals(id_user)) {
            JOptionPane.showMessageDialog(null, "No puede eliminar al usuario autenticado");
        } else {
            int id = Integer.parseInt(views.employees_table.getValueAt(row, 0).toString());
            int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este empleado");
            if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) {
                cleanTable();
                cleanFields();
                views.btn_register_employee.setEnabled(true);
                views.txt_employee_password.setEnabled(true);
                listAllEmployees();
                JOptionPane.showMessageDialog(null, "Empleado eliminado con éxito");
            }
        }
    }

    // Pestaña Empleados, botón Cancelar: Cancelar operaciones sobre el empleado seleccionado
    public void cancelOperationsEmployee() {
        cleanFields();
        views.btn_register_employee.setEnabled(true);
        views.txt_employee_password.setEnabled(true);
        views.txt_employee_id.setEnabled(true);
    }

    // Pestaña Perfil, botón Modificar (Contraseña): Modificar contraseña del empleado
    public void modifyDataEmployee() {
        // Recolectar información de los campos de texto password
        String password = String.valueOf(views.txt_password_modify.getPassword());
        String confirm_password = String.valueOf(views.txt_password_modify_confirm.getPassword());
        // Verificar que los campos de texto de contraseña no están vacíos
        if (!password.equals("") && !confirm_password.equals("")) {
            // Verificar que las contraseñas sean iguales
            if (password.equals(confirm_password)) {
                employee.setPassword(String.valueOf(views.txt_password_modify.getPassword()));
                if (employeeDao.updateEmployeePassword(employee)) {
                    JOptionPane.showMessageDialog(null, "Contraseña modificada con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
        }
    }

    // Funciones invocadas dentro de función implementada mouseClicked
    // Tabla de Empleados: Mostrar información de empleado al hacer click en una fila de la tabla
    public void showEmployeeInfo(MouseEvent e) {
        // Obtener fila en que se hizo click 
        int row = views.employees_table.rowAtPoint(e.getPoint());

        // Llenar los campos de la pestaña Empleados en base a la fila seleccionada
        views.txt_employee_id.setText(views.employees_table.getValueAt(row, 0).toString());
        views.txt_employee_fullname.setText(views.employees_table.getValueAt(row, 1).toString());
        views.txt_employee_username.setText(views.employees_table.getValueAt(row, 2).toString());
        views.txt_employee_address.setText(views.employees_table.getValueAt(row, 3).toString());
        views.txt_employee_telephone.setText(views.employees_table.getValueAt(row, 4).toString());
        views.txt_employee_email.setText(views.employees_table.getValueAt(row, 5).toString());
        views.cmb_rol.setSelectedItem(views.employees_table.getValueAt(row, 6).toString());

        // Deshabilitar campos y botones en la pestaña Empleados 
        views.txt_employee_id.setEditable(false);
        views.txt_employee_password.setEnabled(false);
        views.btn_register_employee.setEnabled(false);
    }

    // Panel de Empleados en menú lateral: Ir a la pestaña de Empleados
    public void goToEmployeesTab() {
        // Si el rol es administrador
        if (rol.equals("Administrador")) {
            // Setear pestaña de Empleados
            views.jTabbedPane1.setSelectedIndex(3);
            cleanTable();
            cleanFields();
            listAllEmployees();
        } else {
            // Si no lo es, deshabilitar la pestaña de Empleados y el panel de Empleados en el menú lateral
            views.jTabbedPane1.setEnabledAt(3, false);
            views.jLabelEmployees.setEnabled(false);
            JOptionPane.showMessageDialog(null, "No tienes permisos de administrador para acceder a esta vista");
        }
    }

    // Funciones invocadas dentro de función implementada keyReleased
    // Campo de búsqueda de Empleados: Filtrar empleados por campo de texto
    public void filterEmployeesByInput() {
        cleanTable();
        listAllEmployees();
    }

}
