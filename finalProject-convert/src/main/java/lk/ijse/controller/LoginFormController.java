package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.controller.Util.Regex;
import lk.ijse.dao.custom.SecurityDAO;
import lk.ijse.dao.custom.impl.SecurityDAOImpl;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblSea;
    public AnchorPane rootnode;
    public TextField emailField;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Hyperlink linkForgetPassword;

    @FXML
    private Hyperlink linkNewAccount;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserId;
    private String welcomeText = "WELCOME";
    private int currentIndex = 0;

    SecurityDAO securityDAO = new SecurityDAOImpl();

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(230), event -> {
            if (currentIndex <= welcomeText.length()) {
                lblWelcome.setText(welcomeText.substring(0, currentIndex));
                currentIndex++;
            }
        }));
        timeline.setCycleCount(welcomeText.length() + 1);
        timeline.play();
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userId = txtUserId.getText();
        String pw = txtPassword.getText();

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            checkCredential(userId, pw);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void checkCredential(String userId, String pw) throws SQLException, IOException, ClassNotFoundException {
        String dbPw = securityDAO.check(userId);
        if (dbPw != null) {
            if (pw.equals(dbPw)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "Sorry! Password is incorrect!").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Sorry! User ID can't be found!").show();
        }
    }

    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(getClass().getResource("/view/mainBoard_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) rootnode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("main Board Form");
    }

    @FXML
    void linkForgetPasswordOnAction(ActionEvent event) throws IOException, MessagingException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/forgetPassword1_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Password Forget Form");
        stage.show();
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtUserId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtPassword)) return false;
        return true;
    }


    @FXML
    void txtPassKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ADDRESS,txtPassword);}

    @FXML
    void txtUserKeyReleased(KeyEvent event) {Regex.setTextColor(lk.ijse.controller.Util.TextField.ID,txtUserId);}
    @FXML
    void linkNewAccountOnAction(ActionEvent event) {}

}