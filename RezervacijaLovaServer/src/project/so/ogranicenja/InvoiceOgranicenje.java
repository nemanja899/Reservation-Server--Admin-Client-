/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import communication.Operation;
import domain.GeneralDObject;
import domain.Invoice;
import java.math.BigDecimal;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class InvoiceOgranicenje implements Ogranicenje{

   

    @Override
    public void AddPrecondition(GeneralDObject odk) throws ValidationException {
        Invoice invoice=(Invoice) odk;
        if (!invoice.isObradjen()) {
            Validator.startValidate().validateNullObject(invoice.getReservation(), "Rezervacija mora biti izabrana")
                  
                    .validateNullObject(invoice.getDateCreated(), "Racun mora imati datum")
                    .throwIfInvalide();
        } else {
            Validator.startValidate().validateNullObject(invoice.getReservation(), "Rezervacija mora biti izabrana")
                    .validateBigDecimal(invoice.getTotalCost(), "Mora biti realan broj")
                    .validateNullObject(invoice.getDateCreated(), "Racun mora imati datum")
                    .validateStringMinLength(invoice.getNumber(), 5, "Broj racuna mora imati najmanje 5 karaktera")
                    .validateInvoiceItems(invoice.getInvoiceItems(), invoice.getTotalCost())
                    .validateNullObject(invoice.getCreatedBy(), "Mora biti poznato ko je napravio racun")
                    .validateIsSelected(invoice.isObradjen(), "Racun mora biti obradjen")
                    .throwIfInvalide();
        }

    }

    @Override
    public void UpdatePrecondition(GeneralDObject odk) throws ValidationException {
        Invoice invoice=(Invoice) odk;
        if (!invoice.isObradjen()) {
            Validator.startValidate().validateNullObject(invoice.getReservation(), "Rezervacija mora biti izabrana")
                    .validateBigDecimal(invoice.getTotalCost(), "Mora biti realan broj")
                    .validateNullObject(invoice.getDateCreated(), "Racun mora imati datum")
                    .throwIfInvalide();
        } else {
            Validator.startValidate().validateNullObject(invoice.getReservation(), "Rezervacija mora biti izabrana")
                    .validateBigDecimal(invoice.getTotalCost(), "Mora biti realan broj")
                    .validateNullObject(invoice.getDateCreated(), "Racun mora imati datum")
                    .validateStringMinLength(invoice.getNumber(), 5, "Broj racuna mora imati najmanje 5 karaktera")
                    .validateInvoiceItems(invoice.getInvoiceItems(), invoice.getTotalCost())
                    .validateNullObject(invoice.getCreatedBy(), "Mora biti poznato ko je napravio racun")
                    .validateIsSelected(invoice.isObradjen(), "Racun mora biti obradjen")
                    .throwIfInvalide();
        }
    }

    @Override
    public void DeletePrecondition(GeneralDObject odk) throws ValidationException {
        Invoice invoice=(Invoice) odk;
        if (invoice.isObradjen()) {
            throw new ValidationException("racun je obrajen ne moze da se obrise");
        } else if (invoice==null || invoice.getId() == null) {
            throw new ValidationException("Racun nema inicijalizovan id");
        }
    }

   
}
