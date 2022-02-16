/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import communication.Operation;
import communication.Request;
import communication.Response;
import communication.ResponseType;
import domain.User;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import project.communication.Communication;
import project.controller.Controller;
import project.form.design.LoginForm;
import project.form.design.MainForm;
import project.form.conroller.cordinator.MainControllerCordinator;
import validation.Validator;

/**
 *
 * @author User
 */
public class LoginFormController {

    private LoginForm frmLogin;
    private Socket socket;

    public LoginFormController() {
        frmLogin = new LoginForm();
        setComponents();
        setListeners();
    }

    private void setComponents() {
        resetFields();
    }

    private void setListeners() {
        login();
        cancel();
    }

    private void resetFields() {
        frmLogin.getTxtEmail().setText("");
        frmLogin.getTxtPassword().setText("");
    }

    private void cancel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void login() {

        frmLogin.getBtnLogin().addActionListener((e) -> {
            try {
                Validator.startValidate().validateEmptyString(frmLogin.getTxtEmail().getText().trim(), "email ne moze biti prazan")
                        .validateEmptyString(String.valueOf(frmLogin.getTxtPassword().getPassword()), "sifra ne moze biti prazna")
                        .throwIfInvalide();
                User userCheck = new User(null, null, null, frmLogin.getTxtEmail().getText().trim(), String.valueOf(frmLogin.getTxtPassword().getPassword()));
                User user = Controller.getInstance().login(userCheck);
                JOptionPane.showMessageDialog(frmLogin, "Dobrodosli: " + user, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                Controller.getInstance().setCurrentUser(user);
                MainControllerCordinator.getInstance().openForm();
                frmLogin.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frmLogin, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    public LoginForm getFrmLogin() {
        return frmLogin;
    }

    public void openForm() {
        frmLogin.setVisible(true);
    }

}
