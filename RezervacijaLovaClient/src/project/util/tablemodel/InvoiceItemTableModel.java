/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util.tablemodel;

import domain.Animal;
import domain.Currency;
import domain.Invoice;
import domain.InvoiceItem;
import domain.Prices;
import java.math.BigDecimal;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author User
 */
public class InvoiceItemTableModel extends AbstractTableModel {

    private final Invoice invoice;
    private String[] columnNames = new String[]{"Redni broj", "Divljac", "Cena", "Br Ulovljenih", "Ukupna Cena", "Valuta"};

    public InvoiceItemTableModel(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public int getRowCount() {
        if (invoice.getInvoiceItems() == null) {
            return 0;
        } else {
            return invoice.getInvoiceItems().size();
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceItem item = invoice.getInvoiceItems().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getOrderNumber();
            case 1:
                return item.getPrices().getAnimal();
            case 2:
                return item.getPrices().getPrice();
            case 3:
                return item.getAnimalNo();
            case 4:
                return item.getTotalCost();
            case 5:
                return item.getCurrency();

            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column > columnNames.length) {
            return "n/a";
        } else {
            return columnNames[column];
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return Animal.class;
            case 2:
                return BigDecimal.class;
            case 3:
                return Integer.class;
            case 4:
                return BigDecimal.class;
            case 5:
                return Currency.class;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void addItem(Prices prices, Integer quantity) {
        InvoiceItem item = new InvoiceItem();
        item.setOrderNumber((long) invoice.getInvoiceItems().size() + 1);
        item.setPrices(prices);
        item.setAnimalNo(quantity);
        item.setCurrency(Currency.RSD);

        if (invoice.getInvoiceItems().contains(item)) {
            int ind = invoice.getInvoiceItems().indexOf(item);
            invoice.getInvoiceItems().get(ind).setAnimalNo(invoice.getInvoiceItems().get(ind).getAnimalNo() + item.getAnimalNo());
            invoice.getInvoiceItems().get(ind).setTotalCost(invoice.getInvoiceItems().get(ind).getTotalCost().add(item.getPrices().getPrice().multiply(new BigDecimal(quantity))));
            fireTableRowsUpdated(ind, ind);
        } else {
            item.setTotalCost(item.getPrices().getPrice().multiply(new BigDecimal(quantity)));
            invoice.getInvoiceItems().add(item);
            fireTableRowsInserted(invoice.getInvoiceItems().size() - 1, invoice.getInvoiceItems().size() - 1);
        }

        invoice.setTotalCost(invoice.getTotalCost().add(item.getPrices().getPrice().multiply(new BigDecimal(quantity))));

    }

    public void removeInvoiceItem(int rowIndex) {
        InvoiceItem item = invoice.getInvoiceItems().get(rowIndex);
        invoice.getInvoiceItems().remove(rowIndex);
        invoice.setTotalCost(invoice.getTotalCost().subtract(item.getPrices().getPrice().multiply(new BigDecimal(item.getAnimalNo()))));
        setOrderNumbers();
        fireTableDataChanged();
    }

    private void setOrderNumbers() {
        long orderNo = 0;
        for (InvoiceItem item : invoice.getInvoiceItems()) {
            item.setOrderNumber(++orderNo);
        }

    }

    public void editInvoiceItem(Integer animalNo, Prices p, InvoiceItem item, int row) {
        item.setPrices(p);
        item.setAnimalNo(animalNo);
        item.setTotalCost(item.getPrices().getPrice().multiply(new BigDecimal(animalNo)));
        this.invoice.getInvoiceItems().set(row, item);
        fireTableRowsUpdated(row, row);
    }

    public InvoiceItem getDataForRow(int row) {
        return this.invoice.getInvoiceItems().get(row);
    }
}
