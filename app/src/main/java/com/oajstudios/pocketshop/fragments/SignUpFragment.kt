package com.oajstudios.pocketshop.fragments

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.activity.SignInUpActivity
import com.oajstudios.pocketshop.models.RequestModel
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.backButton
import kotlinx.android.synthetic.main.activity_sign_up.btnSignIn
import kotlinx.android.synthetic.main.activity_sign_up.btnSignUp
import kotlinx.android.synthetic.main.activity_sign_up.edtEmail
import kotlinx.android.synthetic.main.activity_sign_up.edtPassword
import kotlinx.android.synthetic.main.activity_sign_up.ivPwd

class SignUpFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.activity_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment() {
        changeColor()
        btnSignUp.onClick {
            when {
                validate() -> {
                    createCustomer()
                }
            }
        }
        btnSignIn.onClick {
            (activity as SignInUpActivity).loadSignInFragment()
        }
        backButton.onClick {
            (activity as SignInUpActivity).finish()
        }

        ivPwd.onClick {
            if (edtPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                ivPwd.setImageResource(R.drawable.ic_eye_line)
                edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                edtPassword.setSelection(edtPassword.text.length)
            } else {
                ivPwd.setImageResource(R.drawable.ic_eye_off_line)
                edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                edtPassword.setSelection(edtPassword.text.length)
            }
        }
        ivConfirmPwd.onClick {
            if (edtConfirmPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                ivConfirmPwd.setImageResource(R.drawable.ic_eye_line)
                edtConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                edtConfirmPassword.setSelection(edtConfirmPassword.text.length)
            } else {
                ivConfirmPwd.setImageResource(R.drawable.ic_eye_off_line)
                edtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                edtConfirmPassword.setSelection(edtConfirmPassword.text.length)
            }
        }
    }

    private fun createCustomer() {
        val requestModel = RequestModel()
        requestModel.user_email = edtEmail.textToString()
        requestModel.firstName = edtFirstName.textToString()
        requestModel.lastName = edtLastName.textToString()
        requestModel.user_login = edtUsername.textToString()
        requestModel.user_pass = edtPassword.textToString()

        (requireActivity() as AppBaseActivity).createCustomer(requestModel) {
            snackBar(it.message)
            (activity as SignInUpActivity).loadSignInFragment()
        }
    }

    private fun validate(): Boolean {
        return when {
            edtFirstName.checkIsEmpty() -> {
                edtFirstName.showError(getString(R.string.error_field_required))
                false
            }
            !edtFirstName.isValidText() -> {
                edtFirstName.showError(getString(R.string.error_validText))
                false
            }
            edtLastName.checkIsEmpty() -> {
                edtLastName.showError(getString(R.string.error_field_required))
                false
            }
            !edtLastName.isValidText() -> {
                edtFirstName.showError(getString(R.string.error_validText))
                false
            }
            edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.error_field_required))
                false
            }
            !edtEmail.isValidEmail() -> {
                edtEmail.showError(getString(R.string.error_enter_valid_email))
                false
            }
            edtPassword.checkIsEmpty() -> {
                edtPassword.showError(getString(R.string.error_field_required))
                false
            }
            edtPassword.validPassword() -> {
                edtPassword.showError(getString(R.string.error_pwd_digit_required))
                false
            }
            edtConfirmPassword.checkIsEmpty() -> {
                edtConfirmPassword.showError(getString(R.string.error_field_required))
                false
            }
            !edtPassword.text.toString().equals(edtConfirmPassword.text.toString(), false) -> {
                edtConfirmPassword.showError(getString(R.string.error_password_not_matches))
                false
            }
            else -> true
        }
    }

    private fun changeColor() {
        ivBackground.changeBackgroundImageTint(getPrimaryColor())
        btnSignUp.changeBackgroundTint(getButtonColor())
        lblAlreadyAccount.changeTextPrimaryColor()
        btnSignIn.changePrimaryColorDark()
        edtEmail.changeTextPrimaryColor()
        edtFirstName.changeTextPrimaryColor()
        edtLastName.changeTextPrimaryColor()
        edtUsername.changeTextPrimaryColor()
        edtPassword.changeTextPrimaryColor()
        edtConfirmPassword.changeTextPrimaryColor()
        rlMain.changeBackgroundColor()
    }
}