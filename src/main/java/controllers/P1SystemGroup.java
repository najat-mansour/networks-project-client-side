package controllers;

import enums.AnimationDirection;
import helpers.AnimationMaker;
import helpers.DialogMaker;
import http.HttpHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class P1SystemGroup implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnNext;

    @FXML
    private TextField txtSystemDescription;

    @FXML
    private TextField txtObjectId;

    @FXML
    private TextField txtSystemUpTime;

    @FXML
    private TextField txtSystemContact;

    @FXML
    private TextField txtSystemName;

    @FXML
    private TextField txtSystemLocation;

    @FXML
    private TextField txtSystemContactNewValue;

    @FXML
    private TextField txtSystemNameNewValue;

    @FXML
    private TextField txtSystemLocationNewValue;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnClear;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSystemGroupData();
    }

    @FXML
    public void actionPerformed(ActionEvent event) throws IOException, ParseException {
        if (event.getSource() == btnRefresh) {
            handleBtnRefresh();

        } else if (event.getSource() == btnPrevious) {
            handleBtnPrevious();

        } else if (event.getSource() == btnLogout) {
            handleBtnLogout();

        } else if (event.getSource() == btnNext) {
            handleBtnNext();

        } else if (event.getSource() == btnSubmit) {
            handleBtnSubmit();

        } else if (event.getSource() == btnClear) {
            handleBtnClear();

        }
    }

    public void handleBtnRefresh() {
        setSystemGroupData();
    }

    public void handleBtnPrevious() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p4_snmp_statistics.fxml", AnimationDirection.FROM_RIGHT);
    }

    public void handleBtnLogout() throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")))));
    }

    public void handleBtnNext() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p2_tcp_table.fxml", AnimationDirection.FROM_LEFT);
    }

    public void handleBtnSubmit() throws IOException, ParseException {
        if (txtSystemContactNewValue.getText().isEmpty()
         && txtSystemNameNewValue.getText().isEmpty()
         && txtSystemLocationNewValue.getText().isEmpty()) {
            DialogMaker.showDialog("Warning", "", "Please, fill at least one field!", Alert.AlertType.WARNING);
            return;
        }

        JSONObject jsonObject = request();
        String responseInJsonObject = (String) jsonObject.get("response");

        if (responseInJsonObject.equalsIgnoreCase("Success")) {
            DialogMaker.showDialog("Success", "", "Your data is updated successfully!", Alert.AlertType.CONFIRMATION);
            setSystemGroupData();
        }
    }

    private JSONObject request() throws IOException, ParseException {
        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1/HW_02_Part_01/php/p1_system_group_set.php";
        String body = "";
        if (!txtSystemContact.getText().isEmpty())
            body = httpHandler.addParameter(body, "txtSysContact",  txtSystemContactNewValue.getText());
        else
            body = httpHandler.addParameter(body, "txtSysContact",  "");

        if (!txtSystemNameNewValue.getText().isEmpty())
            body = httpHandler.addParameter(body, "txtSysName", txtSystemNameNewValue.getText());
        else
            body = httpHandler.addParameter(body, "txtSysName", "");

        if (!txtSystemLocationNewValue.getText().isEmpty())
            body = httpHandler.addParameter(body, "txtSysLocation", txtSystemLocationNewValue.getText());
        else
            body = httpHandler.addParameter(body, "txtSysLocation", "");

        String response = httpHandler.requestByPost(queryString, body);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        return (JSONObject)obj;
    }

    public void handleBtnClear() {
        txtSystemContactNewValue.setText("");
        txtSystemNameNewValue.setText("");
        txtSystemLocationNewValue.setText("");
    }

    public void setSystemGroupData() {
        try {
            HttpHandler httpHandler = new HttpHandler();
            String queryString = "http://127.0.0.1/HW_02_Part_01/php/p1_system_group_get.php";
            String response = httpHandler.requestByGet(queryString);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONObject jsonObject = (JSONObject)obj;

            txtSystemDescription.setText((String) jsonObject.get("sysDescription"));
            txtObjectId.setText((String) jsonObject.get("sysObjectID"));
            txtSystemUpTime.setText((String) jsonObject.get("sysUpTime"));
            txtSystemContact.setText((String) jsonObject.get("sysContact"));
            txtSystemName.setText((String) jsonObject.get("sysName"));
            txtSystemLocation.setText((String) jsonObject.get("sysLocation"));


        } catch (Exception e) {
            DialogMaker.showDialog("Error", "", "Error in the network", Alert.AlertType.ERROR);

        }
    }
}
