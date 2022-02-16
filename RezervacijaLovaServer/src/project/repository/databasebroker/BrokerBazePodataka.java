/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.databasebroker;

import domain.Animal;
import domain.GeneralDObject;
import domain.Hunter;
import domain.Invoice;
import domain.InvoiceItem;
import domain.LovackoDrustvo;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
//import project.repository.db.BrokerBazePodataka;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import project.repository.connectionpool.DbConnectionPool;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class BrokerBazePodataka implements IBrokerBazePodataka<GeneralDObject, Object, Connection> {

    @Override
    public List<GeneralDObject> getAll(GeneralDObject odk, Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<GeneralDObject> odks = new ArrayList<>();
            String upit = "SELECT " + odk.getRecordColumns() + " FROM " + odk.getClassNameJoin();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {

                if (odk.hasList()) {
                    GeneralDObject gdo = odk.getNewRecord(rs);
                    gdo.setList(getAllByCondition(gdo.getChild(), gdo.getPk(), connection));
                    odks.add(gdo);
                } else {
                    odks.add(odk.getNewRecord(rs));
                }

            }
            statement.close();

            return odks;

        } catch (ValidationException e) {
            e.printStackTrace();
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
            throw new Exception(odk.getErrorGetAll()+"\n" + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    @Override
    public List<GeneralDObject> getAllByCondition(GeneralDObject odk, Object condition, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            List<GeneralDObject> odks = new ArrayList<>();
            String upit = "SELECT " + odk.getRecordColumnsByCondition() + " FROM " + odk.getClassNameJoinByCondition() + " WHERE " + odk.getConditionGetAll();
            statement = connection.prepareStatement(upit);
            odk.setConditionGetAll(statement, condition);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (odk.hasList()) {
                    GeneralDObject gdo = odk.getNewRecord(rs);

                    gdo.setList(getAllByCondition(gdo.getChild(), gdo.getPk(), connection));
                    odks.add(gdo);

                } else {
                    odks.add(odk.getNewRecord(rs));
                }
            }
            statement.close();

            return odks;

        } catch (ValidationException e) {
            e.printStackTrace();
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
            throw new Exception(odk.getErrorGetAll() + "\n" + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    @Override
    public void add(GeneralDObject odk, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            if (odk.addAnother(odk)) {
                add(odk.getParentDO(), connection);
            }
            String upit = "INSERT INTO " + odk.getClassName() + " (" + odk.getColumns() + ") values(" + odk.getAddValues() + ")";
            if (odk.hasList()) {
                statement = connection.prepareStatement(upit, Statement.RETURN_GENERATED_KEYS);
            } else {
                statement = connection.prepareStatement(upit);
            }
            odk.setAddValues(statement, odk);
            statement.executeUpdate();
            if (odk.hasList()) {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                Long id = rs.getLong(1);
                List<GeneralDObject> odks2 = odk.getList();
                for (GeneralDObject odk2 : odks2) {
                    odk2.setParentID(id);
                    add(odk2, connection);
                }
            }

            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(odk.getErrorAdd());
        }

    }

    @Override
    public void eddit(GeneralDObject odk, Object oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            if (odk.addAnother(odk)) {
                eddit(odk.getParentDO(), oldPk, connection);
            }
            String upit = "UPDATE " + odk.getClassName() + " SET " + odk.getUpdateValues() + " WHERE " + odk.getUpdateCondition();
            statement = connection.prepareStatement(upit);
            odk.setUpdateValues(odk, oldPk, statement);

            statement.executeUpdate();

            if (odk.hasList()) {
                if (!odk.getList().isEmpty()) {
                    GeneralDObject gdo = odk.getChild();
                    gdo.setParentID((Long) odk.getPk());
                    deleteByPkParrent(gdo, oldPk, connection);

                    for (GeneralDObject odk2 : odk.getList()) {
                        add(odk2, connection);
                    }
                }
            }
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(odk.getErrorEddit());
        }
    }

    @Override
    public void deleteByPkParrent(GeneralDObject odk, Object condition, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "DELETE FROM " + odk.getClassName() + " WHERE " + odk.getUpdateCondition();
            statement = connection.prepareStatement(upit);
            odk.setDeletePrimaryKeyParrentCondiotion(condition, statement);
            statement.executeUpdate();
            if (odk.deleteParent()) {
                delete(odk.getParentDO(), connection);
            }
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(odk.getErrorDelete());
        }
    }

    @Override
    public void delete(GeneralDObject odk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "DELETE FROM " + odk.getClassName() + " WHERE " + odk.getUpdateCondition();
            statement = connection.prepareStatement(upit);
            odk.setDeletePrimaryKeyCondiotion(odk, statement);
            statement.executeUpdate();
            if (odk.deleteParent()) {
                GeneralDObject odk2 = odk.getParentDO();

                delete(odk2, connection);
            }
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(odk.getErrorDelete());
        }
    }

    @Override
    public GeneralDObject search(GeneralDObject odk, Object param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "SELECT " + odk.getRecordColumns() + " FROM " + odk.getClassNameJoin() + " WHERE " + odk.getPrimaryKeyCondition();
            statement = connection.prepareStatement(upit);
            odk.setSearchPrimaryKeyCondiotion(param, statement);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                GeneralDObject odk1 = odk.getNewRecord(rs);
                statement.close();
                return odk1;
            }
            throw new ValidationException(odk.getErrorNotFound());
        } catch (ValidationException e) {
            e.printStackTrace();
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {

            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

    }

    public List<Object> getProfitsBySeason(Connection connection) throws Exception {
        List<Object> profits = new ArrayList<>();

        Statement statement = null;
        try {
            connection = connect();
            String upit = "select i.poreziid,round(sum(totalcost*(select provizija from porezi p where p.seasonid=i.poreziid)/100),2) as profit "
                    + "from invoice i "
                    + "group by i.poreziid "
                    + "order by i.poreziid";

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Object[] obj = new Object[]{rs.getString("i.poreziid"), rs.getBigDecimal("profit"), "RSD"};
                profits.add(obj);
            }

            statement.close();
            return profits;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception("Profiti ne mogu da se ucitaju");
        }
    }

    public List<InvoiceItem> getNumberOfHuntedAnimals(Connection connection) throws Exception {
        List<InvoiceItem> items = new ArrayList<>();

        Statement statement = null;
        try {

            String upit = "select i.animalid,i.seasonid, sum(i.animalno) as ulovljena, a.id,a.shortname "
                    + "from invoiceitem i inner join animal a on (i.animalid=a.id) "
                    + "group by i.seasonid,i.animalid,a.id,a.shortname "
                    + "order by i.seasonid";
            statement = connection.prepareStatement(upit);
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Animal a = new Animal();
                a.setShortName(rs.getString("a.shortname"));
                InvoiceItem item = new InvoiceItem();
                Prices p = new Prices();
                p.setAnimal(a);
                p.setSeason(new Season(rs.getString("i.seasonid")));
                item.setPrices(p);
                item.setAnimalNo(rs.getInt("ulovljena"));
                items.add(item);
            }
            statement.close();
            return items;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception("Stavke ne mogu da se ucitaju");

        }
    }

    public List<InvoiceItem> getInvoiceItemsByReservation(Reservation reservation, Connection connection) throws Exception {
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        PreparedStatement statement = null;
        try {

            String upit = "select i.animalno,i.animalid,a.id,a.shortname "
                    + "from invoiceitem i inner join animal a on (i.animalid=a.id) "
                    + "where i.invoiceid=( "
                    + "select id from invoice "
                    + "where reservationid=? "
                    + ")";

            statement = connection.prepareStatement(upit);
            statement.setLong(1, reservation.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Animal animal = new Animal();
                animal.setShortName(rs.getString("a.shortname"));
                InvoiceItem invoiceItem = new InvoiceItem();
                Prices prices = new Prices();
                prices.setAnimal(animal);
                invoiceItem.setPrices(prices);
                invoiceItem.setAnimalNo(rs.getInt("i.animalno"));
                invoiceItems.add(invoiceItem);
            }
            statement.close();

            return invoiceItems;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }

        }
    }

    public List<Reservation> getAllReservationByDrustvoNoInvoice(Object condition, Connection connection) throws ValidationException, Exception {
        List<Reservation> reservations = new ArrayList<>();

        PreparedStatement statement = null;
        try {
            Object[] ob = (Object[]) condition;
            String seasonCond = ((Season) ob[0]).getSeason();
            Long drustvoCond = ((LovackoDrustvo) ob[1]).getId();
            String upit = "select r.id,r.seasonid,r.lovackodrustvoid,r.hunterid,"
                    + "s.seasonid,l.id,l.name,l.county,l.adress,"
                    + "h.passportNo,h.fullname,h.country,h.birthdate "
                    + "from reservation r inner join season s on (r.seasonid=s.seasonid) "
                    + "inner join lovackodrustvo l on (r.lovackodrustvoid=l.id) "
                    + "inner join hunter h on (r.hunterid=h.passportNo) "
                    + "where r.id not in (select reservationid from invoice) and r.seasonid=? and l.id=? "
                    + "order by s.seasonid,l.name,h.fullname";
            statement = connection.prepareStatement(upit);
            statement.setString(1, seasonCond);
            statement.setLong(2, drustvoCond);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                Season season = new Season();
                season.setSeason(rs.getString("s.seasonid"));

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

                r.setId(rs.getLong("r.id"));
                r.setDrustvo(drustvo);
                r.setHunter(hunter);
                r.setSeason(season);

                reservations.add(r);
            }

            return reservations;

        } catch (ValidationException e) {
            throw new ValidationException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }

        }
    }

    public Invoice searchInvoiceByPorezi(Porezi p, Connection conn) throws Exception {
        PreparedStatement statement = null;
        try {
            Invoice invoice=null;
            String upit = "select * from invoice where poreziid=?";
            statement = conn.prepareStatement(upit);
            statement.setString(1, p.getSeason());
             ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                invoice = new Invoice();
                invoice.setPorezi(p);

            }
            statement.close();
            return invoice;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            DbConnectionPool.getInstance().releseConnection(conn);
        }
    }

    public InvoiceItem searchInvoiceItemByPrices(Prices p, Connection conn) throws Exception {
        PreparedStatement statement = null;
        try {
            InvoiceItem item = null;
            String upit = "select * from invoiceitem where seasonid=? and lovackodrustvoid=? and animalid=?";
            statement = conn.prepareStatement(upit);
            statement.setString(1, p.getSeason().getSeason());
            statement.setLong(2, p.getDrustvo().getId());
            statement.setLong(3, p.getAnimal().getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                item = new InvoiceItem();
                item.setPrices(p);

            }
            statement.close();
            return item;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            DbConnectionPool.getInstance().releseConnection(conn);
        }
    }

    public void AddAllPriceses(List<Prices> prices, Connection connection) throws Exception {

        for (Prices p : prices) {
            add(p, connection);
        }

    }

    @Override
    public Connection connect() throws Exception {
        return DbConnectionPool.getInstance().getConnection();
    }

    @Override
    public void disconnect(Connection connection) throws Exception {
        DbConnectionPool.getInstance().releseConnection(connection);
    }

    @Override
    public void commit(Connection connection) throws Exception {
        connection.commit();
    }

    @Override
    public void rollback(Connection conncection) throws Exception {
        conncection.rollback();
    }

}
