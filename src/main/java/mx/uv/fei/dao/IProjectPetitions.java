package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectPetitions;

import java.sql.SQLException;
import java.util.List;

public interface IProjectPetitions {
    List<ProjectPetitions> getProjectPetitions() throws SQLException;
}
