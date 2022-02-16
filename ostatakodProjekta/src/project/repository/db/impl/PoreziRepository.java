/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.List;
import domain.Porezi;
import domain.Season;
import project.repository.db.DbRepository;
import java.sql.*;
import java.util.ArrayList;
import project.repository.db.DbConnectionFactory;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class PoreziRepository implements DbRepository<Porezi, String, Connection>, IBoxingUnboxing<Porezi, String> {

    @Override

    public List<Porezi> getAll(Connection connection) throws Exception {
        Statement statement = null;
        try {
            List<Porezi> porezi = new ArrayList<>();

            String upit = "select seasonid,pdv,provizija from porezi";
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(upit);

            while (rs.next()) {
                Porezi porez = new Porezi();
                porez.setSeason(rs.getString("seasonid"));
                porez.setPDV(rs.getBigDecimal("pdv"));
                porez.setProvision(rs.getBigDecimal("provizija"));
                porezi.add(porez);
            }
            rs.close();
            statement.close();
            return porezi;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void add(Porezi param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            
           new SeasonRepository().add(param, connection);
          
            String upit = "insert into porezi (seasonid,pdv,provizija) values (?,?,?)";
            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getSeason());
            statement.setBigDecimal(2, param.getPDV());
            statement.setBigDecimal(3, param.getProvision());

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
    public void eddit(Porezi param, String oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            
             new SeasonRepository().eddit(param,oldPk, connection);
            
            String upit = "update  porezi set pdv=?,provizija=? where seasonid=?";

            statement = connection.prepareStatement(upit);
            statement.setBigDecimal(1, param.getPDV());
            statement.setBigDecimal(2, param.getProvision());
            statement.setString(3, param.getSeason());
            
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
    public void delete(Porezi param, Connection connection) throws Exception {
        PreparedStatement statement = null;
        try {
            String upit = "delete from porezi  where seasonid=?";

            statement = connection.prepareStatement(upit);

            statement.setString(1, param.getSeason());
            statement.executeUpdate();
           new SeasonRepository().delete(param, connection);
            statement.close();
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Porezi search(String param, Connection connection) throws Exception {
        Statement statement = null;
        try {
            String upit = "SELECT seasonid,pdv,provizija FROM porezi WHERE seasonid="+"'"+param+"'";
            statement = connection.createStatement();
           // statement.setString(1, param);

            ResultSet rs = statement.executeQuery(upit);

            if (rs.next()) {
                Porezi porez = new Porezi();
                porez.setSeason(rs.getString("seasonid"));
                porez.setPDV(rs.getBigDecimal("pdv"));
                porez.setProvision(rs.getBigDecimal("provizija"));

                statement.close();
                return porez;
            }
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        throw new Exception("Porezi nisu pronadjeni");
    }

    @Override
    public String searchUnBoxing(Object obj) {
        return (String) obj;
    }

    @Override
    public Object updateBoxing(Porezi entity, String primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Porezi updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Porezi) ob[0];
    }

    @Override
    public String updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (String) ob[1];
    }

}
