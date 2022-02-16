/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.ConfigDatabaseForm;
import project.repository.connectionpool.DbConnectionPool;
import utill.MyConstants;

/**
 *
 * @author User
 */
public class ConfigDataBaseFormController {

    private ConfigDatabaseForm frmDataBase;

    public ConfigDataBaseFormController() {
        frmDataBase = new ConfigDatabaseForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        setListeners();
    }

    private void setListeners() {
        frmDataBase.getBtnConnect().addActionListener((l) -> {
            FileOutputStream fos = null;
            try {
                Properties prop = new Properties();
                prop.put(MyConstants.USERNAME, frmDataBase.getTxtUserName().getText().trim());
                prop.put(MyConstants.PASSWORD, frmDataBase.getTxtpasswiord().getText().trim());
                prop.put(MyConstants.URL, frmDataBase.getTxtUrl().getText().trim());
                fos = new FileOutputStream(MyConstants.DATA_BASE_FILE_PATH);
                prop.store(fos, "Konfiguracija baze je sacuvana");
                JOptionPane.showMessageDialog(frmDataBase, "Konfiguracija baze je sacuvana");
                frmDataBase.dispose();

            } catch (Exception ex) {
                Logger.getLogger(ConfigDataBaseFormController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(ConfigDataBaseFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void openForm() {
        frmDataBase.setVisible(true);
    }
}
