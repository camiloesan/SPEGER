package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IAdvancement;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Advancement;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a set of functions involving manipulation of advancements in the database as well as consulting information.
 */
public class AdvancementDAO implements IAdvancement {
    /**
     * @param advancement new advancement
     * @return rows affected, if the advancement was added or not.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int addAdvancement(Advancement advancement) throws SQLException {
        String query = "INSERT INTO Avances(nombre, descripcion, fechaInicio, fechaEntrega, ID_proyecto) " +
                "VALUES (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProjectId());
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param advancementId id of the advancement you want to get details from 
     * @return an object Advancement with all its information
     * @throws SQLException if there was an error connecting to the database or getting the information
     */
    @Override
    public Advancement getAdvancementDetailById(int advancementId) throws SQLException {
        String query = "SELECT nombre, descripcion, fechaInicio, fechaEntrega, ID_avance, ID_proyecto FROM Avances " +
                "WHERE ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, advancementId);
        ResultSet resultSet = preparedStatement.executeQuery();
            
        Advancement advancementDetail = new Advancement();
        if (resultSet.next()) {
            advancementDetail.setAdvancementName(resultSet.getString("nombre"));
            advancementDetail.setAdvancementDescription(resultSet.getString("descripcion"));
            advancementDetail.setAdvancementStartDate(resultSet.getString("fechaInicio"));
            advancementDetail.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancementDetail.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementDetail.setAdvancementID(resultSet.getInt("ID_avance"));
        }

        databaseManager.closeConnection();
        return advancementDetail;
    }

    /**
     * @param projectId project id you want to get all the advancements from
     * @return a list containing the advancements of the given project as a list
     * @throws SQLException if there was an error connecting to the database.
     */
    public List<Advancement> getAdvancementListByProjectId(int projectId) throws SQLException {
        String query = "SELECT nombre, descripcion, fechaInicio, fechaEntrega, ID_avance, ID_proyecto FROM Avances " +
                "WHERE ID_proyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, projectId);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementDescription(resultSet.getString("descripcion"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancement.setAdvancementID(resultSet.getInt("ID_avance"));
            advancement.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    /**
     * @param professorID id of the professor you want to get their advancements
     * @return List containing ALL advancements corresponding to the requested professor
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public ArrayList<Advancement> getListAdvancementNamesByProfessorId(int professorID) throws SQLException {
        String query = "SELECT A.nombre, A.fechaInicio, A.fechaEntrega, A.ID_avance FROM Avances A " +
                "INNER JOIN Proyectos P on A.ID_proyecto = P.ID_proyecto " +
                "INNER JOIN Profesores P2 on P.ID_director = P2.ID_profesor WHERE P2.ID_profesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        ArrayList<Advancement> advancementNameList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,professorID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            advancementNameList = new ArrayList<>();
            while(resultSet.next()) {
                Advancement advancement = new Advancement();
                advancement.setAdvancementName(resultSet.getString("nombre"));
                advancement.setAdvancementStartDate(resultSet.getString("fechaInicio"));
                advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
                advancement.setAdvancementID(resultSet.getInt("ID_avance"));
                advancementNameList.add(advancement);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return advancementNameList;
    }

    /**
     * @param studentID id of the student you want to get their advancements
     * @return list of advancements corresponding to a student
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public List<Advancement> getListAdvancementNamesByStudentId(String studentID) throws SQLException {
        String sqlQuery = "SELECT A.ID_avance, A.nombre FROM Avances A " +
                "INNER JOIN Proyectos P ON A.ID_proyecto = P.ID_proyecto " +
                "INNER JOIN SolicitudesProyecto SP ON P.ID_proyecto = SP.ID_proyecto " +
                "WHERE SP.matriculaEstudiante = ? AND SP.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,studentID);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancementItem = new Advancement();
            advancementItem.setAdvancementID(resultSet.getInt("ID_avance"));
            advancementItem.setAdvancementName(resultSet.getString("nombre"));
            advancementList.add(advancementItem);
        }
    
        databaseManager.closeConnection();
        return advancementList;
    }

    /**
     * @param advancementId id of the advancements to modify
     * @param advancement the new advancement object to replace the old one
     * @return rows affected (0 or 1) whether the advancement was added or not
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int modifyAdvancementById(int advancementId, Advancement advancement) throws SQLException {
        String query = "UPDATE Avances SET nombre=(?), descripcion=(?), fechaInicio=(?), fechaEntrega=(?), " +
                "ID_proyecto=(?) WHERE ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProjectId());
        preparedStatement.setInt(6, advancementId);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param advancementId id of the advancement you want to delete from the database
     * @return rows affected (0 or 1) whether the advancement was deleted or not
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int deleteAdvancementById(int advancementId) throws SQLException {
        String query = "DELETE FROM Avances WHERE ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, advancementId);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param studentID the student id you want to get their advancements
     * @return list containing ALL the advancements of the given student id
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public List<Advancement> getAdvancementByStudentID(String studentID) throws SQLException {
        String query = "SELECT Avances.ID_avance, Avances.nombre, Avances.fechaInicio, Avances.fechaEntrega " +
                "FROM Avances INNER JOIN Proyectos ON Avances.ID_proyecto = Proyectos.ID_proyecto " +
                "INNER JOIN SolicitudesProyecto ON Proyectos.ID_proyecto = SolicitudesProyecto.ID_proyecto " +
                "WHERE matriculaEstudiante = (?) AND SolicitudesProyecto.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementID(resultSet.getInt("ID_avance"));
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    /**
     * @param advancementID the student id you want to get their project name
     * @return the number of rows affected
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int getProjectIDByAdvancementID(int advancementID) throws SQLException {
        String query = "SELECT ID_proyecto FROM Avances WHERE ID_avance = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, advancementID);

        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        while (resultSet.next()) {
            result = resultSet.getInt("ID_proyecto");
        }

        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param evidenceID the evidence id you want to get the advancement deadline
     * @return the advancement deadline
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public LocalDate getAdvancementDeadLineByEvidenceID(int evidenceID) throws SQLException {
        String query = "SELECT Avances.fechaEntrega FROM Avances " +
                "INNER JOIN Evidencias ON Avances.ID_avance = Evidencias.ID_avance " +
                "WHERE Evidencias.ID_evidencia = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, evidenceID);

        ResultSet resultSet = preparedStatement.executeQuery();

        String deadLine = "";

        while(resultSet.next()) {
            deadLine = resultSet.getString("fechaEntrega");
        }

        return LocalDate.parse(deadLine, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * FOR TEST PURPOSES ONLY
     * @return last id added on the advancements table
     * @throws SQLException if there was a problem connecting to the database
     */
    @Override
    public int getLastAdvancementID() throws SQLException {
        String query = "SELECT max(ID_avance) FROM Avances";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int result = 0;
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            result = resultSet.getInt("max(ID_avance)");
        }
        
        databaseManager.closeConnection();
        return result;
    }
}
