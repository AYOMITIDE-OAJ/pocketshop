package com.oajstudios.pocketshop.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.oajstudios.pocketshop.utils.extensions.hideSoftKeyboard

class OTPEditText(val mEditTextList: Array<EditText?>, val context: FragmentActivity,val transaparant:Drawable,val dot:Drawable) {
    init {
        mEditTextList.forEachIndexed { index, editText ->
            editText?.setOnKeyListener(PinOnKeyListener(index))
            editText?.addTextChangedListener(CodeTextWatcher(index))
            editText?.onFocusChangeListener = focusChangeListener

        }
    }

    private val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus)
            (v as EditText).background = transaparant
    }

    inner class PinOnKeyListener internal constructor(private val mCurrentIndex: Int) : View.OnKeyListener {

        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (mEditTextList[mCurrentIndex]?.text.toString().isEmpty() && mCurrentIndex != 0) {
                    mEditTextList[mCurrentIndex - 1]?.requestFocus()
                }
            }
            return false
        }
    }

    inner class CodeTextWatcher internal constructor(private val aCurrentIndex: Int) : TextWatcher {
        private var mIsFirst = false
        private var mIsLast = false
        private var mNewString = ""


        init {
            if (aCurrentIndex == 0)
                this.mIsFirst = true
            else if (aCurrentIndex == mEditTextList.size - 1)
                this.mIsLast = true
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            mNewString = s.subSequence(start, start + count).toString().trim { it <= ' ' }
        }

        @SuppressLint("ResourceType")
        override fun afterTextChanged(s: Editable) {
            var text = mNewString
            if (text.length > 1) {
                text = text[0].toString()
            }
            mEditTextList[aCurrentIndex]?.removeTextChangedListener(this)
            mEditTextList[aCurrentIndex]?.setText(text)
            mEditTextList[aCurrentIndex]?.setSelection(text.length)
            mEditTextList[aCurrentIndex]?.addTextChangedListener(this)
            if (text.length == 1) {
                moveToNext()
            } else if (text.isEmpty()) {
                moveToPrevious()
            }
        }

        private val isAllEditTextsFilled: Boolean
            get() {
                mEditTextList.forEach { editText: EditText? ->
                    if (editText?.text.toString().isEmpty())
                        return false
                }
                return true
            }

        private fun moveToNext() {
            if (!mIsLast) {
                mEditTextList[aCurrentIndex + 1]?.requestFocus()
            }

            if (isAllEditTextsFilled && mIsLast) {
                mEditTextList[aCurrentIndex]?.clearFocus()
                (context as Activity).hideSoftKeyboard()
            }
        }

        private fun moveToPrevious() {
            if (!mIsFirst) {

                mEditTextList[aCurrentIndex - 1]?.requestFocus()
            }
            mEditTextList[aCurrentIndex]!!.background = dot
            if (mIsFirst) {
                mEditTextList[aCurrentIndex]!!.background = transaparant
            }
        }
    }
}