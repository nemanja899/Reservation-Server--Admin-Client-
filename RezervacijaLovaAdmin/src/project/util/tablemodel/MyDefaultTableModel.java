/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util.tablemodel;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class MyDefaultTableModel extends DefaultTableModel{
    
     public MyDefaultTableModel(String[] columnNames,int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
}
