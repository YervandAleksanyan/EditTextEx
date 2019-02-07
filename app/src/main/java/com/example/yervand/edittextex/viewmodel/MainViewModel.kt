package com.example.yervand.edittextex.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.yervand.edittextex.viewmodel.base.ValidateableViewModel
import com.example.yervand.edittextex.viewmodel.base.ViewModelValidator

class MainViewModel : ViewModel(),
    ValidateableViewModel {

    private  var validator: ViewModelValidator


    val email: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    init {
        validator = MainValidator(this)
    }

    override fun getValidator(): ViewModelValidator = validator
}