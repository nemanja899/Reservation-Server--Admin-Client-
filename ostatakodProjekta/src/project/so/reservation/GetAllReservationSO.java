/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.so.reservation;

import domain.Reservation;
import java.sql.Connection;
import project.repository.Repository;
import project.repository.db.impl.ReservationRepository;
import project.so.AbstractSO;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class GetAllReservationSO extends AbstractSO{
      private final Repository reservationStorrage;

    public GetAllReservationSO() {
        reservationStorrage = new ReservationRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        // nema preduslova
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
          return  reservationStorrage.getAll(conn);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Rezervacije ne mogu da se ucitaju");
        }
    }

   @Override
    protected Connection startTransaction() throws Exception {
        return (Connection) reservationStorrage.connect();
    }

    @Override
    protected void commitTransaction(Connection conn) throws Exception {
        reservationStorrage.commit(conn);
    }

    @Override
    protected void rollbackTransaction(Connection conn) throws Exception {
        reservationStorrage.rollback(conn);
    }

    @Override
    protected void disconnectTransaction(Connection conn) throws Exception {
        reservationStorrage.disconnect(conn);
    }
}
