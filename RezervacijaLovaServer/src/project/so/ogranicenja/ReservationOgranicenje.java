/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.ogranicenja;

import domain.GeneralDObject;
import domain.Hunter;
import domain.Reservation;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.so.impl.SearchReservationSO;
import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class ReservationOgranicenje implements Ogranicenje {

    @Override
    public void AddPrecondition(GeneralDObject odk) throws ValidationException {
        Reservation r = null;
        Reservation reservation = (Reservation) odk;
        try {

            Object[] condition = new Object[]{reservation.getSeason().getSeason(), reservation.getDrustvo().getId(), reservation.getHunter().getpassportNo()};
            r = (Reservation) new SearchReservationSO().execute(reservation, condition);

            throw new ValidationException("Vec postoji rezervacija za Lovca!!");

        } catch (Exception ex) {
            Logger.getLogger(ReservationOgranicenje.class.getName()).log(Level.SEVERE, null, ex);
            if (r != null) {
                throw new ValidationException(ex.getMessage());
            } else {
                Validator.startValidate().validateNullObject(reservation.getSeason(), "Sezona mora biti uneta")
                        .validateNullObject(reservation.getDrustvo(), "Lovacko drustvo mora biti uneto")
                        .validateNullObject(reservation.getHunter(), "Lovac mora biti unet")
                        .throwIfInvalide();
                if (reservation.addAnother(reservation)) {
                    Hunter hunter = reservation.getHunter();
                    Validator.startValidate()
                            .validateStringMinLength(hunter.getpassportNo(), 6, "Broj u pasosu ne moze biti kraci od 6 karaktera")
                            .validateStringMinLength(hunter.getCountry(), 3, "Zemnja ne moze biti kraci od 3 karaktera")
                            .validateStringMinLength(hunter.getFullName(), 4, "Puno ime ne moze biti krace od 4 karaktera")
                            .throwIfInvalide();
                }
            }

        }
    }

    @Override
    public void UpdatePrecondition(GeneralDObject odk) throws ValidationException {
        Reservation reservation = (Reservation) odk;
        Validator.startValidate().validateNullObject(reservation.getSeason(), "Sezona mora biti uneta")
                .validateNullObject(reservation.getDrustvo(), "Lovacko drustvo mora biti uneto")
                .validateNullObject(reservation.getHunter(), "Lovac mora biti unet")
                .throwIfInvalide();
    }

    @Override
    public void DeletePrecondition(GeneralDObject odk) throws ValidationException {

        Reservation reservation = (Reservation) odk;
        Validator.startValidate().validateNullObject(reservation.getId(), "ID mora biti unet!!")
                .throwIfInvalide();
    }

}
