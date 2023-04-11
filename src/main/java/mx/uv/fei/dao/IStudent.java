package mx.uv.fei.dao;

import mx.uv.fei.logic.Student;

import java.sql.SQLException;
import java.util.List;

public interface IStudent {
    List<Student> getStudents() throws SQLException;
    void insertStudent(Student student) throws SQLException;
    void deleteStudent(String studentTuition) throws  SQLException;
}
