package com.oajstudios.pocketshop.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.fragments.BaseFragment
import com.oajstudios.pocketshop.models.RequestModel
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.toolbar.*

class ChangePwdActivity : AppBaseActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd)
        setToolbar(toolbar)
        title = getString(R.string.lbl_change_pwd)
        mAppBarColor()
        changeColor()
        edtOldPwd.transformationMethod = BaseFragment.BiggerDotTransformation
        edtNewPwd.transformationMethod = BaseFragment.BiggerDotTransformation
        edtConfirmPwd.transformationMethod = BaseFragment.BiggerDotTransformation
        btnChangePassword.onClick {
            when {
                edtOldPwd.checkIsEmpty() -> {
                    edtOldPwd.showError(getString(R.string.error_field_required))
                }
                edtNewPwd.checkIsEmpty() -> {
                    edtNewPwd.showError(getString(R.string.error_field_required))
                }
                edtNewPwd.validPassword() -> {
                    edtNewPwd.showError(getString(R.string.error_pwd_digit_required))
                }
                edtConfirmPwd.checkIsEmpty() -> {
                    edtConfirmPwd.showError(getString(R.string.error_field_required))
                }
                edtConfirmPwd.validPassword() -> {
                    edtConfirmPwd.showError(getString(R.string.error_pwd_digit_required))
                }
                !edtConfirmPwd.text.toString().equals(
                    edtNewPwd.text.toString(),
                    false
                ) -> {
                    edtConfirmPwd.showError(getString(R.string.error_password_not_matches))
                }
                else -> {
                    val requestModel = RequestModel()
                    requestModel.old_password = edtOldPwd.text.toString()
                    requestModel.new_password = edtNewPwd.text.toString()
                    requestModel.username = getUserName()
                    showProgress(true)
                    if (isNetworkAvailable()) {
                        showProgress(true)
                        getRestApiImpl().changePwd(requestModel, onApiSuccess = {
                            showProgress(false)
                            snackBar(it.message!!)
                        }, onApiError = {
                            showProgress(false)
                            snackBar(it)
                        })
                    } else {
                        showProgress(false)
                        noInternetSnackBar()
                    }

                }
            }
        }
    }

    private fun changeColor() {
        btnChangePassword.changeBackgroundTint(getButtonColor())
        edtOldPwd.changeTextPrimaryColor()
        edtNewPwd.changeTextPrimaryColor()
        edtConfirmPwd.changeTextPrimaryColor()
        llMain.changeBackgroundColor()
    }
}
