/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Animal implements Serializable, GeneralDObject {

    private Long id;
    private String name;
    private String shortName;

    private boolean allowed;

    public Animal() {
    }

    public Animal(String name, String shortName, boolean allowed) {
        this.name = name;
        this.shortName = shortName;
        this.allowed = allowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Animal other = (Animal) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String getRecordColumns() {
        return "id,name,shortname,allowed";
    }

    @Override
    public String getClassNameJoin() {
        return "animal";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws SQLException {
        try {
            Animal animal = new Animal();
            animal.setId(rs.getLong("id"));
            animal.setName(rs.getString("name"));
            animal.setShortName(rs.getString("shortname"));
            animal.setAllowed(rs.getBoolean("allowed"));
            return animal;
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u parsovanju podataka Divljaci");
        }

    }

    @Override
    public String getColumns() {
        return "name,shortname,allowed";
    }

    @Override
    public String getClassName() {
        return "animal";
    }

    @Override
    public String getAddValues() {
        return "?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws SQLException {
        try {
            statement.setString(1, ((Animal) param).getName());
            statement.setString(2, ((Animal) param).getShortName());
            statement.setBoolean(3, ((Animal) param).isAllowed());
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u postavljanju vrednosti u statementu Divljaci");
        }
    }

    @Override
    public String getUpdateValues() {
        return "name=?, shortname=?, allowed=?";
    }

    @Override
    public String getUpdateCondition() {
        return "id=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws SQLException {
        try {
            statement.setString(1, ((Animal) param).getName());
            statement.setString(2, ((Animal) param).getShortName());
            statement.setBoolean(3, ((Animal) param).isAllowed());
            statement.setLong(4, (long) oldpk);
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u postavljanju vrednosti divljaci");
        }
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "id=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws SQLException {
        try {
            statement.setLong(1, (long) param);
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u postavljanju uslova promene Divljaci");
        }
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws SQLException {
        try {
            statement.setLong(1, ((Animal) param).getId());
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u postavljanju uslova brisanja zivotinje");
        }

    }

    @Override
    public String getRecordColumnsByCondition() {
        return " id,name,shortname,allowed";

    }

    @Override
    public String getClassNameJoinByCondition() {
        return "animal";
    }

    @Override
    public String getConditionGetAll() {
        return "id not in (select animalid from prices where seasonid=? and lovackodrustvoid=?) and allowed=1";
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws SQLException {
        try {
            Object[] ob = (Object[]) obj;

            statement.setString(1, ((Season) ob[0]).getSeason());
            statement.setLong(2, ((LovackoDrustvo) ob[1]).getId());
        } catch (SQLException ex) {
            Logger.getLogger(Animal.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Greska u postavljanju uslova sezone");
        }
    }

    @Override
    public Object getPk() {
        return id;
    }

    @Override
    public List<GeneralDObject> getList() {
        return null;
    }

    @Override
    public void setList(List<GeneralDObject> list) {
        //
    }

    @Override
    public GeneralDObject getChild() {
        return null;
    }

    @Override
    public void setParentID(Long id) {
        //
    }

    @Override
    public GeneralDObject getParentDO() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public boolean addAnother(GeneralDObject odk) {
        return false;
    }

    @Override
    public boolean deleteParent() {
        return false;
    }

    @Override
    public String getErrorEmptyList() {
        return "Lista Divljaci je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Lista Divljaci ne moze da se ucita";
    }

    @Override
    public String getErrorAdd() {
        return "Divljac ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazivanju Divljaci";
    }

    @Override
    public String getErrorNotFound() {
        return "Divljac nije nadjena";
    }

    @Override
    public String getErrorEddit() {
        return "Divljac ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Divljac ne moze da se obrise";
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws SQLException {
        //
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
