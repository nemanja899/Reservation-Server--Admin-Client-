/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import domain.Animal;
import domain.Currency;
import domain.Invoice;
import domain.InvoiceItem;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import project.controller.Controller;
import project.form.design.InvoiceForm;
import project.form.design.InvoiceModeForm;
import project.util.FormMode;
import project.util.tablemodel.InvoiceItemTableModel;
import validation.Validator;

/**
 *
 * @author User
 */
public class InvoiceFormController {
    
    private Reservation reservation;
    private InvoiceForm frmInvoice;
    private FormMode mode;
    private List<Prices> priceses;
    private Invoice invoice;
    SimpleDateFormat sdf;
    private Map<Animal, Prices> mapaPrice;
    
    private InvoiceItemTableModel tableModel;
    
    InvoiceFormController(Reservation r, Invoice i, FormMode formMode, JDialog form) {
        mapaPrice = new HashMap<>();
        frmInvoice = new InvoiceForm(form);
        mode = formMode;
        
        if (r != null) {
            reservation = r;
        }
        if (i != null) {
            invoice = i;
        }
        prepareComponents();
        setListeners();
    }
    
    private void prepareComponents() {
        setBtns();
        setValues();
        
    }
    
    private void setBtns() {
        frmInvoice.getPnlAddInvocieItem().setEnabled(false);
        if (mode.equals(FormMode.FORM_VIEW)) {
            frmInvoice.getPnlAddInvocieItem().setVisible(false);
            frmInvoice.getBtnSaveInvoice().setEnabled(false);
            frmInvoice.getBtnObradiInvoice().setEnabled(false);
            frmInvoice.getBtnAddItem().setEnabled(false);
            frmInvoice.getBtnEdditItem().setEnabled(false);
            frmInvoice.getBtnDeleteInvoiceItem().setEnabled(false);
            frmInvoice.getTxtNumber().setEnabled(false);
            frmInvoice.pack();
        } else {
            setPnlBtns(false);
        }
    }
    
    public void setPnlBtns(boolean active) {
        frmInvoice.getBtnAddInvoiceItem().setEnabled(active);
        frmInvoice.getBtnEdditInvoiceItem().setEnabled(active);
        frmInvoice.getSpnHuntedAnimals().setEnabled(active);
        frmInvoice.getBtnCancel().setEnabled(active);
        frmInvoice.getCmbAnimal().setEnabled(active);
        
        toggleButtons(!active);
    }
    
    public void setPnlBtnsAddEddit(boolean add) {
        frmInvoice.getBtnAddInvoiceItem().setEnabled(add);
        frmInvoice.getBtnEdditInvoiceItem().setEnabled(!add);
        frmInvoice.getSpnHuntedAnimals().setEnabled(true);
        frmInvoice.getBtnCancel().setEnabled(true);
        frmInvoice.getCmbAnimal().setEnabled(true);
        
        toggleButtons(false);
        
    }
    
    public void toggleButtons(boolean active) {
        frmInvoice.getBtnSaveInvoice().setEnabled(active);
        frmInvoice.getBtnObradiInvoice().setEnabled(active);
        frmInvoice.getBtnAddItem().setEnabled(active);
        frmInvoice.getBtnEdditItem().setEnabled(active);
        frmInvoice.getBtnDeleteInvoiceItem().setEnabled(active);
    }
    
