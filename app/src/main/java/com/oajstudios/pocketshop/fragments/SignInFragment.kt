package com.oajstudios.pocketshop.fragments

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.activity.DashBoardActivity
import com.oajstudios.pocketshop.activity.EditProfileActivity
import com.oajstudios.pocketshop.activity.SignInUpActivity
import com.oajstudios.pocketshop.models.RequestModel
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_USERNAME
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.dialog_change_password.*

class SignInFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.activity_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment() {
        edtEmail.setSelection(edtEmail.length())
        changeColor()
        btnSignIn.onClick {
            if (validate()) {
                doLogin()
            }
        }

        tvForget.onClick {
            snackBar(getString(R.string.lbl_coming_soon))
        }

        btnSignUp.onClick {
            (activity as SignInUpActivity).loadSignUpFragment()
        }

        backButton.onClick {

            (activity as SignInUpActivity).finish()
        }

        tvForget.onClick {
            showChangePasswordDialog()
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
    }

    private fun validate(): Boolean {
        return when {
            edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.error_field_required))
                false
            }

            edtPassword.checkIsEmpty() -> {
                edtPassword.showError(getString(R.string.error_field_required))
                false
            }
            else -> true
        }
    }

    private fun doLogin() {
        val mUsername = edtEmail.textToString()
        (activity as AppBaseActivity).signIn(
            edtEmail.textToString(),
            edtPassword.textToString(),
            onApiSuccess = {
                (activity as AppBaseActivity).setResult(Activity.RESULT_OK)
                getSharedPrefInstance().removeKey(USER_USERNAME)
                getSharedPrefInstance().setValue(USER_USERNAME, mUsername)
                Log.d("Username", mUsername)

                if (it.billing.first_name.isEmpty()) {
                    activity?.launchActivity<EditProfileActivity> { }
                } else {
                    activity?.launchActivityWithNewTask<DashBoardActivity>()
                }
                (activity as AppBaseActivity).finish()
            },
            onError = {
                activity?.snackBarError(it)
            })

    }

    private fun showChangePasswordDialog() {
        val changePasswordDialog = Dialog(requireActivity())
        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        changePasswordDialog.setContentView(R.layout.dialog_change_password)
        changePasswordDialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        changePasswordDialog.lblForgotPwd.changeTextPrimaryColor()
        changePasswordDialog.edtForgotEmail.changeTextPrimaryColor()
        changePasswordDialog.btnForgotPassword.changeBackgroundTint(getButtonColor())
        changePasswordDialog.btnForgotPassword.onClick {
            changePasswordDialog.edtForgotEmail.hideSoftKeyboard()
            if (changePasswordDialog.edtForgotEmail.textToString().isEmpty()) {
                changePasswordDialog.edtForgotEmail.showError(getString(R.string.hint_enter_your_email_id))
                return@onClick
            }
            if (!changePasswordDialog.edtForgotEmail.isValidEmail()) {
                changePasswordDialog.edtForgotEmail.showError(getString(R.string.error_enter_valid_email))
                return@onClick
            }


            val requestModel = RequestModel()
            requestModel.email = changePasswordDialog.edtForgotEmail.textToString()

            if (isNetworkAvailable()) {
                showProgress()
                getRestApiImpl().forgetPassword(requestModel, onApiSuccess = {
                    hideProgress()
                    changePasswordDialog.edtForgotEmail.hideSoftKeyboard()
                    (activity as AppBaseActivity).snackBar(it.message ?: "")
                    changePasswordDialog.dismiss()
                }, onApiError = {
                    hideProgress()
                    changePasswordDialog.dismiss()
                    (activity as AppBaseActivity).snackBar(it)
                })
            } else {
                hideProgress()
                snackBarError(getString(R.string.error_no_internet))
            }
        }
        changePasswordDialog.show()
    }

    private fun changeColor() {
        ivBackground.changeBackgroundImageTint(getPrimaryColor())
        btnSignIn.changeBackgroundTint(getButtonColor())
        tvForget.changeTextSecondaryColor()
        lblAccount.changeTextPrimaryColor()
        btnSignUp.changePrimaryColorDark()
        edtEmail.changeTextPrimaryColor()
        edtPassword.changeTextPrimaryColor()
        llMainUI.changeBackgroundColor()
    }

}