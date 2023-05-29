package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IProfessor;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO implements IProfessor {
    @Override
    public int addProfessor(Professor professor) throws SQLException{
        int result;
        String sqlQuery = "INSERT INTO Profesores (grado, nombre, apellidos, correoInstitucional, nombreUsuario) VALUES (?,?,?,?,?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1,professor.getProfessorDegree());
        preparedStatement.setString(2, professor.getProfessorName());
        preparedStatement.setString(3,professor.getProfessorLastName());
        preparedStatement.setString(4,professor.getProfessorEmail());
        preparedStatement.setString(5, professor.getUsername());

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    @Override
    public List<String> getProfessorsNames() throws SQLException {
        String sqlQuery = "SELECT CONCAT(grado, ' ',nombre, ' ', apellidos) AS nombreCompleto FROM Profesores;";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> professorsNames = new ArrayList<>();
        while (resultSet.next()) {
            Professor professor = new Professor();
            professor.setProfessorFullName(resultSet.getString("nombreCompleto"));
            
            professorsNames.add(professor.getProfessorFullName());
        }
        databaseManager.closeConnection();
        
        return professorsNames;
    }

    @Override
    public int getProfessorIdByUsername(String username) throws SQLException {
        String query = "select ID_profesor from Profesores where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("ID_profesor");
        }
        databaseManager.closeConnection();

        return id;
    }
    
    /**
     * @param projectID
     * @return stirng with director & codirector names
     * @throws SQLException
     */
    @Override
    public String getProfessorsByProject(int projectID) throws SQLException {
        String sqlQuery = "SELECT CONCAT(D.nombre, ' ',D.apellidos, ', ', CD.nombre, ' ',CD.apellidos) AS Directors FROM Profesores D " +
                "INNER JOIN Proyectos P on D.ID_profesor = P.ID_director " +
                "INNER JOIN Profesores CD ON P.ID_codirector = CD.ID_profesor " +
                "WHERE P.ID_proyecto = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        String professorNames = null;
        try {
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            
            preparedStatement.setInt(1, projectID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professorNames = resultSet.getString("Directors");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //log exception
        } finally {
            databaseManager.closeConnection();
        }
        
        return professorNames;
    }
}
