/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.ArrayList;
import java.util.List;
import domain.Hunter;
import project.repository.db.DbRepository;
import java.sql.*;
import project.repository.db.DbConnectionFactory;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class HunterRepository implements DbRepository<Hunter, String, Connection>, IBoxingUnboxing<Hunter, String> {

    @Override
    public List<Hunter> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<Hunter> hunters = new ArrayList<>();
            String upit = "select passportNo,fullname,country,birthdate from hunter";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("passportNo"));
                hunter.setFullName(rs.getString("fullname"));
                hunter.setCountry(rs.getString("country"));
                hunter.setBirthDate(rs.getDate("birthdate"));
                hunters.add(hunter);
            }
            rs.close();
            statement.close();
            return hunters;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void add(Hunter param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "insert into hunter (passportNo,fullname,country,birthdate) values(?,?,?,?)";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getpassportNo());
            statement.setString(2, param.getFullName());
            statement.setString(3, param.getCountry());

            statement.setDate(4, new Date(param.getBirthDate().getTime()));
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
    public void eddit(Hunter param, String oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {

            String upit = "update hunter set passportNo=?,fullname=?,country=? where passportNo=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getpassportNo());
            statement.setString(2, param.getFullName());
            statement.setString(3, param.getCountry());
            statement.setString(4, oldPk);
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
    public void delete(Hunter param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from hunter where passportNo=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getpassportNo());
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
    public Hunter search(String param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "select passportNo,fullname,country,birthdate from hunter where passportNo=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Hunter hunter = new Hunter();
                hunter.setpassportNo(rs.getString("passportNo"));
                hunter.setFullName(rs.getString("fullname"));
                hunter.setCountry(rs.getString("country"));
                hunter.setBirthDate(rs.getDate("birthdate"));

                rs.close();
                statement.close();
                return hunter;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        throw new Exception("Lovac nije nadjen");
    }

    @Override
    public String searchUnBoxing(Object obj) {
        return (String) obj;
    }

    @Override
    public Object updateBoxing(Hunter entity, String primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Hunter updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Hunter) ob[0];
    }

    @Override
    public String updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (String) ob[1];

    }

}
