/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author User
 */
public class Reservation implements Serializable, GeneralDObject {

    private Long id;
    private Season season;
    private LovackoDrustvo drustvo;
    private Hunter hunter;

    public Reservation() {
    }

    public Reservation(Long id, Season season, LovackoDrustvo drustvo, Hunter hunter) {
        this.id = id;
        this.season = season;
        this.drustvo = drustvo;
        this.hunter = hunter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public LovackoDrustvo getDrustvo() {
        return drustvo;
    }

    public void setDrustvo(LovackoDrustvo drustvo) {
        this.drustvo = drustvo;
    }

    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Reservation other = (Reservation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reservation{" + "season=" + season + ", drustvo=" + drustvo + ", hunter=" + hunter + '}';
    }

  

    @Override
    public String getRecordColumns() {
        return "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid,"
                + "s.seasonid,l.id,l.name,l.county,l.adress,"
                + "h.passportNo,h.fullname,h.country,h.birthdate";
    }

    @Override
    public String getClassNameJoin() {
        return "reservation r inner join season s on (r.seasonid=s.seasonid) "
                + "inner join lovackodrustvo l on (r.lovackodrustvoid=l.id) "
                + "inner join hunter h on (r.hunterid=h.passportNo)";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        Reservation r=new Reservation();
        Season season = new Season();
        season.setSeason(rs.getString("s.seasonid"));

        LovackoDrustvo drustvo = new LovackoDrustvo();
        drustvo.setId(rs.getLong("l.id"));
        drustvo.setName(rs.getString("l.name"));
        drustvo.setCounty(rs.getString("l.county"));
        drustvo.setAdress(rs.getString("l.adress"));

        Hunter hunter = new Hunter();
        hunter.setpassportNo(rs.getString("h.passportNo"));
        hunter.setFullName(rs.getString("h.fullname"));
        hunter.setCountry(rs.getString("h.country"));
        hunter.setBirthDate(rs.getDate("h.birthdate"));

        r.setId(rs.getLong("r.id"));
        r.setDrustvo(drustvo);
        r.setHunter(hunter);
        r.setSeason(season);

        return r;

    }

    @Override
    public String getClassName() {
        return "reservation";
    }

    @Override
    public String getColumns() {
        return "seasonid,lovackodrustvoid,hunterid";
    }

    @Override
    public String getAddValues() {
        return "?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setString(1, ((Reservation) param).getSeason().getSeason());
        statement.setLong(2, ((Reservation) param).getDrustvo().getId());
        statement.setString(3, ((Reservation) param).getHunter().getpassportNo());
    }

    @Override
    public String getUpdateValues() {
        return "seasonid=?,lovackodrustvoid=?,hunterid=?";
    }

    @Override
    public String getUpdateCondition() {
        return "id=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setString(1, ((Reservation) param).getSeason().getSeason());
        statement.setLong(2, ((Reservation) param).getDrustvo().getId());
        statement.setString(3, ((Reservation) param).getHunter().getpassportNo());
        statement.setLong(4, (long) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "r.seasonid=? and r.lovackodrustvoid=? and r.hunterid=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        Object[] ob=(Object[]) param;
        statement.setString(1, (String) ob[0]);
        statement.setLong(2, (long) ob[1]);
        statement.setString(3, (String) ob[2]);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setLong(1, ((Reservation) param).getId());
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
        //
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid,"
                + "s.seasonid,l.id,l.name,l.county,l.adress,"
                + "h.passportNo,h.fullname,h.country,h.birthdate";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "reservation r inner join season s on (r.seasonid=s.seasonid) "
                + "inner join lovackodrustvo l on (r.lovackodrustvoid=l.id) "
                + "inner join hunter h on (r.hunterid=h.passportNo)";
    }

    @Override
    public String getConditionGetAll() {
        return "s.seasonid in (?) "
                + "order by s.seasonid,l.name,h.fullname";
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception {
        statement.setString(1, ((Season) obj).getSeason());
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
        return getId();
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
        return getHunter();
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public boolean addAnother(GeneralDObject odk) {
        Reservation res = (Reservation) odk;
        Hunter param=res.getHunter();
        if (param.getFullName() != "" && param.getCountry() != "" && param.getBirthDate() != null && !param.getpassportNo().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteParent() {
        return false;
    }

    @Override
    public String getErrorEmptyList() {
        return "Lista rezervacija je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska u generisanju liste rezervacija";
    }

    @Override
    public String getErrorAdd() {
        return "Greska u dodavanju rezervacije";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazi rezervacije";
    }

    @Override
    public String getErrorNotFound() {
        return "Rezervacija nije pronadjena";
    }

    @Override
    public String getErrorEddit() {
        return "Greska rezervacija ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Greska rezervacija ne moze da se obrise";
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
