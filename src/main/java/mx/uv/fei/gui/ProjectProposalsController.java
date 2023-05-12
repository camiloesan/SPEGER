package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ProjectProposalsController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelHeader;
    @FXML
    private Button buttonVerDetalles;
    @FXML
    private Label labelFilter;
    @FXML
    private ComboBox<String> comboProjectStates;
    @FXML
    private ListView<DetailedProject> listViewProjects;
    @FXML
    private HBox hboxLogOutLabel;
    @FXML
    private Button buttonAcceptProject;
    @FXML
    private Button buttonDeclineProject;
    
    private static final String ALL_COMBO_OPTION = "Todos";
    private static final String PARTICIPATING_COMBO_OPTION = "Mis proyectos";
    private static final String UNVERIFIED_COMBO_OPTION = "Por revisar";
    private static final String VERIFIED_COMBO_OPTION = "Verificados";
    private static final String DECLINED_COMBO_OPTION = "Declinados";
    private static final String UNVERIFIED_PROJECT_STATE = "Por revisar";
    private static final String VERIFIED_PROJECT_STATE = "Verificado";
    private static final String DECLINED_PROJECT_STATE = "Declinado";
    private static String PROJECT_VALIDATION;
 
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        if(!isRCA()) {
            fillProjectListByRole();
            buttonAcceptProject.setVisible(false);
            buttonDeclineProject.setVisible(false);
            labelFilter.setVisible(false);
            comboProjectStates.setVisible(false);
        } else {
            fillProjectStateCombo();
            fillUnfilteredList();
        }
        setProjectTitles();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public boolean isRCA() {
        return Objects.equals(LoginController.sessionDetails.getUserType(), "RepresentanteCA");
    }
    
    public void fillUnfilteredList() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getAllProjects());
        proposedProjects.forEach(element -> listViewProjects.getItems().add(element));
    }
    
    public void fillProjectListByRole() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByRole(Integer.parseInt(LoginController.sessionDetails.getId())));
        proposedProjects.forEach(element -> listViewProjects.getItems().add(element));
    }
    
    public void fillFilteredProjects(String projectState) throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByState(projectState));
        proposedProjects.forEach(element -> listViewProjects.getItems().add(element));
    }
    
    public void fillProjectStateCombo() {
        ObservableList<String > projectStates = FXCollections.observableArrayList(
                ALL_COMBO_OPTION,
                PARTICIPATING_COMBO_OPTION,
                UNVERIFIED_COMBO_OPTION,
                VERIFIED_COMBO_OPTION,
                DECLINED_COMBO_OPTION);
        comboProjectStates.setItems(projectStates);
    }
    
    public boolean noFilterSelected() {
        return comboProjectStates.getValue() == null;
    }
    
    public void refreshFilteredList() throws SQLException {
        if (noFilterSelected()) {
            initialize();
        } else{
            String selectedItem = comboProjectStates.getSelectionModel().getSelectedItem();
            switch (selectedItem) {
                case ALL_COMBO_OPTION -> fillUnfilteredList();
                case PARTICIPATING_COMBO_OPTION -> fillProjectListByRole();
                case UNVERIFIED_COMBO_OPTION -> fillFilteredProjects(UNVERIFIED_PROJECT_STATE);
                case VERIFIED_COMBO_OPTION -> fillFilteredProjects(VERIFIED_PROJECT_STATE);
                case DECLINED_COMBO_OPTION -> fillFilteredProjects(DECLINED_PROJECT_STATE);
            }
            labelHeader.setText(comboProjectStates.getSelectionModel().getSelectedItem());
        }
    }
    
    private void setProjectTitles(){
        listViewProjects.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(DetailedProject item, boolean empty){
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getProjectTitle());
                }
            }
        });
    }
    
    public void openProjectDetails() throws IOException {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            int projectID = (listViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            TransferProject.setProjectID(projectID);
            MainStage.changeView("viewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void acceptProject() {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Verificado";
            try {
                projectDAO.updateProjectState(listViewProjects.getSelectionModel().getSelectedItem().getProjectID(), PROJECT_VALIDATION);
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Aceptarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void declineProject() {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Declinado";
            try {
                projectDAO.updateProjectState((listViewProjects.getSelectionModel().getSelectedItem().getProjectID()), PROJECT_VALIDATION);
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Rechazarlo", AlertStatus.WARNING));
        }
    }
    
    @FXML
    public void openProjectRegistration() throws IOException {
        try {
            MainStage.changeView("registerprojectproposal-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        try {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
