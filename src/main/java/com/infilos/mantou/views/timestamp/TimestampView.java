package com.infilos.mantou.views.timestamp;

import com.dlsc.gemsfx.richtextarea.*;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dustinredmond.fxalert.AlertBuilder;
import com.dustinredmond.fxalert.FXAlert;
import com.infilos.mantou.api.WorkView;
import com.infilos.utils.Datetimes;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import jfxtras.styles.jmetro.JMetroStyleClass;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

@FXMLView
public class TimestampView extends AnchorPane implements WorkView<Void>, Loggable {
    
    @Inject
    private Workbench workbench;

    @FXML
    private Label title;

    @FXML
    private ComboBox<String> timeZone;

    @FXML
    private ComboBox<String> timePattern;

    @FXML
    private TextField timeMills11;

    @FXML
    private TextField timeMills13;

    @FXML
    private TextField timeFormatted;

    @FXML
    private Button resetTimeZone;

    @FXML
    private Button resetTimePattern;

    @FXML
    private Button convertTimeMills11;

    @FXML
    private Button copyTimeMills11;

    @FXML
    private Button convertTimeMills13;

    @FXML
    private Button copyTimeMills13;

    @FXML
    private Button convertTimeFormatted;

    @FXML
    private Button copyTimeFormatted;

    @FXML
    private Button refreshCurrentTime;

    @FXML
    private ScrollPane notePane;

