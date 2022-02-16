/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.repository.db.impl;

import java.util.ArrayList;
import java.util.List;
import domain.Season;
import project.repository.db.DbRepository;
import java.sql.*;
import java.time.LocalDate;
import project.repository.db.DbConnectionFactory;
import project.boxingunboxing.IBoxingUnboxing;

/**
 *
 * @author User
 */
public class SeasonRepository implements DbRepository<Season, String, Connection>, IBoxingUnboxing<Season, String> {

    @Override
    public List<Season> getAll(Connection connection) throws Exception {
        List<Season> seasons = new ArrayList<>();
        Statement statement = null;

        try {

            String upit = "select seasonid from season order by seasonid";

            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(upit);

            while (rs.next()) {

                Season season = new Season();

                season.setSeason(rs.getString(1));

                seasons.add(season);
            }
            rs.close();
            statement.close();
            return seasons;
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public void add(Season param, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {

            String upit = "insert into season (seasonid) values(?)";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getSeason());
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
    public void eddit(Season param, String oldPk, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {

            String upit = "update  season set seasonid=? where seasonid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getSeason());
            statement.setString(2, oldPk);
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
    public void delete(Season param, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            String upit = "delete from  season where seasonid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param.getSeason());
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
    public Season search(String param, Connection connection) throws Exception {
        PreparedStatement statement = null;

        try {
            String upit = "select seasonid from season where seasonid=?";
            statement = connection.prepareStatement(upit);
            statement.setString(1, param);
            ResultSet rs = statement.executeQuery(upit);
            while (rs.next()) {
                Season season = new Season();
                season.setSeason(rs.getString("seasonid"));
                statement.close();
                return season;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (statement != null) {
                statement.close();
            }
            throw new Exception(e.getMessage());
        }
        throw new Exception("sezona nije pronadjena");
    }

    @Override
    public String searchUnBoxing(Object obj) {
        return (String) obj;
    }

    @Override
    public Object updateBoxing(Season entity, String primitive) {
        return new Object[]{entity, primitive};
    }

    @Override
    public Season updateFirstObject(Object obj) {
        Object[] ob = (Object[]) obj;
        return (Season) ob[0];
    }

    @Override
    public String updateSecondPrimitive(Object obj) {
        Object[] ob = (Object[]) obj;
        return (String) ob[1];
    }

}
