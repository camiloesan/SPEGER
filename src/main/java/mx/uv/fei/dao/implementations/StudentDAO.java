package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IStudent;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDAO implements IStudent {
    @Override
    public int insertStudent(Student student) throws SQLException{
        int result;
        String query = "INSERT INTO Estudiantes(matricula, nombre, apellidos, correoInstitucional, nombreUsuario) VALUES(?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, student.getStudentID());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setString(4, student.getAcademicEmail());
        preparedStatement.setString(5, student.getUsername());
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int deleteStudent(String studentID) throws SQLException {
        int result;
        String query = "DELETE FROM Estudiantes WHERE matricula=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public String getNamebyStudentID(String studentID) throws SQLException {
        String query = "SELECT nombre FROM Estudiantes WHERE matricula = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("nombre");
        }

        databaseManager.closeConnection();

        return result;
    }
    
    @Override
    public String getStudentIdByUsername(String username) throws SQLException {
        String sqlQuery = "SELECT matricula FROM Estudiantes WHERE nombreUsuario = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        String studentID = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                studentID = resultSet.getString("matricula");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return studentID;
    }

    @Override
    public String getStudentIDByProfessorID(int professorID) throws SQLException {
        String query = "SELECT Estudiantes.matricula " +
                "FROM Estudiantes " +
                "INNER JOIN SolicitudesProyecto ON Estudiantes.matricula = SolicitudesProyecto.matriculaEstudiante " +
                "INNER JOIN Proyectos ON SolicitudesProyecto.ID_proyecto = Proyectos.ID_proyecto " +
                "INNER JOIN Profesores ON Proyectos.ID_director = Profesores.ID_profesor " +
                "WHERE SolicitudesProyecto.estado = 'Aceptado' AND Profesores.ID_profesor = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, professorID);

        String studentID = "";
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            studentID = resultSet.getString("matricula");
        }
        databaseManager.closeConnection();
        return studentID;
    }
    
    @Override
    public List<Student> getStudentsByProject(int projectID) throws SQLException {
        String sqlQuery = "SELECT CONCAT(E.nombre, ' ', E.apellidos) AS Alumno FROM Estudiantes E " +
                "INNER JOIN SolicitudesProyecto SP on E.matricula = SP.matriculaEstudiante " +
                "INNER JOIN Proyectos P on SP.ID_proyecto = P.ID_proyecto " +
                "WHERE SP.estado = 'Aceptado' AND P.ID_proyecto = ?;";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<Student> studentList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,projectID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                Student student = new Student();
                student.setFullName(resultSet.getString("Alumno"));
                studentList.add(student);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); // log exception
        }
        
        return studentList;
    }
}
