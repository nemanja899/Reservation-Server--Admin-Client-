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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import validation.Validator;
import project.controller.Controller;
import domain.Animal;
import java.awt.BorderLayout;
import project.form.components.decorator.SearchComponentDecorator;
import project.form.components.AnimalListComponent;
import project.form.design.UpdateAnimalForm;

/**
 *
 * @author User
 */
public class UpdateAnimalFormController {

    private AnimalListComponent animalList;   //ovo mi treba kao lista komponente gde smestam sve zivotinje
    private UpdateAnimalForm frmUpdateAnimal;  // forma na kojoj se radi
    private DefaultListModel listModel;      // podaci u kome su smestene sve zivotinje
    private JFileChooser frmImage;        // za biranje slike
    private Long oldPk;                // zivotinja koja je selektovana
    private Image img;                      // nova slika koja je uplodovana
    private String oldName;                 // stari naziv slike koja se brise

    public UpdateAnimalFormController() {
        frmUpdateAnimal = new UpdateAnimalForm();
        listModel = new DefaultListModel();
        animalList = new AnimalListComponent();
        frmImage = new JFileChooser();
        prepareComponents();
        setListeners();
    }

    private void prepareComponents() {
        buttonGroup();
        setImageForm();
        prepareList();
        resetFields();
    }

    public void buttonGroup() {
        ButtonGroup bg = new ButtonGroup();
        bg.add(frmUpdateAnimal.getRbAllowedYes());
        bg.add(frmUpdateAnimal.getRbAllowedNo());
    }

