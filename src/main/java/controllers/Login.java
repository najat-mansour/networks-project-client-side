package controllers;

import helpers.DialogMaker;
import http.HttpHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Label lblIdIsRequired;

    @FXML
    private Label lblUsernameIsRequired;

    @FXML
    private Label lblPasswordIsRequired;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField password;

    @FXML
    private Button btnVerifyById;

    @FXML
    private Button btnVerifyByUsername;

    @FXML
    private Button btnClear;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView imgViewIdCorrect;

    @FXML
    private ImageView imgViewUsernameCorrect;

    @FXML
    private Label lblSignIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SignInLabelAnimation signInLabelAnimation = new SignInLabelAnimation(lblSignIn);
        signInLabelAnimation.startAnimation();
    }

    private record SignInLabelAnimation(Label lblSignIn) {
        public void startAnimation() {
            lblSignIn.setText("");
            final String lblSignInText = " Sign In";
            int[] currentIndex = {0};

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.25), event -> {
                if (currentIndex[0] < lblSignInText.length() - 1) {
                    Platform.runLater(() -> lblSignIn.setText(lblSignIn.getText() + lblSignInText.charAt(currentIndex[0])));
                    currentIndex[0]++;
                } else {
                    lblSignIn.setText("");
                    currentIndex[0] = 0; // Reset index to repeat the animation
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    @FXML
    public void actionPerformed(ActionEvent event) throws IOException {
        if (event.getSource() == btnVerifyById) {
            handleBtnVerifyById();

        } else if (event.getSource() == btnVerifyByUsername) {
            handleBtnVerifyByUsername();

        } else if (event.getSource() == btnClear) {
            handleBtnClear();

        }
    }

    public void handleBtnVerifyById() throws IOException {
        if (validateIdAndPassword()) {
            return;
        }

        lblIdIsRequired.setVisible(false);
        lblPasswordIsRequired.setVisible(false);

        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1:8085/HW_02_Part_02/authenticate.jsp";
        String body = "";
        body = httpHandler.addParameter(body, "id", txtId.getText());
        request(httpHandler, queryString, body, imgViewIdCorrect);
    }

    public void handleBtnVerifyByUsername() throws IOException {
        if (validateUsernameAndPassword()) {
            return;
        }

        lblUsernameIsRequired.setVisible(false);
        lblPasswordIsRequired.setVisible(false);

        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1:8085/HW_02_Part_02/Authentication";
        String body = "";
        body = httpHandler.addParameter(body, "username", txtUsername.getText());
        request(httpHandler, queryString, body, imgViewUsernameCorrect);

    }

    private void request(HttpHandler httpHandler, String queryString, String body, ImageView imageView) throws IOException {
        body = httpHandler.addParameter(body, "password", password.getText());

        String response = httpHandler.requestByPost(queryString, body);

        if (response.equalsIgnoreCase("OK")) {
            if (progressBar.getProgress() != 1) {
                progressBar.setProgress(progressBar.getProgress() + 0.5);
            }
            imageView.setVisible(true);

            if (progressBar.getProgress() == 1) {
                DialogMaker.showDialog("Success", "", "Login in successfully!", Alert.AlertType.CONFIRMATION);
                Stage stage = (Stage) btnClear.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/p1_system_group.fxml")))));
            }

        } else {
            if (progressBar.getProgress() != 0) {
                progressBar.setProgress(progressBar.getProgress() - 0.5);
            }
            imageView.setVisible(false);
            DialogMaker.showDialog("Invalid Credentials", "",
                    "Please, make sure to enter valid credentials", Alert.AlertType.WARNING);

        }
    }

    public void handleBtnClear() {
        txtId.setText("");
        txtUsername.setText("");
        password.setText("");
    }

    public boolean validateIdAndPassword() {
        return validateFields(txtId, lblIdIsRequired);
    }


    public boolean validateUsernameAndPassword() {
        return validateFields(txtUsername, lblUsernameIsRequired);
    }

    private boolean validateFields(TextField txtId, Label lblIdIsRequired) {
        boolean areValidFields = true;

        if (txtId.getText().isEmpty()) {
            lblIdIsRequired.setVisible(true);
            areValidFields = false;
        }

        if (password.getText().isEmpty()) {
            lblPasswordIsRequired.setVisible(true);
            areValidFields = false;
        }

        return !areValidFields;
    }
}
