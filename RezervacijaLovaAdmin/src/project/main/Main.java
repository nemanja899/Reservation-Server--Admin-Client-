/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.main;

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
import project.form.controller.cordinator.MainFormControllerCordinator;
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
        try {
            // TODO code application logic here
            IntelliJTheme.setup(Main.class.getResourceAsStream(
                    "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_Nature_Green.theme.json"));
            Properties prop = new Properties();
         
            prop.load(Main.class.getClassLoader().getResourceAsStream(MyConstants.SERVER_FILE_NAME));
            Socket socket = new Socket(prop.getProperty(MyConstants.IP_ADRESS), Integer.parseInt(prop.getProperty(MyConstants.PORT)));
            Communication.getInstance().setSocket(socket);
          
            new LoginFormController().openForm();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(new LoginFormController().getFrmLogin(), "Neuspesno povezivanje sa serverom");
        }
    }

}
