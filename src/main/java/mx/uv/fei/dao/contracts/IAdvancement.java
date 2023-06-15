package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Advancement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IAdvancement {
    int addAdvancement(Advancement advancement) throws SQLException;
    Advancement getAdvancementDetailById(int advancementId) throws SQLException;
    List<Advancement> getAdvancementListByProjectId(int projectId) throws SQLException;
    List<Advancement> getListAdvancementNamesByProfessorId(int professorID) throws SQLException;
    int modifyAdvancementById(int advancementId, Advancement advancement) throws SQLException;
    int deleteAdvancementById(int advancementId) throws SQLException;
    List<Advancement> getListAdvancementNamesByStudentId(String studentID) throws SQLException;
    List<Advancement> getAdvancementByStudentID(String studentID) throws SQLException;
    String getProjectNameByStudentID(String studentID) throws SQLException;
    LocalDate getAdvancementDeadLineByEvidenceID(int evidenceID) throws SQLException;
    int getLastAdvancementID() throws SQLException;
}
