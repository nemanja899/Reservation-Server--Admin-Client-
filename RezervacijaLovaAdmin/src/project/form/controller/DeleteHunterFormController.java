/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import validation.ValidationException;
import validation.Validator;
import project.controller.Controller;
import domain.Hunter;
import project.form.design.DeleteHunterForm;

/**
 *
 * @author User
 */
public class DeleteHunterFormController {

    private DeleteHunterForm frmDeleteHunter;
    private Hunter hunter;

    public DeleteHunterFormController() {
        frmDeleteHunter = new DeleteHunterForm();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        frmDeleteHunter.getDateBirthday().setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        resetFields();
        resetButtons();

    }

    private void setListeners() {
        searchHunter();
        deleteHunter();
        cancel();
        back();
    }

    private void resetFields() {
        frmDeleteHunter.getTxtPassport().setText("");
        frmDeleteHunter.getTxtCountry().setText("");
        frmDeleteHunter.getTxtFullName().setText("");
        frmDeleteHunter.getDateBirthday().setSelectedDate(Calendar.getInstance());
        hunter = null;
    }

    private void resetButtons() {
        frmDeleteHunter.getBtnDelete().setEnabled(false);
    }

    private void searchHunter() {
        frmDeleteHunter.getBtnSearch().addActionListener((l) -> {
            try {
                Validator.startValidate().validateStringMinLength(frmDeleteHunter.getTxtPassport().getText().trim(), 6, "Broj pasosa mora imati min 6 slova")
                        .throwIfInvalide();
                String passport = frmDeleteHunter.getTxtPassport().getText().trim();
                hunter = Controller.getInstance().searchHuNter(passport);
                setFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmDeleteHunter, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setFields() {
        frmDeleteHunter.getTxtFullName().setText(hunter.getFullName());
        frmDeleteHunter.getTxtCountry().setText(hunter.getCountry());
        Date date = hunter.getBirthDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        frmDeleteHunter.getDateBirthday().setSelectedDate(cal);
        frmDeleteHunter.getBtnDelete().setEnabled(true);
    }

    private void deleteHunter() {
        frmDeleteHunter.getBtnDelete().addActionListener((e) -> {
            try {
                int option = JOptionPane.showConfirmDialog(frmDeleteHunter, "Da li zelite da obrisete lovca?");
                if (option == 0) {
                    Controller.getInstance().deleteHunter(hunter);
                    JOptionPane.showMessageDialog(frmDeleteHunter, "Lovac je obrisan", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                resetFields();
                resetButtons();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frmDeleteHunter, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
                resetButtons();
            }
        });
    }

    private void cancel() {
        frmDeleteHunter.getBtnCancel().addActionListener((l) -> {
            resetButtons();
            resetFields();
        });
    }

    private void back() {
        frmDeleteHunter.getBtnBack().addActionListener((p) -> {
            frmDeleteHunter.dispose();
        });
    }

    public void openForm() {
        frmDeleteHunter.setVisible(true);
    }
}
