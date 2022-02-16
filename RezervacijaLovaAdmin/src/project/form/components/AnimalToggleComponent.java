/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.components;



import domain.Animal;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;


/**
 *
 * @author User
 */
public class AnimalToggleComponent extends javax.swing.JPanel {

    /**
     * Creates new form AnimalToggleComponent
     */
    private Animal animal;          // zivotinja od koje se uzima naziv i slika
    //private boolean selected;
   
    private Image imageIcon;            // potrebna je za deselect ikone
    private BufferedImage rawImage; // potrebna je za metodu creategrayscale
   

    /* public boolean isSelected() {
        return selected;
    }*/
    public AnimalToggleComponent(Animal animal) {
        initComponents();
        this.animal = animal;
      prepareComponents();

    }

    public Animal getAnimal() {
        return animal;
    }

    public void prepareComponents() {
        if(!animal.isAllowed()){
            tgglAnimal.setEnabled(false);
        }
        setIcon();
        setCaption();
    }

    public void setCaption() {
        lblAnimal.setText(animal.getName());  // TODO umesto get name stavi SHORT ENGLISH NAME
    }
            // postavljanje ikone na toggle button
    public void setIcon() {
        try {
            String name = animal.getName();
            tgglAnimal.setText("");
            rawImage = ImageIO.read(new File("img/" + name + ".jpeg"));
            imageIcon = rawImage.getScaledInstance(150, 150, WIDTH);

            tgglAnimal.setIcon(new ImageIcon(imageIcon));
        } catch (Exception ex) {
            try {
                setDefaultIcon();
            } catch (IOException ex1) {
                System.out.println("Default image not found");
            }

        }
    }

    public void setDefaultIcon() throws IOException {
        rawImage = ImageIO.read(new File("img/default.jpeg"));
        imageIcon = rawImage.getScaledInstance(150, 150, WIDTH);

        tgglAnimal.setIcon(new ImageIcon(imageIcon));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tgglAnimal = new javax.swing.JToggleButton();
        lblAnimal = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tgglAnimal.setBackground(java.awt.SystemColor.inactiveCaptionBorder);
        tgglAnimal.setForeground(new java.awt.Color(167, 93, 93));
        tgglAnimal.setText("jToggleButton1");
        tgglAnimal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tgglAnimalItemStateChanged(evt);
            }
        });
        tgglAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgglAnimalActionPerformed(evt);
            }
        });
        add(tgglAnimal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 180, 170));

        lblAnimal.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        lblAnimal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnimal.setText("jLabel1");
        add(lblAnimal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 170, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void tgglAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgglAnimalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tgglAnimalActionPerformed

    private void tgglAnimalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tgglAnimalItemStateChanged
       
    }//GEN-LAST:event_tgglAnimalItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblAnimal;
    private javax.swing.JToggleButton tgglAnimal;
    // End of variables declaration//GEN-END:variables

    public void createGrayScale() {
        //get image width and height
        int width = rawImage.getWidth();
        int height = rawImage.getHeight();

        //convert to grayscale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = rawImage.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                //calculate average
                int avg = (r + g + b) / 3;

                //replace RGB value with avg
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                rawImage.setRGB(x, y, p);
            }
        }

        tgglAnimal.setIcon(new ImageIcon(rawImage.getScaledInstance(150, 150, WIDTH)));

    }


    public Image getImageIcon() {
        return imageIcon;
    }
      
    public JToggleButton getTgglAnimal() {
        return tgglAnimal;
    }
    
}

