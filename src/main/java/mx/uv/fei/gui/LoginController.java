package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField textFieldUser;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private void onActionButtonContinue() throws SQLException, IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        if (accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText())) {
            redirectToWindow();
            closeCurrentWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("El usuario o contraseña no son válidos");
            alert.setContentText("Inténtelo de nuevo");
            alert.showAndWait();
        }
    }

    private void redirectToWindow() throws SQLException, IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        switch (accessAccountDAO.getAccessAccountTypeByUsername(textFieldUser.getText())) {
            case "Administrador":
                CRUDAccessAccountWindow crudAccessAccountWindow = new CRUDAccessAccountWindow();
                crudAccessAccountWindow.start(new Stage());
                break;
            case "Estudiante":
                break;
            case "Profesor":
                break;
            case "RepresentanteCA":
                break;
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) textFieldUser.getScene().getWindow();
        stage.close();
    }
}
