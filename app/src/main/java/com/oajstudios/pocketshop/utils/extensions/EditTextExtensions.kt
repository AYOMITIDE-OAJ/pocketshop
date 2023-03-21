package com.oajstudios.pocketshop.utils.extensions

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText

fun EditText.textToString(): String = this.text.toString()

fun EditText.checkIsEmpty(): Boolean = text == null || "" == textToString() || text.toString().equals("null", ignoreCase = true)

fun EditText.isValidEmail(): Boolean = !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches()

fun EditText.isValidPhoneNumber(): Boolean = text.matches("^(((\\+?\\(91\\))|0|((00|\\+)?91))-?)?[7-9]\\d{9}$".toRegex())

fun EditText.showError(error: String) {
    this.error = error
    this.showSoftKeyboard()
}

fun EditText.validPassword(): Boolean = text.length < 6

fun EditText.isValidText(): Boolean = text.matches("[a-zA-Z]+".toRegex())
