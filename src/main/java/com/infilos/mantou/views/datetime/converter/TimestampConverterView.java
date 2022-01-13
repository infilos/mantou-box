package com.infilos.mantou.views.datetime.converter;

import com.dlsc.gemsfx.richtextarea.*;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.infilos.mantou.views.datetime.converter.TimestampConverter.*;

@FXMLView
public class TimestampConverterView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @FXML
    private ComboBox<String> timeZone;

    @FXML
    private ComboBox<String> timePattern;

    @FXML
    private ComboBox<String> timeMode;

    @FXML
    private TextField timestamp;

    @FXML
    private TextField datetime;

    @FXML
    private Button refresh;

    @FXML
    private Button copy;

    @FXML
    private Button clear;

    @FXML
    private TextArea generated;

    @FXML
    private ScrollPane notePane;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        buildComboItems(timeZone, LocalTimeZoneIndex, AllTimeZones);
        enableRefreshComboValue(timeZone, String::toString, LocalTimeZone);

        //
        buildComboItems(timePattern, TimePatterns);
        enableRefreshComboValue(timePattern, String::toString, "");

        //
        buildComboItems(timeMode, DefaultTimeModeIndex, TimeModes);
        enableRefreshComboValue(timeMode, String::toString, "");

        //
        notePane.setContent(buildNoteArea());

        //
        refreshCurrent(LocalTimeZone, DefaultTimePattern, DefaultTimeMode);
    }

    private void refreshCurrent(String timeZone, String timePattern, String timeMode) {
        Instant currentInstant = currentInstant(timeZone);
        long currentTimestamp = buildTimestamp(currentInstant, timeMode);
        String currentDatetime = buildDatetime(currentInstant, timeZone, timePattern);

        timestamp.setText(String.valueOf(currentTimestamp));
        datetime.setText(currentDatetime);
    }

    private String currentTimeZone() {
        return comboValueOf(timeZone, String.class, String::toString);
    }

    private ZoneId currentTimeZoneId() {
        return ZoneId.of(currentTimeZone());
    }

    private String currentTimePattern() {
        return comboValueOf(timePattern, String.class, String::toString).trim();
    }

    private DateTimeFormatter currentTimePatternFormatter() {
        String pattern = currentTimePattern();
        TimePatternMappings.computeIfAbsent(pattern, k -> DateTimeFormatter.ofPattern(pattern));

        return TimePatternMappings.get(pattern);
    }

    @FXML
    private void handleConvertTimestamp(final ActionEvent event) {
        try {
            String inputTimestamp = timestamp.getText().trim();
            if (!StringUtils.isNumeric(inputTimestamp)) {
                showErrorTooltip(timestamp, "Timestamp must be numeric!", 5);
                return;
            }

            long timestampValue = Long.parseLong(inputTimestamp);
            int timestampLength = inputTimestamp.length();
            String currentTimeMode = comboValueOf(timeMode, String.class, String::toString);

            if (timestampLength != TimeModeLengths.get(currentTimeMode)) {
                showErrorTooltip(
                    timestamp,
                    String.format(
                        "%s must be %s, actual is %s!",
                        currentTimeMode, TimeModeLengths.get(currentTimeMode), timestampLength),
                    5
                );
                return;
            }

            Instant currentInstant = buildInstant(timestampValue, currentTimeMode);
            String currentDatetime = buildDatetime(currentInstant, currentTimeZone(), currentTimePattern());

            datetime.setText(currentDatetime);
            generated.appendText(String.format("Convert: %s to %s\n", inputTimestamp, currentDatetime));
        } catch (Exception e) {
            log().error("Convert timestamp failed", e);
            showErrorDialog(currentTimePattern(), timestamp.getText(), e);
        }
    }

    @FXML
    private void handleCopyTimestamp(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(timestamp.getText());
        Clipboard.getSystemClipboard().setContent(content);

        showInfoMessage("Timestamp Copied!");
    }

    @FXML
    private void handleConvertDatetime(final ActionEvent event) {
        try {
            String inputDatetime = datetime.getText().trim();
            ZonedDateTime currentDateTime = ZonedDateTime.of(
                LocalDateTime.parse(inputDatetime, currentTimePatternFormatter()),
                currentTimeZoneId()
            );
            Instant currentInstant = currentDateTime.toInstant();
            String currentTimeMode = comboValueOf(timeMode, String.class, String::toString);
            long currentTimestamp = buildTimestamp(currentInstant, currentTimeMode);

            timestamp.setText(String.valueOf(currentTimestamp));
            generated.appendText(String.format("Convert: %s to %s\n", inputDatetime, currentTimestamp));
        } catch (Exception e) {
            log().error("Convert datetime failed", e);
            showErrorDialog(currentTimePattern(), timestamp.getText(), e);
        }
    }

    @FXML
    private void handleCopyDatetime(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(datetime.getText());
        Clipboard.getSystemClipboard().setContent(content);

        showInfoMessage("Datetime Copied!");
    }

    @FXML
    private void handleRefresh(final ActionEvent event) {
        String currentTimeZone = comboValueOf(timeZone, String.class, String::toString);
        String currentTimePattern = comboValueOf(timePattern, String.class, String::toString);
        String currentTimeMode = comboValueOf(timeMode, String.class,String::toString);

        refreshCurrent(currentTimeZone, currentTimePattern, currentTimeMode);

        generated.appendText(String.format("Current: %s, %s\n", timestamp.getText(), datetime.getText()));
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(generated.getText());

        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("No history Copied! Convert first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage(String.format("%s Items Copied!", content.getString().split("\n").length));
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        generated.setText("");
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

                RTHeading.create("Time Mode"),
                RTParagraph.create(
                    RTText.create("The timestamp mode with different digits length:"),
                    RTList.create(
                        TimeModes.stream().map(RTListItem::create).toArray(RTListItem[]::new)
                    )
                ),

                RTHeading.create("Timestamp"),
                RTParagraph.create(
                    RTText.create("Timestamp in selected mode.")
                ),

                RTHeading.create("Datetime"),
                RTParagraph.create(
                    RTText.create("Datetime formatted with selected/inputed pattern.")
                )
            )
        );

        return area;
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
    }
}
