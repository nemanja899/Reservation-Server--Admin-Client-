/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.LovackoDrustvo;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class LovackoDrustvoOgranicenje implements Ogranicenje {

    @Override
    public void AddPrecondition(GeneralDObject odk) throws ValidationException {
        LovackoDrustvo drustvo = (LovackoDrustvo) odk;
        Validator.startValidate().validateStringMinLength(drustvo.getName(), 6, "Ime drustva ne moze biti manje od 5 slova")
                .validateStringMinLength(drustvo.getCounty(), 3, "Ime opstine ne moze biti manja od 3 slova")
                .validateStringMinLength(drustvo.getAdress(), 5, "Adresa ne moze biti manja od 5 slova")
                .throwIfInvalide();
    }

    @Override
    public void UpdatePrecondition(GeneralDObject odk) throws ValidationException {
        LovackoDrustvo drustvo = (LovackoDrustvo) odk;
        Validator.startValidate().validateStringMinLength(drustvo.getName(), 6, "Ime drustva ne moze biti manje od 5 slova")
                .validateStringMinLength(drustvo.getCounty(), 3, "Ime opstine ne moze biti manja od 3 slova")
                .validateStringMinLength(drustvo.getAdress(), 5, "Adresa ne moze biti manja od 5 slova")
                .throwIfInvalide();
    }

    @Override
    public void DeletePrecondition(GeneralDObject odk) throws ValidationException {
         LovackoDrustvo drustvo = (LovackoDrustvo) odk;
        Validator.startValidate().validateNullObject(drustvo.getName(), "Ime Lovackog drustva nije inicijalizovano")
                .throwIfInvalide();
    }

}
