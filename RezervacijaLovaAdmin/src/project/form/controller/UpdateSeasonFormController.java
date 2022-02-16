/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Porezi;
import domain.Season;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.UpdateSeasonForm;
import validation.Validator;

/**
 *
 * @author User
 */
public class UpdateSeasonFormController {

    private UpdateSeasonForm frmUpdateSeason;
    private Season selectedSeason;

    public UpdateSeasonFormController() {
        frmUpdateSeason = new UpdateSeasonForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        prepareFields();
        setListeners();
    }

    private void prepareFields() {
        setCmbSeason();
        resetFields();
    }

    private void setCmbSeason() {
        try {
            frmUpdateSeason.getCmbSeason().removeAllItems();  // Napomena sezone moraju da budu sortirane u rastucem redosledu da bi radilo!!!
            DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();

            for (Season season : getSeasonsByCurrentDate()) {
                cmbModel.addElement(season);
            }
            frmUpdateSeason.getCmbSeason().setModel(cmbModel);

            frmUpdateSeason.getCmbSeason().setSelectedIndex(-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmUpdateSeason, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    public List<Season> getSeasonsByCurrentDate() throws Exception {
        List<Season> seasonsDate = new ArrayList<>();
        List<Season> seasons = Controller.getInstance().getAllSeasons();

        if (!seasons.isEmpty()) {
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonth().getValue();

            boolean notFound = true;

            for (Season season : seasons) {
                if (notFound) {
                    if ((season.getSeasonStart() == year && month > 8) || (season.getSeasonEnd() == year && month <= 8)) {
                        notFound = false;
                        seasonsDate.add(season);
                    }
                } else {
                    seasonsDate.add(season);
                }

            }
            return seasonsDate;
        } else {

            throw new Exception("Sezone nisu ucitane");
        }
    }

    private void resetFields() {
        selectedSeason = null;
        frmUpdateSeason.getCmbSeason().setSelectedIndex(-1);
        frmUpdateSeason.getTxtPDV().setText("");
        frmUpdateSeason.getTxtSeason().setText("");
        frmUpdateSeason.getTxtProvision().setText("");
        frmUpdateSeason.getBtnDelete().setEnabled(false);
        frmUpdateSeason.getBtnUpdate().setEnabled(false);

    }

    private void setListeners() {
        btnBack();
        btnCancel();
        btnUpdate();
        btnDelete();
        cmbSeason();

    }

    private void btnBack() {
        frmUpdateSeason.getBtnBack().addActionListener((l) -> {
            frmUpdateSeason.dispose();
        });
    }

    private void btnCancel() {
        frmUpdateSeason.getBtnCancel().addActionListener((l) -> {
            resetFields();
        });
    }

    private void btnUpdate() {
        frmUpdateSeason.getBtnUpdate().addActionListener((l) -> {
            try {
                Validator.startValidate().validateStringToBigDecimal(frmUpdateSeason.getTxtPDV().getText().trim(), "Pdv mora biti decimalni broj odvajanje je . i pozitivan")
                        .validateStringToBigDecimal(frmUpdateSeason.getTxtProvision().getText().trim(), "Provizija mora biti decimalni broj odvajanje je . i pozitivan")
                        .validateSeasonString(frmUpdateSeason.getTxtSeason().getText().trim())
                        .throwIfInvalide();
                Porezi porezi = new Porezi();
                porezi.setSeason(frmUpdateSeason.getTxtSeason().getText().trim());
                porezi.setPDV(new BigDecimal(frmUpdateSeason.getTxtPDV().getText().trim()));
                porezi.setProvision(new BigDecimal(frmUpdateSeason.getTxtProvision().getText().trim()));
                Controller.getInstance().updatePorezi(porezi, selectedSeason);
                JOptionPane.showMessageDialog(frmUpdateSeason, "Sezona i porezi su izmenjeni", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                setCmbSeason();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmUpdateSeason, "Morate uneti decimalan broj za pdv i proviziju", "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frmUpdateSeason, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            }

        });
    }

    private void btnDelete() {
        frmUpdateSeason.getBtnDelete().addActionListener((l) -> {
            int option = JOptionPane.showConfirmDialog(frmUpdateSeason, "Da li zaista zelite da obrisete sezonu i poreze?");
            if (option == 0) {
                try {
                    Porezi porezi = new Porezi();
                    porezi.setSeason(selectedSeason.getSeason());
                    porezi.setPDV(new BigDecimal(frmUpdateSeason.getTxtPDV().getText().trim()));
                    porezi.setProvision(new BigDecimal(frmUpdateSeason.getTxtProvision().getText().trim()));
                    Controller.getInstance().deletePorezi(porezi);
                    JOptionPane.showMessageDialog(frmUpdateSeason, "Upsesno obrisana seazona i porezi", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    resetFields();
                    setCmbSeason();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frmUpdateSeason, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    resetFields();
                }
            }
        });
    }

    private void cmbSeason() {
        frmUpdateSeason.getCmbSeason().addActionListener((l) -> {
            if (frmUpdateSeason.getCmbSeason().getSelectedIndex() > -1) {
                try {
                    selectedSeason = (Season) frmUpdateSeason.getCmbSeason().getSelectedItem();
                    frmUpdateSeason.getBtnUpdate().setEnabled(true);
                    frmUpdateSeason.getTxtSeason().setText(selectedSeason.getSeason());
                    Porezi porezi = Controller.getInstance().searchPorezi(selectedSeason.getSeason());

                    frmUpdateSeason.getTxtPDV().setText(porezi.getPDV() + "");
                    frmUpdateSeason.getTxtProvision().setText(porezi.getProvision() + "");

                    frmUpdateSeason.getBtnDelete().setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frmUpdateSeason, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    resetFields();
                }
            }
        });
    }

    public void openForm() {
        frmUpdateSeason.setVisible(true);
    }
}
