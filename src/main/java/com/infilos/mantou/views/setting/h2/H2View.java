package com.infilos.mantou.views.setting.h2;

import com.dlsc.gemsfx.richtextarea.*;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@FXMLView
public class H2View implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @Inject
    private Stage mainStage;

    @FXML
    private Label connStat;
    
    @FXML
    private Button connect;
    
    @FXML
    private Button disconnect;
    
    @FXML
    private Button tableCreate;
    
    @FXML
    private Button tableDelete;
    
    @FXML
    private Button recordInsert;
    
    @FXML
    private Button recordDelete;
    
    @FXML
    private TextField tableNameField;
    
    @FXML
    private TextField recordField;
    
    @FXML
    private ComboBox<String> tableList;
    
    @FXML
    private ComboBox<String> recordList;

    @FXML
    private ScrollPane notePane;

    private final String disconnectedLabel = "Currently not connected...";

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notePane.setContent(buildNoteArea());

        connStat.setText(disconnectedLabel);

        disableButtons(true);

        tableList.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                recordPooler(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onConnect() {
        try {
            Connectivity.INSTANCE.dbConnect();
            tablePooler();

            String connectedLabel = "Connected...";
            connStat.setText(connectedLabel);

            disableButtons(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDisconnect() {
        Connectivity.INSTANCE.dbDisconnect();
        connStat.setText(disconnectedLabel);

        //clear data
        tableNameField.clear();
        recordField.clear();
        tableList.getItems().clear();
        recordList.getItems().clear();

        disableButtons(true);
    }

    @FXML
    private void onTableCreate() {
        try {
            CreateTable cr = new CreateTable();
            cr.createTable(tableNameField.getText());

            tableNameField.clear();
            tablePooler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTableDelete() {
        try {
            CreateTable cr = new CreateTable();
            cr.deleteTable(tableList.getValue());

            tablePooler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRecordCreate() {
        try {
            InsertRecord rec = new InsertRecord();
            rec.createRecord(tableList.getValue(), recordField.getText());

            recordField.clear();
            recordPooler(tableList.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRecordDelete() {
        try {
            InsertRecord rec = new InsertRecord();
            rec.deleteRecord(tableList.getValue(), recordList.getValue());

            recordPooler(tableList.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void tablePooler() throws SQLException {
        // Execute a query
        String sql = "SHOW TABLES";
        ResultSet rs = Connectivity.INSTANCE.getStmt().executeQuery(sql);

        // Extract data from result set
        ArrayList<String> dataList = new ArrayList<>();
        while (rs.next()) {
            // Retrieve by table name
            String data = rs.getString("TABLE_NAME");
            dataList.add(data);
        }

        tableList.setItems(observableArrayList(dataList));
    }

    public void recordPooler(String table) throws SQLException {
        if (table != null) {
            // Execute a query
            String sql = "SELECT * FROM \"" + table + "\"";
            ResultSet rs = Connectivity.INSTANCE.getStmt().executeQuery(sql);

            // Extract data from result set
            ArrayList<String> recList = new ArrayList<>();
            while (rs.next()) {
                // Retrieve by column name
                String rec = rs.getString("data");
                recList.add(rec);
            }

            recordList.setItems(observableArrayList(recList));
        }
    }

    public void disableButtons(boolean bool) {
        connect.setDisable(!bool);
        disconnect.setDisable(bool);
        tableCreate.setDisable(bool);
        tableDelete.setDisable(bool);
        recordInsert.setDisable(bool);
        recordDelete.setDisable(bool);
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("H2 Store"),
                RTParagraph.create(
                    RTText.create("More features are working in progress. ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }
}
