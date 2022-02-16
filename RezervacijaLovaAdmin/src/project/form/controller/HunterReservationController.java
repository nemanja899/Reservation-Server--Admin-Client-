/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import domain.Hunter;
import domain.LovackoDrustvo;
import domain.Season;
import validation.ValidationException;
import validation.Validator;
import project.controller.Controller;
import domain.Reservation;
import project.form.design.HunterReservationForm;
import project.form.design.LovackoDrustvoReservationForm;

/**
 *
 * @author User
 */
public class HunterReservationController {

    private Season selectedSeason;
    private LovackoDrustvo selectedLovackoDrustvo;
    private HunterReservationForm frmHunter;
    private LovackoDrustvoReservationForm frmReservation;
    private Hunter hunter;
    private String passport;
    private boolean lovacPostoji = false;

    public HunterReservationController(Season selectedSeason, LovackoDrustvo selectedLovackoDrustvo, LovackoDrustvoReservationForm frmReservation) {
        hunter = new Hunter();
        this.selectedSeason = selectedSeason;
        this.selectedLovackoDrustvo = selectedLovackoDrustvo;
        this.frmReservation = frmReservation;
        frmHunter = new HunterReservationForm(frmReservation, true);
        prepareComponents();
    }

    private void prepareComponents() {
        setValues();
        if (selectedLovackoDrustvo != null) {
            frmHunter.getLblLovackoDrustvo().setText(selectedLovackoDrustvo.toString());
            frmHunter.getLblLovackoDrustvo().setForeground(Color.YELLOW);
        }
        resetfields();
        setListeners();
    }

    public void setValues() {
        frmHunter.getDateChooser().setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        frmHunter.getLblLovackoDrustvo().setText("Niste selektovali Lovacko Drustvo");
        frmHunter.getLblLovackoDrustvo().setForeground(Color.red);
        frmHunter.getLblSeason().setText(selectedSeason.toString());
    }

    // postavljanje fildova na pocetnu vrednost
    private void resetfields() {
        frmHunter.getTxtPassport().setText("");
        frmHunter.getDateChooser().setSelectedDate(Calendar.getInstance());
        passport = "";
        frmHunter.getTxtFullName().setText("");
        frmHunter.getTxtCountry().setText("");
        frmHunter.getDateChooser().setEnabled(true);
        lovacPostoji = false;
        frmHunter.getBtnUpdate().setEnabled(false);
        hunter = null;

    }

    private void setListeners() {
        setTxtFieldListeners();
        setButtonListeners();
    }

    private void setTxtFieldListeners() {
        frmHunter.getTxtPassport().addActionListener((e) -> {
            frmHunter.getBtnSearch().setEnabled(true);
        });
        frmHunter.getTxtFullName().addActionListener((e) -> {
            frmHunter.getBtnSearch().setEnabled(false);
        });
        frmHunter.getTxtCountry().addActionListener((e) -> {
            frmHunter.getBtnSearch().setEnabled(false);
        });
    }

    private void setButtonListeners() {
        searchHunter();
        updateHunter();
        reservate();
        cancel();
        back();
    }

