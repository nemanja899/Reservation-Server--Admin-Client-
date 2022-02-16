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
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author User
 */
public class Prices implements Serializable, GeneralDObject {

    private Season season;
    private Animal animal;
    private LovackoDrustvo drustvo;
    private Date seasonStart;
    private Date seasonEnd;
    private BigDecimal price;
    private Currency currency;

    public Prices(Season season, Animal animal, LovackoDrustvo drustvo, Date seasonStart, Date seasonEnd, BigDecimal price, Currency valuta) {
        this.season = season;
        this.animal = animal;
        this.drustvo = drustvo;
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
        this.price = price;
        this.currency = valuta;
    }

    public Prices() {
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LovackoDrustvo getDrustvo() {
        return drustvo;
    }

    public void setDrustvo(LovackoDrustvo drustvo) {
        this.drustvo = drustvo;
    }

    public Date getSeasonStart() {
        return seasonStart;
    }

    public void setSeasonStart(Date seasonStart) {
      
        this.seasonStart = seasonStart;
    }

    public Date getSeasonEnd() {
        return seasonEnd;
    }

    public void setSeasonEnd(Date seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency valuta) {
        this.currency = valuta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.season);
        hash = 73 * hash + Objects.hashCode(this.animal);
        hash = 73 * hash + Objects.hashCode(this.drustvo);
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
        final Prices other = (Prices) obj;
        if (!Objects.equals(this.season, other.season)) {
            return false;
        }
        if (!Objects.equals(this.animal, other.animal)) {
            return false;
        }
        if (!Objects.equals(this.drustvo, other.drustvo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Prices{" + "animal=" + animal + ", drustvo=" + drustvo + ", seasonStart=" + seasonStart + ", seasonEnd=" + seasonEnd + ", price=" + price + ", valuta=" + currency + '}';
    }



    @Override
    public String getRecordColumns() {
        return "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency,"
                + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoin() {
        return "prices p inner join season s on(p.seasonid=s.seasonid) inner join animal a (p.animalid=a.id) "
                + " inner join lovackodrustvo l on (p.lovackodrustvoid=l.id)";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        Prices prices=new Prices();
        Season season = new Season();
        season.setSeason(rs.getString("s.seasonid"));

        Animal animal = new Animal();
        animal.setId(rs.getLong("a.id"));
        animal.setName(rs.getString("a.name"));
        animal.setShortName(rs.getString("a.shortname"));
        animal.setAllowed(rs.getBoolean("a.allowed"));

        LovackoDrustvo drustvo = new LovackoDrustvo();
        drustvo.setId(rs.getLong("l.id"));
        drustvo.setName(rs.getString("l.name"));
        drustvo.setCounty(rs.getString("l.county"));
        drustvo.setAdress(rs.getString("l.adress"));

        prices.setSeason(season);
        prices.setAnimal(animal);
        prices.setDrustvo(drustvo);
        prices.setPrice(rs.getBigDecimal("p.price"));
        prices.setCurrency(Currency.valueOf(rs.getString("p.currency").toUpperCase()));
        prices.setSeasonStart(rs.getDate("p.seasonstart"));
        prices.setSeasonEnd(rs.getDate("p.seasonend"));

  
        return prices;
    }

    @Override
    public String getClassName() {
        return "prices";
    }

    @Override
    public String getColumns() {
        return "seasonid,animalid,lovackodrustvoid,seasonstart,seasonend,price,currency";
    }

    @Override
    public String getAddValues() {
        return "?,?,?,?,?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setString(1, ((Prices) param).getSeason().getSeason());
        statement.setLong(2, ((Prices) param).getAnimal().getId());
        statement.setLong(3, ((Prices) param).getDrustvo().getId());
        statement.setDate(4, new java.sql.Date(((Prices) param).getSeasonStart().getTime()));
        statement.setDate(5, new java.sql.Date(((Prices) param).getSeasonEnd().getTime()));
        statement.setBigDecimal(6, ((Prices) param).getPrice());
        statement.setString(7, ((Prices) param).getCurrency().name());

    }

    @Override
    public String getUpdateValues() {
        return "seasonid=?,animalid=?,lovackodrustvoid=?,"
                + "seasonstart=?,seasonend=?,price=?,currency=?";
    }

    @Override
    public String getUpdateCondition() {
        return "seasonid=? and animalid=? and lovackodrustvoid=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldPk, PreparedStatement statement) throws Exception {
        Object[] obj = (Object[]) oldPk;
        String sezona = ((Season) obj[0]).getSeason();
        Long animal = ((Animal) obj[1]).getId();
        Long drustvo = ((LovackoDrustvo)obj[2]).getId();

        statement.setString(1, ((Prices) param).getSeason().getSeason());
        statement.setLong(2, ((Prices) param).getAnimal().getId());
        statement.setLong(3, ((Prices) param).getDrustvo().getId());
        statement.setDate(4, new java.sql.Date(((Prices) param).getSeasonStart().getTime()));
        statement.setDate(5, new java.sql.Date(((Prices) param).getSeasonEnd().getTime()));
        statement.setBigDecimal(6, ((Prices) param).getPrice());
        statement.setString(7, ((Prices) param).getCurrency().name());
        statement.setString(8, sezona);
        statement.setLong(9, animal);
        statement.setLong(10, drustvo);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "p.seasonid=? and p.animalid=? and p.lovackodrustvoid=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        Object[] obj = (Object[]) param;
        String sezona = (String) obj[0];
        Long animal = (Long) obj[1];
        Long drustvo = (Long) obj[2];
        statement.setString(1, sezona);
        statement.setLong(2, animal);
        statement.setLong(3, drustvo);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setString(1, ((Prices) param).getSeason().getSeason());
        statement.setLong(2, ((Prices) param).getAnimal().getId());
        statement.setLong(3, ((Prices) param).getDrustvo().getId());
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception {
        //
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency,"
                + "s.seasonid, a.id,a.name,a.shortname,a.allowed,"
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "prices p inner join season s on ( p.seasonid=s.seasonid ) inner join animal a on ( p.animalid=a.id ) "
                + " inner join lovackodrustvo l on ( p.lovackodrustvoid=l.id )";
    }

    @Override
    public String getConditionGetAll() {
        return "p.seasonid = ? ";
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
        return new Object[]{getSeason().getSeason(), getAnimal().getId(), getDrustvo().getId()};
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
        return "Lista cenovnika je prazna";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska lista cenovnika ne moze da se generise";
    }

    @Override
    public String getErrorAdd() {
        return "Greska cenovnik ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska cenovnik ne moze da se pronadje";
    }

    @Override
    public String getErrorNotFound() {
        return "Cenovnik nije pronadjen";
    }

    @Override
    public String getErrorEddit() {
        return "Cenovnik ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Cenovnik ne moze da se obrise";
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
