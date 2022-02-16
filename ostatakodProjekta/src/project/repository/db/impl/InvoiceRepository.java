/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import domain.Animal;
import domain.Currency;
import domain.Hunter;
import java.util.List;
import domain.Invoice;
import domain.InvoiceItem;
import domain.LovackoDrustvo;
import domain.Porezi;
import domain.Prices;
import domain.Reservation;
import domain.Season;
import domain.User;
import java.math.BigDecimal;
import project.repository.db.DbRepository;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import project.repository.db.connectionpool.DbConnectionPool;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class InvoiceRepository implements DbRepository<Invoice, Long, Connection>, IBoxingUnboxing<Invoice, Long> {

    @Override
    public List<Invoice> getAll(Connection connection) throws Exception {
        List<Invoice> invoices = new ArrayList<>();
        Statement statement = null;
        try {
            String upit = "select i.id, i.number,i.totalCost,i.currency,i.dateCreated,i.poreziid,i.hunterid,i.obradjen,i.reservationid,i.createdBy,i.lovackodrustvoid "
                    + ",p.seasonid,p.pdv,p.provizija "
                    + ",h.passportNo,h.fullname,h.country,h.birthdate "
                    + "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid "
                    + "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password "
                    + "l.id,l.name,l.county,l.adress "
                    + "from invoice i inner join porezi p on (i,poreziid=p.seasonid)"
                    + " inner join hunter h on(i.hunterid=h.passportNo) "
                    + "inner join reservation r on (i.reservationid=r.id) "
                    + "inner join user u on (i.createdBy=u.id,i.lovackodrustvoid=u.lovackodrustvoid) "
                    + "inner join lovadkodrustvo l  on (i.lovackodrustvoid=l.id)";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("i.id"));
                invoice.setCurrency(Currency.valueOf(rs.getString("i.currency").toUpperCase()));
                invoice.setTotalCost(rs.getBigDecimal("i.totalcost"));
                invoice.setNumber(rs.getString("i.number"));
                invoice.setDateCreated(rs.getDate("i.dateCreated"));
                invoice.setObradjen(rs.getBoolean("i.obrajden"));

                Porezi porezi = new Porezi();
                porezi.setSeason(rs.getString("p.seasonid"));
                porezi.setPDV(rs.getBigDecimal("p.pdv"));
                porezi.setProvision(rs.getBigDecimal("p.provizija"));
                invoice.setPorezi(porezi);

                LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
                lovackoDrustvo.setId(rs.getLong("l.id"));
                lovackoDrustvo.setName(rs.getString("l.name"));
                lovackoDrustvo.setCounty(rs.getString("l.county"));
                lovackoDrustvo.setAdress(rs.getString("l.adress"));

                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("h.passportNo"));
                hunter.setFullName(rs.getString("h.fullname"));
                hunter.setCountry(rs.getString("h.country"));
                hunter.setBirthDate(rs.getDate("h.birthdate"));

                User user = new User();
                user.setLovackoDrustvoid(lovackoDrustvo);
                user.setId(rs.getLong("u.id"));
                user.setEmail("u.email");

                Reservation reservation = new Reservation();
                reservation.setDrustvo(lovackoDrustvo);
                reservation.setHunter(hunter);
                reservation.setSeason(new Season(porezi.getSeason()));
                reservation.setId(rs.getLong("r.id"));

                invoice.setReservation(reservation);
                invoice.setCreatedBy(user);
                invoice.setHunter(hunter);

                List<InvoiceItem> cItems = new ArrayList<>();
                String upit2 = "select t.ordernumber,t.invoiceid,t.animalno,t.totalcost,t.animalid,t.seasonid,t.lovackodrustvoid,t.currency, "
                        + "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency "
                        + "a.id,a.name,a.shortname,a.allowed "
                        + "l.id,l.name,l.county,l.adress"
                        + "from invoiceitem t inner join prices p on (t.seasonid=p.seasonid,t.animalid=p.animalid,t.lovackodrustvoid=p.lovackodrustvoid) "
                        + "inner join animal a on (p.animalid=a.id) inner join lovackodrustvo l on (p.lovackodrustvoid=l.id) "
                        + "where t.invoiceid=?";
                PreparedStatement s = connection.prepareStatement(upit2);
                s.setLong(1, invoice.getId());
                ResultSet rs2 = s.executeQuery();
                while (rs2.next()) {
                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setAnimalNo(rs2.getInt("t.animalno"));
                    invoiceItem.setCurrency(Currency.valueOf(rs2.getString("t.currency")));
                    invoiceItem.setTotalCost(rs2.getBigDecimal("t.totalcost"));
                    invoiceItem.setOrderNumber(rs2.getLong("t.ordernumber"));

                    Animal animal = new Animal();
                    animal.setId(rs2.getLong("a.id"));
                    animal.setName(rs.getString("a.name"));
                    animal.setShortName(rs2.getString("a.shortname"));
                    animal.setAllowed(rs2.getBoolean("a.allowed"));

                    Prices prices = new Prices();
                    prices.setAnimal(animal);
                    LovackoDrustvo lovackoDrustvo2 = new LovackoDrustvo();
                    lovackoDrustvo2.setId(rs.getLong("l.id"));
                    lovackoDrustvo2.setName(rs.getString("l.name"));
                    lovackoDrustvo2.setCounty(rs.getString("l.county"));
                    lovackoDrustvo2.setAdress(rs.getString("l.adress"));
                    prices.setDrustvo(lovackoDrustvo2);
                    prices.setSeason(new Season(porezi.getSeason()));
                    prices.setCurrency(Currency.valueOf(rs2.getString("p.currency")));
                    prices.setPrice(rs2.getBigDecimal("p.price"));
                    prices.setSeasonEnd(rs2.getDate("p.seasonend"));
                    prices.setSeasonStart(rs2.getDate("p.seasonstart"));

                    invoiceItem.setPrices(prices);

                    cItems.add(invoiceItem);

                }
                s.close();
                invoice.setInvoiceItems(cItems);
                invoices.add(invoice);
            }

            rs.close();
            statement.close();
            return invoices;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<Object> getProfitsBySeason() throws Exception {
        List<Object> profits = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DbConnectionPool.getInstance().getConnection();
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
        } finally {
            if (connection != null) {
                DbConnectionPool.getInstance().releseConnection(connection);
            }
        }
    }

    public List<InvoiceItem> getAnimalsAndNum() throws Exception {
        List<InvoiceItem> items = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DbConnectionPool.getInstance().getConnection();
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
        } finally {
            if (connection != null) {
                DbConnectionPool.getInstance().releseConnection(connection);
            }
        }

    }

    @Override
    public void add(Invoice param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into invoice (number,totalcost,currency,dateCreated,poreziid,hunterid,obradjen,reservationid,createdBy,lovackodrustvoid) "
                    + " values (?,?,?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(upit, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, param.getNumber());
            statement.setBigDecimal(2, param.getTotalCost());
            statement.setString(3, param.getCurrency().name());
            statement.setDate(4, new java.sql.Date(param.getDateCreated().getTime()));
            statement.setString(5, param.getPorezi().getSeason());
            statement.setString(6, param.getHunter().getpassportNo());
            statement.setBoolean(7, param.isObradjen());
            statement.setLong(8, param.getReservation().getId());
            statement.setLong(9, param.getCreatedBy().getId());
            statement.setLong(10, param.getCreatedBy().getLovackoDrustvoid().getId());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                Long invoiceid = rs.getLong(1);
                param.setId(invoiceid);
                upit = "insert into invoiceitem (ordernumber,invoiceid,animalno,totalcost,animalid,seasonid,lovackodrustvoid,currency) "
                        + "values (?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(upit);
                for (InvoiceItem invoiceItem : param.getInvoiceItems()) {
                    statement.setLong(1, invoiceItem.getOrderNumber());
                    statement.setLong(2, param.getId());
                    statement.setInt(3, invoiceItem.getAnimalNo());
                    statement.setBigDecimal(4, invoiceItem.getTotalCost());
                    statement.setLong(5, invoiceItem.getPrices().getAnimal().getId());
                    statement.setString(6, invoiceItem.getPrices().getSeason().getSeason());
                    statement.setLong(7, invoiceItem.getPrices().getDrustvo().getId());
                    statement.setString(8, invoiceItem.getCurrency().name());

                    statement.executeUpdate();
                }
            }
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void eddit(Invoice param, Long oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        Statement st = null;
        try {
            String upit = "update  invoice set number=?,totalcost=?,currency=?,dateCreated=?,poreziid=?,hunterid=?,obradjen=?,reservationid=?,createdBy=?,lovackodrustvoid=? "
                    + " where id=?";

            statement.setString(1, param.getNumber());
            statement.setBigDecimal(2, param.getTotalCost());
            statement.setString(3, param.getCurrency().name());
            statement.setDate(4, new java.sql.Date(param.getDateCreated().getTime()));
            statement.setString(5, param.getPorezi().getSeason());
            statement.setString(6, param.getHunter().getpassportNo());
            statement.setBoolean(7, param.isObradjen());
            statement.setLong(8, param.getReservation().getId());
            statement.setLong(9, param.getCreatedBy().getId());
            statement.setLong(10, param.getCreatedBy().getLovackoDrustvoid().getId());
            statement.setLong(11, oldPk);

            statement.executeUpdate();
            String delete = "delete from invoiceitem where invoiceid=" + oldPk;
            st = connection.createStatement();
            st.executeUpdate(upit);

            upit = "insert into invoiceitem (ordernumber,invoiceid,animalno,totalcost,animalid,seasonid,lovackodrustvoid,currency) "
                    + "values (?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(upit);
            for (InvoiceItem invoiceItem : param.getInvoiceItems()) {
                statement.setLong(1, invoiceItem.getOrderNumber());
                statement.setLong(2, param.getId());
                statement.setInt(3, invoiceItem.getAnimalNo());
                statement.setBigDecimal(4, invoiceItem.getTotalCost());
                statement.setLong(5, invoiceItem.getPrices().getAnimal().getId());
                statement.setString(6, invoiceItem.getPrices().getSeason().getSeason());
                statement.setLong(7, invoiceItem.getPrices().getDrustvo().getId());
                statement.setString(8, invoiceItem.getCurrency().name());

                statement.executeUpdate();
            }
            st.close();
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            if (st != null) {
                st.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Invoice param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from invoice where id=?";
            statement = connection.prepareStatement(upit);
            statement.setLong(1, param.getId());
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Invoice search(Long param, Connection connection) throws Exception {

        PreparedStatement statement = null;
        try {
            String upit = "select i.id, i.number,i.totalCost,i.currency,i.dateCreated,i.poreziid,i.hunterid,i.obradjen,i.reservationid,i.createdBy,i.lovackodrustvoid "
                    + ",p.seasonid,p.pdv,p.provizija "
                    + ",h.passportNo,h.fullname,h.country,h.birthdate "
                    + "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid "
                    + "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password "
                    + "l.id,l.name,l.county,l.adress "
                    + "from invoice i inner join porezi p on (i,poreziid=p.seasonid)"
                    + " inner join hunter h on(i.hunterid=h.passportNo) "
                    + "inner join reservation r on (i.reservationid=r.id) "
                    + "inner join user u on (i.createdBy=u.id,i.lovackodrustvoid=u.lovackodrustvoid) "
                    + "inner join lovadkodrustvo l  on (i.lovackodrustvoid=l.id) "
                    + "where id=?";
            statement = connection.prepareStatement(upit);
            statement.setLong(1, param);
            ResultSet rs = statement.executeQuery(upit);
            Invoice invoice = new Invoice();
            if (rs.next()) {

                invoice.setId(rs.getLong("i.id"));
                invoice.setCurrency(Currency.valueOf(rs.getString("i.currency").toUpperCase()));
                invoice.setTotalCost(rs.getBigDecimal("i.totalcost"));
                invoice.setNumber(rs.getString("i.number"));
                invoice.setDateCreated(rs.getDate("i.dateCreated"));
                invoice.setObradjen(rs.getBoolean("i.obrajden"));

                Porezi porezi = new Porezi();
                porezi.setSeason(rs.getString("p.seasonid"));
                porezi.setPDV(rs.getBigDecimal("p.pdv"));
                porezi.setProvision(rs.getBigDecimal("p.provizija"));
                invoice.setPorezi(porezi);

                LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
                lovackoDrustvo.setId(rs.getLong("l.id"));
                lovackoDrustvo.setName(rs.getString("l.name"));
                lovackoDrustvo.setCounty(rs.getString("l.county"));
                lovackoDrustvo.setAdress(rs.getString("l.adress"));

                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("h.passportNo"));
                hunter.setFullName(rs.getString("h.fullname"));
                hunter.setCountry(rs.getString("h.country"));
                hunter.setBirthDate(rs.getDate("h.birthdate"));

                User user = new User();
                user.setLovackoDrustvoid(lovackoDrustvo);
                user.setId(rs.getLong("u.id"));
                user.setEmail("u.email");

                Reservation reservation = new Reservation();
                reservation.setDrustvo(lovackoDrustvo);
                reservation.setHunter(hunter);
                reservation.setSeason(new Season(porezi.getSeason()));
                reservation.setId(rs.getLong("r.id"));

                invoice.setReservation(reservation);
                invoice.setCreatedBy(user);
                invoice.setHunter(hunter);

                List<InvoiceItem> cItems = new ArrayList<>();
                String upit2 = "select t.ordernumber,t.invoiceid,t.animalno,t.totalcost,t.animalid,t.seasonid,t.lovackodrustvoid,t.currency, "
                        + "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency "
                        + "select a.id,a.name,a.shortname,a.allowed "
                        + "from invoiceitem t inner join prices p on (t.seasonid=p.seasonid,t.animalid=p.animalid,t.lovackodrustvoid=p.lovackodrustvoid) "
                        + "inner join animal a on (p.animalid=a.id) "
                        + "where t.invoiceid=?";
                PreparedStatement s = connection.prepareStatement(upit2);
                s.setLong(1, invoice.getId());
                ResultSet rs2 = s.executeQuery();
                while (rs2.next()) {
                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setAnimalNo(rs2.getInt("t.animalno"));
                    invoiceItem.setCurrency(Currency.valueOf(rs2.getString("t.currency")));
                    invoiceItem.setTotalCost(rs2.getBigDecimal("t.totalcost"));
                    invoiceItem.setOrderNumber(rs2.getLong("t.ordernumber"));

                    Animal animal = new Animal();
                    animal.setId(rs2.getLong("a.id"));
                    animal.setName(rs2.getString("a.name"));
                    animal.setShortName(rs2.getString("a.shortname"));
                    animal.setAllowed(rs2.getBoolean("a.allowed"));

                    Prices prices = new Prices();
                    prices.setAnimal(animal);
                    prices.setDrustvo(lovackoDrustvo);
                    prices.setSeason(new Season(porezi.getSeason()));
                    prices.setCurrency(Currency.valueOf(rs2.getString("p.currency")));
                    prices.setPrice(rs.getBigDecimal("p.price"));
                    prices.setSeasonEnd(rs2.getDate("p.seasonend"));
                    prices.setSeasonStart(rs2.getDate("p.seasonstart"));

                    invoiceItem.setPrices(prices);

                    cItems.add(invoiceItem);

                }
                s.close();
                invoice.setInvoiceItems(cItems);

            }

            rs.close();
            statement.close();
            return invoice;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public List<InvoiceItem> getInvoiceItemsByReservation(Reservation reservation) throws Exception {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbConnectionPool.getInstance().getConnection();
            String upit = "select i.animalno,i.animalid,a.id,a.shortname "
                    + "from invoiceitem i inner join animal a on (i.animalid=a.id) "
                    + "where i.invoiceid=( "
                    + "select id from invoice "
                    + "where reservationid=? "
                    + ")";

            statement = connection.prepareStatement(upit);
            statement.setLong(1, reservation.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
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
            if (!invoiceItems.isEmpty()) {

                return invoiceItems;
            }

            throw new Exception("Podaci ne postoje!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                DbConnectionPool.getInstance().releseConnection(connection);
            }
        }
    }

    @Override
    public Long searchUnBoxing(Object obj) {
        return (Long) obj;
    }

    @Override
    public Object updateBoxing(Invoice entity, Long primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Invoice updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Invoice) ob[0];
    }

    @Override
    public Long updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Long) ob[1];
    }

}
