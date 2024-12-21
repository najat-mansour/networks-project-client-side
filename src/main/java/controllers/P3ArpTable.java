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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Arp;
import models.Tcp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class P3ArpTable implements Initializable  {
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
    private TableView<Arp> arpTable;

    @FXML
    private TableColumn<Arp, Long> indexColumn;

    @FXML
    private TableColumn<Arp, String> macColumn;

    @FXML
    private TableColumn<Arp, String> ipColumn;

    @FXML
    private TableColumn<Arp, String> ttlColumn;

    @FXML
    private TextField txtMac;

    @FXML
    private TextField txtIp;

    @FXML
    private TextField txtTtl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        macColumn.setCellValueFactory(new PropertyValueFactory<>("mac"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        ttlColumn.setCellValueFactory(new PropertyValueFactory<>("ttl"));

        try {
            setArpTableData();

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
        setArpTableData();
    }

    public void handleBtnPrevious() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p2_tcp_table.fxml", AnimationDirection.FROM_RIGHT);
    }

    public void handleBtnLogout() throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")))));
    }

    public void handleBtnNext() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p4_snmp_statistics.fxml", AnimationDirection.FROM_LEFT);
    }

    @FXML
    public void setArpTableData() throws IOException, ParseException {
        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1/HW_02_Part_01/php/p3_arp_table.php";
        String response = httpHandler.requestByGet(queryString);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONArray arr = (JSONArray) obj;

        ObservableList<Arp> arpTableData = getArpTableData(arr);
        arpTable.setItems(arpTableData);
    }

    private ObservableList<Arp> getArpTableData(JSONArray arr) {
        ObservableList<Arp> arpTableData = FXCollections.observableArrayList();
        for (Object o : arr) {
            JSONObject jsonObject = (JSONObject) o;
            Arp arp = new Arp((Long) jsonObject.get("index"),
                    (String) jsonObject.get("mac"),
                    (String) jsonObject.get("ip"),
                    (String) jsonObject.get("ttl"));

            if ((txtMac.getText().isEmpty() || arp.getMac().contains(txtMac.getText()))
             && (txtIp.getText().isEmpty() || arp.getIp().contains(txtIp.getText()))
             && (txtTtl.getText().isEmpty() || arp.getTtl().contains(txtTtl.getText()))) {
                arpTableData.add(arp);
            }
        }
        return arpTableData;
    }
}
