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
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class InvoiceItem implements Serializable, GeneralDObject {

    private Long orderNumber;
    private Invoice invoice;
    private int animalNo;
    private BigDecimal totalCost;
    private Prices prices;
    private Currency currency;

    public InvoiceItem(Long orderNumber, Invoice invoice, int animalNo, BigDecimal totalCost, Prices prices, Currency currency) {
        this.orderNumber = orderNumber;
        this.invoice = invoice;
        this.animalNo = animalNo;
        this.totalCost = totalCost;
        this.prices = prices;
        this.currency = currency;
    }

    public InvoiceItem() {
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getAnimalNo() {
        return animalNo;
    }

    public void setAnimalNo(int animalNo) {
        this.animalNo = animalNo;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.orderNumber);
        hash = 37 * hash + Objects.hashCode(this.invoice);
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
        final InvoiceItem other = (InvoiceItem) obj;
        
        if (!Objects.equals(this.prices, other.prices)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" + "orderNumber=" + orderNumber + ", invoice=" + invoice + ", animalNo=" + animalNo + ", totalCost=" + totalCost + ", prices=" + prices + ", currency=" + currency + '}';
    }

    @Override
    public String getRecordColumns() {
        return "t.ordernumber,t.invoiceid,t.animalno,t.totalcost,t.animalid,t.seasonid,t.lovackodrustvoid,t.currency, "
                + "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency "
                + "a.id,a.name,a.shortname,a.allowed "
                + "l.id,l.name,l.county,l.adress ";

    }

    @Override
    public String getClassNameJoin() {
        return "invoiceitem t inner join prices p on (t.seasonid=p.seasonid and t.animalid=p.animalid and t.lovackodrustvoid=p.lovackodrustvoid) "
                + "inner join animal a on (p.animalid=a.id) inner join lovackodrustvo l on (p.lovackodrustvoid=l.id)";
    }

    @Override
    public String getClassName() {
        return "invoiceitem";
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws Exception {
        InvoiceItem item = new InvoiceItem();
        item.setAnimalNo(rs.getInt("t.animalno"));
        item.setCurrency(Currency.valueOf(rs.getString("t.currency")));
        item.setTotalCost(rs.getBigDecimal("t.totalcost"));
        item.setOrderNumber(rs.getLong("t.ordernumber"));

        Animal animal = new Animal();
        animal.setId(rs.getLong("a.id"));
        animal.setName(rs.getString("a.name"));
        animal.setShortName(rs.getString("a.shortname"));
        animal.setAllowed(rs.getBoolean("a.allowed"));

        Prices prices = new Prices();
        prices.setAnimal(animal);
        LovackoDrustvo lovackoDrustvo2 = new LovackoDrustvo();
        lovackoDrustvo2.setId(rs.getLong("l.id"));
        lovackoDrustvo2.setName(rs.getString("l.name"));
        lovackoDrustvo2.setCounty(rs.getString("l.county"));
        lovackoDrustvo2.setAdress(rs.getString("l.adress"));
        prices.setDrustvo(lovackoDrustvo2);
        prices.setSeason(new Season(rs.getString("t.seasonid")));
        prices.setCurrency(Currency.valueOf(rs.getString("p.currency")));
        prices.setPrice(rs.getBigDecimal("p.price"));
        prices.setSeasonEnd(rs.getDate("p.seasonend"));
        prices.setSeasonStart(rs.getDate("p.seasonstart"));
        item.setPrices(prices);

        return item;
    }

    @Override
    public String getColumns() {
        return "ordernumber,invoiceid,animalno,totalcost,animalid,seasonid,lovackodrustvoid,currency";
    }

    @Override
    public String getAddValues() {
        return "?,?,?,?,?,?,?,?";
    }

    @Override
    public void setAddValues(PreparedStatement statement, GeneralDObject odk) throws Exception {
        statement.setLong(1, ((InvoiceItem) odk).getOrderNumber());
        statement.setLong(2, invoice.getId());
        statement.setInt(3, ((InvoiceItem) odk).getAnimalNo());
        statement.setBigDecimal(4, ((InvoiceItem) odk).getTotalCost());
        statement.setLong(5, ((InvoiceItem) odk).getPrices().getAnimal().getId());
        statement.setString(6, ((InvoiceItem) odk).getPrices().getSeason().getSeason());
        statement.setLong(7, ((InvoiceItem) odk).getPrices().getDrustvo().getId());
        statement.setString(8, ((InvoiceItem) odk).getCurrency().name());
    }

    @Override
    public String getUpdateCondition() {
        return "invoiceid =?";
    }

    @Override
    public void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws SQLException {
        statement.setLong(1, invoice.getId());
    }

    @Override
    public String getUpdateValues() {
        return "";
    }

    @Override
    public void setUpdateValues(GeneralDObject odk, Object oldpk, PreparedStatement statement) throws Exception {
        statement.setLong(1, (long) oldpk);
    }

    @Override
    public String getPrimaryKeyCondition() {
        return "ordernumber=? and ivoiceid=?";
    }

    @Override
    public void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception {
        statement.setLong(1, (long) ((Object[]) param)[0]);
        statement.setLong(2, (long) ((Object[]) param)[1]);
    }

    @Override
    public void setDeletePrimaryKeyCondiotion(GeneralDObject odk, PreparedStatement statement) throws Exception {
        statement.setLong(1, (long) ((Object[]) odk.getPk())[0]);
        statement.setLong(2, (long) ((Object[]) odk.getPk())[1]);
    }

    @Override
    public void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception {
        statement.setLong(1, (long) obj);
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
    public GeneralDObject getChild() {
        return null;
    }

    @Override
    public String getRecordColumnsByCondition() {
        return "t.ordernumber,t.invoiceid,t.animalno,t.totalcost,t.animalid,t.seasonid,t.lovackodrustvoid,t.currency, "
                + "p.seasonid,p.animalid,p.lovackodrustvoid,p.seasonstart,p.seasonend,p.price,p.currency, "
                + "a.id,a.name,a.shortname,a.allowed, "
                + "l.id,l.name,l.county,l.adress";
    }

    @Override
    public String getClassNameJoinByCondition() {
        return "invoiceitem t inner join prices p on (t.seasonid=p.seasonid and t.animalid=p.animalid and t.lovackodrustvoid=p.lovackodrustvoid) "
                + "inner join animal a on (p.animalid=a.id) inner join lovackodrustvo l on (p.lovackodrustvoid=l.id)";
    }

    @Override
    public void setParentID(Long id) {
        invoice = new Invoice();
        invoice.setId(id);
    }

    @Override
    public String getConditionGetAll() {
        return "t.invoiceid=?";
    }

    @Override
    public GeneralDObject getParentDO() {
        return new Invoice();
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
        return "Stavke racuna su prazne";
    }

    @Override
    public String getErrorGetAll() {
        return "Stavke racuna ne mogu da se ucitaju";
    }

    @Override
    public String getErrorAdd() {
        return "Stavka racuna ne moze da se doda";
    }

    @Override
    public String getErrorSearch() {
        return "Greska u pretrazi stavke racuna";
    }

    @Override
    public String getErrorNotFound() {
        return "Stavka racuna ne moze da se pronadje";
    }

    @Override
    public String getErrorEddit() {
        return "Stavka racuna ne moze da se izmeni";
    }

    @Override
    public String getErrorDelete() {
        return "Stavka racuna ne moze da se obrise";
    }

    @Override
    public Object getPk() {
        return new Object[]{orderNumber, invoice.getId()};
    }

    @Override
    public Object getParrentPk() {
        return invoice.getId();
    }

}
