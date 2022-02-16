/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.LovackoDrustvo;
import domain.Season;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import project.communication.Communication;
import project.controller.Controller;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.SendNotificationForm;


/**
 *
 * @author User
 */
public class SendNotificationPricesFormController {

    private SendNotificationForm frmNotification;
    private Season selectedSeason;
    private DefaultListModel<LovackoDrustvo> listModel;
    private LovackoDrustvo drustvo;
    private List<LovackoDrustvo> drustva;

    public SendNotificationPricesFormController() {
        frmNotification = new SendNotificationForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        listModel = new DefaultListModel<>();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        frmNotification.getListLovackoDrustvo().setModel(listModel);
        setCmbSeason();
        setListDrustvo();
    }

    private void setCmbSeason() {
        try {
            frmNotification.getCmbSeason().removeAllItems();  // Napomena sezone moraju da budu sortirane u rastucem redosledu da bi radilo!!!
            DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();

            for (Season season : getSeasonsByCurrentDate()) {
                cmbModel.addElement(season);
            }
            frmNotification.getCmbSeason().setModel(cmbModel);

            frmNotification.getCmbSeason().setSelectedIndex(-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmNotification, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

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

    private void setListDrustvo() {

        resetList();
        if (selectedSeason != null) {
            fillList(selectedSeason);
        }
    }

    private void resetList() {
        listModel.removeAllElements();
        drustva = null;
        frmNotification.getBtnNotify().setEnabled(false);
    }

    private void fillList(Season season) {
        try {
            drustva = Controller.getInstance().getAllDrustvoWithoutPrices(season);
            if (!drustva.isEmpty()) {
                listModel.addAll(drustva);
            } else {
                JOptionPane.showMessageDialog(frmNotification, "Sva drustva su kreirala cenovnik");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmNotification, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setListeners() {
        cmbListener();
        btnListener();
        ListListener();

    }

    private void cmbListener() {
        frmNotification.getCmbSeason().addActionListener((l) -> {
            if (frmNotification.getCmbSeason().getSelectedIndex() > -1) {
                selectedSeason = (Season) frmNotification.getCmbSeason().getSelectedItem();
                setListDrustvo();
            }else drustvo=null;
        });
    }

    private void btnListener() {
        btnNotifyAll();
        btnNotify();
        btnBack();
    }

    private void ListListener() {
        frmNotification.getListLovackoDrustvo().addListSelectionListener((l) -> {
            if (frmNotification.getListLovackoDrustvo().getSelectedIndex()> -1) {
                frmNotification.getBtnNotify().setEnabled(true);
                drustvo = (LovackoDrustvo) frmNotification.getListLovackoDrustvo().getSelectedValue();

            }else drustvo=null;
        });
    }

    private void btnNotifyAll() {
        frmNotification.getBtnNotifyAll().addActionListener((l) -> {
            if (!drustva.isEmpty()) {
                try {
                    Controller.getInstance().notifyAllDrustvaPrices(drustva,selectedSeason);
                   
                    JOptionPane.showMessageDialog(frmNotification, "Drustva koja nisu popunila cenovnik su obavestena");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frmNotification, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frmNotification, "Ne mozete da posaljete notifikaciju sva drustva su kreirala cenovnik");
            }
        });
    }

    private void btnBack() {
        frmNotification.getBtnBack().addActionListener((l) -> {
            frmNotification.dispose();
        });
    }

    private void btnNotify() {
        frmNotification.getBtnNotify().addActionListener((l) -> {
            try {
               Controller.getInstance().notifyDrustvaPrices(drustvo,selectedSeason);
               JOptionPane.showMessageDialog(frmNotification, "Drustvo je obavesteno");
            } catch (Exception ex) {
                Logger.getLogger(SendNotificationPricesFormController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frmNotification, ex.getMessage());
            }
        });
    }

    public void openForm() {
        frmNotification.setVisible(true);
    }
}
