package com.example.yervand.edittextex.viewmodel.base;


import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;

import java.util.ArrayList;

public interface ViewModelValidator {
    boolean validate(ObservableField property);

    boolean validateAll();

    ObservableArrayMap<ObservableField, String> getErrors();

    ArrayList<String> getAllErrorsInString();

    String getError(ObservableField property);
}
