/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.main;

import com.formdev.flatlaf.IntelliJTheme;

import project.form.controller.cordinator.MainFormControllerCordinator;
import project.repository.connectionpool.DbConnectionPool;

/**
 *
 * @author User
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IntelliJTheme.setup(Main.class.getResourceAsStream(
                "/com/formdev/flatlaf/intellijthemes/themes/Gradianto_Nature_Green.theme.json"));
        
            MainFormControllerCordinator.getInstance().openForm();
            
           
      
        //Gradianto_Nature_Green
        //SolarizedDark.theme.json
        //Cobalt_2
    }

}
