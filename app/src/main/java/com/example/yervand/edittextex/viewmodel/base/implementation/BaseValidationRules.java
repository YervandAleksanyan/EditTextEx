package com.example.yervand.edittextex.viewmodel.base.implementation;

import android.text.TextUtils;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

import java.util.ArrayList;

public abstract class BaseValidationRules<T> extends ValidatorHandler<T> implements Validator<T> {

    private ArrayList<String> errorMessages = new ArrayList<>();
    private int n = 0;


    public void addMessage(String validationErrorMessage) {
        boolean found = false;
        for (String message : errorMessages) {
            if (message.equals(validationErrorMessage)) {
                found = true;
                break;
            }
        }


        if (!found) {
            errorMessages.add(validationErrorMessage);
        }
    }

    public void clear() {
        errorMessages = new ArrayList<>();
    }


    @Override
    public boolean validate(ValidatorContext context, T t) {

        if (errorMessages.size() == 0) {

            return true;
        }
        String message = TextUtils.join(" ", errorMessages);
        context.addErrorMsg(message);
        return false;
    }
}
