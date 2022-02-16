/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.LovackoDrustvo;
import domain.User;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.components.LovackoDrustvoListComponent;
import project.form.components.decorator.SearchComponentDecorator;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.AddUserForm;

/**
 *
 * @author User
 */
public class AddUserFormController {

    private AddUserForm frmAddUser;
    private LovackoDrustvoListComponent listLovackoDrustvo;
    private LovackoDrustvo drustvo;

    public AddUserFormController() {
        frmAddUser = new AddUserForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        listLovackoDrustvo = new LovackoDrustvoListComponent();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        setList();
    }

    private void setList() {
        try {
            DefaultListModel<LovackoDrustvo> listModel = new DefaultListModel();
            List<LovackoDrustvo> drustva = Controller.getInstance().getAllLovackoDrustvo();
            listModel.addAll(drustva);
            listLovackoDrustvo.getjList().setModel(listModel);
            listLovackoDrustvo.setDrustva(drustva);
            new SearchComponentDecorator().decorate(listLovackoDrustvo, LovackoDrustvoListComponent::Filter, frmAddUser.getPnlDrustvo());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmAddUser, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            frmAddUser.dispose();
        }
    }

    private void setListeners() {
        listListener();
        btnBackListener();
        btnAddListener();
    }

    private void listListener() {
        listLovackoDrustvo.getjList().addListSelectionListener((e) -> {
            if (listLovackoDrustvo.getjList().getSelectedIndex() > -1) {
                drustvo = listLovackoDrustvo.getjList().getSelectedValue();

            } else {
                drustvo = null;
            }
        });
    }

    private void btnAddListener() {
        frmAddUser.getBtnAdd().addActionListener((l) -> {
            try {
                User user = new User();
                user.setLovackoDrustvoid(drustvo);
                user.setName(frmAddUser.getTxtName().getText().trim());
                user.setLastName(frmAddUser.getTxtLastname().getText().trim());
                user.setEmail(frmAddUser.getTxtEmail().getText().trim());
                user.setPassword(frmAddUser.getTxtPassword().getText().trim());
                Controller.getInstance().addUser(user);
                JOptionPane.showMessageDialog(frmAddUser, "Korisnik je dodat", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmAddUser, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    private void btnBackListener() {
        frmAddUser.getBtnBack().addActionListener((l)->{
            frmAddUser.dispose();
        });
    }
    
    public void openForm(){
        frmAddUser.setVisible(true);
    }

}
