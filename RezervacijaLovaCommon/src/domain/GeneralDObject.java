/* GeneralDObject.java
 * @autor  prof. dr Sinisa Vlajic,
 * Univerzitet u Beogradu
 * Fakultet organizacionih nauka 
 * Katedra za softversko inzenjerstvo
 * Laboratorija za softversko inzenjerstvo
 * 06.11.2017
 */
package domain;

import java.io.Serializable;
import java.sql.*;
import java.util.List;

// Operacije navedenog interfejsa je potrebno da implementira svaka od domenskih klasa,
// koja zeli da joj bude omogucena komunikacija sa Database broker klasom.
public interface GeneralDObject extends Serializable {

    public  String getRecordColumns();

    public  String getClassNameJoin();

    public  GeneralDObject getNewRecord(ResultSet rs) throws Exception;

    public  String getClassName();

    public  String getColumns();

    public  String getAddValues();

    public  void setAddValues(PreparedStatement statement, GeneralDObject odk) throws Exception;

    public  String getUpdateValues();

    public  String getUpdateCondition();

    public  void setUpdateValues(GeneralDObject odk, Object oldpk, PreparedStatement statement) throws Exception;

    public  String getPrimaryKeyCondition();

    public  void setSearchPrimaryKeyCondiotion(Object param, PreparedStatement statement) throws Exception;

    public  void setDeletePrimaryKeyCondiotion(GeneralDObject odk, PreparedStatement statement) throws Exception;

    public  void setDeletePrimaryKeyParrentCondiotion(Object condition, PreparedStatement statement) throws Exception;

    public  String getRecordColumnsByCondition();

    public  String getClassNameJoinByCondition();

    public  String getConditionGetAll();

    public  void setConditionGetAll(PreparedStatement statement, Object obj) throws Exception;

    public  void setList(List<GeneralDObject> odk);

    public  List<GeneralDObject> getList();

    public  Object getPk();
    
    public Object getParrentPk();

    public  void setParentID(Long id);

    public  GeneralDObject getChild();

    public  GeneralDObject getParentDO();

    public  boolean hasList();

    public  boolean addAnother(GeneralDObject odk);

    public  boolean deleteParent();

    public  String getErrorEmptyList();

    public  String getErrorGetAll();

    public  String getErrorAdd();

    public  String getErrorSearch();

    public  String getErrorNotFound();

    public  String getErrorEddit();

    public  String getErrorDelete();

}
