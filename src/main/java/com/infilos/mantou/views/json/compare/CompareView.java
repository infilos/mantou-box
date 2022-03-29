package com.infilos.mantou.views.json.compare;

import com.dlsc.workbenchfx.Workbench;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.*;
import com.infilos.mantou.views.json.JsonService;
import com.infilos.utils.Loggable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.*;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class CompareView implements Initializable, Loggable, DialogSupport, NotifySupport, TooltipSupport {

    @FXML
    private Button compareButton;

    @FXML
    private VirtualizedScrollPane<CodeArea> jsonPaneLeft;

    @FXML
    private VirtualizedScrollPane<CodeArea> jsonPaneRight;

    @FXML
    private CodeArea jsonAreaLeft;

    @FXML
    private CodeArea jsonAreaRight;

    @FXML
    private Button findPrev;

    @FXML
    private Button findNext;

    @FXML
    private Label foundDiffs;

    @Inject
    private Stage mainStage;

    @Inject
    private Workbench workbench;

    private final Color oldColor = Color.valueOf("#16383f");
    private final Color newColor = Color.valueOf("#16492f");
    private final IntegerProperty diffCount = new SimpleIntegerProperty(0);
    private final ObservableListIterator<Integer> diffIterator = new ObservableListIterator<>();

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public Workbench workbench() {
        return workbench;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //jsonAreaLeft.setParagraphGraphicFactory(LineNumberFactory.get(jsonAreaLeft));
        //jsonAreaRight.setParagraphGraphicFactory(LineNumberFactory.get(jsonAreaRight));

        jsonAreaLeft.textProperty().addListener((obs, oldText, newText) -> {
            jsonAreaLeft.setStyleSpans(0, JsonService.computeHighlighting(newText));
        });
        jsonAreaRight.textProperty().addListener((obs, oldText, newText) -> {
            jsonAreaRight.setStyleSpans(0, JsonService.computeHighlighting(newText));
        });

        //jsonPaneLeft.estimatedScrollYProperty().bindBidirectional(jsonPaneRight.estimatedScrollYProperty());

        findPrev.setOnAction(e -> scrollTo(diffIterator.previous()));
        findPrev.disableProperty().bind(Bindings.not(diffIterator.hasPreviousProperty()));

        findNext.setOnAction(e -> scrollTo(diffIterator.next()));
        findNext.disableProperty().bind(Bindings.not(diffIterator.hasNextProperty()));

        //foundDiffs.textProperty().bind(diffCount.asString());
    }

    @FXML
    private void handleCompare(final ActionEvent event) {
        String leftText = jsonAreaLeft.getText();
        String rightText = jsonAreaRight.getText();

        if (StringUtils.isAnyBlank(leftText, rightText)) {
            showErrorTooltip(compareButton, "Input JSON first!", 3);
        }
        if (leftText.lines().count() == 1) {
            jsonAreaLeft.replaceText(JsonService.formatSilent(leftText));
        }
        if (rightText.lines().count() == 1) {
            jsonAreaRight.replaceText(JsonService.formatSilent(rightText));
        }

        CompletableFuture<List<DiffRow>> diffFuture = CompletableFuture.supplyAsync(() -> {
            DiffRowGenerator generator = DiffRowGenerator.create()
                .ignoreWhiteSpaces(true)
                .build();
            return generator.generateDiffRows(
                List.of(jsonAreaLeft.getText().split("\\R")),
                List.of(jsonAreaRight.getText().split("\\R"))
            );
        });

        diffFuture.thenAcceptAsync(rows -> {
            jsonAreaLeft.clear();
            jsonAreaRight.clear();

            AtomicInteger index = new AtomicInteger(0);
            List<Integer> diffIndexes = new ArrayList<>();

            rows.forEach(diffRow -> {
                jsonAreaLeft.appendText(diffRow.getOldLine());
                jsonAreaLeft.appendText("\n");
                jsonAreaRight.appendText(diffRow.getNewLine());
                jsonAreaRight.appendText("\n");

                // fixme: DiffState
                switch (diffRow.getTag()) {
                    case CHANGE:
                    case DELETE:
                    case INSERT:
                        if (!diffRow.getOldLine().isBlank() && !diffRow.getNewLine().isBlank()) {
                            jsonAreaLeft.addSelection(createSelection(jsonAreaLeft, diffRow.getOldLine(), index.get(), oldColor));
                            jsonAreaRight.addSelection(createSelection(jsonAreaRight, diffRow.getNewLine(), index.get(), oldColor));
                        }
                        if (diffRow.getOldLine().isBlank()) {
                            jsonAreaRight.addSelection(createSelection(jsonAreaRight, diffRow.getNewLine(), index.get(), newColor));
                        }
                        if (diffRow.getNewLine().isBlank()) {
                            jsonAreaLeft.addSelection(createSelection(jsonAreaLeft, diffRow.getOldLine(), index.get(), newColor));
                        }
                        diffIndexes.add(index.get());
                }
                index.getAndIncrement();
            });
            List<Integer> diffBlocks = Utils.skipSequences(diffIndexes);
            diffIterator.setIterator(diffBlocks);
            diffCount.set(diffBlocks.size());

            log().info("diff count: {}, index: {}, blocks: {}", diffCount.get(), StringUtils.join(",", diffIndexes), StringUtils.join(",", diffBlocks));

            foundDiffs.setText(String.valueOf(diffCount.get()));
        }, FXExecutor.getInstance());
    }

    private Selection<Collection<String>, String, Collection<String>> createSelection(CodeArea codeArea, String text, int index, Color color) {
        Selection<Collection<String>, String, Collection<String>> selection = new SelectionImpl<>(
            "diff" + System.currentTimeMillis(),
            codeArea,
            path -> path.setHighlightFill(color));
        selection.selectRange(index, 0, index, text.length());

        return selection;
    }

    // fixme: DiffState
    private void scrollTo(int lineNumber) {
        Bounds bb = jsonAreaLeft.getLayoutBounds();
        int percent = (int) (bb.getHeight() / 3);

        jsonAreaLeft.showParagraphRegion(lineNumber, new BoundingBox(bb.getMinX() + percent, 0, 0, bb.getHeight() - percent));
        jsonAreaLeft.moveTo(lineNumber, 0);
        String leftLine = jsonAreaLeft.getText().lines().toList().get(lineNumber);
        jsonAreaLeft.selectRange(lineNumber, 0, lineNumber, leftLine.length());

        jsonAreaRight.showParagraphRegion(lineNumber, new BoundingBox(bb.getMinX() + percent, 0, 0, bb.getHeight() - percent));
        jsonAreaRight.moveTo(lineNumber, 0);
        String rightLine = jsonAreaRight.getText().lines().toList().get(lineNumber);
        jsonAreaRight.selectRange(lineNumber, 0, lineNumber, rightLine.length());

        foundDiffs.setText(String.format("%d/%d", diffIterator.indexOf(lineNumber) + 1, diffCount.get()));
    }

    @FXML
    private void handleFindKeyEvent(final KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.F) {
            handleCompare(new ActionEvent());
            event.consume();
        } else if (event.isControlDown() && event.getCode() == KeyCode.J) {
            handleFindPrevAction(new ActionEvent());
            event.consume();
        } else if (event.isControlDown() && event.getCode() == KeyCode.K) {
            handleFindNextAction(new ActionEvent());
            event.consume();
        } else if (event.isControlDown() && event.getCode() == KeyCode.SPACE) {
            handleFindNextAction(new ActionEvent());
            event.consume();
        }
    }

    @FXML
    private void handleFindPrevAction(final ActionEvent event) {
        if (diffIterator.hasPrevious()) {
            scrollTo(diffIterator.previous());
        }
    }

    @FXML
    private void handleFindNextAction(final ActionEvent event) {
        if (diffIterator.hasNext()) {
            scrollTo(diffIterator.next());
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        jsonAreaLeft.replaceText("");
        jsonAreaRight.replaceText("");
        scrollTo(0);
    }
}
