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
public class LovackoDrustvo implements Serializable, GeneralDObject {

    private Long id;
    private String name;
    private String county;
    private String adress;

    public LovackoDrustvo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
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
        final LovackoDrustvo other = (LovackoDrustvo) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public LovackoDrustvo(Long id, String name, String opstina, String adresa) {
        this.id = id;

        this.name = name;
        this.county = opstina;
        this.adress = adresa;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String opstina) {
        this.county = opstina;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adresa) {
        this.adress = adresa;
    }


    @Override
    public String getRecordColumns() {
        return "id,name,county,adress ";
    }

    @Override
    public String getClassNameJoin() {
        return "lovackodrustvo";
    }

    @Override
    public String getClassName() {
        return "lovackodrustvo";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        LovackoDrustvo drustvo= new LovackoDrustvo();
        drustvo.setId(rs.getLong("id"));
        drustvo.setName(rs.getString("name"));
        drustvo.setCounty(rs.getString("county"));
        drustvo.setAdress(rs.getString("adress"));
        return drustvo;
    }

    @Override
    public String getColumns() {
        return "name,county,adress";
    }

    @Override
    public String getAddValues() {
        return "?,?,?";
    }

    @Override
    public List<GeneralDObject> getList() {
        return null;
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setString(1, ((LovackoDrustvo) param).getName());
        statement.setString(2, ((LovackoDrustvo) param).getCounty());
        statement.setString(3, ((LovackoDrustvo) param).getAdress());
    }

    @Override
    public String getUpdateValues() {
        return "name=?,county=?,adress=?";
    }

    @Override
    public String getUpdateCondition() {
        return "name=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setString(1, ((LovackoDrustvo) param).getName());
        statement.setString(2, ((LovackoDrustvo) param).getCounty());
        statement.setString(3, ((LovackoDrustvo) param).getAdress());
        statement.setString(4, (String) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "name=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        statement.setString(1, (String) param);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setString(1, ((LovackoDrustvo) param).getName());
    }

    @Override
    public void setList(List<GeneralDObject> odk) {
        //
    }

    @Override
    public GeneralDObject getChild() {
        return null;
    }

    @Override
    public Object getPk() {
        return id;
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "id,name,county,adress";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "lovackodrustvo where id not in ("
                + "select distinct lovackodrustvoid "
                + "from prices";
    }

    @Override
    public String getConditionGetAll() {
        return "seasonid=?)";
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception {
        statement.setString(1, ((Season) obj).getSeason());
    }

    @Override
    public void setParentID(Long id) {
        //
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
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
        return "Lista lovackih drustva je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska Lovacka drustva ne mogu da se ucitaju";
    }

    @Override
    public String getErrorAdd() {
        return "Greska lovаcko drustvo ne moze da se sacuva";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazi lovackog drustva";
    }

    @Override
    public String getErrorNotFound() {
        return "Lovacko drustvo nije pronadjeno";
    }

    @Override
    public String getErrorEddit() {
        return "Lovacko drustvo ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Lovacko drustvo ne moze da se obrise";
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
