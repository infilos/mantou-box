package com.infilos.mantou.api;

import com.infilos.mantou.utils.AwareResource;
import javafx.fxml.Initializable;

public interface WorkView<M> extends Initializable, AwareResource {

    void setModel(M model);

    M getModel();
}
