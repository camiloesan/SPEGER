package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectEvidence;

import java.sql.SQLException;

public interface IProjectEvidence {
    void addProjectEvidence(ProjectEvidence projectEvidence) throws SQLException;
    void updateProjectEvidenceGradeByMatricula(String matricula, int grade) throws SQLException;
    void getProjectEvidenceByStudentName(String studentName) throws SQLException;
}
