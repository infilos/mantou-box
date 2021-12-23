package com.infilos.mantou.views.timestamp;

import com.infilos.mantou.api.View;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.mantou.utils.Elements;
import com.tangorabox.reactivedesk.FXMLView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;

@FXMLView
public class TimestampView extends BorderPane implements View<Void>, Initializable, AwareResource {

    @FXML
    private Label currentEpochLabel;
    @FXML
    private TextField currentEpoch;
    @FXML
    private Button currentEpochRefreshButton;
    @FXML
    private TextField tsToHumanField;
    @FXML
    private Button tsToHumanButton;
    @FXML
    private Button millisToTimeButton;
    @FXML
    private TextArea tsToHumanResult;
    @FXML
    private Button humanToTsButton;
    @FXML
    private TextArea humanToTsResult;
    @FXML
    private TextField epochYear;
    @FXML
    private TextField epochMonth;
    @FXML
    private TextField epochDay;
    @FXML
    private TextField epochHour;
    @FXML
    private TextField epochMinute;
    @FXML
    private TextField epochSecond;
    @FXML
    private ComboBox<String> timeZoneComboBox;

    private int timeZoneComboBoxIndex;

    @Override
    public void setModel(Void model) {
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentEpochRefreshButton.setGraphic(loadImageView("icons8-refresh-16.png"));
        
        HBox.setMargin(currentEpochLabel, new Insets(15, 5, 10, 0));
        HBox.setMargin(currentEpoch, new Insets(10, 5, 10, 0));
        HBox.setMargin(currentEpochRefreshButton, new Insets(10, 5, 10, 0));
        HBox.setMargin(tsToHumanField, new Insets(10, 5, 10, 0));
        HBox.setMargin(tsToHumanButton, new Insets(10, 5, 10, 0));
        HBox.setMargin(millisToTimeButton, new Insets(10, 5, 10, 0));

        GridPane.setMargin(epochYear, new Insets(10, 5, 0, 0));
        GridPane.setMargin(epochMonth, new Insets(10, 5, 0, 0));
        GridPane.setMargin(epochDay, new Insets(10, 5, 0, 0));
        GridPane.setMargin(epochHour, new Insets(10, 5, 0, 0));
        GridPane.setMargin(epochMinute, new Insets(10, 5, 0, 0));
        GridPane.setMargin(epochSecond, new Insets(10, 5, 0, 0));
        GridPane.setMargin(humanToTsButton, new Insets(10, 5, 0, 0));
        GridPane.setMargin(timeZoneComboBox, new Insets(10, 5, 0, 0));

        long now = System.currentTimeMillis();
        currentEpoch.setText(Long.toString(now));
        tsToHumanField.setText(Long.toString(now));

        LocalDateTime date = LocalDateTime.now();
        epochYear.setText(String.valueOf(date.getYear()));
        epochMonth.setText(String.valueOf(date.getMonthValue()));
        epochDay.setText(String.valueOf(date.getDayOfMonth()));
        epochHour.setText(String.valueOf(date.getHour()));
        epochMinute.setText(String.valueOf(date.getMinute()));
        epochSecond.setText(String.valueOf(date.getSecond()));

        timeZoneComboBox.getItems().setAll(TimeZone.getAvailableIDs());
        timeZoneComboBox.setValue("UTC");
        timeZoneComboBoxIndex = 0;
    }

    @FXML
    private void handleRefreshEpoch(final ActionEvent event) {
        currentEpoch.setText(Long.toString(System.currentTimeMillis()));
    }

    @FXML
    private void handleTsToHumanEpoch(final ActionEvent event) {
        tsToHumanField.setBorder(Border.EMPTY);
        try {
            LocalDateTime dt = TimestampService.tsToLocalDateTime(tsToHumanField.getText());
            String result = TimestampService.toHumanEpoch(dt);
            tsToHumanResult.setText(result);
        } catch (Exception e) {
            tsToHumanField.setBorder(Elements.alertBorder);
            tsToHumanResult.setText("");
        }
    }

    @FXML
    private void handleMillisToTime(final ActionEvent actionEvent) {
        tsToHumanField.setBorder(Border.EMPTY);
        try {
            long millis = Long.parseLong(tsToHumanField.getText());
            String result = DurationFormatUtils.formatDurationWords(millis, true, true);
            tsToHumanResult.setText(result);
        } catch (Exception e) {
            tsToHumanField.setBorder(Elements.alertBorder);
            tsToHumanResult.setText("");
        }
    }

    @FXML
    private void handleHumanToTsEpoch(final ActionEvent event) {
        resetBorders();
        try {
            int year = TimestampService.validate(epochYear, 1970, Integer.MAX_VALUE);
            int month = TimestampService.validate(epochMonth, 1, 12);
            int day = TimestampService.validate(epochDay, 1, 31);
            int hour = TimestampService.validate(epochHour, 0, 24);
            int minute = TimestampService.validate(epochMinute, 0, 59);
            int second = TimestampService.validate(epochSecond, 0, 59);
            String timeZone = timeZoneComboBox.getSelectionModel().getSelectedItem();
            String result = TimestampService.toTsEpoch(year, month, day, hour, minute, second, timeZone);
            humanToTsResult.setText(result);
        } catch (Exception e) {
            humanToTsResult.setText("");
        }
    }

    @FXML
    private void handleTimeZoneSearch(KeyEvent keyEvent) {
        String key = keyEvent.getText();
        if (key.length() == 0) return;
        int i = 0;
        for (String item : timeZoneComboBox.getItems()) {
            if (item.toLowerCase().startsWith(key) && i > timeZoneComboBoxIndex) {
                timeZoneComboBox.setValue(item);
                timeZoneComboBoxIndex = i;
                return;
            }
            i++;
        }
        timeZoneComboBoxIndex = 0;
    }

    private void resetBorders() {
        epochYear.setBorder(Border.EMPTY);
        epochMonth.setBorder(Border.EMPTY);
        epochDay.setBorder(Border.EMPTY);
        epochHour.setBorder(Border.EMPTY);
        epochMinute.setBorder(Border.EMPTY);
        epochSecond.setBorder(Border.EMPTY);
    }
}
