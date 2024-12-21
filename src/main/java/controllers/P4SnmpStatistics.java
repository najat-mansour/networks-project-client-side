package controllers;

import enums.AnimationDirection;
import helpers.AnimationMaker;
import helpers.DialogMaker;
import http.HttpHandler;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Arp;
import models.SnmpStatistics;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class P4SnmpStatistics implements Initializable {
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
    private TableView<SnmpStatistics> snmpStatisticsTable;

    @FXML
    private TableColumn<SnmpStatistics, Long> indexColumn;

    @FXML
    private TableColumn<SnmpStatistics, String> nameColumn;

    @FXML
    private TableColumn<SnmpStatistics, String> valueColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        try {
            setSnmpStatisticsTableData();

        } catch (IOException | ParseException e) {
            DialogMaker.showDialog("Error", "", "Error in the network", Alert.AlertType.ERROR);

        }
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

        }
    }

    public void handleBtnRefresh() throws IOException, ParseException {
        setSnmpStatisticsTableData();
    }

    public void handleBtnPrevious() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p3_arp_table.fxml", AnimationDirection.FROM_RIGHT);
    }

    public void handleBtnLogout() throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")))));
    }

    public void handleBtnNext() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p1_system_group.fxml", AnimationDirection.FROM_LEFT);
    }

    public void setSnmpStatisticsTableData() throws IOException, ParseException {
        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1/HW_02_Part_01/php/p4_snmp_statistics_walk.php";
        String response = httpHandler.requestByGet(queryString);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONArray arr = (JSONArray) obj;

        ObservableList<SnmpStatistics> snmpStatisticsData = getSnmpStatisticsData(arr);
        snmpStatisticsTable.setItems(snmpStatisticsData);
    }

    private static ObservableList<SnmpStatistics> getSnmpStatisticsData(JSONArray arr) {
        ObservableList<SnmpStatistics> snmpStatisticsData = FXCollections.observableArrayList();
        for (Object o : arr) {
            JSONObject jsonObject = (JSONObject) o;
            SnmpStatistics snmpStatistics = new SnmpStatistics((Long) jsonObject.get("index"),
                    (String) jsonObject.get("name"),
                    (String) jsonObject.get("value"));
            snmpStatisticsData.add(snmpStatistics);
        }
        return snmpStatisticsData;
    }
}
