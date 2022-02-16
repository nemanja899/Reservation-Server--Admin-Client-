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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author User
 */
public class PricesTableModel extends AbstractTableModel {
    private String[] columnNames=new String[] {"Lovacko drustvo","Divljac","Cena","Pocetak","Kraj"};
    private List<Prices> priceses;

    public PricesTableModel() {
        priceses= new ArrayList<>();
    }
    
    

    @Override
    public int getRowCount() {
      return  priceses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
        
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prices prices=priceses.get(rowIndex);
        switch(columnIndex){
            case 0:
                return prices.getDrustvo();
            case 1:
                return prices.getAnimal();
            case 2:
                return prices.getPrice()+"     "+prices.getCurrency().name();
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
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return LovackoDrustvo.class;
            case 1:
                return Animal.class;
            case 2:
                return String.class;
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
        Prices prices=priceses.get(rowIndex);
          switch(columnIndex){
            case 0:
                 prices.setDrustvo((LovackoDrustvo) aValue);
                 break;
            case 1:
                 prices.setAnimal((Animal) aValue);
                 break;
            case 2:
                String[] s=((String) aValue).split("\\s+");
                
                 prices.setPrice(new BigDecimal(s[0]));
                 prices.setCurrency(Currency.valueOf(s[1].toUpperCase()));
                 break;
            case 3:
                 prices.setSeasonStart((Date) aValue);
                 break;
            case 4:
                 prices.setSeasonEnd((Date) aValue);
                 break;
            default:
                break;
        }
    }

    public void setPriceses(List<Prices> priceses) {
        this.priceses = priceses;
    }

}
