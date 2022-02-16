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


/**
 *
 * @author User
 */
public class User implements Serializable, GeneralDObject {

    private Long id;
    private LovackoDrustvo lovackoDrustvoid;
    private String name;
    private String lastName;
    private String email;
    private String password;

    public User(LovackoDrustvo lovackoDrustvoid, String name, String lastName, String email, String password) {
        this.lovackoDrustvoid = lovackoDrustvoid;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LovackoDrustvo getLovackoDrustvoid() {
        return lovackoDrustvoid;
    }

    public void setLovackoDrustvoid(LovackoDrustvo lovackoDrustvoid) {
        this.lovackoDrustvoid = lovackoDrustvoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        final User other = (User) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return lovackoDrustvoid + ": " + email;
    }
 

    @Override
    public String getRecordColumns() {
        return "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password, "
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoin() {
        return "user u inner join lovackodrustvo l on (u.lovackodrustvoid=l.id)";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        User u= new User();
        LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
        lovackoDrustvo.setId(rs.getLong("l.id"));
        lovackoDrustvo.setName(rs.getString("l.name"));
        lovackoDrustvo.setCounty(rs.getString("l.county"));
        lovackoDrustvo.setAdress(rs.getString("l.adress"));

        u.setLovackoDrustvoid(lovackoDrustvo);
        u.setId(rs.getLong("u.id"));
        u.setName(rs.getString("u.name"));
        u.setLastName(rs.getString("u.lastname"));
        u.setEmail(rs.getString("u.email"));
        u.setPassword(rs.getString("u.password"));

        return u;
    }

    @Override
    public String getClassName() {
        return "user";
    }

    @Override
    public String getColumns() {
        return "lovackodrustvoid,name,lastname,email,password";
    }

    @Override
    public String getAddValues() {
        return "?,?,?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setLong(1, ((User) param).getLovackoDrustvoid().getId());
        statement.setString(2, ((User) param).getName());
        statement.setString(3, ((User) param).getLastName());
        statement.setString(4, ((User) param).getEmail());
        statement.setString(5, ((User) param).getPassword());
    }

    @Override
    public String getUpdateValues() {
        return "lovackodrustvoid=?,name=?,lastname=?,email=?,password=?";
    }

    @Override
    public String getUpdateCondition() {
        return "email=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setLong(1, ((User) param).getLovackoDrustvoid().getId());
        statement.setString(2, ((User) param).getName());
        statement.setString(3, ((User) param).getLastName());
        statement.setString(4, ((User) param).getEmail());
        statement.setString(5, ((User) param).getPassword());
        statement.setString(6, (String) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "email=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        statement.setString(1, (String) param);

    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setString(1, ((User) param).getEmail());
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
        //
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password, "
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "user u inner join lovackodrustvo l on (u.lovackodrustvoid=l.id)";
    }

    @Override
    public String getConditionGetAll() {
        return "u.email=?";
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
        return new Object[]{getId(), getLovackoDrustvoid().getId()};
    }

    @Override
    public void setParentID(Long id) {
        //
    }

    @Override
    public GeneralDObject getChild() {
        return null;
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
        return "Korisnici ne mogu da se ucitaju";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska u generisanju liste korisnika";
    }

    @Override
    public String getErrorAdd() {
        return "Greska u dodavanju korisnika";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazi korisnika";
    }

    @Override
    public String getErrorNotFound() {
        return "Korisnik nije pronadjen";
    }

    @Override
    public String getErrorEddit() {
        return "Korisnik ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Korisnik ne moze da se obrise";
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
