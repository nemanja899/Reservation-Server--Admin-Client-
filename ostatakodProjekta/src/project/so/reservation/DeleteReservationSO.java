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
public class DeleteReservationSO extends AbstractSO{
     private final Repository reservationStorrage;

    public DeleteReservationSO() {
        reservationStorrage = new ReservationRepository();
    }

    @Override
    protected void precondition(Object entity) throws ValidationException {
        if (!(entity instanceof Reservation)) {
            throw new ValidationException("Nije rezervacija");
        }
        Reservation reservation=(Reservation) entity;
        if(reservation.getId()<0)
            throw new ValidationException("Podaci nisu validni");
    }

    @Override
    protected Object executeOperation(Object entity, Connection conn) throws Exception {
        try {
            reservationStorrage.delete((Reservation) entity, conn);
            return true;
        } catch (Exception e) {
            throw new Exception("Rezervacija ne moze da se Obrise");
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
