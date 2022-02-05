package com.infilos.mantou.views.textgen.uuid;

import com.dlsc.gemsfx.richtextarea.*;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.codec.StringCodec;
import com.github.f4b6a3.uuid.codec.UriCodec;
import com.github.f4b6a3.uuid.codec.base.*;
import com.github.f4b6a3.uuid.codec.other.*;
import com.github.f4b6a3.uuid.enums.*;
import com.github.f4b6a3.uuid.util.*;
import com.infilos.mantou.controls.*;
import com.infilos.mantou.utils.AwareResource;
import com.infilos.utils.Loggable;
import com.tangorabox.reactivedesk.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@FXMLView
public class UUIDGeneratorView implements Initializable, AwareResource, ComboBoxSupport, NotifySupport, TooltipSupport, Loggable {

    @Inject
    private Stage mainStage;

    @FXML
    private ComboBox<Integer> generateCount;

    @FXML
    private CheckBox useUppercase;

    @FXML
    private CheckBox useHyphens;

    @FXML
    private ComboBox<String> generateMode;

    @FXML
    private Label modeLabel1;

    @FXML
    private Label modeLabel2;

    @FXML
    private Label modeLabel3;

    @FXML
    private ComboBox<String> modeTimeAttri;

    @FXML
    private HBox modeTimeOrdered;

    @FXML
    private CheckBox modeTimeOrderedCheck;

    @FXML
    private TextField modeNameName;

    @FXML
    private ComboBox<String> modeNameNamespace;

    @FXML
    private ComboBox<String> modeNameType;

    @FXML
    private ComboBox<String> modeDceDomain;

    @FXML
    private TextField modeDceIdentifier;

    @FXML
    private ComboBox<String> modeDceAttri;

    @FXML
    private ComboBox<String> modeCombPosition;

    @FXML
    private HBox modeCombShort;

    @FXML
    private CheckBox modeCombShortCheck;

    @FXML
    private Button generate;

    @FXML
    private Button copy;

    @FXML
    private Button clear;

    @FXML
    private TextArea generated;

    @FXML
    private TextFlow transformed;

    @FXML
    private ScrollPane transformedScroll;

    private boolean showingGenerated;
    private boolean showingTransormed;
    
    @FXML
    private Button backGenerate;

    @FXML
    private Button verify;

    @FXML
    private ComboBox<String> verifyMode;

    @FXML
    private Button extract;

    @FXML
    private ComboBox<String> extractMode;

    @FXML
    private Button convert;

    @FXML
    private ComboBox<String> convertMode;

    @FXML
    private ScrollPane notePane;

    @Override
    public Stage mainStage() {
        return mainStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
        buildComboItems(generateCount, List.of(1, 2, 3, 5, 10, 20, 50, 100));
        enableRefreshComboValue(generateCount, Integer::parseInt, 1);

        //
        buildComboItems(generateMode, "Random", "Time", "Name", "Dce Security", "COMB");
        enableRefreshComboValue(generateMode, String::toString, "Random");

        //
        buildComboItems(modeTimeAttri, "None", "Mac", "Hash", "Random");
        enableRefreshComboValue(modeTimeAttri, String::toString, "None");

        //
        buildComboItems(modeNameNamespace, "None", "DNS", "URL", "ISO_OID", "X500_DN");
        enableRefreshComboValue(modeNameNamespace, String::toString, "None");
        buildComboItems(modeNameType, "MD5", "SHA-1");
        enableRefreshComboValue(modeNameType, String::toString, "None");

        //
        buildComboItems(modeDceDomain, "PERSON", "GROUP", "ORG");
        enableRefreshComboValue(modeDceDomain, String::toString, "PERSON");
        buildComboItems(modeDceAttri, "None", "Mac", "Hash", "Random");
        enableRefreshComboValue(modeDceAttri, String::toString, "None");

        //
        buildComboItems(modeCombPosition, "Prefix", "Suffix");
        enableRefreshComboValue(modeCombPosition, String::toString, "Prefix");

        //
        hideAllModeBasedForms();

        //
        buildComboItems(verifyMode, "Valid", "Random", "Rfc4122", "ReservedNcs", "ReservedMicrosoft", "ReservedFuture", "NameBasedMd5", "NameBasedSha1", "TimeBased", "TimeOrdered", "DceSecurity");
        enableRefreshComboValue(verifyMode, String::toString, "Valid");

        // 
        buildComboItems(extractMode, "UUID", "COMB GUID");
        enableRefreshComboValue(extractMode, String::toString, "UUID");

        // 
        buildComboItems(convertMode, "Canonical", "URI", "Base16", "Base32", "Base58", "Base62", "Base64", "Slug", "NcName", ".NET GUID V1", ".NET GUID V4");
        enableRefreshComboValue(convertMode, String::toString, "Canonical");

        //
        notePane.setContent(buildNoteArea());

        // 
        buildTransformedStyle();
        switchToGenerateTextArea();
    }

