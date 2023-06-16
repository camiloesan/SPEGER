package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ViewEvidenceDetailsController implements IStudentNavigationBar {
    @FXML
    private Label labelTitleEvidence;
    @FXML
    private Label labelStatusEvidence;
    @FXML
    private Label labelGradeEvidence;
    @FXML
    private Label labelDescriptionEvidence;
    @FXML
    private Label labelAdvancementEvidence;
    @FXML
    private Label labelStudentEvidence;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelRequest;
    @FXML
    private Button buttonFeedback;
    private static final Logger logger = Logger.getLogger(ViewEvidenceDetailsController.class);

    @FXML
    private void initialize() {
        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_STUDENT)) {
            labelRequest.setText("Mi Petición");
            buttonFeedback.setText("Retroalimentación");
        }
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillEvidence();
    }

    @FXML
    private void openFeedback() throws IOException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String studentID = null;
        try {
            studentID = evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId());
        } catch (SQLException studentIDException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar al estudiante", AlertStatus.ERROR));
            logger.error(studentIDException);
        }

        TransferEvidence.setStudentID(studentID);
        TransferEvidence.setEvidenceName(labelTitleEvidence.getText());

        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_STUDENT)) {
            MainStage.changeView("viewfeedback-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("addfeedback-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }


    @FXML
    public void fillEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Evidence evidenceInfo = null;
        try {
            evidenceInfo = evidenceDAO.getEvidenceInfoByID(TransferEvidence.getEvidenceId());
        } catch (SQLException evidenceInfoException) {
            DialogGenerator.getDialog(new AlertMessage("Error al recuperar la información", AlertStatus.ERROR));
            logger.error(evidenceInfoException);
        }
        labelTitleEvidence.setText(evidenceInfo.getEvidenceTitle());
        labelStatusEvidence.setText(evidenceInfo.getEvidenceStatus());
        labelGradeEvidence.setText(String.valueOf(evidenceInfo.getEvidenceGrade()));
        labelDescriptionEvidence.setText(evidenceInfo.getEvidenceDescription());
        labelAdvancementEvidence.setText(evidenceInfo.getAdvancementName());
        labelStudentEvidence.setText(evidenceInfo.getStudentName());
    }

    @FXML
    private void redirectToEvidenceFiles() throws IOException {
        MainStage.changeView("evidencefiles-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }


    @Override
    public void redirectToAdvancements() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView(
                    "advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView(
                    "studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToProjects() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToRequest() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView(
                    "studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
        }
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
