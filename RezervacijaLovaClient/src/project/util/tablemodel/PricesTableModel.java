/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util.tablemodel;

import domain.Animal;
import domain.Currency;
import domain.LovackoDrustvo;
import domain.Prices;
import domain.Season;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import project.controller.Controller;
import project.util.FormMode;

/**
 *
 * @author User
 */
public class PricesTableModel extends AbstractTableModel {

    private String[] columnNames = new String[]{"Divljac", "Cena", "Valuta", "Pocetak lova", "Kraj lova"};
    private List<Prices> priceses; 
    private FormMode frmMode;
    private Season season;
    public PricesTableModel(FormMode mode,Season season) {
        frmMode=mode;
        priceses = new ArrayList<>();
        this.season=season;
    }

    @Override
    public int getRowCount() {
        return priceses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prices prices = priceses.get(rowIndex);
        switch (columnIndex) {

            case 0:
                return prices.getAnimal();
            case 1:
                return prices.getPrice();
            case 2:
                return prices.getCurrency();
            case 3:
                return prices.getSeasonStart();
            case 4:
                return prices.getSeasonEnd();
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(frmMode.equals(FormMode.FORM_VIEW) || (frmMode.equals(FormMode.FORM_UPDATE) && columnIndex==0))
            return false;
        return true;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Animal.class;
            case 1:
                return BigDecimal.class;
            case 2:
                return Currency.class;
            case 3:
                return Date.class;
            case 4:
                return Date.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Prices prices = priceses.get(rowIndex);
        switch (columnIndex) {

            case 0:
                prices.setAnimal((Animal) aValue);
                break;
            case 1:
                prices.setPrice((BigDecimal) aValue);
                break;
            case 2:
                prices.setCurrency(Currency.valueOf(((String) aValue).toUpperCase()));
            case 3:
                if (prices.getSeasonEnd() != null) {
                    if (prices.getSeasonEnd().after((Date) aValue)) {
                        prices.setSeasonStart((Date) aValue);
                    }
                } else {
                    prices.setSeasonStart((Date) aValue);
                }

                break;
            case 4:
                if (prices.getSeasonStart() != null) {
                    if (prices.getSeasonStart().before((Date) aValue)) {
                        prices.setSeasonEnd((Date) aValue);
                    }
                } else {
                    prices.setSeasonEnd((Date) aValue);
                }
                break;
            default:
                break;
        }
    }

    public void setPriceses(List<Prices> priceses) {
        this.priceses = priceses;
        fireTableDataChanged();
    }

    public void addPrices() {
        Prices p = new Prices();
        p.setSeason(season);
        p.setDrustvo(Controller.getInstance().getCurrentUser().getLovackoDrustvoid());
        p.setCurrency(Currency.RSD);
        priceses.add(p);
        fireTableRowsInserted(priceses.size() - 1, priceses.size() - 1);
    }

    public void deletePrices(int row) {
        priceses.remove(row);
        fireTableRowsDeleted(row, row);

    }

    public List<Prices> getPriceses() {
        return priceses;
    }

}