    // pretraga ako lovac vec postoji
    public void searchHunter() {
        frmHunter.getBtnSearch().addActionListener((e) -> {
            try {
                Validator.startValidate()
                        .validateStringMinLength(frmHunter.getTxtPassport().getText().trim(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                        .throwIfInvalide();
                hunter = Controller.getInstance().searchHuNter(frmHunter.getTxtPassport().getText().trim());
                lovacPostoji = true;
                setFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmHunter, "Lovac nije nadjen " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                lovacPostoji = false;

            }
        });
    }

    // ako je nadjen lovac postavljanje vrednosti
    private void setFields() {
        passport = hunter.getpassportNo();
        frmHunter.getTxtPassport().setText(hunter.getpassportNo());
        frmHunter.getTxtFullName().setText(hunter.getFullName());
        frmHunter.getTxtCountry().setText(hunter.getCountry());

        Date date = hunter.getBirthDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        frmHunter.getDateChooser().setSelectedDate(cal);
        frmHunter.getBtnUpdate().setEnabled(true);
        frmHunter.getDateChooser().setEnabled(false);

    }

    // ako je pronadjen lovac mogu da se promene vrednosti
    private void updateHunter() {
        frmHunter.getBtnUpdate().addActionListener((e) -> {
            try {
                Validator.startValidate()
                        .validateStringMinLength(frmHunter.getTxtPassport().getText().trim(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                        .validateStringMinLength(frmHunter.getTxtCountry().getText().trim(), 3, "Zemnja ne moze biti kraci od 3 karaktera")
                        .validateStringMinLength(frmHunter.getTxtFullName().getText().trim(), 4, "Puno ime ne moze biti krace od 4 karaktera")
                        .throwIfInvalide();

                hunter.setpassportNo(frmHunter.getTxtPassport().getText().trim());
                hunter.setFullName(frmHunter.getTxtFullName().getText().trim());
                hunter.setCountry(frmHunter.getTxtCountry().getText().trim());
                hunter.setBirthDate(frmHunter.getDateChooser().getSelectedDate().getTime());
                Controller.getInstance().updateHunter(hunter, passport);
                lovacPostoji = true;
                JOptionPane.showMessageDialog(frmHunter, "Uspesno izmenjeno", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmHunter, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                resetfields();
                hunter = new Hunter();
                lovacPostoji = false;

            }
        });
    }

    private void reservate() {
        frmHunter.getBtnReservation().addActionListener((e) -> {
            try {
                Validator.startValidate()
                        .validateNullObject(selectedLovackoDrustvo, "Lovacko Drustvo")
                        .validateNullObject(selectedSeason, "Sezona")
                        .validateStringMinLength(frmHunter.getTxtPassport().getText().trim(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                        .validateStringMinLength(frmHunter.getTxtCountry().getText().trim(), 3, "Zemlja ne moze biti kraca od 3 karaktera")
                        .validateStringMinLength(frmHunter.getTxtFullName().getText().trim(), 4, "Puno ime ne moze biti krace od 4 karaktera")
                        .validateDatePattern(frmHunter.getDateChooser().getText())
                        .throwIfInvalide();
                Reservation reservation = new Reservation();

                if (!lovacPostoji) {
                    hunter = new Hunter();
                    hunter.setpassportNo(frmHunter.getTxtPassport().getText().trim());
                    hunter.setFullName(frmHunter.getTxtFullName().getText().trim());
                    hunter.setCountry(frmHunter.getTxtCountry().getText().trim());
                    hunter.setBirthDate(frmHunter.getDateChooser().getSelectedDate().getTime());

                    reservation.setDrustvo(selectedLovackoDrustvo);
                    reservation.setHunter(hunter);
                    reservation.setSeason(selectedSeason);
                    passport = "";
                } else {
                    Hunter h = new Hunter();
                    h.setpassportNo(passport);
                    reservation = new Reservation();
                    reservation.setDrustvo(selectedLovackoDrustvo);
                    reservation.setHunter(h);
                    reservation.setSeason(selectedSeason);
                }
                Controller.getInstance().reservate(reservation);
                if (lovacPostoji) {
                    JOptionPane.showMessageDialog(frmHunter, "Rezervacija je uspesna", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frmHunter.dispose();
                } else {
                    JOptionPane.showMessageDialog(frmHunter, "Rezervacija je uspesna!" + "\nLovac je sacuvan!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frmHunter.dispose();
                }

            } catch (Exception ex) {
                if (!lovacPostoji) {
                    JOptionPane.showMessageDialog(frmHunter, ex.getMessage()+"\nLovac ne moze da se sacuva!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frmHunter, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    // vraca formu na pocetno stanje

    private void cancel() {
        frmHunter.getBtnCancel().addActionListener((e) -> {
            resetfields();
        });
    }

    private void back() {
        frmHunter.getBtnBack().addActionListener((e) -> {
            frmHunter.dispose();
        });

    }

    public void openForm() {
        frmHunter.setVisible(true);
    }

}
