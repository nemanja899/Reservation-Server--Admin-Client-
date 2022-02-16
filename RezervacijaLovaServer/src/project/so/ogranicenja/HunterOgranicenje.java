/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.Hunter;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class HunterOgranicenje implements Ogranicenje {

    @Override
    public void AddPrecondition(GeneralDObject odk) throws ValidationException {
        Hunter hunter = (Hunter) odk;
        Validator.startValidate()
                .validateStringMinLength(hunter.getpassportNo(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                .validateStringMinLength(hunter.getCountry(), 3, "Zemnja ne moze biti kraci od 3 karaktera")
                .validateStringMinLength(hunter.getFullName(), 4, "Puno ime ne moze biti krace od 4 karaktera")
                .throwIfInvalide();
    }

    @Override
    public void UpdatePrecondition(GeneralDObject odk) throws ValidationException {
        Hunter hunter = (Hunter) odk;
        Validator.startValidate()
                .validateStringMinLength(hunter.getpassportNo(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                .validateStringMinLength(hunter.getCountry(), 3, "Zemnja ne moze biti kraci od 3 karaktera")
                .validateStringMinLength(hunter.getFullName(), 4, "Puno ime ne moze biti krace od 4 karaktera")
                .throwIfInvalide();
    }

    @Override
    public void DeletePrecondition(GeneralDObject odk) throws ValidationException {
        Hunter hunter = (Hunter) odk;
        Validator.startValidate()
                .validateStringMinLength(hunter.getpassportNo(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                .throwIfInvalide();
    }

}
