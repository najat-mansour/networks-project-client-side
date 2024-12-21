package controllers;

import enums.AnimationDirection;
import helpers.AnimationMaker;
import helpers.DialogMaker;
import http.HttpHandler;
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
import models.Tcp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class P2TcpTable implements Initializable {
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
    private TableView<Tcp> tcpTable;

    @FXML
    private TableColumn<Tcp, Long> indexColumn;

    @FXML
    private TableColumn<Tcp, String> stateColumn;

    @FXML
    private TableColumn<Tcp, String> sourceIpColumn;

    @FXML
    private TableColumn<Tcp, String> sourcePortColumn;

    @FXML
    private TableColumn<Tcp, String> destinationIpColumn;

    @FXML
    private TableColumn<Tcp, String> destinationPortColumn;

    @FXML
    private TextField txtState;

    @FXML
    private TextField txtSrcIp;

    @FXML
    private TextField txtSrcPort;

    @FXML
    private TextField txtDestIp;

    @FXML
    private TextField txtDestPort;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        sourceIpColumn.setCellValueFactory(new PropertyValueFactory<>("SourceIp"));
        sourcePortColumn.setCellValueFactory(new PropertyValueFactory<>("SourcePort"));
        destinationIpColumn.setCellValueFactory(new PropertyValueFactory<>("DestinationIp"));
        destinationPortColumn.setCellValueFactory(new PropertyValueFactory<>("DestinationPort"));

        try {
            setTcpTableData();

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
        setTcpTableData();
    }

    public void handleBtnPrevious() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p1_system_group.fxml", AnimationDirection.FROM_RIGHT);
    }

    public void handleBtnLogout() throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/login.fxml")))));
    }

    public void handleBtnNext() throws IOException {
        AnimationMaker.makeAnimation(stackPane, anchorPane, "/views/p3_arp_table.fxml", AnimationDirection.FROM_LEFT);
    }

    @FXML
    public void setTcpTableData() throws IOException, ParseException {
        HttpHandler httpHandler = new HttpHandler();
        String queryString = "http://127.0.0.1/HW_02_Part_01/php/p2_tcp_table.php";
        String response = httpHandler.requestByGet(queryString);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(response);
        JSONArray arr = (JSONArray) obj;

        ObservableList<Tcp> tcpTableData = getTcpTableData(arr);
        tcpTable.setItems(tcpTableData);
        
    }

    private ObservableList<Tcp> getTcpTableData(JSONArray arr) {
        ObservableList<Tcp> tcpTableData = FXCollections.observableArrayList();
        for (Object o : arr) {
            JSONObject jsonObject = (JSONObject) o;
            Tcp tcp = new Tcp((Long) jsonObject.get("index"),
                    (String) jsonObject.get("state"),
                    (String) jsonObject.get("localAddress"),
                    (String) jsonObject.get("localPort"),
                    (String) jsonObject.get("remoteAddress"),
                    (String) jsonObject.get("remotePort"));

            if ((txtState.getText().isEmpty() || tcp.getState().contains(txtState.getText()))
             && (txtSrcIp.getText().isEmpty() || tcp.getSourceIp().contains(txtSrcIp.getText()))
             && (txtSrcPort.getText().isEmpty() || tcp.getSourcePort().contains(txtSrcPort.getText()))
             && (txtDestIp.getText().isEmpty() || tcp.getDestinationIp().contains(txtDestIp.getText()))
             && (txtDestPort.getText().isEmpty() || tcp.getDestinationPort().contains(txtDestPort.getText()))
            ) {
                tcpTableData.add(tcp);
            }
        }
        return tcpTableData;
    }
}
