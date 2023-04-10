package mx.uv.fei.logic;

import mx.uv.fei.dao.IProjectPetitions;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectPetitionsDAO implements IProjectPetitions {
    @Override
    public List<ProjectPetitions> getProjectPetitions() throws SQLException {
        List<ProjectPetitions> listPetitions = new ArrayList<>();
        String query = "SELECT * FROM SolicitudesProyecto";
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        do {

            ProjectPetitions objectPetitions = new ProjectPetitions();

            objectPetitions.setProjectPetitionID(results.getInt("ID_solicitudProyecto"));
            objectPetitions.setProjectID(results.getInt("ID_proyecto"));
            objectPetitions.setStudentTuition(results.getString("matriculaEstudiante"));
            objectPetitions.setStatus(results.getString("estado"));
            objectPetitions.setDescription(results.getString("motivos"));

            listPetitions.add(objectPetitions);
        } while (results.next());
        dataBaseManager.closeConnection();
        return listPetitions;
    }
}
