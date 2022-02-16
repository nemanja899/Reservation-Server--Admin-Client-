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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class Invoice implements Serializable, GeneralDObject {

    private Long id;
    private String number;
    private BigDecimal totalCost;
    private Currency currency;
    private Date dateCreated;
    private Porezi porezi;
    private Hunter hunter;
    private Reservation reservation;
    private List<InvoiceItem> invoiceItems;
    private boolean obradjen;
    private User createdBy;

    public Invoice() {
        obradjen = false;
        invoiceItems=new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Porezi getPorezi() {
        return porezi;
    }

    public void setPorezi(Porezi porezi) {
        this.porezi = porezi;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Invoice(Long id, BigDecimal totalCost, Currency currency, Date dateCreated, BigDecimal PDV, BigDecimal provizija, Hunter hunter, List<InvoiceItem> invoiceItems, Porezi porezi, Reservation reservation) {
        this.id = id;
        this.totalCost = totalCost;
        this.currency = currency;
        this.dateCreated = dateCreated;
        this.porezi = porezi;
        this.reservation = reservation;
        this.hunter = hunter;
        this.invoiceItems = invoiceItems;
        obradjen = false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Invoice other = (Invoice) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Broj:"+number+"     ,"+dateCreated + ",      Pasos=" + hunter.getpassportNo()+",    "+"Ime:"+hunter.getFullName()+",    "+(obradjen? "obradjen":"neobradjen");
    }

    public boolean isObradjen() {
        return obradjen;
    }

    public void setObradjen(boolean obradjen) {
        this.obradjen = obradjen;
    }

    @Override
    public String getRecordColumns() {
        return "i.id, i.number,i.totalCost,i.currency,i.dateCreated,i.poreziid,i.hunterid,i.obradjen,i.reservationid,i.createdBy "
                + ",p.seasonid,p.pdv,p.provizija "
                + ",h.passportNo,h.fullname,h.country,h.birthdate "
                + "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid "
                + "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password "
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoin() {
        return "invoice i inner join porezi p on (i.poreziid=p.seasonid)"
                + " inner join hunter h on (i.hunterid=h.passportNo) "
                + "inner join reservation r on (i.reservationid=r.id) "
                + "inner join user u on (i.createdBy=u.id) "
                + "inner join lovadkodrustvo l  on (u.lovackodrustvoid=l.id)";
    }

    @Override
    public String getClassName() {
        return "invoice";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("i.id"));
        invoice.setCurrency(Currency.valueOf(rs.getString("i.currency").toUpperCase()));
        invoice.setTotalCost(rs.getBigDecimal("i.totalcost"));
        invoice.setNumber(rs.getString("i.number"));
        invoice.setDateCreated(rs.getDate("i.dateCreated"));
        invoice.setObradjen(rs.getBoolean("i.obradjen"));

        Porezi porezi = new Porezi();
        porezi.setSeason(rs.getString("p.seasonid"));
        porezi.setPDV(rs.getBigDecimal("p.pdv"));
        porezi.setProvision(rs.getBigDecimal("p.provizija"));
        invoice.setPorezi(porezi);

        LovackoDrustvo lovackoDrustvo = new LovackoDrustvo();
        lovackoDrustvo.setId(rs.getLong("l.id"));
        lovackoDrustvo.setName(rs.getString("l.name"));
        lovackoDrustvo.setCounty(rs.getString("l.county"));
        lovackoDrustvo.setAdress(rs.getString("l.adress"));

        Hunter hunter = new Hunter();
        hunter.setpassportNo(rs.getString("h.passportNo"));
        hunter.setFullName(rs.getString("h.fullname"));
        hunter.setCountry(rs.getString("h.country"));
        hunter.setBirthDate(rs.getDate("h.birthdate"));

        User user = new User();
        user.setLovackoDrustvoid(lovackoDrustvo);
        user.setId(rs.getLong("u.id"));
        user.setEmail("u.email");

        Reservation reservation = new Reservation();
        reservation.setDrustvo(lovackoDrustvo);
        reservation.setHunter(hunter);
        reservation.setSeason(new Season(porezi.getSeason()));
        reservation.setId(rs.getLong("r.id"));

        invoice.setReservation(reservation);
        invoice.setCreatedBy(user);
        invoice.setHunter(hunter);
        return invoice;
    }

    @Override
    public void setList(List<GeneralDObject> list) {
        for (GeneralDObject gdo : list) {
            invoiceItems.add((InvoiceItem) gdo);
        }
    }

    @Override
    public GeneralDObject getChild() {
        return (GeneralDObject) new InvoiceItem();
    }

    @Override
    public String getColumns() {
        return "number,totalcost,currency,dateCreated,poreziid,hunterid,obradjen,reservationid,createdBy";
    }

    @Override
    public String getAddValues() {
        return "?,?,?,?,?,?,?,?,?";
    }

    @Override
    public List<GeneralDObject> getList() {
        List<GeneralDObject> gdos = new ArrayList();
        for (InvoiceItem item : invoiceItems) {
            gdos.add((GeneralDObject) item);
        }
        return gdos;
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject param) throws Exception {
        statement.setString(1, ((Invoice) param).getNumber());
        statement.setBigDecimal(2, ((Invoice) param).getTotalCost());
        statement.setString(3, ((Invoice) param).getCurrency().name());
        statement.setDate(4, new java.sql.Date(((Invoice) param).getDateCreated().getTime()));
        statement.setString(5, ((Invoice) param).getPorezi().getSeason());
        statement.setString(6, ((Invoice) param).getHunter().getpassportNo());
        statement.setBoolean(7, ((Invoice) param).isObradjen());
        statement.setLong(8, ((Invoice) param).getReservation().getId());
        statement.setLong(9, ((Invoice) param).getCreatedBy().getId());

    }

    @Override
    public String getUpdateValues() {
        return "number=?,totalcost=?,currency=?,dateCreated=?,poreziid=?,hunterid=?,obradjen=?,reservationid=?,createdBy=?";
    }

    @Override
    public String getUpdateCondition() {
        return "id=?";
    }

    @Override
    public void setUpdateValues(GeneralDObject param, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setString(1, ((Invoice) param).getNumber());
        statement.setBigDecimal(2, ((Invoice) param).getTotalCost());
        statement.setString(3, ((Invoice) param).getCurrency().name());
        statement.setDate(4, new java.sql.Date(((Invoice) param).getDateCreated().getTime()));
        statement.setString(5, ((Invoice) param).getPorezi().getSeason());
        statement.setString(6, ((Invoice) param).getHunter().getpassportNo());
        statement.setBoolean(7, ((Invoice) param).isObradjen());
        statement.setLong(8, ((Invoice) param).getReservation().getId());
        statement.setLong(9, ((Invoice) param).getCreatedBy().getId());

        statement.setLong(10, (long) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "id=?";

    }

    @Override
    public Object getPk() {
        return id;
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        statement.setLong(1, (long) param);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject param, PreparedStatement statement) throws Exception {
        statement.setLong(1, ((Invoice) param).getId());
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "i.id, i.number,i.totalCost,i.currency,i.dateCreated,i.poreziid,i.hunterid,i.obradjen,i.reservationid,i.createdBy "
                + ",p.seasonid,p.pdv,p.provizija "
                + ",h.passportNo,h.fullname,h.country,h.birthdate, "
                + "r.id,r.seasonid,r.lovackodrustvoid,r.hunterid, "
                + "u.lovackodrustvoid,u.id,u.name,u.lastname,u.email,u.password, "
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "invoice i inner join porezi p on (i.poreziid=p.seasonid)"
                + " inner join hunter h on(i.hunterid=h.passportNo) "
                + "inner join reservation r on (i.reservationid=r.id) "
                + "inner join user u on (i.createdBy=u.id) "
                + "inner join lovackodrustvo l  on (u.lovackodrustvoid=l.id)";
    }

    @Override
    public String getConditionGetAll() {
        return "l.id=?";
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception {
        statement.setLong(1, ((LovackoDrustvo)obj).getId());
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
        return true;
    }

    @Override
    public boolean deleteParent() {
        return false;
    }

    @Override
    public String getErrorEmptyList() {
        return "Racuni ne mogu da se ucitaju";
    }

    @Override
    public String getErrorGetAll() {
        return "Greska racuni ne mogu da se ucitaju";
    }

    @Override
    public String getErrorAdd() {
        return "Greska racun ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska racun ne moze da se pronadje";
    }

    @Override
    public String getErrorNotFound() {
        return "Racun nije nadjen";
    }

    @Override
    public String getErrorEddit() {
        return "Racun ne moze da se promeni";
    }

    @Override
    public String getErrorDelete() {
        return "Greska u brisanju racuna";
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) {
        //
    }

    @Override
    public boolean addAnother(GeneralDObject odk) {
        return false;
    }

    @Override
    public Object getParrentPk() {
        return null;
    }

}
