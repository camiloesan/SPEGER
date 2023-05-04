package mx.uv.fei.gui;

import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.io.IOException;
import java.sql.SQLException;

public class ViewProjectDetailsController {
    @FXML
    private Label labelAcademicBody;
    @FXML
    private TextFlow textInvestigationProjectName;
    @FXML
    private Label labelLGAC;
    @FXML
    private TextFlow textInvestigationLine;
    @FXML
    private Label labelDuration;
    @FXML
    private TextFlow textModalityRW;
    @FXML
    private TextFlow textReceptionWorkName;
    @FXML
    private TextFlow textRequisites;
    @FXML
    private Label labelDirector;
    @FXML
    private Label labelCodirector;
    @FXML
    private Label labelStudents;
    @FXML
    private TextFlow textInvestigationDescription;
    @FXML
    private TextFlow textReceptionWorkDescription;
    @FXML
    private TextFlow textExpectedResults;
    @FXML
    private TextFlow textBibliography;

    public String getReceptionWorkName() {
        return TransferProject.getReceptionWorkName();
    }
    
    public void getDetailedProject() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        DetailedProject detailedProject = (projectDAO.getProjectInfo(getReceptionWorkName()));
        
        labelAcademicBody.setText(detailedProject.getAcademicBodyName());
        
        Text investigationProjectName = new Text(detailedProject.getInvestigationProjectName());
        textInvestigationProjectName.getChildren().add(investigationProjectName);
        
        labelLGAC.setText(detailedProject.getLgacDescription());
        
        Text investigationLine = new Text(detailedProject.getInvestigationLine());
        textInvestigationLine.getChildren().add(investigationLine);
        
        labelDuration.setText(detailedProject.getApproxDuration());
        
        Text modalityRW = new Text(detailedProject.getReceptionWorkModality());
        textModalityRW.getChildren().add(modalityRW);
        
        Text receptionWorkName = new Text(detailedProject.getReceptionWorkName());
        textReceptionWorkName.getChildren().add(receptionWorkName);
        
        Text requisites = new Text(detailedProject.getRequisites());
        textRequisites.getChildren().add(requisites);
        
        labelDirector.setText(detailedProject.getDirector());
        labelCodirector.setText(detailedProject.getCoDirector());
        labelStudents.setText(String.valueOf(detailedProject.getNumberStudents()));
        
        Text investigationDescription = new Text(detailedProject.getInvestigationDescription());
        textInvestigationDescription.getChildren().add(investigationDescription);
        
        Text receptionWorkDescription = new Text(detailedProject.getReceptionWorkDescription());
        textReceptionWorkDescription.getChildren().add(receptionWorkDescription);
        
        Text expectedResults = new Text(detailedProject.getExpectedResults());
        textExpectedResults.getChildren().add(expectedResults);
        
        Text bibliography = new Text(detailedProject.getBibliography());
        textBibliography.getChildren().add(bibliography);
    }

    @FXML
    public void deleteProject() {
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            projectDAO.deleteProjectByTitle(textReceptionWorkName.getAccessibleText());
        } catch (SQLException deleteException) {
            deleteException.printStackTrace();
        }
    }

    public void initialize() throws SQLException {
        getDetailedProject();
    }
    
    public void actionProjects(MouseEvent mouseEvent) throws IOException {
        //ViewProjectProposalsApp.changeView("viewprojectproposals-view.fxml",800,600);
    }
    
    public void actionProfessors(MouseEvent mouseEvent) {
    }
}
