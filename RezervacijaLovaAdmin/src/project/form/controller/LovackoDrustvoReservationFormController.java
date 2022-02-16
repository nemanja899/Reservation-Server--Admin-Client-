/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.form.controller;

import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import domain.Animal;
import domain.LovackoDrustvo;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import project.form.components.AnimalToggleComponent;
import project.form.components.LovackoDrustvoListComponent;
import project.form.components.AnimalPanelComponent;

import javax.swing.ImageIcon;

import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

import domain.Season;

import project.controller.Controller;
import domain.Prices;
import project.form.components.decorator.SearchComponentDecorator;

import project.form.design.LovackoDrustvoReservationForm;
import project.util.tablemodel.PricesTableModel;


/**
 *
 * @author User
 */
public class LovackoDrustvoReservationFormController {

    private LovackoDrustvoReservationForm frmRezervation;
    private Season selectedSeason;
    private PricesTableModel pricesTableModel;
    private LovackoDrustvoListComponent drustvoListComponent;
    private List<Animal> selectedAnimals;
    private DefaultListModel<LovackoDrustvo> listModel;
    private AnimalPanelComponent pnlAnimalComponent;
    private LovackoDrustvo selectedLovackoDrustvo;
    private List<Prices> pricesBySeason;
    private List<Animal> animalsBySeason;
    private List<LovackoDrustvo> drustvaBySeason;

    public LovackoDrustvoReservationFormController(Season selectedSeason) {
        this.selectedSeason = selectedSeason;
        frmRezervation = new LovackoDrustvoReservationForm();
        setAllData();
        prepareFrmRezervation();
        setListeners();
    }

