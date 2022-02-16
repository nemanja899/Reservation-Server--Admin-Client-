/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.List;
import domain.Animal;
import domain.Season;
import project.repository.db.DbRepository;
import java.sql.*;
import java.util.ArrayList;
import project.controller.Controller;
import domain.Currency;
import domain.LovackoDrustvo;
import domain.Prices;
import project.repository.db.DbConnectionFactory;
import project.repository.db.connectionpool.DbConnectionPool;

import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class AnimalRepository implements DbRepository<Animal, Long, Connection>, IBoxingUnboxing<Animal, Long> {

    @Override
    public List<Animal> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<Animal> animals = new ArrayList<>();
            String upit = "select id,name,shortname,allowed from animal";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Animal a = new Animal();
                a.setId(rs.getLong("id"));
                a.setName(rs.getString("name"));
                a.setShortName(rs.getString("shortname"));
                a.setAllowed(rs.getBoolean("allowed"));
                animals.add(a);
            }
            rs.close();
            statement.close();

            return animals;

        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<Animal> getAllBySeasonAndDrustvo(Season season, LovackoDrustvo drustvo, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            List<Animal> animals = new ArrayList<>();
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,"
                    + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                    + "l.id "
                    + "from prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on(p.lovackodrustvoid=l.id) "
                    + "where p.seasonid=? and p.lovackodrustvoid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());
            statement.setLong(2, drustvo.getId());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Animal animal = new Animal();
                animal.setId(rs.getLong("a.id"));
                animal.setName(rs.getString("a.name"));
                animal.setShortName(rs.getString("a.shortname"));
                animal.setAllowed(rs.getBoolean("a.allowed"));

                animals.add(animal);
            }
            rs.close();
            statement.close();
            return animals;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    public List<Animal> getAllBySeason(Season season) throws Exception {
        PreparedStatement statement = null;
        try {
            Connection connection = DbConnectionPool.getInstance().getConnection();
            List<Animal> animals = new ArrayList<>();
            String upit = "select p.seasonid,p.animalid,p.lovackodrustvoid,"
                    + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                    + "l.id "
                    + "from prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                    + " inner join lovackodrustvo l on(p.lovackodrustvoid=l.id) "
                    + "where p.seasonid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, season.getSeason());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Animal animal = new Animal();
                animal.setId(rs.getLong("a.id"));
                animal.setName(rs.getString("a.name"));
                animal.setShortName(rs.getString("a.shortname"));
                animal.setAllowed(rs.getBoolean("a.allowed"));

                animals.add(animal);
            }
            rs.close();
            statement.close();
            DbConnectionPool.getInstance().releseConnection(connection);
            return animals;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void add(Animal param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into animal (name,shortname,allowed) values(?,?,?)";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getName());
            statement.setString(2, param.getShortName());
            statement.setBoolean(3, param.isAllowed());
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
    public void eddit(Animal param, Long oldpk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "update animal set name=?, shortname=?, allowed=? where id=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getName());
            statement.setString(2, param.getShortName());
            statement.setBoolean(3, param.isAllowed());
            statement.setLong(4, oldpk);
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
    public void delete(Animal param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from animal  where id=?";
            statement = connection.prepareStatement(upit);
            statement.setLong(1, param.getId());
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
    public Animal search(Long id, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "select id,name,shortname,allowed from animal where id=?";
            statement = connection.prepareStatement(upit);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery(upit);
            if (rs.next()) {
                Animal a = new Animal();
                a.setId(rs.getLong("id"));
                a.setName(rs.getString("name"));
                a.setShortName(rs.getString("shortname"));
                a.setAllowed(rs.getBoolean("allowed"));
                rs.close();
                statement.close();
                return a;
            }
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        throw new Exception("Zivotinja nije pronadjena");
    }

    @Override
    public Object updateBoxing(Animal animal, Long oldPk) {
        return new Object[]{animal, oldPk};
    }

    @Override
    public Animal updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Animal) ob[0];
    }

    @Override
    public Long updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Long) ob[1];
    }

    @Override
    public Long searchUnBoxing(Object obj) {
        return (Long) obj;
    }

}
