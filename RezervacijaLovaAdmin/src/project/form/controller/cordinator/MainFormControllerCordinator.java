/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller.cordinator;

import communication.Operation;
import communication.Request;
import communication.Sender;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JToggleButton;
import project.communication.Communication;

import project.form.controller.AddAnimalFormController;
import project.form.controller.AddDrustvoController;
import project.form.controller.AddSeasonFormController;
import project.form.controller.AddUserFormController;
import project.form.controller.DeleteHunterFormController;
import project.form.controller.SeasonReservationFormController;
import project.form.controller.SendNotificationPricesFormController;
import project.form.controller.UpdateAnimalFormController;
import project.form.controller.UpdateLovackoDrustvoFormController;
import project.form.controller.UpdateReservationFromController;
import project.form.controller.UpdateSeasonFormController;
import project.form.controller.ViewAnimalHuntedFormController;
import project.form.controller.ViewFinanceFormController;
import project.form.controller.ViewReservationFormController;
import project.form.design.MainForm;
import project.form.design.UpdateReservationForm;


/**
 *
 * @author User
 */
public class MainFormControllerCordinator {

    private static MainFormControllerCordinator instance;
    private MainForm frmMain;
    private JLabel serverStatus;
    private JToggleButton serverButton;
  

    private MainFormControllerCordinator() {
        frmMain = new MainForm();
   
        setListeners();

    }

    private void setListeners() {
        menuItemsListeners();

    }

    public static MainFormControllerCordinator getInstance() {
        if (instance == null) {
            instance = new MainFormControllerCordinator();
        }
        return instance;
    }

    private void menuItemsListeners() {
        dodajLovackoDrustvo();
        dodajDivljac();
        dodajRezervaciju();
        izmeniLovackoDrustvo();
        izmeniDivljac();
        obrisiLovca();
        pogledajRezervaciju();
        dodajPorezeSezonu();
        izmeniPorezeSezonu();
        obavestiDrustvaZaCenovnik();
        otkaziRezervaciju();
        pogledajProfit();
        pogledajUlovljeneDivljaciPoSezoni();       
        dodajKorisnika();        
        izlogujSe();
    }

    public void izlogujSe() {
        frmMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Request request = new Request(Operation.EXIT, null, null);
                    new Sender(Communication.getInstance().getSocket()).send(request);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frmMain, "Greska prilikom odjavljivanja");
                    
                }
            }
            
        });
    }

    public void dodajKorisnika() {
        frmMain.getMenuItemAddUser().addActionListener((l)->{
            new AddUserFormController().openForm();
        });
    }

    public void pogledajUlovljeneDivljaciPoSezoni() {
        frmMain.getMenuItemHuntedAnimalPerSeason().addActionListener((l) -> {
            new ViewAnimalHuntedFormController().openForm();
        });
    }

    public void pogledajProfit() {
        frmMain.getMenuItemFinance().addActionListener((l) -> {
            new ViewFinanceFormController().openForm();
        });
    }

    public void otkaziRezervaciju() {
        frmMain.getMenuItemUpdateReservation().addActionListener((l) -> {
            new UpdateReservationFromController().openForm();
        });
    }

    public void obavestiDrustvaZaCenovnik() {
        frmMain.getMenuItemNotifyDrustvoPrices().addActionListener((l) -> {
            new SendNotificationPricesFormController().openForm();
        });
    }

    public void izmeniPorezeSezonu() {
        frmMain.getMenuItemUpdateSeason().addActionListener((l) -> {
            new UpdateSeasonFormController().openForm();
        });
    }

    public void dodajPorezeSezonu() {
        frmMain.getMenuItemAddSeason().addActionListener((l) -> {
            new AddSeasonFormController().openForm();
        });
    }

    public void pogledajRezervaciju() {
        frmMain.getMenuItemViewReservation().addActionListener((e) -> {
            new ViewReservationFormController().openForm();
        });
    }

    public void obrisiLovca() {
        frmMain.getMenuItemDeleteHunter().addActionListener((l) -> {
            new DeleteHunterFormController().openForm();
        });
    }

    public void izmeniDivljac() {
        frmMain.getMnItmChangeAnimal().addActionListener((e) -> {
            new UpdateAnimalFormController().openForm();
        });
    }

    public void izmeniLovackoDrustvo() {
        frmMain.getMenuItemUpdateDrustvo().addActionListener((e) -> {
            new UpdateLovackoDrustvoFormController().openForm();
        });
    }

    public void dodajRezervaciju() {
        frmMain.getMenuItemReservate().addActionListener((e) -> {
            new SeasonReservationFormController().openForm();
            
        });
    }

    public void dodajDivljac() {
        frmMain.getMnItmAddAnimal().addActionListener((e) -> {
            new AddAnimalFormController().openForm();
            
        });
    }

    public void dodajLovackoDrustvo() {
        frmMain.getMenuItemAddDrustvo().addActionListener((e) -> {
            new AddDrustvoController().openForm();
            
        });
    }

    public void openForm() {
        frmMain.setVisible(true);
    }



   
    public MainForm getFrmMain() {
        return frmMain;
    }


 
}
