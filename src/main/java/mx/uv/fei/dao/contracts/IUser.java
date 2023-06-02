package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;

import java.sql.SQLException;
import java.util.List;

public interface IUser {
    int addAdminUser(AccessAccount accessAccount) throws SQLException;
    boolean addStudentUserTransaction(AccessAccount accessAccount, Student student) throws SQLException;
    boolean addProfessorUserTransaction(AccessAccount accessAccount, Professor professor) throws SQLException;
    boolean modifyStudentUserTransaction(String username, AccessAccount accessAccount, Student student)
            throws SQLException;
    boolean modifyProfessorUserTransaction(String username, AccessAccount accessAccount, Professor professor)
            throws SQLException;
    int deleteUserByUsername(String username) throws SQLException;
    boolean areCredentialsValid(String username, String password) throws SQLException;
    String getAccessAccountTypeByUsername(String username) throws SQLException;
    List<AccessAccount> getAccessAccountsList() throws SQLException;
}
