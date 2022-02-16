/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import validation.ValidationException;
import validation.Validator;
import project.controller.Controller;
import domain.LovackoDrustvo;
import project.form.design.AddLovackoDrustvoForm;

/**
 *
 * @author User
 */
public class AddDrustvoController {

    private AddLovackoDrustvoForm frmDrustvo;

    public AddDrustvoController() {
        frmDrustvo = new AddLovackoDrustvoForm();
        prepareComponents();
        setListeners();

    }

    private void prepareComponents() {
        resetFields();

    }

    private void resetFields() {
        
        frmDrustvo.getTxtAdress().setText("");
        frmDrustvo.getTxtCounty().setText("");
        frmDrustvo.getTxtLovackoDrustvo().setText("");
    }

    private void setListeners() {
        setBtnSaveListener();
        setBtnBackListener();
        setBtnCancelListener();
    }

    public void openForm() {
        frmDrustvo.setVisible(true);
    }

    private void setBtnSaveListener() {
        frmDrustvo.getBtnSave().addActionListener((e) -> {
            try {
                Validator.startValidate().validateStringMinLength(frmDrustvo.getTxtLovackoDrustvo().getText().trim(), 6, "Ime drustva ne moze biti manje od 5 slova")
                        .validateStringMinLength(frmDrustvo.getTxtCounty().getText().trim(), 3, "Ime opstine ne moze biti manja od 3 slova")
                        .validateStringMinLength(frmDrustvo.getTxtAdress().getText().trim(), 5, "Adresa ne moze biti manja od 5 slova")
                        .throwIfInvalide();
                
                LovackoDrustvo drustvo= new LovackoDrustvo();
                drustvo.setName(frmDrustvo.getTxtLovackoDrustvo().getText().trim());
                drustvo.setCounty(frmDrustvo.getTxtCounty().getText().trim());
                drustvo.setAdress(frmDrustvo.getTxtAdress().getText().trim());
                Controller.getInstance().addLovackoDrustvo(drustvo);
                JOptionPane.showMessageDialog(frmDrustvo, "Lovacko drustvo je sacuvano", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmDrustvo, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setBtnCancelListener() {
        frmDrustvo.getBtnCancel().addActionListener((e)->{resetFields();});
    }

    private void setBtnBackListener() {
        frmDrustvo.getBtnBack().addActionListener((e)->{frmDrustvo.dispose();});
    }

}
