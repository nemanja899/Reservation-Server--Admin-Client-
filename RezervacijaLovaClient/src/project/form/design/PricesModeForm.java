/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.design;

import domain.Season;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
 *
 * @author User
 */
public class PricesModeForm extends javax.swing.JDialog {

    /**
     * Creates new form PricesModeForm
     */
    public PricesModeForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnView = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        cmbSeason = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Izaberi sezonu i opciju");
        setModal(true);
        setResizable(false);

        btnView.setText("Pogledaj");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnAdd.setText("Dodaj");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Izmeni");

        cmbSeason.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSeason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSeasonActionPerformed(evt);
            }
        });

        jLabel1.setText("Sezona");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbSeason, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnView, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSeason, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(45, 45, 45)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void cmbSeasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSeasonActionPerformed

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnView() {
        return btnView;
    }

    public JComboBox<Season> getCmbSeason() {
        return cmbSeason;
    }

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox cmbSeason;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
