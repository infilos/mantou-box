package com.infilos.mantou.views.json;

import com.infilos.mantou.utils.AwareResource;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.time.Duration;

public class JsonEditor extends CodeArea implements AwareResource {
    
    public JsonEditor() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.getStylesheets().add(loadStyle("JsonView.css"));
        this.getStylesheets().add(loadStyle("JsonHighlight.css"));
        this.multiPlainChanges()
            .successionEnds(Duration.ofMillis(100))
            .subscribe(__ -> highlight());
    }
    
    private void highlight() {
        this.setStyleSpans(0, JsonService.computeHighlighting(getText()));
    }
}