    @FXML
    private void handleModeChanged(ActionEvent event) {
        hideAllModeBasedForms();

        String mode = comboValueOf(generateMode, String.class, String::toString);
        if ("Time".equals(mode)) {
            modeLabel1.setText("With Attri:");
            modeLabel2.setText("Is Ordered:");
            modeLabel3.setText("");
            modeTimeAttri.setVisible(true);
            modeTimeOrdered.setVisible(true);
        } else if ("Name".equals(mode)) {
            modeLabel1.setText("Name:");
            modeLabel2.setText("Namespace:");
            modeLabel3.setText("SHA1:");
            modeNameName.setVisible(true);
            modeNameNamespace.setVisible(true);
            modeNameType.setVisible(true);
        } else if ("Dce Security".equals(mode)) {
            modeLabel1.setText("Local Domain:");
            modeLabel2.setText("Local Identifier:");
            modeLabel3.setText("With Attri:");
            modeDceDomain.setVisible(true);
            modeDceIdentifier.setVisible(true);
            modeDceAttri.setVisible(true);
        } else if ("COMB".equals(mode)) {
            modeLabel1.setText("Position:");
            modeLabel2.setText("Is Short:");
            modeLabel3.setText("");
            modeCombPosition.setVisible(true);
            modeCombShort.setVisible(true);
        } else {
            modeLabel1.setText("");
            modeLabel2.setText("");
            modeLabel3.setText("");
        }
    }

    private void hideAllModeBasedForms() {
        modeLabel1.setText("");
        modeLabel2.setText("");
        modeLabel3.setText("");
        modeTimeAttri.setVisible(false);
        modeTimeOrdered.setVisible(false);
        modeNameName.setVisible(false);
        modeNameNamespace.setVisible(false);
        modeNameType.setVisible(false);
        modeDceDomain.setVisible(false);
        modeDceIdentifier.setVisible(false);
        modeDceAttri.setVisible(false);
        modeCombPosition.setVisible(false);
        modeCombShort.setVisible(false);
    }

    private void switchToGenerateTextArea() {
        generated.setVisible(true);
        transformed.setVisible(false);
        transformedScroll.setVisible(false);
        backGenerate.setVisible(false);
        showingGenerated = true;
        showingTransormed = false;
    }

    private void switchToTransformTextArea() {
        generated.setVisible(false);
        transformed.setVisible(true);
        transformedScroll.setVisible(true);
        backGenerate.setVisible(true);
        showingGenerated = false;
        showingTransormed = true;
    }

    private void clearTransformTextArea() {
        transformed.getChildren().clear();
    }

    @FXML
    private void handleGenerate(final ActionEvent event) {
        StringBuilder buff = new StringBuilder();
        int expectCount = comboValueOf(generateCount, Integer.class, Integer::parseInt);

        for (int i = 0; i < expectCount; i++) {

            String uuid = genModeBasedUuid();

            if (useUppercase.isSelected()) {
                uuid = uuid.toUpperCase();
            }
            if (!useHyphens.isSelected()) {
                uuid = uuid.replace("-", "");
            }
            buff.append(uuid).append("\n");
        }

        generated.setText(buff.toString());

        switchToGenerateTextArea();
    }

