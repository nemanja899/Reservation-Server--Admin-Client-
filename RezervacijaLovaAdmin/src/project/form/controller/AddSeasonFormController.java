/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Porezi;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.AddSeasonForm;
import validation.Validator;

/**
 *
 * @author User
 */
public class AddSeasonFormController {

    private AddSeasonForm frmAddSeason;

    public AddSeasonFormController() {
        frmAddSeason = new AddSeasonForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        setFields();
        setListeners();
    }

    private void setFields() {
        resetFields();
    }

    private void resetFields() {
        frmAddSeason.getTxtPDV().setText("");
        frmAddSeason.getTxtSeason().setText("");
        frmAddSeason.getTxtProvision().setText("");

    }

    private void setListeners() {
        btnBack();
        btnSave();
        btnCancel();
    }

    public void btnSave() {
        frmAddSeason.getBtnSave().addActionListener((l) -> {
            try {
                Validator.startValidate().validateBigDecimal(new BigDecimal(frmAddSeason.getTxtPDV().getText().trim()), "Pdv mora biti decimalni broj odvajanje je . ")
                        .validateBigDecimal(new BigDecimal(frmAddSeason.getTxtProvision().getText().trim()), "Provizija mora biti decimalni broj odvajanje je . ")
                        .validateSeasonString(frmAddSeason.getTxtSeason().getText().trim())
                        .throwIfInvalide();
                Porezi porezi = new Porezi();
                porezi.setSeason(frmAddSeason.getTxtSeason().getText().trim());
                porezi.setPDV(new BigDecimal(frmAddSeason.getTxtPDV().getText().trim()));
                porezi.setProvision(new BigDecimal(frmAddSeason.getTxtProvision().getText().trim()));
                Controller.getInstance().addPorezi(porezi);
                JOptionPane.showMessageDialog(frmAddSeason, "Sezona i porezi su dodati", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmAddSeason, "Morate uneti decimalan broj za pdv i proviziju", "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frmAddSeason, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            }
        });
    }

    public void btnBack() {
        frmAddSeason.getBtnBack().addActionListener((l) -> {
            frmAddSeason.dispose();
        });
    }

    public void openForm() {
        frmAddSeason.setVisible(true);
    }

    private void btnCancel() {
        frmAddSeason.getBtncancel().addActionListener((l)->{resetFields();});
    }

}
