/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import datechooser.beans.DateChooserCombo;
import domain.Animal;
import domain.Prices;
import domain.Season;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.swing.table.TableColumn;
import project.controller.Controller;

import project.form.design.PricesForm;
import project.util.DateChoserEditor;
import project.util.FormMode;
import project.util.tablemodel.PricesTableModel;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class PricesFormController {

    private PricesForm frmPrices;
    private PricesTableModel tableModel;
    private Season selectedSeason;
    private JComboBox<Animal> animals;

    private FormMode mode;

    public PricesFormController(FormMode mode, Season season, JDialog frm) {
        frmPrices = new PricesForm(frm);
        selectedSeason = season;
        this.mode = mode;
        initData();
        prepareComponents();
        setListeners();
    }

    public void initData() {

        tableModel = new PricesTableModel(mode, selectedSeason);
        animals = new JComboBox<>();
        frmPrices.getTblPrices().setModel(tableModel);
        frmPrices.getLblSeason().setText(selectedSeason.toString());
    }

    private void prepareComponents() {
        prepareBtn();
        prepareCmb();
        prepareTable();
    }

    private void prepareBtn() {
        if (mode.equals(FormMode.FORM_VIEW)) {
            frmPrices.getBtnAddPricesItem().setEnabled(false);
            frmPrices.getBtnSave().setEnabled(false);
            frmPrices.getBtnUpdate().setEnabled(false);
            frmPrices.getBtnDeletePricesItem().setEnabled(false);
            frmPrices.getBtnDelete().setEnabled(false);
        } else if (mode.equals(FormMode.FORM_UPDATE)) {
            frmPrices.getBtnAddPricesItem().setEnabled(false);
            frmPrices.getBtnDeletePricesItem().setEnabled(false);
            frmPrices.getBtnSave().setEnabled(false);
        } else {
            frmPrices.getBtnDelete().setEnabled(false);
            frmPrices.getBtnUpdate().setEnabled(false);
        }
    }

    private void prepareCmb() {  // puni se sa svim zivotinjama koje se ne nalaze u vec kreiranom cenovniku!!!!
        try {
            if (mode.equals(FormMode.FORM_ADD)) {
                for (Animal a : Controller.getInstance().getAllAnimalsByAllowedNotInPrices(selectedSeason)) {
                    animals.addItem(a);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prepareTable() {

        if (!mode.equals(FormMode.FORM_VIEW)) {

            TableColumn tcAnimal = frmPrices.getTblPrices().getColumnModel().getColumn(0);
            TableColumn tcSeasonStart = frmPrices.getTblPrices().getColumnModel().getColumn(3);
            TableColumn tcSeasonEnd = frmPrices.getTblPrices().getColumnModel().getColumn(4);
            tcSeasonStart.setCellEditor(new DateChoserEditor(selectedSeason));
            tcSeasonEnd.setCellEditor(new DateChoserEditor(selectedSeason));
            tcAnimal.setCellEditor(new DefaultCellEditor(animals));

        }
        if (mode.equals(FormMode.FORM_VIEW) || mode.equals(FormMode.FORM_UPDATE)) {
            fillTable();
        }

    }

    private void fillTable() {
        try {
            List<Prices> priceses = Controller.getInstance().getAllPricesesBySeasonByDrustvo(selectedSeason);

            tableModel.setPriceses(priceses);
        } catch (Exception ex) {
            Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setListeners() {
        btnListeners();
    }

    public void openForm() {
        frmPrices.setVisible(true);
    }

    private void btnListeners() {
        addItem();
        back();
        deleteItem();
        savePrices();
        deletePrices();
        updatePrices();

    }

    public void updatePrices() {
        frmPrices.getBtnUpdate().addActionListener((l) -> {
            if (frmPrices.getTblPrices().getSelectedRow() > -1) {
                try {
                    Prices p = tableModel.getPriceses().get(frmPrices.getTblPrices().getSelectedRow());
                    Controller.getInstance().edditPrices(p, new Object[]{selectedSeason, p.getAnimal(), p.getDrustvo()});
                    JOptionPane.showMessageDialog(frmPrices, "Stavka cenovnika je izmenjena", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frmPrices, "Nije selektovan cenovnik");
            }
        });
    }

    public void deletePrices() {
        frmPrices.getBtnDelete().addActionListener((l) -> {
            if (frmPrices.getTblPrices().getSelectedRow() > -1) {
                try {
                    Prices p = tableModel.getPriceses().get(frmPrices.getTblPrices().getSelectedRow());
                    int option = JOptionPane.showConfirmDialog(frmPrices, "Da li zelite da obrisete stavku cenovnika?");
                    if (option == 0) {
                        Controller.getInstance().deletePrices(p);
                        tableModel.deletePrices(frmPrices.getTblPrices().getSelectedRow());
                    }
                    JOptionPane.showMessageDialog(frmPrices, "Stavka cenovnika je obrisana", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frmPrices, "Nije selektovan cenovnik");
            }
        });
    }

    public void savePrices() {
        frmPrices.getBtnSave().addActionListener((l) -> {

            try {
                if (!tableModel.getPriceses().isEmpty()) {
                    Controller.getInstance().addAllPrices(tableModel.getPriceses());

                    JOptionPane.showMessageDialog(frmPrices, "Sve stavke cenovnika su dodate", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frmPrices, "Cenovnik je prazan!");
                }
            } catch (Exception ex) {
                Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "SUCCESS", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void deleteItem() {
        frmPrices.getBtnDeletePricesItem().addActionListener((l) -> {
            if (frmPrices.getTblPrices().getSelectedRow() > -1 && !tableModel.getPriceses().isEmpty()) {
                tableModel.deletePrices(frmPrices.getTblPrices().getSelectedRow());
            } else {
                JOptionPane.showMessageDialog(frmPrices, "Nije selektovan red u tabeli", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void back() {
        frmPrices.getBtnBack().addActionListener((l) -> {
            frmPrices.dispose();
        });
    }

    public void addItem() {
        frmPrices.getBtnAddPricesItem().addActionListener((l) -> {
            if (tableModel.getPriceses().isEmpty()) {
                tableModel.addPrices();
            }else {
                try {
                    Prices p=tableModel.getPriceses().get(tableModel.getPriceses().size()-1);
                    Validator.startValidate().validateNullObject(p.getSeason(), "Sezona mora biti uneta")
                            .validateNullObject(p.getAnimal(), "Zivotinja mora biti izabrana")
                            .validateNullObject(p.getDrustvo(), "Lovacko Drustvo mora biti uneto")
                            .validateNullObject(p.getSeasonStart(), "Pocetak sezone mora biti izabrano")
                            .validateNullObject(p.getSeasonEnd(), "Kraj sezone mora biti izabrano")
                            .validateBigDecimal(p.getPrice(), "Cena mora biti veca od nule")
                            .throwIfInvalide();
                    tableModel.addPrices();
                } catch (ValidationException ex) {
                    Logger.getLogger(PricesFormController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(frmPrices, ex.getMessage(), "WARNNING", JOptionPane.WARNING_MESSAGE);
                   
                }
            }
        });
    }

}
