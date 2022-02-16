/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.awt.BorderLayout;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import validation.ValidationException;
import validation.Validator;
import project.controller.Controller;
import domain.LovackoDrustvo;
import project.form.components.decorator.SearchComponentDecorator;
import project.form.components.LovackoDrustvoListComponent;
import project.form.design.UpdateLovackoDrustvoForm;

/**
 *
 * @author User
 */
public class UpdateLovackoDrustvoFormController {

    private UpdateLovackoDrustvoForm frmUpdateDrustvo;
    private String name;
    private LovackoDrustvoListComponent listLovackoDrustvo;
    private DefaultListModel listModel;

    public UpdateLovackoDrustvoFormController() {
        frmUpdateDrustvo = new UpdateLovackoDrustvoForm();
        listLovackoDrustvo = new LovackoDrustvoListComponent();
        listModel = new DefaultListModel();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        resetFields();
        setList();
    }

    private void setListeners() {
        frmUpdateDrustvo.getBtnBack().addActionListener((l) -> {
            frmUpdateDrustvo.dispose();
        });
        frmUpdateDrustvo.getBtnCancel().addActionListener((l) -> {
            resetFields();
        });
        listListener();
        updateLovackoDrustvo();
        deleteDrustvo();
    }

    private void updateLovackoDrustvo() {
        frmUpdateDrustvo.getBtnUpdate().addActionListener((l) -> {
            try {
                Validator.startValidate().validateStringMinLength(frmUpdateDrustvo.getTxtLovackoDrustvo().getText().trim(), 5, "U imenu morate uneti 5 ili vise karaktera")
                        .validateStringMinLength(frmUpdateDrustvo.getTxtCounty().getText().trim(), 3, "Opstina mora imati 3 ili vise slova")
                        .validateStringMinLength(frmUpdateDrustvo.getTxtAdress().getText().trim(), 5, "Adresa mora imati vise od 5 slova")
                        .throwIfInvalide();
                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setName(frmUpdateDrustvo.getTxtLovackoDrustvo().getText().trim());
                drustvo.setCounty(frmUpdateDrustvo.getTxtCounty().getText().trim());
                drustvo.setAdress(frmUpdateDrustvo.getTxtAdress().getText().trim());
                Controller.getInstance().updateLovackoDrustvo(drustvo, name);
                JOptionPane.showMessageDialog(frmUpdateDrustvo, "Lovacko drustvo je izmenjeno", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                frmUpdateDrustvo.getPnlLovackoDrustvo().removeAll();
                frmUpdateDrustvo.getPnlLovackoDrustvo().setLayout(new BorderLayout());
                prepareComponents();
                frmUpdateDrustvo.revalidate();
                frmUpdateDrustvo.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmUpdateDrustvo, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            }
        });

    }

    private void deleteDrustvo() {
        frmUpdateDrustvo.getBtnDelete().addActionListener((l) -> {
            try {
                Validator.startValidate().validateStringMinLength(name, 5, "U imenu morate uneti 5 ili vise karaktera")
                        .throwIfInvalide();
                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setName(name);
                int i = JOptionPane.showConfirmDialog(frmUpdateDrustvo, "Potvrdite brisanje lovackog drustva");
                if (i == 0) {
                    Controller.getInstance().deleteLovackoDrustvo(drustvo);
                    JOptionPane.showMessageDialog(frmUpdateDrustvo, "Lovacko drustvo " + name + " je obrisano", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    frmUpdateDrustvo.getPnlLovackoDrustvo().removeAll();
                    frmUpdateDrustvo.getPnlLovackoDrustvo().setLayout(new BorderLayout());
                    prepareComponents();
                    frmUpdateDrustvo.revalidate();
                    frmUpdateDrustvo.repaint();
                } else {

                    resetFields();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmUpdateDrustvo, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            }
        });

    }

    private void resetFields() {
        frmUpdateDrustvo.getTxtLovackoDrustvo().setText("");
        frmUpdateDrustvo.getTxtCounty().setText("");
        frmUpdateDrustvo.getTxtAdress().setText("");
        frmUpdateDrustvo.getBtnDelete().setEnabled(false);
        frmUpdateDrustvo.getBtnUpdate().setEnabled(false);
        name = "";
    }

    public void openForm() {
        frmUpdateDrustvo.setVisible(true);
    }

    private void setList() {
        try {
            listModel.clear();
            List<LovackoDrustvo> drustva = Controller.getInstance().getAllLovackoDrustvo();
            listModel.addAll(drustva);
            listLovackoDrustvo.getjList().setModel(listModel);
            listLovackoDrustvo.setDrustva(drustva);
            new SearchComponentDecorator().decorate(listLovackoDrustvo, LovackoDrustvoListComponent::Filter, frmUpdateDrustvo.getPnlLovackoDrustvo());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmUpdateDrustvo, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            frmUpdateDrustvo.dispose();
        }
    }

    private void listListener() {
        listLovackoDrustvo.getjList().addListSelectionListener((e) -> {
            if (listLovackoDrustvo.getjList().getSelectedIndex() > -1) {
                name = listLovackoDrustvo.getjList().getSelectedValue().getName();
                frmUpdateDrustvo.getTxtLovackoDrustvo().setText(name);
                frmUpdateDrustvo.getTxtCounty().setText(listLovackoDrustvo.getjList().getSelectedValue().getCounty());
                frmUpdateDrustvo.getTxtAdress().setText(listLovackoDrustvo.getjList().getSelectedValue().getAdress());
                frmUpdateDrustvo.getBtnDelete().setEnabled(true);
                frmUpdateDrustvo.getBtnUpdate().setEnabled(true);
            } else {
                resetFields();
            }
        });
    }

}
