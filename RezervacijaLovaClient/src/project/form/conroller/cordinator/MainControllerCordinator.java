/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.conroller.cordinator;

import communication.Operation;
import communication.Receiver;
import communication.Response;
import communication.ResponseType;
import domain.Animal;
import domain.LovackoDrustvo;
import domain.Reservation;
import domain.Season;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import project.communication.Communication;
import project.controller.Controller;
import project.form.design.MainForm;
import project.form.components.NotificationComponent;
import project.form.controller.InvoiceModeFormController;
import project.form.controller.PricesFormController;
import project.form.controller.PricesModeFormController;
import project.form.design.InvoiceModeForm;

/**
 *
 * @author User
 */
public class MainControllerCordinator {

    private static MainControllerCordinator instance;
    private MainForm frmMain;

    private MainControllerCordinator() {
        frmMain = new MainForm();
        
        setListeners();
        serverResponseThread();
        frmMain.getLblUser().setText(Controller.getInstance().getCurrentUser().getName()+" "+Controller.getInstance().getCurrentUser().getLastName());

    }

    public static MainControllerCordinator getInstance() {
        if (instance == null) {
            instance = new MainControllerCordinator();
        }
        return instance;
    }

    public void openForm() {
        frmMain.setVisible(true);
    }

    private void setListeners() {
        frmMain.getMenuItemPrices().addActionListener((e) -> {
            new PricesModeFormController().openForm();
        });
        frmMain.getMenuItemInvoice().addActionListener((l)->{
            new InvoiceModeFormController().openForm();
        });
    }

    public void serverResponseThread() {
        new Thread(() -> {
            try {
                while (true) {
                    Response response = (Response) new Receiver(Communication.getInstance().getSocket()).receive();
                    switch (response.getOperation()) {
                        case NOTIFY:
                            NotificationComponent notification = new NotificationComponent();
                            notification.prepareComponent(response);

                            break;
                        case GET_ALL_SEASON:

                            Controller.getInstance().putInMap(Operation.GET_ALL_SEASON, response);
                            break;

                        case GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON:

                            Controller.getInstance().putInMap(Operation.GET_ALL_ANIMALS_BY_ALLOWED_BY_DRUSTVO_BY_SEASON, response);
                            break;
                        case ADD_ALL_PRICES:

                            Controller.getInstance().putInMap(Operation.ADD_ALL_PRICES, response);
                            break;
                        case GET_ALL_PRICES_BY_SEASON:
                            Controller.getInstance().putInMap(Operation.GET_ALL_PRICES_BY_SEASON, response);
                            break;

                        case GET_ALL_ANIMALS:
                            Controller.getInstance().putInMap(Operation.GET_ALL_ANIMALS, response);
                            break;

                        case DELETE_PRICES:
                            Controller.getInstance().putInMap(Operation.DELETE_PRICES, response);
                            break;
                        case UPDATE_PRICES:
                            Controller.getInstance().putInMap(Operation.UPDATE_PRICES, response);
                            break;
                        case SEARCH_POREZ:
                            Controller.getInstance().putInMap(Operation.SEARCH_POREZ, response);
                            break;
                        case GET_ALL_INVOICE_BY_DRUSTVO:
                            Controller.getInstance().putInMap(Operation.GET_ALL_INVOICE_BY_DRUSTVO, response);
                            break;
                        case ADD_INVOICE:
                            Controller.getInstance().putInMap(Operation.ADD_INVOICE, response);
                            break;

                        case UPDATE_INVOICE:
                            Controller.getInstance().putInMap(Operation.UPDATE_INVOICE, response);
                            break;
                        case GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE:
                            Controller.getInstance().putInMap(Operation.GET_ALL_RESERVATION_BY_SEASON_BY_DRUSTVO_NO_INVOICE, response);
                            break;
                            case CLOSE:
                                JOptionPane.showMessageDialog(frmMain, response.getPoruka(), "DOVIDJENJA", WIDTH);
                                System.exit(0);
                                
                                break;
                        default:
                            break;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(MainControllerCordinator.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frmMain, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    public MainForm getFrmMain() {
        return frmMain;
    }

}
