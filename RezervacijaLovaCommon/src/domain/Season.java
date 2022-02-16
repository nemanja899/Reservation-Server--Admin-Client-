/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import validation.ValidationException;
import validation.Validator;

/**
 *
 * @author User
 */
public class Season implements Serializable, GeneralDObject {

    private String season;

    public Season(String season) {
        this.season = season;
    }

    public Season() {
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        // if(season.split("/")[0]. && season.split("/")[0])

        this.season = season;
    }

    public boolean isDateInSeason(int year, int month) {

        if ((this.getSeasonStart() == year && month > 8) || (this.getSeasonEnd() == year && month <= 8)) {
            return true;
        }

        return false;
    }

    public int getSeasonStart() {

        return Integer.parseInt(season.split("/")[0]);
    }

    public int getSeasonEnd() {

        return Integer.parseInt(season.split("/")[1]);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.season);
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
        final Season other = (Season) obj;
        if (!Objects.equals(this.season, other.season)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return season;
    }


    @Override
    public String getRecordColumns() {
        return "seasonid";
    }

    @Override
    public String getClassNameJoin() {
        return "season order by seasonid";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        Season s=new Season();
        s.setSeason(rs.getString(1));
        return s;
    }

    @Override
    public String getClassName() {
        return "season";
    }

    @Override
    public String getColumns() {
        return "seasonid";
    }

    @Override
    public String getAddValues() {
        return "?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject odk) throws Exception {
        statement.setString(1, ((Season) odk).getSeason());
    }

    @Override
    public String getUpdateValues() {
        return "seasonid=?";
    }

    @Override
    public String getUpdateCondition() {
        return "seasonid=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject odk, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setString(1, ((Season) odk).getSeason());
        statement.setString(2, (String) oldpk);
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
        statement.setString(1, ((Season) param).getSeason());
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
        //
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "seasonid";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "season";
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
        Porezi p = new Porezi();
        p.setSeason(getSeason());
        return p;
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
        return "Lista sezona je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska u generisanju liste sezona";
    }

    @Override
    public String getErrorAdd() {
        return "Greska u dodavanju sezone";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazi sezone";
    }

    @Override
    public String getErrorNotFound() {
        return "Sezona nije pronadjena";
    }

    @Override
    public String getErrorEddit() {
        return "Greska u izmeni sezone";
    }

    @Override
    public String getErrorDelete() {
        return "Greska u brisanju sezone";
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
