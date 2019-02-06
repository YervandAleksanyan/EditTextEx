package com.example.yervand.edittextex.viewmodel.base.implementation;

import android.util.Log;
import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.example.yervand.edittextex.viewmodel.base.ViewModelValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

public class BaseViewModelValidator implements ViewModelValidator {


    private final String TAG = "ScheduleeValidator";

    private Map<ObservableField, Validator> validationRules = new HashMap<>();

    private ObservableArrayMap<ObservableField, String> errors = new ObservableArrayMap<>();


    private HashMap<ObservableField, Class> propertiesClassMap = new HashMap<>();


    protected void addRule(ObservableField property, Class propertyClass, Validator validator) {
        property.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
                PropertyChanged(sender);
            }
        });


        if (!validationRules.containsKey(property)) {

            if (!propertiesClassMap.containsKey(property)) {
                propertiesClassMap.put(property, propertyClass);
            }

            validationRules.put(property, validator);
        }
    }

    private void PropertyChanged(androidx.databinding.Observable sender) {

        ObservableField property = (ObservableField) sender;
        if (validationRules.size() == 0) {
            Log.e(TAG, "There isn't t any defined validation rule");
            return;
        }
        validate(property);

    }


    @Override
    public boolean validate(ObservableField property) {


        if (errors.containsKey(property)) {
            errors.remove(property);
        }


        if (property.get() == null) {
            Class c = propertiesClassMap.get(property);
            try {
                property.set(c.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Result result = FluentValidator.checkAll().on(property.get(),
                validationRules.get(property)).doValidate().result(toSimple());


        if (!result.isSuccess()) {
            errors.put(property, result.getErrors().get(0));

            return false;
        }
        return true;

    }

    @Override
    public boolean validateAll() {
        boolean isValid = true;
        for (ObservableField property : validationRules.keySet()) {
            boolean result = validate(property);
            if (!result) isValid = false;

        }
        return isValid;
    }


    @Override
    public String getError(ObservableField property) {
        if (errors.containsKey(property)) {
            return errors.get(property);
        }
        return "";
    }

    @Override
    public ObservableArrayMap<ObservableField, String> getErrors() {
        return errors;
    }

    @Override
    public ArrayList<String> getAllErrorsInString() {
        ArrayList<String> res = new ArrayList<>();
        for (ObservableField item : errors.keySet()) {
            res.add(errors.get(item));
        }
        return res;

    }
}

