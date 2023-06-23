package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProfessorEvidencesController implements IProfessorNavigationBar {
    @FXML
    private TableView<Evidence> tableViewEvidence;
    @FXML
    private Label labelUsername;
    private static final Logger logger = Logger.getLogger(ProfessorEvidencesController.class);

    @FXML
    private void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        TableColumn<Evidence, String> titleEvidence = new TableColumn<>("Título");
        titleEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        TableColumn<Evidence, String> statusEvidence = new TableColumn<>("Estado");
        statusEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceStatus"));
        tableViewEvidence.getColumns().addAll(titleEvidence, statusEvidence);
        try {
            fillTableViewEvidence();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos," +
                    " no se pudo recuperar las evidencias registradas.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    @FXML
    private void redirectToFiles() throws IOException {
        if (isItemSelected()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            TransferAdvancement.setAdvancementID(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementId());
            MainStage.changeView("evidencefiles-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione una evidencia para ver sus archivos.",
                    AlertStatus.WARNING));
        }
    }

    @FXML
    public void redirectToViewEvidenceDetails() throws IOException {
        if (isItemSelected()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            MainStage.changeView(
                    "viewevidencedetails-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Debes seleccionar una evidencia para continuar", AlertStatus.WARNING));
        }
    }

    private boolean isItemSelected() {
        return tableViewEvidence.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void fillTableViewEvidence() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidence.getItems().clear();
        tableViewEvidence.getItems().addAll(evidenceDAO
                .getEvidenceListByProfessorID(Integer.parseInt(SessionDetails.getInstance().getId())));
    }

    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_REPRESENTATIVE)) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_PROFESSOR)) {
            MainStage.changeView(
                    "professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
