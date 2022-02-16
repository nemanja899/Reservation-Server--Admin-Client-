/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.io.FileOutputStream;
import java.util.Properties;
import javax.swing.JOptionPane;
import project.form.controller.cordinator.MainFormControllerCordinator;
import project.form.design.ConfigServerForm;
import utill.MyConstants;

/**
 *
 * @author User
 */
public class ConfigServerFormController {

    private ConfigServerForm frmConfigServer;

    public ConfigServerFormController() {
        frmConfigServer = new ConfigServerForm(MainFormControllerCordinator.getInstance().getFrmMain(), true);
        setListeners();
    }

    private void setListeners() {
        frmConfigServer.getBtnSave().addActionListener((l) -> {
            Properties prop = new Properties();
            try {
                String IP=frmConfigServer.getTxtIP().getText().trim();
                String port= frmConfigServer.getTxtPort().getText().trim();
                prop.put(MyConstants.IP_ADRESS, IP);
                prop.put(MyConstants.PORT, port);
                FileOutputStream fout= new FileOutputStream(MyConstants.SERVER_FILE_PATH);
                prop.store(fout, "Konfiguracija za server je sacuvana");
                JOptionPane.showMessageDialog(frmConfigServer, "Konfiguracija za server je sacuvana");
                fout.close();
                frmConfigServer.dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frmConfigServer, e.getMessage());
            }
        });
    }
    
    public void openForm(){
        frmConfigServer.setVisible(true);
    }

}
