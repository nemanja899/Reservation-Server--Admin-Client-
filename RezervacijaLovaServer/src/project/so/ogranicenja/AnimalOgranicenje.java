/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.Animal;
import domain.GeneralDObject;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class AnimalOgranicenje implements Ogranicenje {

    @Override
    public void addPrecondition(GeneralDObject odk) throws ValidationException {
        Animal animal = (Animal) odk;
        Validator.startValidate().validateStringMinLength(animal.getName(), 6, "Puno ime mora imati min 6 slova")
                .validateStringMinLength(animal.getShortName(), 3, "Kratko ime mora imati min 3 slova")
                .validateIsSelected((animal.isAllowed() || !animal.isAllowed()), "Nije selektovano da li lov dozvoljen")
                .throwIfInvalide();
    }

    @Override
    public void updatePrecondition(GeneralDObject odk) throws ValidationException {
        Animal animal = (Animal) odk;
        Validator.startValidate().validateStringMinLength(animal.getName(), 6, "Puno ime mora imati min 6 slova")
                .validateStringMinLength(animal.getShortName(), 3, "Kratko ime mora imati min 3 slova")
                .validateIsSelected((animal.isAllowed() || !animal.isAllowed()), "Nije selektovano da li lov dozvoljen")
                .throwIfInvalide();
    }

    @Override
    public void deletePrecondition(GeneralDObject odk) throws ValidationException {
         Animal animal = (Animal) odk;
        Validator.startValidate().validateNullObject(animal.getId(), "ID Divljaci nije inicijalizovan")
                .throwIfInvalide();
    }

}
