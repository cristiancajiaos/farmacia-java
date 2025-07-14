package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Employees;
import models.EmployeesDAO;
import views.LoginView;
import views.SystemView;

public class LoginController implements ActionListener {

    private Employees employee;
    private EmployeesDAO employees_dao;
    private LoginView login_view;

    public LoginController(Employees employee, EmployeesDAO employees_dao, LoginView login_view) {
        this.employee = employee;
        this.employees_dao = employees_dao;
        this.login_view = login_view;

        // Vista de Autenticación
        // Botón Ingresar (Ingresar al sistema)
        this.login_view.btn_enter.addActionListener(this);
    }

    // Función actionPerformed de ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login_view.btn_enter) {
            login();
        }
    }

    // Funciones invocadas dentro de función implementada actionPerformed
    // Botón Ingresar: Inicio de sesión (declarado private para evitar acceso en la vista)
    private void login() {
        // Obtener los datos de la vista
        String user = login_view.txt_username.getText().trim();
        String pass = String.valueOf(login_view.text_password.getPassword());

        // Validar que los campos no estén vacíos
        if (!user.equals("") || !pass.equals("")) {
            // Pasar los parámetros al método login
            employee = employees_dao.loginQuery(user, pass);
            // Verificar la existencia del usuario
            if (employee.getUsername() != null) {
                // Verificar rol del usuario para mostrar sistema en función de ese rol
                if (employee.getRol().equals("Administrador")) {
                    SystemView admin = new SystemView();
                    admin.setVisible(true);
                } else if (employee.getRol().equals("Auxiliar")) {
                    SystemView aux = new SystemView();
                    aux.setVisible(true);
                } else {
                    // Reservado para otros roles
                }
                this.login_view.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos están vacíos");
        }
    }

}
