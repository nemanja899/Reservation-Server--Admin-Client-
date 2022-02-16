/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.ArrayList;
import java.util.List;
import domain.Animal;
import domain.LovackoDrustvo;
import domain.Season;
import project.repository.db.DbConnectionFactory;
import project.repository.db.DbRepository;
import java.sql.*;
import project.repository.db.connectionpool.DbConnectionPool;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class LovackoDrustvoRepository implements DbRepository<LovackoDrustvo, String, Connection>, IBoxingUnboxing<LovackoDrustvo, String> {

    @Override
    public List<LovackoDrustvo> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<LovackoDrustvo> drustva = new ArrayList<>();
            String upit = "select id,name,county,adress from lovackodrustvo";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("id"));
                drustvo.setName(rs.getString("name"));
                drustvo.setCounty(rs.getString("county"));
                drustvo.setAdress(rs.getString("adress"));

                drustva.add(drustvo);
            }
            rs.close();
            statement.close();
            return drustva;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<LovackoDrustvo> getAllDrustvoWithoutPrices(Season season) throws Exception {
        List<LovackoDrustvo> drustva = new ArrayList<>();
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = DbConnectionPool.getInstance().getConnection();
            String upit = "select id,name,county,adress "
                    + "from lovackodrustvo where id not in ("
                    + "select distinct lovackodrustvoid "
                    + "from prices where seasonid=?)";
            //  '"+season.getSeason()+"')
            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("id"));
                drustvo.setName(rs.getString("name"));
                drustvo.setCounty(rs.getString("county"));
                drustvo.setAdress(rs.getString("adress"));

                drustva.add(drustvo);

            }
            statement.close();
            
            return drustva;

        } catch (Exception e) {
            if(statement!=null)
                statement.close();
            e.printStackTrace();
            throw new Exception("Podaci ne mogu da se ucitaju");
        } finally {
            if(connection!=null)
                DbConnectionPool.getInstance().releseConnection(connection);
        }
    }

    public List<LovackoDrustvo> getAllBySeasonAndAnimal(Season season, Animal animal, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            List<LovackoDrustvo> drustva = new ArrayList<>();
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,"
                    + "s.seasonid, a.id,"
                    + "l.id,l.name,l.county,l.adress "
                    + "from prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on(p.lovackodrustvoid=l.id) "
                    + "where p.seasonid=? and p.lovackodrustvoid=?";

            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());
            statement.setLong(2, animal.getId());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setCounty(rs.getString("l.county"));
                drustvo.setAdress(rs.getString("l.adress"));

                drustva.add(drustvo);
            }
            rs.close();
            statement.close();
            return drustva;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<LovackoDrustvo> getAllBySeason(Season season, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            List<LovackoDrustvo> drustva = new ArrayList<>();
            String upit = "select distinct p.seasonid,p.animalid,p.lovackodrustvoid,"
                    + "s.seasonid, a.id,"
                    + "l.id,l.name,l.county,l.adress "
                    + "from prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on(p.lovackodrustvoid=l.id) "
                    + "where p.seasonid=?";

            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                LovackoDrustvo drustvo = new LovackoDrustvo();
                drustvo.setId(rs.getLong("l.id"));
                drustvo.setName(rs.getString("l.name"));
                drustvo.setCounty(rs.getString("l.county"));
                drustvo.setAdress(rs.getString("l.adress"));

                drustva.add(drustvo);
            }
            rs.close();
            statement.close();

            return drustva;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void add(LovackoDrustvo param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into lovackodrustvo (name,county,adress) values(?,?,?)";
            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getName());
            statement.setString(2, param.getCounty());

            statement.setString(3, param.getAdress());
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
    public void eddit(LovackoDrustvo param, String name, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "update lovackodrustvo set name=?,county=?,adress=? where name=?";
            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getName());
            statement.setString(2, param.getCounty());
            statement.setString(3, param.getAdress());
            statement.setString(4, name);
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
    public void delete(LovackoDrustvo param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from lovackodrustvo where name=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getName());
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
    public LovackoDrustvo search(String param, Connection connection) throws Exception {

        String upit = "select name,county,adress from lovackodrustvo where name=?";
        PreparedStatement statement = connection.prepareStatement(upit);
        statement.setString(1, param);
        ResultSet rs = statement.executeQuery(upit);
        if (rs.next()) {
            LovackoDrustvo drustvo = new LovackoDrustvo();
            drustvo.setName(rs.getString("name"));
            drustvo.setCounty(rs.getString("county"));
            drustvo.setAdress(rs.getString("adress"));
            rs.close();
            statement.close();
            return drustvo;
        }

        throw new Exception("Lovacko drustvo nije pronadjeno");

    }

    @Override
    public String searchUnBoxing(Object obj) {
        return (String) obj;
    }

    @Override
    public Object updateBoxing(LovackoDrustvo entity, String primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public LovackoDrustvo updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (LovackoDrustvo) ob[0];
    }

    @Override
    public String updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (String) ob[1];
    }

}