    private void setValues() {
        try {
            sdf = new SimpleDateFormat("dd-MM-yyyy");
            frmInvoice.getTxtInvoiceDate().setDateFormat(sdf);
            
            if (mode.equals(FormMode.FORM_ADD)) {
                invoice = new Invoice();
                frmInvoice.getLblCurrency().setText(Currency.RSD.name());
                frmInvoice.getTxtInvoiceDate().setSelectedDate(Calendar.getInstance());
                frmInvoice.getLblSeason().setText(reservation.getSeason().toString());
                Porezi porezi = Controller.getInstance().searchPorezi(reservation.getSeason());
                frmInvoice.getLblPDV().setText(porezi.getPDV().toString());
                frmInvoice.getLblHunter().setText(reservation.getHunter().getpassportNo() + "," + reservation.getHunter().getFullName());
                frmInvoice.getLblLovackoDrustvo().setText(reservation.getDrustvo().getName() + "," + Controller.getInstance().getCurrentUser().getName()
                        + " " + Controller.getInstance().getCurrentUser().getLastName());
                frmInvoice.getTxtTotalCost().setText(0 + "");
                frmInvoice.getTxtNumber().setText("");
                
                invoice.setReservation(reservation);
                invoice.setHunter(reservation.getHunter());
                invoice.setPorezi(porezi);
                invoice.setCreatedBy(Controller.getInstance().getCurrentUser());
                invoice.setObradjen(false);
                invoice.setDateCreated(sdf.parse(frmInvoice.getTxtInvoiceDate().getText()));
                invoice.setTotalCost(BigDecimal.ZERO);
                
                setPnlItemsValues();
                
                tableModel = new InvoiceItemTableModel(invoice);
                
            } else if (mode.equals(FormMode.FORM_UPDATE)) {
                
                frmInvoice.getLblCurrency().setText(invoice.getCurrency().name());
                Calendar cal = Calendar.getInstance();
                cal.setTime(invoice.getDateCreated());
                frmInvoice.getTxtInvoiceDate().setSelectedDate(cal);
                frmInvoice.getTxtInvoiceDate().setText(invoice.getDateCreated().toString());
                if (invoice.getNumber() != null && !invoice.getNumber().equals("")) {
                    frmInvoice.getTxtNumber().setText(invoice.getNumber());
                    
                } else {
                    frmInvoice.getTxtNumber().setText("");
                }
                frmInvoice.getLblHunter().setText(invoice.getReservation().getHunter().getpassportNo() + "," + reservation.getHunter().getFullName());
                frmInvoice.getLblLovackoDrustvo().setText(Controller.getInstance().getCurrentUser().getName()
                        + " " + Controller.getInstance().getCurrentUser().getLastName());
                frmInvoice.getLblSeason().setText("Sezona, " + invoice.getPorezi().getSeason());
                frmInvoice.getLblPDV().setText(invoice.getPorezi().getPDV().toString());
                frmInvoice.getTxtTotalCost().setText(invoice.getTotalCost().toString());
                
                setPnlItemsValues();
                
                tableModel = new InvoiceItemTableModel(invoice);
            } else {
                frmInvoice.getLblCurrency().setText(invoice.getCurrency().name());
                Calendar cal = Calendar.getInstance();
                cal.setTime(invoice.getDateCreated());
                frmInvoice.getTxtInvoiceDate().setSelectedDate(cal);
                frmInvoice.getTxtInvoiceDate().setText(invoice.getDateCreated().toString());
                frmInvoice.getTxtNumber().setText(invoice.getNumber());
                frmInvoice.getLblHunter().setText(invoice.getReservation().getHunter().getpassportNo() + "," + reservation.getHunter().getFullName());
                frmInvoice.getLblLovackoDrustvo().setText(Controller.getInstance().getCurrentUser().getName()
                        + " " + Controller.getInstance().getCurrentUser().getLastName());
                frmInvoice.getLblSeason().setText("Sezona, " + invoice.getPorezi().getSeason());
                frmInvoice.getLblPDV().setText(invoice.getPorezi().getPDV().toString());
                frmInvoice.getTxtTotalCost().setText(invoice.getTotalCost().toString());
                tableModel = new InvoiceItemTableModel(invoice);
                System.out.println(invoice.getDateCreated());
            }
            frmInvoice.getTblInvoiceItem().setModel(tableModel);
            frmInvoice.getTxtInvoiceDate().setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmInvoice, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setPnlItemsValues() {
        frmInvoice.getPnlInvoiceItem().setEnabled(false);
        frmInvoice.getTxtPrice().setText("");
        frmInvoice.getLblPricesCurrency().setText(Currency.RSD.name());
        fillAnimalCmb();
    }
    
    private void fillAnimalCmb() {
        try {
            priceses = Controller.getInstance().getAllPricesesBySeasonByDrustvo(new Season(invoice.getPorezi().getSeason()));
            DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();
            for (Prices p : priceses) {
                if (p.getAnimal().isAllowed()) {
                    cmbModel.addElement(p.getAnimal());
                    mapaPrice.put(p.getAnimal(), p);
                }
            }
            frmInvoice.getCmbAnimal().setModel(cmbModel);
            
        } catch (Exception ex) {
            Logger.getLogger(InvoiceFormController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frmInvoice, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setListeners() {
        
        activatePnlAddItem();
        activatePnlEdditItem();
        setCmbListener();
        setAddInvoiceItemListener();
        setEdditInvoiceItemListener();
        cancelListener();
        deleteItemListener();
        SaveInvoiceListener();
        ObradiInvoiceListener();
        backListener();
        
    }
    
    public void activatePnlEdditItem() {
        frmInvoice.getBtnEdditItem().addActionListener((e) -> {
            int row = frmInvoice.getTblInvoiceItem().getSelectedRow();
            if (row > -1) {
                frmInvoice.getPnlAddInvocieItem().setEnabled(true);
                setPnlBtnsAddEddit(false);
                InvoiceItem item = tableModel.getInvoice().getInvoiceItems().get(row);
                frmInvoice.getCmbAnimal().setSelectedItem(item.getPrices().getAnimal());
                frmInvoice.getTxtPrice().setText(item.getPrices().getPrice().toString());
                frmInvoice.getSpnHuntedAnimals().setValue(item.getAnimalNo());
            } else {
                JOptionPane.showMessageDialog(frmInvoice, "Nije selektovana stavka", "WARNNING", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    public void activatePnlAddItem() {
        frmInvoice.getBtnAddItem().addActionListener((e) -> {
            frmInvoice.getPnlAddInvocieItem().setEnabled(true);
            setPnlBtnsAddEddit(true);
        });
    }
    
    private void setCmbListener() {
        frmInvoice.getCmbAnimal().addActionListener((l) -> {
            if (frmInvoice.getCmbAnimal().getSelectedIndex() > -1) {
                frmInvoice.getTxtPrice().setText(mapaPrice.get(((Animal) frmInvoice.getCmbAnimal().getSelectedItem())).getPrice().toString());
            }
        });
    }
    
    private void setAddInvoiceItemListener() {
        frmInvoice.getBtnAddInvoiceItem().addActionListener((l) -> {
            try {
                if (frmInvoice.getCmbAnimal().getSelectedIndex() > -1) {
                    Prices prices = mapaPrice.get(frmInvoice.getCmbAnimal().getSelectedItem());
                    Integer quantity = (Integer) frmInvoice.getSpnHuntedAnimals().getValue();
                    if (quantity <= 0) {
                        throw new Exception("Kolicina ulovljenih mora biti veca od nule");
                        
                    }
                    ((InvoiceItemTableModel) frmInvoice.getTblInvoiceItem().getModel()).addItem(prices, quantity);
                    resetPnlFields();
                    setPnlBtns(false);
                } else {
                    throw new Exception("Nije selektovana zivotinja");
                }
                frmInvoice.getTxtTotalCost().setText(invoice.getTotalCost().toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frmInvoice, e.getMessage());
            }
        });
    }
    
    private void resetPnlFields() {
        frmInvoice.getCmbAnimal().setSelectedIndex(-1);
        frmInvoice.getTxtPrice().setText("");
        frmInvoice.getSpnHuntedAnimals().setValue(0);
    }
    
    private void setEdditInvoiceItemListener() {
        frmInvoice.getBtnEdditInvoiceItem().addActionListener((a) -> {
            int num = (int) frmInvoice.getSpnHuntedAnimals().getValue();
            if (num > 0 && frmInvoice.getCmbAnimal().getSelectedIndex() > -1) {
                int row = frmInvoice.getTblInvoiceItem().getSelectedRow();
                InvoiceItem item = invoice.getInvoiceItems().get(row);
                
                Prices p = mapaPrice.get(frmInvoice.getCmbAnimal().getSelectedItem());
                
                ((InvoiceItemTableModel) tableModel).editInvoiceItem(num, p, item, row);
                calculateInvoiceTotal();
                setPnlBtns(false);
                resetPnlFields();
            } else {
                JOptionPane.showMessageDialog(frmInvoice, "Broj ulovljenih zivotinja mora biti veci od nule i morate selektovati zivotinju");
            }
        });
    }
    
    private void calculateInvoiceTotal() {
        if (this.invoice != null && this.invoice.getInvoiceItems() != null) {
            BigDecimal invoiceTotal = this.invoice.getInvoiceItems().stream()
                    .map(item -> item.getPrices().getPrice().multiply(new BigDecimal(item.getAnimalNo())))
                    .reduce(BigDecimal.ZERO, (subtotal, item) -> subtotal.add(item));
            this.invoice.setTotalCost(invoiceTotal);
            frmInvoice.getTxtTotalCost().setText(String.valueOf(invoiceTotal));
        }
    }
    
    private void cancelListener() {
        frmInvoice.getBtnCancel().addActionListener((l) -> {
            resetPnlFields();
            setPnlBtns(false);
        });
    }
    
    private void SaveInvoiceListener() {
        frmInvoice.getBtnSaveInvoice().addActionListener((l) -> {
            try {
                if (mode.equals(FormMode.FORM_ADD)) {
                    invoice.setCurrency(Currency.valueOf(frmInvoice.getLblCurrency().getText().toUpperCase()));
                    for (InvoiceItem item : invoice.getInvoiceItems()) {
                        item.setInvoice(invoice);
                    }
                    invoice.setDateCreated(sdf.parse(frmInvoice.getTxtInvoiceDate().getText()));
                    invoice.setNumber(frmInvoice.getTxtNumber().getText().trim());
                    Controller.getInstance().addInvoice(invoice);
                    JOptionPane.showMessageDialog(frmInvoice, "Racun je sacuvan", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } else if (mode.equals(FormMode.FORM_UPDATE)) {
                    for (InvoiceItem item : invoice.getInvoiceItems()) {
                        item.setInvoice(invoice);
                    }
                    frmInvoice.getTxtInvoiceDate().setEnabled(true);
                    frmInvoice.getTxtInvoiceDate().setSelectedDate(Calendar.getInstance());
                    invoice.setDateCreated(sdf.parse(frmInvoice.getTxtInvoiceDate().getText()));
                    
                    invoice.setNumber(frmInvoice.getTxtNumber().getText().trim());
                    Controller.getInstance().updateInvoice(invoice, invoice.getId());
                    frmInvoice.getTxtInvoiceDate().setEnabled(false);
                    JOptionPane.showMessageDialog(frmInvoice, "Racun je sacuvan", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmInvoice, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void ObradiInvoiceListener() {
        frmInvoice.getBtnObradiInvoice().addActionListener((l) -> {
            try {
                invoice.setObradjen(true);
                if (mode.equals(FormMode.FORM_ADD)) {
                    Controller.getInstance().addInvoice(invoice);
                    JOptionPane.showMessageDialog(frmInvoice, "Racun je obradjen", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                } else if (mode.equals(FormMode.FORM_UPDATE)) {
                    for (InvoiceItem item : invoice.getInvoiceItems()) {
                        item.setInvoice(invoice);
                    }
                    Controller.getInstance().updateInvoice(invoice, invoice.getId());
                    JOptionPane.showMessageDialog(frmInvoice, "Racun je obradjen", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmInvoice, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void backListener() {
        frmInvoice.getBtnBack().addActionListener((l) -> {
            frmInvoice.dispose();
        });
    }
    
    private void deleteItemListener() {
        frmInvoice.getBtnDeleteInvoiceItem().addActionListener((l) -> {
            if (frmInvoice.getTblInvoiceItem().getSelectedRow() > -1) {
                tableModel.removeInvoiceItem(frmInvoice.getTblInvoiceItem().getSelectedRow());
                frmInvoice.getTxtTotalCost().setText(invoice.getTotalCost().toPlainString());
            } else {
                JOptionPane.showMessageDialog(frmInvoice, "Morate selektovati stavku", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    void openForm() {
        frmInvoice.setVisible(true);
    }
    
}