    private String genModeBasedUuid() {
        String mode = comboValueOf(generateMode, String.class, String::toString);

        if ("Random".equals(mode)) {
            return genRandomUuid();
        } else if ("Time".equals(mode)) {
            return genTimeUuid();
        } else if ("Name".equals(mode)) {
            return genNameUuid();
        } else if ("Dce Security".equals(mode)) {
            return genDceSecurityUuid();
        } else if ("COMB".equals(mode)) {
            return genCOMBUuid();
        }

        return "";
    }

    private String genRandomUuid() {
        return UuidCreator.getRandomBased().toString();
    }

    private String genTimeUuid() {
        String attri = comboValueOf(modeTimeAttri, String.class, String::toString);
        boolean ordered = modeTimeOrderedCheck.isSelected();

        if ("None".equals(attri) && ordered) {
            return UuidCreator.getTimeOrdered().toString();
        } else if ("None".equals(attri)) {
            return UuidCreator.getTimeBased().toString();
        } else if ("Mac".equals(attri) && ordered) {
            return UuidCreator.getTimeOrderedWithMac().toString();
        } else if ("Mac".equals(attri)) {
            return UuidCreator.getTimeBasedWithMac().toString();
        } else if ("Hash".equals(attri) && ordered) {
            return UuidCreator.getTimeOrderedWithHash().toString();
        } else if ("Hash".equals(attri)) {
            return UuidCreator.getTimeBasedWithHash().toString();
        } else if ("Random".equals(attri) && ordered) {
            return UuidCreator.getTimeOrderedWithRandom().toString();
        } else if ("Random".equals(attri)) {
            return UuidCreator.getTimeBasedWithRandom().toString();
        }

        return "";
    }

