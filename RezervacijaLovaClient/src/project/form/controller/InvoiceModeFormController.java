/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Invoice;
import domain.Season;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.components.InvoiceListComponent;
import project.form.components.decorator.SearchComponentDecorator;
import project.form.conroller.cordinator.MainControllerCordinator;
import project.form.design.InvoiceModeForm;
import project.util.FormMode;

/**
 *
 * @author User
 */
public class InvoiceModeFormController {

    private InvoiceModeForm frmInvoiceMode;
    private InvoiceListComponent listComponent;
    private Season seasonByCurrentDate;
    private Invoice invoice;

    public InvoiceModeFormController() {
        frmInvoiceMode = new InvoiceModeForm(MainControllerCordinator.getInstance().getFrmMain());
        listComponent = new InvoiceListComponent();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        setSeasonByCurrentDate();
        setList();
    }

    private void setSeasonByCurrentDate() {

        try {
            List<Season> seasons = Controller.getInstance().getAllSeasons();
            if (!seasons.isEmpty()) {
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonth().getValue();
                for (Season season : seasons) {
                    if ((season.getSeasonStart() == year && month > 8) || (season.getSeasonEnd() == year && month <= 8)) {
                        seasonByCurrentDate = season;
                        break;
                    }
                }
            } else {

                throw new Exception("Sezone nisu ucitane");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmInvoiceMode, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setList() {
        try {
            List<Invoice> invoices = Controller.getInstance().getAllInvoicesByDrustvo();
         
            
            DefaultListModel listModel= new DefaultListModel();
            for(Invoice i:invoices){
                listModel.addElement(i);
              
            }
            listComponent.getListInvoice().setModel(listModel);           
            listComponent.setInvoices(invoices);
            new SearchComponentDecorator().decorate(listComponent, InvoiceListComponent::Filter, frmInvoiceMode.getPnlInvoice());
             frmInvoiceMode.getPnlInvoice().revalidate();
            frmInvoiceMode.getPnlInvoice().repaint();
        } catch (Exception ex) {
            Logger.getLogger(InvoiceModeFormController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmInvoiceMode, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setListeners() {
        setListListener();
        setBtnListeners();
        setWindowActiveListner();
    }

    private void setListListener() {
        listComponent.getListInvoice().addListSelectionListener((l) -> {
            if (listComponent.getListInvoice().getSelectedIndex() > -1) {
                invoice = (Invoice) listComponent.getListInvoice().getSelectedValue();
            
            } else {
                invoice = null;
            }
        });
    }

    private void setBtnListeners() {
        addInvoice();
        updateBtn();
        viewBtn();
        backBtn();
    }

    public void backBtn() {
        frmInvoiceMode.getBtnBack().addActionListener((l) -> {
            frmInvoiceMode.dispose();
            
        });
    }

    public void viewBtn() {
        frmInvoiceMode.getBtnView().addActionListener((l) -> {
            if (invoice != null) {
                new InvoiceFormController(invoice.getReservation(),invoice, FormMode.FORM_VIEW, frmInvoiceMode).openForm();
            } else {
                JOptionPane.showMessageDialog(frmInvoiceMode, "Racun nije selektovan", "WARNNING", JOptionPane.WARNING_MESSAGE);
            }

        });
    }

    public void updateBtn() {
        frmInvoiceMode.getBtnUpdate().addActionListener((l) -> {
            if (invoice != null && invoice.getPorezi().getSeason().equals(seasonByCurrentDate.getSeason()) && !invoice.isObradjen()) {
                new InvoiceFormController(invoice.getReservation(),invoice, FormMode.FORM_UPDATE, frmInvoiceMode).openForm();
            } else {
                JOptionPane.showMessageDialog(frmInvoiceMode, "Racun je ili obradjen ili nije selektovan ili sezona mora biti sadasnja", "WARNNING", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void addInvoice() {
        frmInvoiceMode.getBtnAdd().addActionListener((l) -> {
            new ViewReservationFormController(seasonByCurrentDate, frmInvoiceMode).openForm();
        });
    }

    public void openForm() {
        frmInvoiceMode.setVisible(true);
    }

    private void setWindowActiveListner() {
        frmInvoiceMode.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                frmInvoiceMode.getPnlInvoice().removeAll();
                frmInvoiceMode.getPnlInvoice().setLayout(new BorderLayout());
                setList();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

}
