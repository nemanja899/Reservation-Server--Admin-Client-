/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.formdev.flatlaf.IntelliJTheme;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import project.communication.Communication;
import project.form.controller.LoginFormController;
import utill.MyConstants;

/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        IntelliJTheme.setup(Main.class.getResourceAsStream(
                "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_Nature_Green.theme.json"));
        java.awt.EventQueue.invokeLater(() -> {
            try {
                Properties prop= new Properties();
                
                prop.load(Main.class.getClassLoader().getResourceAsStream(MyConstants.SERVER_FILE_NAME));
                Socket socket = new Socket(prop.getProperty(MyConstants.IP_ADRESS), Integer.parseInt(prop.getProperty(MyConstants.PORT)));
                Communication.getInstance().setSocket(socket);
          
                new LoginFormController().openForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(new LoginFormController().getFrmLogin(), "Neuspesno povezivanje sa serverom");

            }
        });
    }

}
