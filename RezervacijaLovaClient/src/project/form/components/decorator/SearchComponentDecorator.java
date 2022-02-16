/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.components.decorator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author User
 */
public class SearchComponentDecorator {
    
    public  <T> JPanel decorate(IComponentFilter component, BiPredicate<T, String> userFilter,JPanel panel) {
      
     
      List<T> items = component.getItems();
      JTextField textField = new JTextField();
     textField.addMouseListener(new MouseListener() {
          @Override
          public void mouseClicked(MouseEvent e) {
              
          }

          @Override
          public void mousePressed(MouseEvent e) {
             
          }

          @Override
          public void mouseReleased(MouseEvent e) {
             
          }

          @Override
          public void mouseEntered(MouseEvent e) {
              textField.setEnabled(true);
          }

          @Override
          public void mouseExited(MouseEvent e) {
              textField.setEnabled(false);
          }
     });
      textField.getDocument().addDocumentListener(new DocumentListener() {
          @Override
          public void insertUpdate(DocumentEvent e) {
              filter();
          }

          @Override
          public void removeUpdate(DocumentEvent e) {
              filter();
          }

          @Override
          public void changedUpdate(DocumentEvent e) {
              filter();
          }

          private void filter() {
              component.clear();
              String s = textField.getText();
              for (T item : items) {
                  if(userFilter.test(item, s)){
                      component.addItem(item);
                     
                  }
              }
          }
      });

      
      panel.add(textField, BorderLayout.NORTH);
      //JScrollPane pane = new JScrollPane((Component) component);
      panel.add((Component) component,BorderLayout.CENTER);
      panel.setBorder(javax.swing.BorderFactory.createTitledBorder(component.getDomainName()));
      return panel;
  }

}
