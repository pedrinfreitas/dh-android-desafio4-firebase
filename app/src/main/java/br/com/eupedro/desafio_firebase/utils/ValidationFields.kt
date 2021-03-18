package br.com.eupedro.desafio_firebase.utils

import br.com.eupedro.desafio_firebase.R
import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.validateRequiredField(field: Int): Boolean {
    val value = this.editText?.text.toString()
    if(value.isBlank()) {
        this.error = this.context.getString(R.string.campo_obrigatorio,
            this.context.getString(field))
        return false
    }
    this.isErrorEnabled = false
    return true
}

fun TextInputLayout.validateFieldValue(errorMsg: String, valueToCompare: String): Boolean {
    val value = this.editText?.text.toString()
    if(value.isBlank() || valueToCompare.isBlank()) {
        return false
    }
    if(!value.equals(valueToCompare)) {
        this.error = errorMsg
        return false
    }
    this.isErrorEnabled = false
    return true
}

fun TextInputLayout.validateEmailValue(): Boolean {
    val value = this.editText?.text.toString()
    if(value.isBlank())
        return false
    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
        this.error = context.getString(R.string.email_invalido)
        return false
    }
    this.isErrorEnabled = false
    return true
}