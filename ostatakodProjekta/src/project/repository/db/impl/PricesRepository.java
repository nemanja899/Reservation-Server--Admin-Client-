/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import domain.Prices;
import domain.Season;
import project.repository.db.DbConnectionFactory;
import project.repository.db.DbRepository;
import java.sql.*;
import domain.Animal;
import domain.Currency;
import domain.LovackoDrustvo;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class PricesRepository implements DbRepository<Prices, Object, Connection>, IBoxingUnboxing<Prices, Object> {

    @Override
    public List<Prices> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<Prices> priceses = new ArrayList<>();
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency,"
                    + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                    + "l.id,l.name,l.county,l.adress "
                    + "from prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on (p.lovackodrustvoid=l.id) ";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);

            while (rs.next()) {
                Season season = new Season();
                season.setSeason(rs.getString("s.seasonid"));

                Animal animal = new Animal();
                animal.setId(rs.getLong("a.id"));
                animal.setName(rs.getString("a.name"));
                animal.setShortName(rs.getString("a.shortname"));
                animal.setAllowed(rs.getBoolean("a.allowed"));

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setCounty(rs.getString("l.county"));
                drustvo.setAdress(rs.getString("l.adress"));

                Prices prices = new Prices();
                prices.setSeason(season);
                prices.setAnimal(animal);
                prices.setDrustvo(drustvo);
                prices.setPrice(rs.getBigDecimal("p.price"));
                prices.setCurrency(Currency.valueOf(rs.getString("p.currency").toUpperCase()));
                prices.setSeasonStart(rs.getDate("p.seasonstart"));
                prices.setSeasonStart(rs.getDate("p.seasonend"));

                priceses.add(prices);
            }
            rs.close();
            statement.close();
            return priceses;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<Prices> getAllBySeason(Season param, Connection connection) throws Exception {
        List<Prices> priceses = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency,"
                    + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                    + "l.id,l.name,l.county,l.adress "
                    + "from prices p inner join season s on ( p.seasonid=s.seasonid ) inner join animal a on ( p.animalid=a.id ) "
                    + " inner join lovackodrustvo l on ( p.lovackodrustvoid=l.id ) "
                    + "where p.seasonid=?";

            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getSeason());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Season season = new Season();
                season.setSeason(rs.getString("s.seasonid"));

                Animal animal = new Animal();
                animal.setId(rs.getLong("a.id"));
                animal.setName(rs.getString("a.name"));
                animal.setShortName(rs.getString("a.shortname"));
                animal.setAllowed(rs.getBoolean("a.allowed"));

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setCounty(rs.getString("l.county"));
                drustvo.setAdress(rs.getString("l.adress"));

                Prices prices = new Prices();
                prices.setSeason(season);
                prices.setAnimal(animal);
                prices.setDrustvo(drustvo);
                prices.setPrice(rs.getBigDecimal("p.price"));
                prices.setCurrency(Currency.valueOf(rs.getString("p.currency").toUpperCase()));
                prices.setSeasonStart(rs.getDate("p.seasonstart"));
                prices.setSeasonEnd(rs.getDate("p.seasonend"));

                priceses.add(prices);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        return priceses;
    }

    @Override
    public void add(Prices param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into prices (seasonid,animalid,lovackodrustvoid,seasonstart,seasonend,price,currency)"
                    + " values (?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getSeason().getSeason());
            statement.setLong(2, param.getAnimal().getId());
            statement.setLong(3, param.getDrustvo().getId());
            statement.setDate(4, new Date(param.getSeasonStart().getTime()));
            statement.setDate(5, new Date(param.getSeasonEnd().getTime()));
            statement.setBigDecimal(6, param.getPrice());
            statement.setString(7, param.getCurrency().name());

            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void eddit(Prices param, Object oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            Object[] obj = (Object[]) oldPk;
            String sezona = (String) obj[0];
            Long animal = (Long) obj[1];
            Long drustvo = (Long) obj[2];

            String upit = "update  prices set seasonid=?,animalid=?,lovackodrustvoid=?,"
                    + "seasonstart=?,seasonend=?,price=?,currency=? where seasonid=? and animalid=? and lovackodrustvoid=?";

            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getSeason().getSeason());
            statement.setLong(2, param.getAnimal().getId());
            statement.setLong(3, param.getDrustvo().getId());
            statement.setDate(4, new Date(param.getSeasonStart().getTime()));
            statement.setDate(5, new Date(param.getSeasonEnd().getTime()));
            statement.setBigDecimal(6, param.getPrice());
            statement.setString(7, param.getCurrency().name());
            statement.setString(8, sezona);
            statement.setLong(9, animal);
            statement.setLong(10, drustvo);

            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Prices param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from prices  where seasonid=? and animalid=? and lovackodrustvoid=? ";

            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getSeason().getSeason());
            statement.setLong(2, param.getAnimal().getId());
            statement.setLong(3, param.getDrustvo().getId());
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Prices search(Object param, Connection connection) throws Exception {
        Object[] obj = (Object[]) param;
        String sezona = (String) obj[0];
        Long animal = (Long) obj[1];
        Long drustvo = (Long) obj[2];
        PreparedStatement statement = null;
        try {
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency,"
                    + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                    + "l.id,l.name,l.county,l.adress "
                    + "from prices p inner join season s on (p.seasonid=s.seasonid) inner join animal a on (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on (p.lovackodrustvoid=l.id) "
                    + "where p.seasonid=? and p.animalid=? and p.lovackodrustvoid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, sezona);
            statement.setLong(2, animal);
            statement.setLong(3, drustvo);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Season season = new Season();
                season.setSeason(rs.getString("s.seasonid"));

                Animal a = new Animal();
                a.setId(rs.getLong("a.id"));
                a.setName(rs.getString("a.name"));
                a.setShortName(rs.getString("a.shortname"));
                a.setAllowed(rs.getBoolean("a.allowed"));

                LovackoDrustvo d = new LovackoDrustvo();
                d.setId(rs.getLong("l.id"));
                d.setName(rs.getString("l.name"));
                d.setCounty(rs.getString("l.county"));
                d.setAdress(rs.getString("l.adress"));

                Prices prices = new Prices();
                prices.setSeason(season);
                prices.setAnimal(a);
                prices.setDrustvo(d);
                prices.setPrice(rs.getBigDecimal("p.price"));
                prices.setCurrency(Currency.valueOf(rs.getString("p.currency").toUpperCase()));
                prices.setSeasonStart(rs.getDate("p.seasonstart"));
                prices.setSeasonStart(rs.getDate("p.seasonend"));
                rs.close();
                statement.close();
                return prices;
            }
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        throw new Exception("Cenovnik nije nadjen");
    }
    

    @Override
    public Object searchUnBoxing(Object oldPk) {
        Object[] obj = (Object[]) oldPk;
        String sezona = (String) obj[0];
        Long animal = (Long) obj[1];
        Long drustvo = (Long) obj[2];
        return obj;
    }

    @Override
    public Object updateBoxing(Prices entity, Object primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Prices updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;

        return (Prices) ob[0];
    }

    @Override
    public Object updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;

        return (Prices) ob[1];
    }

}