    public void setAllData() {
        try {
            drustvoListComponent = new LovackoDrustvoListComponent();
            selectedAnimals = new ArrayList<>();
            listModel = new DefaultListModel<>();

            pricesBySeason = Controller.getInstance().getAllPricesBySeason(selectedSeason);

            animalsBySeason = Controller.getInstance().getAllAnimalsBySeason(pricesBySeason);
            drustvaBySeason = Controller.getInstance().getAllDrustvoBySeason(pricesBySeason);

            pnlAnimalComponent = new AnimalPanelComponent(this);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmRezervation, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void prepareFrmRezervation() {
        prepareSeasonLabel();
        new Thread(() -> prepareJListDrustvo()).start();
        new Thread(() -> preparePnlAnimal(animalsBySeason)).start();
        new Thread(() -> setPanelAnimalDecorator()).start();

        new Thread(() -> prepareTable()).start();

    }

    // postavljanje naslova sezone
    private void prepareSeasonLabel() {
        frmRezervation.getLblSeason().setText(selectedSeason.toString());
    }

    public void setPanelAnimalDecorator() {

        new SearchComponentDecorator().decorate(pnlAnimalComponent, AnimalPanelComponent::Filter, frmRezervation.getPnlAnimal());

        frmRezervation.getPnlAnimal().revalidate();
        frmRezervation.getPnlAnimal().repaint();
    }

// priprema JList Lovacko drustvo
    private void prepareJListDrustvo() {

        fillListModel();
        drustvoListComponent.getjList().setModel(listModel);
        setListDecorator();

    }

// inicijalizacija dekorator filtera
    public void setListDecorator() {
        drustvoListComponent.setDrustva(drustvaBySeason);
        new SearchComponentDecorator().decorate(drustvoListComponent, LovackoDrustvoListComponent::Filter, frmRezervation.getPnlLovackoDrustvo());
    }

    //puni se JLista lovackih drustava 
    private void fillListModel() {
        listModel.removeAllElements();
        if (selectedAnimals.isEmpty()) {
            listModel.addAll(drustvaBySeason);  // puni se lista ako je selektovana zivotinja pa de selektovana
            return;                                                               // u tom slucaju lista selektovanih ce biti prazna
        }
        listModel.addAll(Controller.getInstance().getAllLovackoDrustvoByAnimals(selectedAnimals, drustvaBySeason, pricesBySeason, animalsBySeason)); // puni se Jlista selektovanim zivotnjama
    }

// punjenje panela sa zivotinjama iz liste
    private void preparePnlAnimal(List<Animal> animals) {

        for (int i = 0; i < animals.size(); i++) {
            AnimalToggleComponent component = new AnimalToggleComponent(animals.get(i)); // kreiranje panela zivotinja i slanje reference na listu selektovanih
            setPnlAnimalsListeners(component);                             // postavljanje listenera za toogle button za svaku Animal Componentu
            // dodavanje componenta zivotinje na frame panela
            pnlAnimalComponent.getPnlAnimlas().add(component);

        }

    }

    //postavljanje tabele
    public void prepareTable() {
        pricesTableModel = new PricesTableModel();
        frmRezervation.getTblPrices().setModel(pricesTableModel);
     
        if (selectedLovackoDrustvo == null) {
            frmRezervation.getTblPrices().setVisible(false);
            return;
        }
        fillTablePrices(selectedLovackoDrustvo, selectedSeason);

    }
    //punjenje tabele

    private void fillTablePrices(LovackoDrustvo drustvo, Season season) {
        pricesTableModel.setPriceses( Controller.getInstance().getAllPricesByDrustvo(drustvo, pricesBySeason));
        pricesTableModel.fireTableDataChanged();

    }

    // postavljanje lisenera
    private void setListeners() {
        setListListener();
        setBtnListeners();

        setFormListener();

    }

// postavljanje list lovackog drustva listenera
    private void setListListener() {
        drustvoListComponent.getjList().addListSelectionListener((javax.swing.event.ListSelectionEvent evt) -> {
            listLovackoDrustvoValueChanged(evt);
        });
    }

    private void listLovackoDrustvoValueChanged(javax.swing.event.ListSelectionEvent evt) {
        // TODO add your handling code here:
        selectedAnimals.clear();      // nakon svakog novog klika na listu lovakog drustva brise se lista selektovanih zivotinja
        // frmRezervation.getPnlAnimal().removeAll(); //  brise se i panel na kome se nalaze zivotinje
        pnlAnimalComponent.getPnlAnimlas().removeAll();

        if (drustvoListComponent.getjList().getSelectedIndex() > -1) {     // ako je nesto selektovano
            selectedLovackoDrustvo = drustvoListComponent.getjList().getSelectedValue();
            List<Animal> animalsByDrustvo = Controller.getInstance().getAllAnimalByLovackoDrustvo(selectedLovackoDrustvo, pricesBySeason, animalsBySeason);// uzima listu
            preparePnlAnimal(animalsByDrustvo);
            frmRezervation.getTblPrices().setVisible(true);
            selectedAnimals.clear();
            fillTablePrices(selectedLovackoDrustvo, selectedSeason);

        } else {
            preparePnlAnimal(Controller.getInstance().getAllAnimalsBySeason(pricesBySeason)); // napuni ponovo panel
            frmRezervation.getTblPrices().setVisible(false);
            selectedLovackoDrustvo = null;
            selectedAnimals.clear();
        }
        frmRezervation.getPnlAnimal().revalidate();    // ove dve funkcije sluze da se refresuje panel sa novim vrednostima
        frmRezervation.getPnlAnimal().repaint();

    }

// Liseneri za toogle button 
    public void setPnlAnimalsListeners(AnimalToggleComponent animalComponent) {
        animalComponent.getTgglAnimal().addItemListener((evt) -> {
            tgglAnimalItemStateChanged(evt, animalComponent);
        } // listeneri su da li stanje na njemu promenjeno
        );
    }

// Metoda za punjenje liste selektovane zivotinje i za punjenje JListe lovackih drustava na osnovu liste
    public void tgglAnimalItemStateChanged(ItemEvent evt, AnimalToggleComponent animalComponent) {
        if (animalComponent.getTgglAnimal().isSelected()) {
            selectedAnimals.add(animalComponent.getAnimal());
            fillListModel();                                            // punjenje Jliste lov drustava selektovanim zivotinjama
            animalComponent.createGrayScale();                          //zatamniti sliku

        } else {
            selectedAnimals.remove(animalComponent.getAnimal());        // uklanjanje deselektone zivotinje

            fillListModel();                                            // ponovno punjenje liste

            animalComponent.getTgglAnimal().setIcon(new ImageIcon(animalComponent.getImageIcon())); //vracanje prvobitne ikone u boji

        }
    }

    private void setBtnListeners() {
        setBtnMainFormListenr();
        setBtnSeasonListener();
        setBtnHunterListener();
    }

    private void setBtnMainFormListenr() {
        frmRezervation.getBtnBack().addActionListener((e) -> {

            frmRezervation.dispose();
        });
    }

    private void setBtnSeasonListener() {
        frmRezervation.getBtnSeason().addActionListener((e) -> {
            new SeasonReservationFormController().openForm();
            frmRezervation.dispose();
        });
    }

    private void setBtnHunterListener() {
        frmRezervation.getBtnEnterHunter().addActionListener((e) -> {
            new HunterReservationController(selectedSeason, selectedLovackoDrustvo, frmRezervation).openForm();
        });

    }

    private void setFormListener() {
        frmRezervation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                listModel.clear();
                selectedAnimals.clear();

                fillListModel();
                frmRezervation.getPnlAnimal().revalidate();
                frmRezervation.getPnlAnimal().repaint();

            }
        });
    }

    public void openForm() {
        frmRezervation.setVisible(true);
    }

    public List<Animal> getAnimalsBySeason() {
        return animalsBySeason;
    }

}
