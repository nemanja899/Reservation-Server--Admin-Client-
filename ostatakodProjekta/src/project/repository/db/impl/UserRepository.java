/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import domain.LovackoDrustvo;
import domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import project.repository.db.DbRepository;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class UserRepository implements DbRepository<User, String, Connection>,IBoxingUnboxing<User, String> {

    @Override
    public List<User> getAll(Connection connection) throws Exception {
        List<User> users = new ArrayList<>();
        Statement statement = null;
        try {
            String upit = "select u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password, "
                    + "l.id,l.name,l.county,l.adress "
                    + "from user u inner join lovackodrustvo l on (u.lovackodrustvoid=l.id) ";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
                lovackoDrustvo.setId(rs.getLong("l.id"));
                lovackoDrustvo.setName(rs.getString("l.name"));
                lovackoDrustvo.setCounty(rs.getString("l.county"));
                lovackoDrustvo.setAdress(rs.getString("l.adress"));

                User u = new User();
                u.setLovackoDrustvoid(lovackoDrustvo);
                u.setId(rs.getLong("u.id"));
                u.setName(rs.getString("u.name"));
                u.setLastName(rs.getString("u.lastname"));
                u.setEmail(rs.getString("u.email"));
                u.setPassword(rs.getString("u.password"));

                users.add(u);
            }
            return users;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void add(User param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into user (lovackodrustvoid,name,lastname,email,password) values (?,?,?,?,?)";
            statement.setLong(1, param.getLovackoDrustvoid().getId());
            statement.setString(2, param.getName());
            statement.setString(3, param.getLastName());
            statement.setString(4, param.getEmail());
            statement.setString(5, param.getPassword());
            statement.executeUpdate();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void eddit(User param, String emailOld, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "update  user set lovackodrustvoid=?,name=?,lastname=?,email=?,password=? where email=?";
            statement.setLong(1, param.getLovackoDrustvoid().getId());
            statement.setString(2, param.getName());
            statement.setString(3, param.getLastName());
            statement.setString(4, param.getEmail());
            statement.setString(5, param.getPassword());
            statement.setString(6, emailOld);
           
            statement.executeUpdate();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(User param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from  user where id=? and lovackodrustvoid=?";
            statement.setLong(1, param.getId());
            statement.setLong(2, param.getLovackoDrustvoid().getId());
            statement.executeUpdate();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public User search(String param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "select u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password, "
                    + "l.id,l.name,l.county,l.adress "
                    + "from user u inner join lovackodrustvo l on (u.lovackodrustvoid=l.id) "
                    + "where u.email=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1,param);
           
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
                lovackoDrustvo.setId(rs.getLong("l.id"));
                lovackoDrustvo.setName(rs.getString("l.name"));
                lovackoDrustvo.setCounty(rs.getString("l.county"));
                lovackoDrustvo.setAdress(rs.getString("l.adress"));

                User u = new User();
                u.setLovackoDrustvoid(lovackoDrustvo);
                u.setId(rs.getLong("u.id"));
                u.setName(rs.getString("u.name"));
                u.setLastName(rs.getString("u.lastname"));
                u.setEmail(rs.getString("u.email"));
                u.setPassword(rs.getString("u.password"));

                return u;
            }throw new Exception("Korisnik nije pronadjen");
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String searchUnBoxing(Object obj) {
        return (String) obj;
    }

    @Override
    public Object updateBoxing(User entity, String primitive) {
        return new Object[]{entity,primitive};
    }

    @Override
    public User updateFirstObject(Object obj) {
        Object[] ob=(Object[]) obj;
        return (User) ob[0];
    }

    @Override
    public String updateSecondPrimitive(Object obj) {
         Object[] ob=(Object[]) obj;
        return  (String) ob[1];
    }

}
