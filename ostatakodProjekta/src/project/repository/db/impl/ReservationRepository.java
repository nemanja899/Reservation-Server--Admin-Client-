/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.List;
import domain.Reservation;
import project.repository.db.DbRepository;
import java.sql.*;
import java.util.ArrayList;
import domain.Hunter;
import domain.LovackoDrustvo;
import domain.Season;
import project.repository.db.DbConnectionFactory;
import project.repository.db.connectionpool.DbConnectionPool;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class ReservationRepository implements DbRepository<Reservation, Long, Connection>, IBoxingUnboxing<Reservation, Long> {

    @Override
    public List<Reservation> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<Reservation> reservations = new ArrayList<>();
            String upit = "select r.id,r.seasonid,r.lovackodrustvoid,r.hunterid,"
                    + "s.seasonid,l.id,l.name,l.county,l.adress,"
                    + "h.passportNo,h.fullname,h.country,h.birthdate "
                    + "from reservation r inner join season s on (r.seasonid=s.seasonid) "
                    + "inner join lovackodrustvo l on (r.lovackodrustvoid=l.id) "
                    + "inner join hunter h on (r.hunterid=h.passportNo) order by s.seasonid,l.name,h.fullname";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Season season = new Season();
                season.setSeason(rs.getString("s.seasonid"));

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setName(rs.getString("l.county"));
                drustvo.setName(rs.getString("l.adress"));

                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("h.passportNo"));
                hunter.setFullName(rs.getString("h.fullname"));
                hunter.setCountry(rs.getString("h.country"));
                hunter.setBirthDate(rs.getDate("h.birthdate"));

                Reservation reservation = new Reservation();
                reservation.setId(rs.getLong("r.id"));
                reservation.setDrustvo(drustvo);
                reservation.setHunter(hunter);
                reservation.setSeason(season);

                reservations.add(reservation);
            }
            rs.close();
            statement.close();
            return reservations;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<Reservation> getAllBySeason(Season season, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            List<Reservation> reservations = new ArrayList<>();
            String upit = "select r.id,r.seasonid,r.lovackodrustvoid,r.hunterid,"
                    + "s.seasonid,l.id,l.name,l.county,l.adress,"
                    + "h.passportNo,h.fullname,h.country,h.birthdate "
                    + "from reservation r inner join season s on (r.seasonid=s.seasonid) "
                    + "inner join lovackodrustvo l on (r.lovackodrustvoid=l.id) "
                    + "inner join hunter h on (r.hunterid=h.passportNo) "
                    + " where s.seasonid in (?) "
                    + "order by s.seasonid,l.name,h.fullname ";
            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Season s = new Season();
                s.setSeason(rs.getString("s.seasonid"));

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setCounty(rs.getString("l.county"));
                drustvo.setAdress(rs.getString("l.adress"));

                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("h.passportNo"));
                hunter.setFullName(rs.getString("h.fullname"));
                hunter.setCountry(rs.getString("h.country"));
                hunter.setBirthDate(rs.getDate("h.birthdate"));

                Reservation reservation = new Reservation();
                reservation.setId(rs.getLong("r.id"));
                reservation.setDrustvo(drustvo);
                reservation.setHunter(hunter);
                reservation.setSeason(season);

                reservations.add(reservation);
            }
            rs.close();
            statement.close();
            return reservations;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception("Rezervacije ne mogu biti ucitane");
        } finally {
            if (connection != null) {
                DbConnectionPool.getInstance().releseConnection(connection);
                
            }
        }
    }

    @Override
    public void add(Reservation param, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {

            String upit = "insert into reservation (seasonid,lovackodrustvoid,hunterid) values(?,?,?)";
            statement = connection.prepareStatement(upit);
            if (param.getHunter().getFullName() != "" && param.getHunter().getCountry() != "" && param.getHunter().getBirthDate() != null && !param.getHunter().getpassportNo().isEmpty()) {
                new HunterRepository().add(param.getHunter(), connection);
                //samo ako su sve vrednosti popunjene, ako postoji vec lovac onda u  formi HunterReservationForm lovcu postavimo samo br pasosa!!!!
            }
            statement.setString(1, param.getSeason().getSeason());
            statement.setLong(2, param.getDrustvo().getId());
            statement.setString(3, param.getHunter().getpassportNo());

            statement.executeUpdate();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void eddit(Reservation param, Long oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {

            String upit = "update  reservation set seasonid=?,lovackodrustvoid=?,hunterid=? where id=?";
            statement = connection.prepareStatement(upit);
            if (param.getHunter().getFullName() != "" && param.getHunter().getCountry() != "" && param.getHunter().getBirthDate() != null && !param.getHunter().getpassportNo().isEmpty()) {
                new HunterRepository().eddit(param.getHunter(), param.getHunter().getpassportNo(), connection);

                //samo ako su sve vrednosti popunjene, ako postoji vec lovac onda u formi UpdateReservationForm lovcu postavimo samo br pasosa!!!!
            }
            statement.setString(1, param.getSeason().getSeason());
            statement.setLong(2, param.getDrustvo().getId());
            statement.setString(3, param.getHunter().getpassportNo());
            statement.setLong(4,oldPk );
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Reservation param, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            String upit = "delete from  reservation  where id=?";
            statement = connection.prepareStatement(upit);
            statement.setLong(1, param.getId());
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Reservation search(Long param, Connection connection) throws Exception {
        return null;
    }

    @Override
    public Long searchUnBoxing(Object obj) {
        return (Long) obj;
    }

    @Override
    public Object updateBoxing(Reservation entity, Long primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Reservation updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Reservation) ob[0];
    }

    @Override
    public Long updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Long) ob[1];
    }

}
