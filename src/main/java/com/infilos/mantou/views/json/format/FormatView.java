package com.infilos.mantou.views.json.format;

import com.dlsc.workbenchfx.Workbench;
import com.infilos.mantou.controls.DialogSupport;
import com.infilos.mantou.controls.NotifySupport;
import com.infilos.mantou.views.json.JsonService;
import com.infilos.utils.Loggable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.CodeArea;

import javax.inject.Inject;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FormatView implements Initializable, Loggable, DialogSupport, NotifySupport {

    @FXML
    private Button clearSpacesButton;
    @FXML
    private Button formatButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button findButton;
    @FXML
    private HBox findBar;
    @FXML
    private TextField findField;
    @FXML
    private Button findPrev;
    @FXML
    private Button findNext;
    @FXML
    private Label findMatches;
    @FXML
    private CodeArea jsonArea;

    private FindState searchState;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public Workbench workbench() {
        return workbench;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jsonArea.setPrefHeight(1024);
        jsonArea.setWrapText(true);
        jsonArea.textProperty().addListener((obs, oldText, newText) -> {
            jsonArea.setStyleSpans(0, JsonService.computeHighlighting(newText));
        });
        findBar.setVisible(false);
        findBar.setManaged(false);
    }

    @FXML
    private void handlePrettyPrint(final ActionEvent event) {
        String data = jsonArea.getText();
        try {
            String pretty = JsonService.format(data);
            if(!data.equals(pretty)) {
                jsonArea.replaceText(pretty);    
            }
        } catch (Exception e) {
            showErrorDialog("Invalid JSON", e);
        }
    }

    @FXML
    private void handleClearSpaces(final ActionEvent event) {
        String data = jsonArea.getText();
        try {
            String cleared = JsonService.clearSpaces(data);
            jsonArea.replaceText(cleared);
        } catch (Exception e) {
            showErrorDialog("Invalid JSON", e);
        }
    }
    

    @FXML
    private void handleFind(final ActionEvent event) {
        if(findBar.isVisible()) {
            handleStopFindAction(event);
        } else {
            findBar.setVisible(true);
            findBar.setManaged(true);
            findField.requestFocus();    
        }
    }

    @FXML
    private void handleFindKeyEvent(final KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.F) {
            findBar.setVisible(true);
            findBar.setManaged(true);
            findField.requestFocus();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            findBar.setVisible(false);
            findBar.setManaged(false);
        }
    }

    @FXML
    private void handleStopFindAction(final ActionEvent event) {
        jsonArea.deselect();
        findField.setText("");
        findMatches.setText("");
        findBar.setVisible(false);
        findBar.setManaged(false);
    }

    @FXML
    private void handleFindNextKeyEvent(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleFindNextAction(new ActionEvent());
        }
    }

    @FXML
    private void handleFindPrevAction(final ActionEvent event) {
        doSearch(getSearchState().prev());
    }

    @FXML
    private void handleFindNextAction(final ActionEvent event) {
        doSearch(getSearchState().next());
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void doSearch(Optional<FindState.SearchSpan> span) {
        jsonArea.deselect();
        span.ifPresent(searchSpan -> {
            jsonArea.moveTo(searchSpan.getFrom());
            jsonArea.requestFollowCaret();
            jsonArea.selectRange(searchSpan.getFrom(), searchSpan.getTo());
        });
        findMatches.setText(getSearchState().toString());
    }

    private FindState getSearchState() {
        if (searchState == null || !searchState.isValid(findField.getText(), jsonArea.getText())) {
            searchState = new FindState(findField.getText(), jsonArea.getText());
        }
        
        return searchState;
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        content.putString(jsonArea.getText());

        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("Nothing Copied! Input first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage("Json Copied!");
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        jsonArea.replaceText("");
        handleStopFindAction(event);
    }
}
