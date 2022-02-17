/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.User;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class UserOgranicenje implements Ogranicenje {

    @Override
    public void addPrecondition(GeneralDObject odk) throws ValidationException {
        User user = (User) odk;
        Validator.startValidate().validateNullObject(user.getLovackoDrustvoid(), "Lovacko Drustvo mora biti izabrano")
                .validateInputPattern(user.getEmail(), "^[a-zA-Z]+[a-zA-Z0-9]*@.{3,}$", "Email ne sme poceti brojem, mora sadrzati @ i nakon mora imati bar 3 slova")
                .validateInputPattern(user.getPassword(), "^[a-zA-Z]{1}.{4,}$", "Sifra ne sme poceti brojem i mora imati bar 5 karaktera")
                .validateInputPattern(user.getName(), "^[A-Z]{1}[a-z]{2,}$", "Prvo slovo u imenu mora biti veliko i ime mora sadrzati bar 3 slova")
                .validateInputPattern(user.getLastName(), "^[A-Z]{1}[a-z]{1,}$", "Prvo slovo u prezimenu mora biti veliko i prezime mora imati bar dva slova")
                .throwIfInvalide();
    }

    @Override
    public void updatePrecondition(GeneralDObject odk) throws ValidationException {
        User user = (User) odk;
        Validator.startValidate().validateNullObject(user.getLovackoDrustvoid(), "Lovacko Drustvo mora biti izabrano")
                .validateInputPattern(user.getEmail(), "^[a-zA-Z]+[a-zA-Z0-9]*@.{3,}$", "Email ne sme poceti brojem, mora sadrzati @ i nakon mora imati bar 3 slova")
                .validateInputPattern(user.getPassword(), "^[a-zA-Z]{1}.{4,}$", "Sifra ne sme poceti brojem i mora imati bar 5 karaktera")
                .validateInputPattern(user.getName(), "^[A-Z]{1}[a-z]{2,}$", "Prvo slovo u imenu mora biti veliko i ime mora sadrzati bar 3 slova")
                .validateInputPattern(user.getLastName(), "^[A-Z]{1}[a-z]{1,}$", "Prvo slovo u prezimenu mora biti veliko i prezime mora imati bar dva slova")
                .throwIfInvalide();
    }

    @Override
    public void deletePrecondition(GeneralDObject odk) throws ValidationException {
        User user = (User) odk;
        Validator.startValidate()
                .validateInputPattern(user.getEmail(), "^[a-zA-Z]+[a-zA-Z0-9]*@.{3,}$", "Email ne sme poceti brojem, mora sadrzati @ i nakon mora imati bar 3 slova")
                .throwIfInvalide();
    }

}
