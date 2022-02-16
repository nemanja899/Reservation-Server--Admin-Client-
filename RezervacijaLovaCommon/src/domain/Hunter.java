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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import validation.ValidationException;

/**
 *
 * @author User
 */
public class Hunter implements Serializable, GeneralDObject {

    private String passportNo;
    private String fullName;
    private String country;
    private Date birthDate;

    public Hunter(String passportNo, String fullName, String country, Date birthDate) {
        this.passportNo = passportNo;
        this.fullName = fullName;
        this.country = country;
        this.birthDate = birthDate;
    }

    public Hunter() {
    }

    public String getpassportNo() {
        return passportNo;
    }

    public void setpassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) throws ValidationException, ParseException {

        Date birth = birthDate;
        Date today = new Date();

        long diff = today.getTime() - birth.getTime();

        TimeUnit time = TimeUnit.DAYS;
        long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
        long year = diffrence / 365l;
        if (year >= 18l) {
            this.birthDate = birth;
        } else {
            throw new ValidationException("Osoba je maloletna");
        }

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
        final Hunter other = (Hunter) obj;
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.passportNo, other.passportNo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return passportNo + ", " + fullName + ", " + country;
    }


    @Override
    public String getRecordColumns() {
        return "passportNo,fullname,country,birthdate";
    }

    @Override
    public String getClassNameJoin() {
        return "hunter";
    }

    @Override
    public String getClassName() {
        return "hunter";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        try {
            Hunter hunter=new Hunter();
            hunter.setpassportNo(rs.getString("passportNo"));
            hunter.setFullName(rs.getString("fullname"));
            hunter.setCountry(rs.getString("country"));
            hunter.setBirthDate(rs.getDate("birthdate"));
            return hunter;
        } catch (ValidationException ex) {
            throw new Exception(ex.getMessage());
        } catch (ParseException ex) {
            throw new Exception(ex.getMessage());
        }
       
    }

    @Override
    public String getColumns() {
        return "passportNo,fullname,country,birthdate";
    }

    @Override
    public String getAddValues() {
        return "?,?,?,?";
    }

    @Override
    public List<GeneralDObject> getList() {
        return null;
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws SQLException {
        statement.setString(1, ((Hunter) param).getpassportNo());
        statement.setString(2, ((Hunter) param).getFullName());
        statement.setString(3, ((Hunter) param).getCountry());
        statement.setDate(4, new java.sql.Date(((Hunter) param).getBirthDate().getTime()));
    }

    @Override
    public String getUpdateValues() {
        return "passportNo=?,fullname=?,country=?";
    }

    @Override
    public String getUpdateCondition() {
        return "passportNo=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws SQLException {
        statement.setString(1, ((Hunter) param).getpassportNo());
        statement.setString(2, ((Hunter) param).getFullName());
        statement.setString(3, ((Hunter) param).getCountry());
        statement.setString(4, (String) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "passportNo in (?)";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws SQLException {
        statement.setString(1, (String) param);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws SQLException {
        statement.setString(1, ((Hunter) param).getpassportNo());
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws SQLException {
        //
    }

    @Override
    public void setList(List<GeneralDObject> list) {
        //
    }

    @Override
    public Object getPk() {
        return passportNo;
    }

    @Override
    public GeneralDObject getChild() {
        return null;
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "";
    }

    @Override
    public void setParentID(Long id) {
        //
    }

    @Override
    public String getConditionGetAll() {
        return "";
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
        return "Lista lovaca je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska u generisanju liste Lovaca";
    }

    @Override
    public String getErrorAdd() {
        return "Lovac ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazivanju Lovca";
    }

    @Override
    public String getErrorNotFound() {
        return "Lovac nije nadjen";
    }

    @Override
    public String getErrorEddit() {
        return "Grska u izmeni lovca";
    }

    @Override
    public String getErrorDelete() {
        return "Greska u brisanju lovca";
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
