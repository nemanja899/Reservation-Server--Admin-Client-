/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.WIDTH;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import validation.ValidationException;
import validation.Validator;
import project.controller.Controller;
import domain.Animal;
import project.form.design.AddAnimalForm;

/**
 *
 * @author User
 */
public class AddAnimalFormController {

    private AddAnimalForm frmAddAnimal;
    private JFileChooser frmImage;
    private Image img;

    public AddAnimalFormController() {
        frmAddAnimal = new AddAnimalForm();
        frmImage = new JFileChooser();
        prepareComponents();
        setListeners();

    }

    private void prepareComponents() {
        setButtonGroup();
        setImageForm();
        resetFields();

    }

    public void setButtonGroup() {

        ButtonGroup bg = new ButtonGroup();
        bg.add(frmAddAnimal.getRbLovYes());
        bg.add(frmAddAnimal.getRbLovNo());
    }

    private void setImageForm() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG Image (.jpeg)", "jpeg");
        frmImage.addChoosableFileFilter(filter);
    }

    private void resetFields() {
        frmAddAnimal.getTxtFullName().setText("");
        frmAddAnimal.getTxtShortName().setText("");
        frmAddAnimal.getLblAnimalImage().setText("");
        frmAddAnimal.getLblAnimalImage().setIcon(null);
        frmAddAnimal.getLblAnimalImage().repaint();
        img = null;
    }

    private void setListeners() {
        setAnimalBtnListeners();

    }

    private void setAnimalBtnListeners() {
        reset();
        uploadImage();
        back();
        saveAnimal();

    }

    public void saveAnimal() {
        frmAddAnimal.getBtnSave().addActionListener((e) -> {
            try {

                Validator.startValidate().validateStringMinLength(frmAddAnimal.getTxtFullName().getText().trim(), 6, "Puno ime mora imati min 6 slova")
                        .validateStringMinLength(frmAddAnimal.getTxtFullName().getText().trim(), 3, "Kratko ime mora imati min 3 slova")
                        .validateIsSelected((frmAddAnimal.getRbLovYes().isSelected() || frmAddAnimal.getRbLovNo().isSelected()), "Nije selektovano da li lov dozvoljen")
                        .throwIfInvalide();
                Animal animal = new Animal();
                animal.setName(frmAddAnimal.getTxtFullName().getText().trim());
                animal.setShortName(frmAddAnimal.getTxtShortName().getText().trim());
                if (frmAddAnimal.getRbLovYes().isSelected()) {
                    animal.setAllowed(true);
                } else {
                    animal.setAllowed(false);
                }
                Controller.getInstance().addAnimal(animal);
                if (img != null) {
                    ImageIO.write(imageToBufferedImage(img), "jpeg", new File("img/" + animal.getName() + ".jpeg"));
                }
                JOptionPane.showMessageDialog(frmImage, "Zivotinja je sacuvana", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                resetFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmImage, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void back() {
        frmAddAnimal.getBtnBack().addActionListener((e) -> {

            frmAddAnimal.dispose();
        });
    }

    public void reset() {
        frmAddAnimal.getBtnCancel().addActionListener((e) -> {
            resetFields();

        });
    }

    public void uploadImage() {
        frmAddAnimal.getBtnUpload().addActionListener((e) -> {
            int selectedOption = frmImage.showDialog(frmImage, "Otvori sliku");
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                File slika = frmImage.getSelectedFile();
                if (slika.getName().contains(".jpeg")) {
                    try {
                        img = ImageIO.read(slika).getScaledInstance(200, 200, WIDTH);
                        frmAddAnimal.getLblAnimalImage().setIcon(new ImageIcon(img));
                        frmAddAnimal.getLblAnimalImage().revalidate();
                        frmAddAnimal.getLblAnimalImage().repaint();
                        frmAddAnimal.getPnlAnimalImage().revalidate();
                        frmAddAnimal.getPnlAnimalImage().repaint();
                        System.out.println("Slika je izabrana");
                    } catch (IOException ex) {
                        Logger.getLogger(AddAnimalFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Izabrana je pogresna ekstenzija slike");
                }
            }

        });
    }

    public void openForm() {
        frmAddAnimal.setVisible(true);
    }

    public BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }
}
