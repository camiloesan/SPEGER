package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.ProjectRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProjectRequestsController implements IProfessorNavigationBar {
    @FXML
    Label labelDescription;
    @FXML
    Text textMotive;
    @FXML
    TableView<ProjectRequest> tableViewRequests;
    @FXML
    Button buttonAccept;
    @FXML
    Button buttonReject;
    private static String VALIDATION_REQUEST;

    @FXML
    private void initialize() {
        TableColumn<ProjectRequest, String> studentIdColumn = new TableColumn<>("Matrícula");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProjectRequest, String> projectColumn = new TableColumn<>("Estado");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequests.getColumns().addAll(studentIdColumn, projectColumn);
        try {
            fillTableViewProjectRequests();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo conectar con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void handleItemClick() {
        if (tableViewRequests.getSelectionModel().getSelectedItem() != null) {
            labelDescription.setVisible(true);
            buttonAccept.setVisible(true);
            buttonReject.setVisible(true);
            ProjectRequest projectRequest = tableViewRequests.getSelectionModel().getSelectedItem();
            textMotive.setText(projectRequest.getDescription());
        }
    }

    @FXML
    private void acceptRequest() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        VALIDATION_REQUEST = "Aceptado";
        try {
            projectRequestDAO.validateProjectRequest(VALIDATION_REQUEST, tableViewRequests
                    .getSelectionModel()
                    .getSelectedItem()
                    .getProjectPetitionID());
        } catch (SQLException requestException) {
            requestException.printStackTrace();
        }
        tableViewRequests.getItems().clear();
        try {
            fillTableViewProjectRequests();
        } catch (SQLException tableException) {
            tableException.printStackTrace();
        }
    }

    @FXML
    private void rejectRequest() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        VALIDATION_REQUEST = "Rechazado";
        try {
            projectRequestDAO.validateProjectRequest(VALIDATION_REQUEST,tableViewRequests
                    .getSelectionModel()
                    .getSelectedItem()
                    .getProjectPetitionID());
        } catch (SQLException requestException) {
            requestException.printStackTrace();
        }
        tableViewRequests.getItems().clear();
        try {
            fillTableViewProjectRequests();
        } catch (SQLException tableException) {
            tableException.printStackTrace();
        }
    }

    private void fillTableViewProjectRequests() throws SQLException {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        tableViewRequests.getItems().addAll(projectRequestDAO.getProjectRequestsListByProfessorId(Integer.parseInt(LoginController.sessionDetails.getId())));
    }

    @Override
    public void redirectToAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequests() {

    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if(confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
