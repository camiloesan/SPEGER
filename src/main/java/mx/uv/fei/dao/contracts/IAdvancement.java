package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Advancement;

import java.sql.SQLException;
import java.util.List;

public interface IAdvancement {
    int addAdvancement(Advancement advancement) throws SQLException;
    Advancement getAdvancementDetailByName(String advancementName) throws SQLException;
    List<Advancement> getAdvancementListByProjectName(String projectName) throws SQLException;
    List<Advancement> getListAdvancementName(int professorID) throws SQLException;
    int modifyAdvancementByName(String advancementName, Advancement advancement) throws SQLException;
    int deleteAdvancementByName(String advancement) throws SQLException;
    String getAdvancementNameByID(int id) throws SQLException;
    List<Advancement> getListAdvancementNameStudent(String studentID) throws SQLException;
}
