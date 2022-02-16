/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class Porezi extends Season implements Serializable, GeneralDObject {

    private BigDecimal PDV;
    private BigDecimal provision;

    public BigDecimal getPDV() {
        return PDV;
    }

    public void setPDV(BigDecimal PDV) {
        this.PDV = PDV;
    }

    public BigDecimal getProvision() {
        return provision;
    }

    public void setProvision(BigDecimal provision) {
        this.provision = provision;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Porezi other = (Porezi) obj;
        if (!Objects.equals(this.getSeason(), other.getSeason())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Porezi{" + "season=" + getSeason() + ", PDV=" + PDV + ", provision=" + provision + '}';
    }

    @Override
    public String getRecordColumns() {
        return "seasonid,pdv,provizija";
    }

    @Override
    public String getClassNameJoin() {
        return "porezi";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        Porezi porezi= new Porezi();
        porezi.setSeason(rs.getString("seasonid"));
        porezi.setPDV(rs.getBigDecimal("pdv"));
        porezi.setProvision(rs.getBigDecimal("provizija"));
        return porezi;
    }

    @Override
    public String getClassName() {
        return "porezi";
    }

    @Override
    public String getColumns() {
        return "seasonid,pdv,provizija";
    }

    @Override
    public String getAddValues() {
        return "?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setString(1, ((Porezi) param).getSeason());
        statement.setBigDecimal(2, ((Porezi) param).getPDV());
        statement.setBigDecimal(3, ((Porezi) param).getProvision());

    }

    @Override
    public String getUpdateValues() {
        return "seasonid=?,pdv=?,provizija=?";
    }

    @Override
    public String getUpdateCondition() {
        return "seasonid=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setBigDecimal(2, ((Porezi) param).getPDV());
        statement.setBigDecimal(3, ((Porezi) param).getProvision());
        statement.setString(1, ((Porezi) param).getSeason());
        statement.setString(4, (String) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "seasonid=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        statement.setString(1, (String) param);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setString(1, ((Porezi) param).getSeason());
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
        statement.setString(1, (String) condition);
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "seasonid,pdv,provizija";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "porezi";
    }

    @Override
    public String getConditionGetAll() {
        return "seasonid=?";
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception {
        statement.setString(1, (String) obj);

    }

    @Override
    public void setList(List<GeneralDObject> odk) {
        //
    }

    @Override
    public List<GeneralDObject> getList() {
        return null;
    }

    @Override
    public Object getPk() {
        return getSeason();
    }

    @Override
    public void setParentID(Long id) {
        //
    }

    @Override
    public GeneralDObject getChild() {
        return  null;
    }

    @Override
    public GeneralDObject getParentDO() {
        return new Season(getSeason());
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public boolean addAnother(GeneralDObject odk) {
        return true;
    }

    @Override
    public boolean deleteParent() {
        return true;
    }

    @Override
    public String getErrorEmptyList() {
        return "Lista Poreza je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Lista poreza ne mogu da se ucitaju";
    }

    @Override
    public String getErrorAdd() {
        return "Porez ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska Porez ne moze da se pronadje";
    }

    @Override
    public String getErrorNotFound() {
        return "Porez ne postoji";
    }

    @Override
    public String getErrorEddit() {
        return "Porez ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return  "Porez ne moze da se obrise";
    }

    @Override
    public Object getParrentPk() {
        return getSeason();
    }

}
