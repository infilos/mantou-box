package com.infilos.mantou.views.material.label;

import com.infilos.mantou.api.WorkView;
import com.tangorabox.reactivedesk.FXMLView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.effects.ripple.MFXCircleRippleGenerator;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLView
public class LabelsView extends StackPane implements WorkView<Void> {
    
    @FXML
    private MFXLabel custom;

    @Override
    public void setModel(Void model) {
        
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MFXIconWrapper leading = new MFXIconWrapper(new MFXFontIcon("mfx-filter", 15), 22).defaultRippleGeneratorBehavior();
        MFXIconWrapper trailing = new MFXIconWrapper(new MFXFontIcon("mfx-info-circle", 15), 22).defaultRippleGeneratorBehavior();

        NodeUtils.makeRegionCircular(leading);
        NodeUtils.makeRegionCircular(trailing);

        
        MFXCircleRippleGenerator lrg = leading.getRippleGenerator();
        lrg.setRippleColor(Color.web("#849ED7"));

        MFXCircleRippleGenerator trg = trailing.getRippleGenerator();
        trg.setRippleColor(Color.web("#849ED7"));

        custom.setLeadingIcon(leading);
        custom.setTrailingIcon(trailing);
    }
}