    private void prepareList() {
        try {
            setListData();
            new SearchComponentDecorator().decorate(animalList, AnimalListComponent::Filter, frmUpdateAnimal.getPnlListAnimal());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmUpdateAnimal, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setListData() throws Exception {
        listModel.clear();

        List<Animal> animals = Controller.getInstance().getAllAnimals();
        listModel.addAll(animals);
        animalList.getListAnimals().setModel(listModel);
        animalList.setAnimals(animals);
    }

    private void resetFields() {
        oldPk = null;
        img = null;
        oldName = "";
        frmUpdateAnimal.getLblImage().setIcon(null);
        frmUpdateAnimal.getLblImage().repaint();
        frmUpdateAnimal.getTxtName().setText("");
        frmUpdateAnimal.getTxtShortName().setText("");
        frmUpdateAnimal.getBtnChangeImage().setEnabled(false);
        frmUpdateAnimal.getBtnDelete().setEnabled(false);
        frmUpdateAnimal.getBtnUpdate().setEnabled(false);
    }

    private void setListeners() {
        setListListener();
        setBtnListeners();
    }

    private void setListListener() {
        animalList.getListAnimals().addListSelectionListener((e) -> {
            if (animalList.getListAnimals().getSelectedIndex() > -1) {
                Animal animal = animalList.getListAnimals().getSelectedValue();
                frmUpdateAnimal.getTxtName().setText(animal.getName());
                frmUpdateAnimal.getTxtShortName().setText(animal.getShortName());
                frmUpdateAnimal.getTxtShortName().setText(animal.getShortName());
                oldPk = animal.getId();
                if (animal.isAllowed()) {
                    frmUpdateAnimal.getRbAllowedYes().setSelected(true);
                    frmUpdateAnimal.getRbAllowedNo().setSelected(false);
                } else {
                    frmUpdateAnimal.getRbAllowedYes().setSelected(false);
                    frmUpdateAnimal.getRbAllowedNo().setSelected(true);
                }
                frmUpdateAnimal.getBtnChangeImage().setEnabled(true);
                frmUpdateAnimal.getBtnDelete().setEnabled(true);
                frmUpdateAnimal.getBtnUpdate().setEnabled(true);
                img = null;
                oldName = animal.getName();// potrebno da se obrise prvobitna slika ako se zamenila pa moramo da cuvamo proslo ime za brisanje
                setIcon(animal);
            } else {
                resetFields();
            }
        });
    }

    private void setIcon(Animal animal) {

        try {

            frmUpdateAnimal.getLblImage().setIcon(new ImageIcon(ImageIO.read(new File("img/" + animal.getName() + ".jpeg")).getScaledInstance(200, 200, WIDTH)));
            frmUpdateAnimal.getLblImage().revalidate();
            frmUpdateAnimal.getLblImage().repaint();
        } catch (Exception ex) {
            System.out.println("Zivotinja nema sliku");

            try {
                frmUpdateAnimal.getLblImage().setIcon(new ImageIcon(ImageIO.read(new File("img/default.jpeg")).getScaledInstance(200, 200, WIDTH)));
            } catch (IOException ex1) {
                Logger.getLogger(UpdateAnimalFormController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    private void setBtnListeners() {
        changeImage();
        deleteAnimal();
        updateAnimal();
        resetAnimal();
        back();
    }

    public void back() {
        frmUpdateAnimal.getBtnBack().addActionListener((l) -> {
            frmUpdateAnimal.dispose();
        });
    }

    public void resetAnimal() {
        frmUpdateAnimal.getBtnCancel().addActionListener((l) -> {
            resetFields();
        });
    }

    public void updateAnimal() {
        frmUpdateAnimal.getBtnUpdate().addActionListener((l) -> {
            try {
                Validator.startValidate().validateStringMinLength(frmUpdateAnimal.getTxtName().getText().trim(), 5, "Puno ime mora imati vise od 5 slova")
                        .validateStringMinLength(frmUpdateAnimal.getTxtName().getText().trim(), 3, "Skraceno ime mora imati vise od 3 slova")
                        .validateIsSelected((frmUpdateAnimal.getRbAllowedNo().isSelected() || frmUpdateAnimal.getRbAllowedYes().isSelected()), "niste selektovali da li lov dozvoljen")
                        .throwIfInvalide();
                Animal a = new Animal();
                a.setName(frmUpdateAnimal.getTxtName().getText().trim());
                a.setShortName(frmUpdateAnimal.getTxtShortName().getText().trim());
                if (!frmUpdateAnimal.getRbAllowedNo().isSelected()) {
                    a.setAllowed(true);
                } else {
                    a.setAllowed(false);
                }
                Controller.getInstance().updateAnimal(a, oldPk);
                if (!a.isAllowed()) {
                    Controller.getInstance().sendAnimalNotAllowedNotification(a);
                }
                JOptionPane.showMessageDialog(frmUpdateAnimal, "Zivotinja je izmenjena", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                if (img != null) {
                    Files.deleteIfExists(Paths.get("img/" + oldName + ".jpeg"));
                    ImageIO.write(imageToBufferedImage(img), "jpeg", new File("img/" + a.getName() + ".jpeg"));
                }
                frmUpdateAnimal.getPnlListAnimal().removeAll();
                frmUpdateAnimal.getPnlListAnimal().setLayout(new BorderLayout());
                prepareList();
                resetFields();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frmUpdateAnimal, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                resetFields();
            }

        });
    }

    public void deleteAnimal() {
        frmUpdateAnimal.getBtnDelete().addActionListener((l) -> {
            int option = JOptionPane.showConfirmDialog(frmUpdateAnimal, "Da li zelite da obrisete zivotinju");
            if (option == 0) {
                try {
                    Animal animal = new Animal();
                    animal.setId(oldPk);
                    Controller.getInstance().deleteAnimal(animal);

                    Files.deleteIfExists(Paths.get("img/" + animal.getName() + ".jpeg"));

                    JOptionPane.showMessageDialog(frmUpdateAnimal, "Zivotinja je obrisana", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    frmUpdateAnimal.getPnlListAnimal().removeAll();
                    frmUpdateAnimal.getPnlListAnimal().setLayout(new BorderLayout());
                    prepareList();
                    resetFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frmUpdateAnimal, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    resetFields();
                }
            }
        });
    }

    public void changeImage() {
        frmUpdateAnimal.getBtnChangeImage().addActionListener((e) -> {

            int selectedOption = frmImage.showDialog(frmUpdateAnimal, "Otvori sliku");
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                File slika = frmImage.getSelectedFile();
                if (slika.getName().contains(".jpeg")) {
                    try {
                        img = ImageIO.read(slika).getScaledInstance(200, 200, WIDTH);
                        frmUpdateAnimal.getLblImage().setIcon(new ImageIcon(img));
                        frmUpdateAnimal.getLblImage().revalidate();
                        frmUpdateAnimal.getLblImage().repaint();
                        frmUpdateAnimal.getPnlListAnimal().revalidate();
                        frmUpdateAnimal.getPnlListAnimal().repaint();

                    } catch (IOException ex) {
                        Logger.getLogger(AddAnimalFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Izabrana je pogresna ekstenzija slike");
                }
            }

        });
    }

    public BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    public void openForm() {
        frmUpdateAnimal.setVisible(true);
    }

    private void setImageForm() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG Image (.jpeg)", "jpeg");
        frmImage.addChoosableFileFilter(filter);
    }
}
