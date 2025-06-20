package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Employees;
import models.EmployeesDAO;
import static models.EmployeesDAO.rol_user;
import views.SystemView;

public class EmployeesController implements ActionListener {
    private Employees employee;
    private EmployeesDAO employeeDao;
    private SystemView views;
    // Rol
    String rol = rol_user;

    public EmployeesController(Employees employee, EmployeesDAO employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;
        
        // Botón de registrar empleado
        this.views.btn_register_employee.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_employee) {
            // Verificar si los campos no están vacíos
            if (views.txt_employee_id.getText().equals("") ||
                views.txt_employee_fullname.getText().equals("") ||
                views.txt_employee_username.getText().equals("") ||
                views.txt_employee_address.getText().equals("") ||
                views.txt_employee_telephone.getText().equals("") ||  
                views.txt_employee_email.getText().equals("") ||
                views.cmb_rol.getSelectedItem().toString().equals("") ||
                String.valueOf(views.txt_employee_password.getPassword()).equals("")) {
               JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
               // Realizar la inserción
               employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
               employee.setFull_name(views.txt_employee_fullname.getText().trim());
               employee.setUsername(views.txt_employee_username.getText().trim());
               employee.setAddress(views.txt_employee_address.getText().trim());
               employee.setTelephone(views.txt_employee_address.getText().trim());
               employee.setEmail(views.txt_employee_email.getText().trim());
               employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
               employee.setRol(views.cmb_rol.getSelectedItem().toString());
               if (employeeDao.registerEmployeeQuery(employee)) {
                   JOptionPane.showMessageDialog(null, "Empleado registrado con éxito");
               } else {
                   JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el empleado");
               }
            }
        }
        
    }
    
    
    
}
