/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Season;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.conroller.cordinator.MainControllerCordinator;
import project.form.design.PricesModeForm;
import project.util.FormMode;

/**
 *
 * @author User
 */
public class PricesModeFormController {

    private PricesModeForm frmMode;
    private Season selectedSeason;
    private List<Season> seasonsByCurrentDate;

    public PricesModeFormController() {
        seasonsByCurrentDate = new ArrayList<>();
        frmMode = new PricesModeForm(MainControllerCordinator.getInstance().getFrmMain(), true);
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        getSeasonsByCurrentDate();

    }

    public void getSeasonsByCurrentDate() {
        try {
            DefaultComboBoxModel<Season> cmbModel = new DefaultComboBoxModel();
            List<Season> seasons = Controller.getInstance().getAllSeasons();

            if (!seasons.isEmpty()) {
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonth().getValue();

                boolean notFound = true;

                for (Season season : seasons) {
                    cmbModel.addElement(season);
                    if (notFound) {
                        if ((season.getSeasonStart() == year && month > 8) || (season.getSeasonEnd() == year && month <= 8)) {
                            notFound = false;
                            seasonsByCurrentDate.add(season);
                        }
                    } else {
                        seasonsByCurrentDate.add(season);
                    }

                }
                frmMode.getCmbSeason().setModel(cmbModel);
                selectedSeason = (Season) frmMode.getCmbSeason().getSelectedItem();

            } else {

                throw new Exception("Sezone nisu ucitane");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmMode, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setListeners() {
        btnListeners();
        cmbListener();

    }

    private void btnListeners() {
        add();
        update();
        view();
    }

    public void view() {
        frmMode.getBtnView().addActionListener((t) -> {
            new PricesFormController(FormMode.FORM_VIEW, selectedSeason, frmMode).openForm();
        });
    }

    public void update() {
        frmMode.getBtnUpdate().addActionListener((t) -> {
            if (seasonsByCurrentDate.contains(selectedSeason)) {
                new PricesFormController(FormMode.FORM_UPDATE, selectedSeason, frmMode).openForm();
            } else {
                JOptionPane.showMessageDialog(frmMode, "Ne mozete izmeniti Cenovnik iz prethodnih sezona", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void add() {
        frmMode.getBtnAdd().addActionListener((l)
                -> {
            if (seasonsByCurrentDate.contains(selectedSeason)) {
                new PricesFormController(FormMode.FORM_ADD, selectedSeason, frmMode).openForm();
            } else {
                JOptionPane.showMessageDialog(frmMode, "Ne mozete dodati Cenovnik u prethodnoj sezoni", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void cmbListener() {
        frmMode.getCmbSeason().addActionListener((t) -> {
            if (frmMode.getCmbSeason().getSelectedIndex() > -1) {

                selectedSeason = (Season) frmMode.getCmbSeason().getSelectedItem();
                Calendar cal = Calendar.getInstance();
                Calendar season = Calendar.getInstance();

                if (selectedSeason.getSeasonStart() >= cal.get(Calendar.YEAR) && 9 > cal.get(Calendar.MONTH) + 1) {
                    frmMode.getBtnAdd().setEnabled(false);        // ovo je stavljeno da ne mogu da se dodaje cenovnik za prethodnu sezonuuu
                    frmMode.getBtnUpdate().setEnabled(false);       // tj ne moze da menja , ali mogu da se vide svi cenovnici po sezonis
                } else {
                    frmMode.getBtnAdd().setEnabled(true);
                    frmMode.getBtnUpdate().setEnabled(true);
                }

            }
        });
    }

    public void openForm() {
        frmMode.setVisible(true);
    }

}
