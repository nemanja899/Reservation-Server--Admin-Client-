/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util.tablemodel;

import domain.Hunter;
import domain.LovackoDrustvo;
import domain.Reservation;
import domain.Season;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.UIManager.get;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author User
 */
public class ReservationTableModel extends AbstractTableModel{
private List<Reservation> reservations;
private String[] columns= new String[]{"Lovacko drustvo","Lovac","Sezona"};
    public ReservationTableModel() {
        reservations= new ArrayList<>();
    }


    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       Reservation reservation=reservations.get(rowIndex);
        switch(columnIndex){
            case 0:
                return reservation.getDrustvo();
            case 1:
                return reservation.getHunter();
            case 2:
                return reservation.getSeason();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return LovackoDrustvo.class;
            case 1:
                return Hunter.class;
            case 2:
                return Season.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    public void removeRows(List<Reservation> reservations){
        reservations.clear();
        fireTableDataChanged();
        setReservations(reservations);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Reservation reservation= reservations.get(rowIndex);
        switch(columnIndex){
            case 0:
                reservation.setDrustvo((LovackoDrustvo) aValue);
                break;
            case 1:
                reservation.setHunter((Hunter) aValue);
                break;
            case 2:
                reservation.setSeason((Season) aValue);
                break;
            default:
                break;
        }
    }

    public void deleteReservation(int selectedRow) {
        reservations.remove(selectedRow);
        fireTableRowsDeleted(selectedRow, selectedRow);
    }
    
    
}
