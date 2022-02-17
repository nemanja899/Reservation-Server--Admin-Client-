/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller.cordinator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.JLabel;

import javax.swing.JToggleButton;
import project.controller.Controller;
import project.form.controller.ConfigDataBaseFormController;
import project.form.controller.ConfigServerFormController;

import project.form.design.MainForm;
import project.server.communication.Communication;

import project.server.thread.ServerThread;
import utill.MyConstants;

/**
 *
 * @author User
 */
public class MainFormControllerCordinator {
    
    private static MainFormControllerCordinator instance;
    private MainForm frmMain;
    private JLabel serverStatus;
    private JToggleButton serverButton;
    private ServerThread server;
    
    private MainFormControllerCordinator() {
        frmMain = new MainForm();
        prepareComponents();
        setListeners();
        
    }
    
    private void setListeners() {
        menuItemsListeners();
        buttonListener();
        exitListener();
    }
    
    public static MainFormControllerCordinator getInstance() {
        if (instance == null) {
            instance = new MainFormControllerCordinator();
        }
        return instance;
    }
    
    private void menuItemsListeners() {
        
    }
    
    public void openForm() {
        frmMain.setVisible(true);
    }
    
    private void prepareComponents() {
        serverStatus = new JLabel();
        serverStatus.setOpaque(true);
        serverButton = new JToggleButton();
        
        frmMain.getPnlServer().add(serverButton, BorderLayout.WEST);
        frmMain.getPnlServer().add(serverStatus, BorderLayout.CENTER);
        
        frmMain.getPnlServer().setBorder(BorderFactory.createTitledBorder("Server"));
        frmMain.getPnlServer().revalidate();
        frmMain.getPnlServer().repaint();
        
        setFields(false);
        
    }
    
    private void setFields(boolean b) {
        if (b) {
            serverStatus.setText("Server je pokrenut!");
            serverStatus.setForeground(Color.white);
            serverButton.setText("STOP SERVER");
            serverButton.setForeground(Color.white);
            serverButton.setBackground(Color.green);
        } else {
            serverStatus.setText("Server nije pokrenut!!");
            serverStatus.setForeground(Color.white);
            serverButton.setBackground(Color.red);
            serverButton.setForeground(Color.white);
            serverButton.setText("START SERVER");
        }
        
    }
    
    public MainForm getFrmMain() {
        return frmMain;
    }
    
    private void buttonListener() {
        startStopServer();
        menuDataBase();
        menuServer();
    }

    public void menuServer() {
        frmMain.getMenuItemServer().addActionListener((l) -> {
            new ConfigServerFormController().openForm();
        });
    }
    
    public void menuDataBase() {
        frmMain.getMenuItemDatabase().addActionListener((l) -> {
            new ConfigDataBaseFormController().openForm();
        });
    }
    
    public void startStopServer() {
        serverButton.addActionListener((e) -> {
            if (serverButton.isSelected() && (server == null || !server.isAlive())) {
                try {

                    //  pokreni server
                    Properties properties = new Properties();
                    
                    properties.load(MainFormControllerCordinator.class.getClassLoader().getResourceAsStream(MyConstants.SERVER_FILE_NAME));
                    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(String.valueOf(properties.get(MyConstants.PORT))));
                    server = new ServerThread(serverSocket);
                    server.start();
                    setFields(true);
                    
                } catch (Exception ex) {
                    Logger.getLogger(MainFormControllerCordinator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                // zaustavi server
                if (server.getServerSocket().isBound() || server != null) {
                    try {
                        
                        server.zatvoriSveKlijente();
                        server.getServerSocket().close();
                        setFields(false);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFormControllerCordinator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        });
    }

    private void exitListener() {
        frmMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.zatvoriSveKlijente();
            }
            
        });
    }
    
}
