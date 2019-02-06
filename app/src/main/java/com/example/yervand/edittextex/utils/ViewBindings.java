package com.example.yervand.edittextex.utils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import com.google.android.material.textfield.TextInputLayout;

public class ViewBindings {
    @BindingAdapter(value = {"errors", "property"})
    public static void setValidationProperties(TextInputLayout textInputLayout,
                                               ObservableArrayMap<ObservableField, String> errors,
                                               ObservableField property) {

        if (!errors.containsKey(property)) {
            textInputLayout.setError("");
            return;
        }
        String error = errors.get(property);
        if (error != null) {
            textInputLayout.setError(error);
        }
    }
}
