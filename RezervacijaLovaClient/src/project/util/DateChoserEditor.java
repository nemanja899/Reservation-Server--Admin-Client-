/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.util;

import datechooser.beans.DateChooserCombo;
import datechooser.beans.DateChooserDialog;
import domain.Season;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import project.form.design.MainForm;

/**
 *
 * @author User
 */
public class DateChoserEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private DateChooserDialog dateChoser;
    private Date date;
    private JButton button;

    private Season season;
 
     

    public DateChoserEditor(Season s) {

        dateChoser = new DateChooserDialog();
        season = s;

        date = new Date();
        button = new JButton();
        button.setActionCommand("EDDIT");
        button.addActionListener(this);
        button.setOpaque(false);

        dateChoser.addCommitListener((e) -> {
          Calendar cal = dateChoser.getSelectedDate();
          
            if (season.isDateInSeason(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1)) {
                date = cal.getTime();
            }else date= null;
        });

    }

    @Override
    public Object getCellEditorValue() {

        return date;

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        date = (Date) value;
        return button;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("EDDIT".equals(e.getActionCommand())) {
            dateChoser.showDialog(new MainForm());
            fireEditingStopped();
        }
    }

}