    private String genNameUuid() {
        String name = modeNameName.getText();
        String namespace = comboValueOf(modeNameNamespace, String.class, String::toString);
        String type = comboValueOf(modeNameType, String.class, String::toString);

        if ("MD5".equals(type)) {
            if (StringUtils.equals(namespace, "None")) {
                return UuidCreator.getNameBasedMd5(name).toString();
            } else if (StringUtils.equals(namespace, "DNS")) {
                return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_DNS, name).toString();
            } else if (StringUtils.equals(namespace, "URL")) {
                return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_URL, name).toString();
            } else if (StringUtils.equals(namespace, "ISO_OID")) {
                return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_ISO_OID, name).toString();
            } else if (StringUtils.equals(namespace, "X500_DN")) {
                return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_X500_DN, name).toString();
            }
        } else if ("SHA-1".equals(type)) {
            if (StringUtils.equals(namespace, "None")) {
                return UuidCreator.getNameBasedSha1(name).toString();
            } else if (StringUtils.equals(namespace, "DNS")) {
                return UuidCreator.getNameBasedSha1(UuidNamespace.NAMESPACE_DNS, name).toString();
            } else if (StringUtils.equals(namespace, "URL")) {
                return UuidCreator.getNameBasedSha1(UuidNamespace.NAMESPACE_URL, name).toString();
            } else if (StringUtils.equals(namespace, "ISO_OID")) {
                return UuidCreator.getNameBasedSha1(UuidNamespace.NAMESPACE_ISO_OID, name).toString();
            } else if (StringUtils.equals(namespace, "X500_DN")) {
                return UuidCreator.getNameBasedSha1(UuidNamespace.NAMESPACE_X500_DN, name).toString();
            }
        }

        return "";
    }

    @SuppressWarnings("ConstantConditions")
    private String genDceSecurityUuid() {
        if (StringUtils.isBlank(modeDceIdentifier.getText()) ||
            !StringUtils.isNumeric(modeDceIdentifier.getText())) {
            showErrorTooltip(generate, "Local Identifier is required!", 3);
            return "";
        }

        String domain = comboValueOf(modeDceDomain, String.class, String::toString);
        int identifier = Integer.parseInt(modeDceIdentifier.getText());
        String attri = comboValueOf(modeDceAttri, String.class, String::toString);
        UuidLocalDomain localDomain = UuidLocalDomain.LOCAL_DOMAIN_PERSON;

        if (domain.equals("PERSON")) {
            localDomain = UuidLocalDomain.LOCAL_DOMAIN_PERSON;
        } else if (domain.equals("GROUP")) {
            localDomain = UuidLocalDomain.LOCAL_DOMAIN_GROUP;
        } else if (domain.equals("ORG")) {
            localDomain = UuidLocalDomain.LOCAL_DOMAIN_ORG;
        }

        if ("None".equals(attri)) {
            return UuidCreator.getDceSecurity(localDomain, identifier).toString();
        } else if ("Mac".equals(attri)) {
            return UuidCreator.getDceSecurityWithMac(localDomain, identifier).toString();
        } else if ("Hash".equals(attri)) {
            return UuidCreator.getDceSecurityWithHash(localDomain, identifier).toString();
        } else if ("Random".equals(attri)) {
            return UuidCreator.getDceSecurityWithRandom(localDomain, identifier).toString();
        }

        return "";
    }

    private String genCOMBUuid() {
        String position = comboValueOf(modeCombPosition, String.class, String::toString);
        boolean isShort = modeCombShortCheck.isSelected();

        if ("Prefix".equals(position) && isShort) {
            return UuidCreator.getShortPrefixComb().toString();
        } else if ("Prefix".equals(position)) {
            return UuidCreator.getPrefixComb().toString();
        } else if ("Suffix".equals(position) && isShort) {
            return UuidCreator.getShortSuffixComb().toString();
        } else if ("Suffix".equals(position)) {
            return UuidCreator.getSuffixComb().toString();
        }

        return "";
    }

    @FXML
    private void handleCopy(final ActionEvent event) {
        final ClipboardContent content = new ClipboardContent();
        
        if (showingGenerated) {
            content.putString(generated.getText());
        } else if(showingTransormed) {
            content.putString(transformed.getChildren().stream()
                .filter(n -> n instanceof Text)
                .map(t -> ((Text) t).getText())
                .collect(Collectors.joining()));
        } else {
            content.putString("");
        }
        
        if (StringUtils.isBlank(content.getString())) {
            showInfoMessage("No UUID Copied! Generate or Paste first.");
        } else {
            Clipboard.getSystemClipboard().setContent(content);
            showInfoMessage(String.format("%s Items Copied!", content.getString().split("\n").length));
        }
    }

    @FXML
    private void handleClear(final ActionEvent event) {
        generated.setText("");
    }
    
    @FXML
    private void handleBackGenerate(final ActionEvent event) {
        if(showingTransormed) {
            switchToGenerateTextArea();
        }
    }

    @FXML
    private void handleVerify(final ActionEvent event) {
        clearTransformTextArea();

        String mode = comboValueOf(verifyMode, String.class, String::toString);
        List<String> uuids = Arrays.asList(StringUtils.split(generated.getText(), "\n"));
        List<Boolean> isValids = uuids.stream().map(uuid -> UuidValidator.isValid(uuid.toLowerCase())).toList();

        for (int idx = 0; idx < uuids.size(); idx++) {
            String uuid = uuids.get(idx);
            Boolean isValid = isValids.get(idx);

            if ("Valid".equals(mode)) {
                transformed.getChildren().add(buildVerifiedUuidValue(uuid, isValid));
                continue;
            }

            if (isValid && "Random".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isRandomBased(UuidCreator.fromString(uuid)));
            } else if ("Rfc4122".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isRfc4122(UuidCreator.fromString(uuid)));
            } else if ("ReservedNcs".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isReservedNcs(UuidCreator.fromString(uuid)));
            } else if ("ReservedMicrosoft".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isReservedMicrosoft(UuidCreator.fromString(uuid)));
            } else if ("ReservedFuture".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isReservedFuture(UuidCreator.fromString(uuid)));
            } else if ("NameBasedMd5".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isNameBasedMd5(UuidCreator.fromString(uuid)));
            } else if ("NameBasedSha1".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isNameBasedSha1(UuidCreator.fromString(uuid)));
            } else if ("TimeBased".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isTimeBased(UuidCreator.fromString(uuid)));
            } else if ("TimeOrdered".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isTimeOrdered(UuidCreator.fromString(uuid)));
            } else if ("DceSecurity".equals(mode)) {
                isValid = tryToBoolean(() -> UuidUtil.isDceSecurity(UuidCreator.fromString(uuid)));
            }

            transformed.getChildren().add(buildVerifiedUuidValue(uuid, isValid));
        }

        switchToTransformTextArea();
    }

    private Text buildVerifiedUuidValue(String uuid, boolean valid) {
        Text uuidText = new Text(uuid + "\n");

        if (valid) {
            uuidText.setStyle("-fx-fill: #4F8A10;-fx-font-weight:bold; -fx-font-family: 'Ubuntu Regular'; -fx-start-margin: 4");
        } else {
            uuidText.setStyle("-fx-fill: #9d9d44;-fx-font-weight:bold; -fx-font-family: 'Ubuntu Regular'; -fx-start-margin: 4");
        }

        return uuidText;
    }

    private Text buildFlowText(String string) {
        Text text = new Text(string + "\n");
        text.setStyle("-fx-fill: white;-fx-font-weight:bold; -fx-font-family: 'Ubuntu Regular'; -fx-start-margin: 4");

        return text;
    }

    @FXML
    private void handleExtract(final ActionEvent event) {
        clearTransformTextArea();

        String mode = comboValueOf(extractMode, String.class, String::toString);

        String machineId = String.valueOf(MachineId.getMachineId());
        String machineUuid = MachineId.getMachineUuid().toString();

        // machine id
        transformed.getChildren().add(buildFlowText("Machine ID: " + machineId));
        transformed.getChildren().add(buildFlowText("Machine UUID: " + machineUuid));
        transformed.getChildren().add(buildFlowText(""));

        // uuids
        List<String> uuids = Arrays.asList(StringUtils.split(generated.getText(), "\n"));
        List<Boolean> isValids = uuids.stream().map(uuid -> UuidValidator.isValid(uuid.toLowerCase())).toList();

        for (int idx = 0; idx < uuids.size(); idx++) {
            boolean isValid = isValids.get(idx);
            if (!isValid) {
                transformed.getChildren().add(buildVerifiedUuidValue(uuids.get(idx), isValid));
                transformed.getChildren().add(buildFlowText(""));
                continue;
            }

            UUID uuid = UUID.fromString(fillupUuidDashes(uuids.get(idx)));
            transformed.getChildren().add(buildVerifiedUuidValue(uuids.get(idx), isValid));

            // UUID
            if ("UUID".equals(mode)) {
                String instant = tryToString(() -> UuidUtil.getInstant(uuid), Instant::toString);
                String clocksq = tryToString(() -> UuidUtil.getClockSequence(uuid), String::valueOf);
                String nodeid = tryToString(() -> UuidUtil.getNodeIdentifier(uuid), String::valueOf);
                String version = tryToString(() -> UuidUtil.getVersion(uuid), UuidVersion::toString);
                String variant = tryToString(() -> UuidUtil.getVariant(uuid), UuidVariant::toString);

                transformed.getChildren().add(buildVerifiedUuidValue("  - Instant: " + instant, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - Clocksq: " + clocksq, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - Nodeid: " + nodeid, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - Version: " + version, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - Variant: " + variant, isValid));
            }
            // COMB GUID
            else {
                String prefix = tryToString(() -> CombUtil.getPrefix(uuid), String::valueOf);   // Unix milliseconds
                String suffix = tryToString(() -> CombUtil.getSuffix(uuid), String::valueOf);   // Unix milliseconds
                String prefixInstant = tryToString(() -> CombUtil.getPrefixInstant(uuid), Instant::toString);
                String suffixInstant = tryToString(() -> CombUtil.getSuffixInstant(uuid), Instant::toString);

                transformed.getChildren().add(buildVerifiedUuidValue("  - Prefix: " + prefix, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - PrefixInstant: " + prefixInstant, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - Suffix: " + suffix, isValid));
                transformed.getChildren().add(buildVerifiedUuidValue("  - SuffixInstant: " + suffixInstant, isValid));
            }

            transformed.getChildren().add(buildFlowText(""));
        }

        switchToTransformTextArea();
    }

    @FXML
    private void handleConvert(final ActionEvent event) {
        clearTransformTextArea();

        String mode = comboValueOf(convertMode, String.class, String::toString);

        List<String> uuids = Arrays.asList(StringUtils.split(generated.getText(), "\n"));
        List<Boolean> isValids = uuids.stream().map(uuid -> UuidValidator.isValid(uuid.toLowerCase())).toList();

        for (int idx = 0; idx < uuids.size(); idx++) {
            boolean isValid = isValids.get(idx);
            if (!isValid) {
                transformed.getChildren().add(buildVerifiedUuidValue(uuids.get(idx), isValid));
                transformed.getChildren().add(buildFlowText(""));
                continue;
            }

            UUID uuid = UUID.fromString(fillupUuidDashes(uuids.get(idx)));
            transformed.getChildren().add(buildVerifiedUuidValue(uuids.get(idx), isValid));
            String converted = "  - ";

            if ("Canonical".equals(mode)) {
                converted = converted + tryToString(() -> StringCodec.INSTANCE.encode(uuid));
            } else if ("URI".equals(mode)) {
                converted = converted + tryToString(() -> UriCodec.INSTANCE.encode(uuid), URI::toString);
            } else if ("Base16".equals(mode)) {
                converted = converted + tryToString(() -> Base16Codec.INSTANCE.encode(uuid));
            } else if ("Base32".equals(mode)) {
                converted = converted + tryToString(() -> Base32Codec.INSTANCE.encode(uuid));
            } else if ("Base58".equals(mode)) {
                converted = converted + tryToString(() -> Base58BtcCodec.INSTANCE.encode(uuid));
            } else if ("Base62".equals(mode)) {
                converted = converted + tryToString(() -> Base62Codec.INSTANCE.encode(uuid));
            } else if ("Base64".equals(mode)) {
                converted = converted + tryToString(() -> Base64Codec.INSTANCE.encode(uuid));
            } else if ("Slug".equals(mode)) {
                converted = converted + tryToString(() -> SlugCodec.INSTANCE.encode(uuid));
            } else if ("NcName".equals(mode)) {
                converted = converted + tryToString(() -> NcnameCodec.INSTANCE.encode(uuid));
            } else if (".NET GUID V1".equals(mode)) {
                converted = converted + tryToString(() -> DotNetGuid1Codec.INSTANCE.encode(uuid), UUID::toString);
            } else if (".NET GUID V4".equals(mode)) {
                converted = converted + tryToString(() -> DotNetGuid4Codec.INSTANCE.encode(uuid), UUID::toString);
            }

            transformed.getChildren().add(buildVerifiedUuidValue(converted, isValid));
            transformed.getChildren().add(buildFlowText(""));
        }

        switchToTransformTextArea();
    }

    /**
     * Make TextFlow looks like TextArea, but contains colored texts.
     */
    private void buildTransformedStyle() {
        transformed.setLineSpacing(2);
        transformedScroll.setBorder(new Border(new BorderStroke(
            Paint.valueOf("#858585"), Paint.valueOf("#858585"), Paint.valueOf("#858585"), Paint.valueOf("#858585"),
            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            new BorderWidths(2),
            Insets.EMPTY)
        ));
    }

    private RichTextArea buildNoteArea() {
        RichTextArea area = new RichTextArea();
        area.getStyleClass().add(loadStyle("Workbench.css"));

        area.setDocument(
            RTDocument.create(
                RTHeading.create("UUID Generator"),
                RTParagraph.create(
                    RTText.create("More features are working in progress. ")
                ),
                RTParagraph.create()
            )
        );

        return area;
    }

    private boolean tryToBoolean(Provider<Boolean> checkValid) {
        try {
            return checkValid.get();
        } catch (Throwable ignore) {
            return false;
        }
    }

    private <T> String tryToString(Provider<T> provider, Function<T, String> converter, String fallback) {
        try {
            return converter.apply(provider.get());
        } catch (Throwable ignore) {
            return fallback;
        }
    }

    private <T> String tryToString(Provider<T> provider, Function<T, String> converter) {
        return tryToString(provider, converter, "");
    }

    private String tryToString(Provider<String> provider) {
        return tryToString(provider, Function.identity(), "");
    }

    private String fillupUuidDashes(String uuid) {
        if (uuid.contains("-")) {
            return uuid;
        }

        return uuid.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
        );
    }
}
