package com.example.yervand.edittextex.utils;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.databinding.*;
import androidx.databinding.adapters.ListenerUtil;
import com.example.yervand.edittextex.EditTextEx;
import com.example.yervand.edittextex.R;

public class ViewBindings {
    @BindingAdapter(value = {"errors", "property"})
    public static void setValidationProperties(EditTextEx textInputLayout,
                                               ObservableArrayMap<ObservableField, String> errors,
                                               ObservableField property) {

        if (!errors.containsKey(property)) {
            textInputLayout.setFloatHintText("");
            return;
        }
        String error = errors.get(property);
        if (error != null) {
            textInputLayout.setFloatHintText(error);
        }
    }

    @BindingAdapter(value = {"bindableText", "textChanged"}, requireAll = false)
    public static void getBindableText(EditTextEx editTextEx, String message, final InverseBindingListener listener) {
        if (editTextEx.getText().equals(message)) return;
        editTextEx.setText(message);
        editTextEx.getEditText().setSelection(editTextEx.getText().length());
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        TextWatcher oldValue = ListenerUtil.trackListener(editTextEx, watcher, R.id.textWatcher);
        if (oldValue != null) {
            editTextEx.getEditText().removeTextChangedListener(oldValue);
        }
        editTextEx.getEditText().addTextChangedListener(watcher);
    }

    @InverseBindingAdapter(attribute = "bindableText", event = "textChanged")
    public static String setBindableText(EditTextEx editTextEx) {
        return editTextEx.getText();
    }
}
