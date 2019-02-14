package com.example.yervand.edittextex.viewmodel

import android.util.Patterns
import com.baidu.unbiz.fluentvalidator.ValidatorContext
import com.example.yervand.edittextex.viewmodel.base.implementation.BaseValidationRules
import com.example.yervand.edittextex.viewmodel.base.implementation.BaseViewModelValidator

class MainValidator(viewModel: MainViewModel) : BaseViewModelValidator() {
    init {
        addRule(viewModel.email, String::class.java, object : BaseValidationRules<String>() {
            override fun validate(context: ValidatorContext, s: String?): Boolean {
                clear()
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    addMessage("Email required")
                }
                return super.validate(context, s)
            }
        })
        addRule(viewModel.password, String::class.java, object : BaseValidationRules<String>() {
            override fun validate(context: ValidatorContext, s: String?): Boolean {
                clear()
                if (s == null || s.isEmpty()) {
                    addMessage("Password required")
                }
                return super.validate(context, s)
            }
        })
    }
}