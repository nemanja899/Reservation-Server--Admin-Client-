/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.components;

import domain.LovackoDrustvo;
import domain.Reservation;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import project.form.components.decorator.IComponentFilter;
import project.util.tablemodel.MyDefaultTableModel;


/**
 *
 * @author User
 */
public class ReservationTableComponent extends javax.swing.JPanel implements IComponentFilter<Reservation> {

    /**
     * Creates new form ReservationTableComponent
     */
    public ReservationTableComponent() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblViewReservation = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        tblViewReservation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblViewReservation);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
private List<Reservation> reservations;

    public JTable getTblViewReservation() {
        return tblViewReservation;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblViewReservation;
    // End of variables declaration//GEN-END:variables

    @Override
    public List<Reservation> getItems() {
        return reservations;
    }

    @Override
    public String getDomainName() {
        return "Rezervacije";
    }

    @Override
    public void clear() {
        ((MyDefaultTableModel)tblViewReservation.getModel()).setRowCount(0);
       ((MyDefaultTableModel)tblViewReservation.getModel()).fireTableDataChanged();
    }

    @Override
    public void addItem(Reservation item) {
       ((MyDefaultTableModel)tblViewReservation.getModel()).addRow(new Object[]{item.getDrustvo(),item.getHunter(),item.getSeason()});
       ((MyDefaultTableModel)tblViewReservation.getModel()).fireTableDataChanged();
    }

    public static boolean Filter(Reservation reservation, String str) {
        if (reservation.getHunter().getFullName().toLowerCase().contains(str.toLowerCase())
                || reservation.getHunter().getCountry().toLowerCase().contains(str.toLowerCase()) || reservation.getSeason().getSeason().toLowerCase().contains(str.toLowerCase())) {
            return true;
        }
        return false;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}