    private static final Map<String, DateTimeFormatter> TimePatternMappings = new HashMap<>() {{
        put(Datetimes.Formats.AtSeconds, Datetimes.Patterns.AtSeconds);
        put(Datetimes.Formats.AtMinutes, Datetimes.Patterns.AtMinutes);
        put(Datetimes.Formats.AtHours, Datetimes.Patterns.AtHours);
        put(Datetimes.Formats.AtDays, Datetimes.Patterns.AtDays);
        put(Datetimes.Formats.AtMonths, Datetimes.Patterns.AtMonths);
        put(Datetimes.Formats.AtYears, Datetimes.Patterns.AtYears);
        put("yyyy-MM-dd[ HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()
        );
        put("yyyy-MM[-dd HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM[-dd HH:mm:ss]")
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter());
        put("yyyy[-MM-dd HH:mm:ss]", new DateTimeFormatterBuilder()
            .appendPattern("yyyy[-MM-dd HH:mm:ss]")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()
        );
    }};

    private static final List<String> TimePatterns = List.of(
        Datetimes.Formats.AtSeconds,
        Datetimes.Formats.AtMinutes,
        Datetimes.Formats.AtHours,
        Datetimes.Formats.AtDays,
        Datetimes.Formats.AtMonths,
        Datetimes.Formats.AtYears,
        "yyyy-MM-dd[ HH:mm:ss]",
        "yyyy-MM[-dd HH:mm:ss]",
        "yyyy[-MM-dd HH:mm:ss]"
    );

    private int selectTimeZoneIndex;
    private String selectTimePattern;

    @Override
    public void setModel(Void model) {
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getStyleClass().add(JMetroStyleClass.BACKGROUND);

        // time-zone
        timeZone.getItems().setAll(TimeZone.getAvailableIDs());
        timeZone.setValue(TimeZone.getDefault().getID());
        selectTimeZoneIndex = timeZone.getItems().indexOf(TimeZone.getDefault().getID());

        // time-pattern
        timePattern.getItems().setAll(TimePatterns);
        timePattern.getEditor().setText(Datetimes.Formats.AtSeconds);
        selectTimePattern = Datetimes.Formats.AtSeconds;

        // time-values
        handleRefreshCurrentTime(null);

        // help-note
        notePane.setContent(buildNoteArea());
    }

    @FXML
    private void handleTimeZoneMatching(KeyEvent event) {
        String key = event.getText();
        if (StringUtils.isBlank(key)) {
            return;
        }

        for (int idx = 0; idx < timeZone.getItems().size(); idx++) {
            if (timeZone.getItems().get(idx).toLowerCase().startsWith(key.toLowerCase()) &&
                idx > selectTimeZoneIndex) {
                timeZone.setValue(timeZone.getItems().get(idx));
                selectTimeZoneIndex = idx;
                return;
            }
        }

        selectTimeZoneIndex = 0;
    }

    @FXML
    private void handleTimeZoneSelection(ActionEvent event) {
        String selected = timeZone.getSelectionModel().getSelectedItem();
        
        for (int idx = 0; idx < timeZone.getItems().size(); idx++) {
            if (timeZone.getItems().get(idx).equals(selected) &&
                idx > selectTimeZoneIndex) {
                timeZone.setValue(timeZone.getItems().get(idx));
                selectTimeZoneIndex = idx;
                return;
            }
        }

        selectTimeZoneIndex = 0;
    }

    @FXML
    private void handleResetTimeZone(ActionEvent event) {
        timeZone.setValue(TimeZone.getDefault().getID());
        selectTimeZoneIndex = indexOfTimeZoneId(TimeZone.getDefault().getID());
    }
    
    private int indexOfTimeZoneId(String zoneId) {
        for (int idx = 0; idx < timeZone.getItems().size(); idx++) {
            if (timeZone.getItems().get(idx).equals(zoneId)) {
                return idx;
            }
        }
        
        return -1;
    }

    private ZoneId currentTimeZone() {
        return ZoneId.of(timeZone.getItems().get(selectTimeZoneIndex));
    }

    @FXML
    private void handleTimePatternSelection(ActionEvent event) {
        log().info("select time pattern: item={}, text={}", timePattern.getSelectionModel().getSelectedItem(), timePattern.getEditor().getText());
        String edited = timePattern.getEditor().getText();
        String selected = timePattern.getSelectionModel().getSelectedItem();
        selectTimePattern = StringUtils.isNotBlank(edited) ? edited : selected;
    }

    @FXML
    private void handleResetTimePattern(ActionEvent event) {
        timePattern.getEditor().setText(Datetimes.Formats.AtSeconds);
        selectTimePattern = Datetimes.Formats.AtSeconds;
    }

    private DateTimeFormatter currentTimePattern() {
        TimePatternMappings.computeIfAbsent(selectTimePattern.trim(), k -> DateTimeFormatter.ofPattern(selectTimePattern));

        return TimePatternMappings.get(selectTimePattern.trim());
    }

    @FXML
    private void handleConvertByTimeMills11(ActionEvent event) {
        try {
            long interimTimeMills11 = Long.parseLong(timeMills11.getText());
            long interimTimeMills13 = interimTimeMills11 * 1000;
            Instant interimInstant = Instant.ofEpochMilli(interimTimeMills13);
            ZonedDateTime interimDateTime = ZonedDateTime.ofInstant(interimInstant, currentTimeZone());

            timeMills13.setText(String.valueOf(interimTimeMills13));
            timeFormatted.setText(interimDateTime.format(currentTimePattern()));
        } catch (Exception e) {
            showErrorDialog(selectTimePattern, timeMills11.getText(), e);
        }
    }

    @FXML
    private void handleConvertByTimeMills13(ActionEvent event) {
        try {
            long interimTimeMills13 = Long.parseLong(timeMills13.getText());
            long interimTimeMills11 = interimTimeMills13 / 1000;
            Instant interimInstant = Instant.ofEpochMilli(interimTimeMills13);
            ZonedDateTime interimDateTime = ZonedDateTime.ofInstant(interimInstant, currentTimeZone());

            timeMills11.setText(String.valueOf(interimTimeMills11));
            timeFormatted.setText(interimDateTime.format(currentTimePattern()));
        } catch (Exception e) {
            showErrorDialog(selectTimePattern, timeMills13.getText(), e);
        }
    }

    @FXML
    private void handleConvertByTimeFormatted(ActionEvent event) {
        try {
            ZonedDateTime interimDateTime = ZonedDateTime.of(
                LocalDateTime.parse(timeFormatted.getText().trim(), currentTimePattern()),
                currentTimeZone()
            );
            Timestamp timestamp = Timestamp.from(interimDateTime.toInstant());
            long interimTimeMills13 = timestamp.getTime();
            long interimTimeMills11 = interimTimeMills13 / 1000;

            timeMills11.setText(String.valueOf(interimTimeMills11));
            timeMills13.setText(String.valueOf(interimTimeMills13));
        } catch (Exception e) {
            log().error("Convert failed",e);
            showErrorDialog(selectTimePattern, timeFormatted.getText(), e);
        }
    }

    private void showErrorDialog(String pattern, Object value, Exception error) {
        workbench.showDialog(WorkbenchDialog
            .builder(
                "Convert failed!",
                String.format("Check the time pattern and value: \"%s\", \"%s\". \n%s", pattern, value, error.getMessage()),
                ButtonType.CLOSE)
            .exception(error)
            .build()
        );

        //AlertBuilder builder = FXAlert.exception(error).withTextFormat(
        //    "Convert failed! \nCheck the time pattern and value: \"%s\", \"%s\"", pattern, value
        //);
        //builder.getAlert();
    }

    @FXML
    private void handleCopyTimeMills11(ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(timeMills11.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    private void handleCopyTimeMills13(ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(timeMills13.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    private void handleCopyTimeFormatted(ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(timeFormatted.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    private void handleRefreshCurrentTime(ActionEvent event) {
        log().info("Current timezone: " + currentTimeZone().getId());
        ZonedDateTime dateTime = ZonedDateTime.now(currentTimeZone());
        Timestamp timestamp = Timestamp.from(dateTime.toInstant());
        long currentTimeMills13 = timestamp.getTime();
        long currentTimeMills11 = currentTimeMills13 / 1000;
        String currentTimeFormatted = dateTime.format(TimePatternMappings.get(selectTimePattern));

        timeMills11.setText(String.valueOf(currentTimeMills11));
        timeMills13.setText(String.valueOf(currentTimeMills13));
        timeFormatted.setText(currentTimeFormatted);
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));
        
        area.setDocument(
            RTDocument.create(
                RTHeading.create("Time Zone"),
                RTParagraph.create(
                    RTText.create("Make sure choose the right time zone. ")
                ),
                RTParagraph.create(),
                
                RTHeading.create("Time Pattern"),
                RTParagraph.create(
                    RTText.create("Supported datetime format pattern:"),
                    RTList.create(
                        TimePatterns.stream().map(RTListItem::create).toArray(RTListItem[]::new)
                    )
                ),

                RTHeading.create("Time Mills 11"),
                RTParagraph.create(
                    RTText.create("Datetime in millisecond of 11.")
                ),

                RTHeading.create("Time Mills 13"),
                RTParagraph.create(
                    RTText.create("Datetime in millisecond of 13.")
                ),

                RTHeading.create("Time Formatted"),
                RTParagraph.create(
                    RTText.create("Datetime formatted with selected pattern.")
                )
            )
        );

        return area;
    }
}
