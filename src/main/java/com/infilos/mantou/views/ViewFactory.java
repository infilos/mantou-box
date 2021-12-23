package com.infilos.mantou.views;

import com.infilos.mantou.views.material.button.ButtonsModule;
import com.infilos.mantou.views.material.button.ButtonsView;
import com.infilos.mantou.views.material.check_box.CheckBoxModule;
import com.infilos.mantou.views.material.check_box.CheckBoxView;
import com.infilos.mantou.views.material.combo_box.ComboBoxesView;
import com.infilos.mantou.views.material.combo_box.ComboBoxModule;
import com.infilos.mantou.views.material.date_picker.DatePickerModule;
import com.infilos.mantou.views.material.date_picker.DatePickerView;
import com.infilos.mantou.views.material.label.LabelsModule;
import com.infilos.mantou.views.material.label.LabelsView;
import com.infilos.mantou.views.material.text_field.TextFieldsModule;
import com.infilos.mantou.views.material.text_field.TextFieldsView;
import com.infilos.mantou.views.timestamp.TimestampModule;
import com.infilos.mantou.views.timestamp.TimestampView;

import javax.inject.*;

@Singleton
public class ViewFactory {

    @Inject
    private Provider<ButtonsView> buttonsViewProvider;

    @Inject
    private Provider<ButtonsModule> buttonsModuleProvider;

    //@Inject
    //private Provider<CheckBoxView> checkBoxViewProvider;
    //
    //@Inject
    //private Provider<CheckBoxModule> checkBoxModuleProvider;

    @Inject
    private Provider<DatePickerView> datePickerViewProvider;

    @Inject
    private Provider<DatePickerModule> datePickerModuleProvider;
    
    @Inject
    private Provider<TextFieldsView> textFieldsViewProvider;
    
    @Inject
    private Provider<TextFieldsModule> textFieldsModuleProvider;
    
    @Inject
    private Provider<ComboBoxesView> comboBoxesViewProvider;
    
    @Inject
    private Provider<ComboBoxModule> comboBoxModuleProvider;
    
    @Inject
    private Provider<LabelsView> labelsViewProvider;
    
    @Inject
    private Provider<LabelsModule> labelsModuleProvider;

    @Inject
    private Provider<TimestampView> timestampViewProvider;

    @Inject
    private Provider<TimestampModule> timestampModuleProvider;
    
    //default <M> View<M> create() {
    //    
    //}
}
