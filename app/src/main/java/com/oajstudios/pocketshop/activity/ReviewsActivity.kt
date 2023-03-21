package com.oajstudios.pocketshop.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.adapter.BaseAdapter
import com.oajstudios.pocketshop.models.ProductReviewData
import com.oajstudios.pocketshop.models.RequestModel
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.extensions.*
import kotlinx.android.synthetic.main.activity_reviews.*
import kotlinx.android.synthetic.main.dialog_rate.*
import kotlinx.android.synthetic.main.item_review.view.*
import kotlinx.android.synthetic.main.layout_nodata.*
import kotlinx.android.synthetic.main.toolbar.*

class ReviewsActivity : AppBaseActivity() {
    private var mPId: Int = 0
    private var mIsCheckUser: Boolean = false

    private val mReviewAdapter =
        BaseAdapter<ProductReviewData>(R.layout.item_review, onBind = { view, model, _ ->
            view.tvProductReviewRating.text = model.rating.toString()
            view.tvProductReviewSubHeading.text = model.review.getHtmlString()
            view.tvProductReviewCmt.text = model.name

            view.tvProductReviewDuration.text = convertToLocalDate(model.date_created)
            if (model.rating == 1) {
                view.tvProductReviewRating.changeBackgroundTint(color(R.color.red))
            }
            if (model.rating == 2 || model.rating == 3) {
                view.tvProductReviewRating.changeBackgroundTint(color(R.color.yellow))
            }
            if (model.rating == 5 || model.rating == 4) {
                view.tvProductReviewRating.changeBackgroundTint(color(R.color.green))
            }

            view.ivMenu.onClick {
                val popup = PopupMenu(this@ReviewsActivity, view.ivMenu)
                popup.menuInflater.inflate(R.menu.menu_review, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item!!.itemId) {
                        R.id.nav_delete -> confirmDialog(model)

                        R.id.nav_update -> updateReview(model)
                    }
                    true
                }
                popup.show()
            }

            if (model.email == getEmail()) {
                view.ivMenu.show()
                model.isExist = true
                mIsCheckUser = true
            } else {
                view.ivMenu.hide()
                model.isExist = false
                mIsCheckUser = false
            }
            view.tvProductReviewCmt.changeTextPrimaryColor()
            view.tvProductReviewSubHeading.changeTextSecondaryColor()
            view.tvProductReviewDuration.changeTextSecondaryColor()
        })

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)
        setToolbar(toolbar)
        mAppBarColor()
        title = getString(R.string.lbl_reviews)
        changeColor()
        mPId = intent.getIntExtra(Constants.KeyIntent.PRODUCT_ID, 0)
        ivBackground.setStrokedBackground(
            color(R.color.favourite_unselected_background),
            color(R.color.dots_color)
        )
        rvReview.setVerticalLayout()
        rvReview.adapter = mReviewAdapter
        listProductReviews()
        btnRateNow.onClick {
            if (isLoggedIn()) {
                createProductReview()
            } else {
                launchActivity<SignInUpActivity>()
            }
        }
        sb1Star.setOnTouchListener { _, _ -> true }
        sb2Star.setOnTouchListener { _, _ -> true }
        sb3Star.setOnTouchListener { _, _ -> true }
        sb4Star.setOnTouchListener { _, _ -> true }
        sb5Star.setOnTouchListener { _, _ -> true }
    }

    private fun checkBtnOption() {
        if (mIsCheckUser) {
            btnRateNow.hide()
        } else {
            btnRateNow.show()
        }
    }

    private fun setRating(data: List<ProductReviewData>) {
        if (data.isEmpty()) {
            tvReviewRate.hide()
            tvTotalReview.text = getString(R.string.lbl_no_reviews)
            sb1Star.progress = 0
            sb2Star.progress = 0
            sb3Star.progress = 0
            sb4Star.progress = 0
            sb5Star.progress = 0
            tv5Count.text = "0"
            tv4Count.text = "0"
            tv3Count.text = "0"
            tv2Count.text = "0"
            tv1Count.text = "0"
            return
        }
        var fiveStar = 0
        var fourStar = 0
        var threeStar = 0
        var twoStar = 0
        var oneStar = 0
        for (reviewModel in data) {
            when (reviewModel.rating) {
                5 -> fiveStar++
                4 -> fourStar++
                3 -> threeStar++
                2 -> twoStar++
                1 -> oneStar++
            }
        }

        if (fiveStar == 0 && fourStar == 0 && threeStar == 0 && twoStar == 0 && oneStar == 0) {
            return
        }
        sb1Star.max = data.size
        sb2Star.max = data.size
        sb3Star.max = data.size
        sb4Star.max = data.size
        sb5Star.max = data.size

        sb1Star.progress = oneStar
        sb2Star.progress = twoStar
        sb3Star.progress = threeStar
        sb4Star.progress = fourStar
        sb5Star.progress = fiveStar
        tvTotalReview.text = String.format("%d " + getString(R.string.lbl_reviews), data.size)
        tv5Count.text = fiveStar.toString()
        tv4Count.text = fourStar.toString()
        tv3Count.text = threeStar.toString()
        tv2Count.text = twoStar.toString()
        tv1Count.text = oneStar.toString()

        tvReviewRate.show()

        val mAvgRating =
            (5 * fiveStar + 4 * fourStar + 3 * threeStar + 2 * twoStar + 1 * oneStar) / (fiveStar + fourStar + threeStar + twoStar + oneStar)
        tvReviewRate.text = mAvgRating.toString()
    }

    private fun createProductReview() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.setContentView(R.layout.dialog_rate)
        dialog.setCanceledOnTouchOutside(false)

        dialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.lblReview.changeTextPrimaryColor()
        dialog.TIHint.hintTextColor = ColorStateList.valueOf(Color.parseColor(getPrimaryColor()))
        dialog.edtReview.changeTextPrimaryColor()
        dialog.btnSubmit.changeBackgroundTint(getButtonColor())

        dialog.btnSubmit.onClick {
            if (dialog.edtReview.textToString().isNotEmpty()) {
                val requestModel = RequestModel()
                requestModel.product_id = mPId
                requestModel.reviewer = getFirstName() + " " + getLastName()
                requestModel.reviewer_email = getEmail()
                requestModel.review = dialog.edtReview.textToString()
                requestModel.rating = dialog.ratingBar.rating.toString()

                if (isNetworkAvailable()) {
                    showProgress(true)
                    getRestApiImpl().createProductReview(requestModel, onApiSuccess = {
                        showProgress(false)
                        toast(R.string.success_add)
                        dialog.dismiss()
                        mIsCheckUser = true
                        listProductReviews()
                        checkBtnOption()
                    }, onApiError = {
                        showProgress(false)
                        dialog.dismiss()
                        snackBarError(it)
                    })
                } else {
                    showProgress(false)
                    dialog.dismiss()
                    noInternetSnackBar()
                }
            } else {
                toast("Write your review", Toast.LENGTH_SHORT)
            }
        }
        dialog.viewCloseDialog.onClick {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun confirmDialog(mode: ProductReviewData) {
        getAlertDialog(getString(R.string.msg_confirmation), onPositiveClick = { dialog, _ ->
            if (isNetworkAvailable()) {
                showProgress(true)
                getRestApiImpl().DeleteProductReview(mode.id, onApiSuccess = {
                    showProgress(false)
                    snackBar(getString(R.string.success))
                    mIsCheckUser = false
                    listProductReviews()
                    checkBtnOption()
                }, onApiError = {
                    showProgress(false)
                    snackBarError(it)
                })
            } else {
                showProgress(false)
                dialog.dismiss()
                noInternetSnackBar()
            }
        }, onNegativeClick = { dialog, _ ->
            dialog.dismiss()
        }).show()
    }

    //
    private fun updateReview(mode: ProductReviewData) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.setContentView(R.layout.dialog_rate)

        dialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.edtReview.setText(mode.review.getHtmlString())
        dialog.ratingBar.rating = mode.rating.toFloat()

        dialog.lblReview.changeTextPrimaryColor()
        dialog.edtReview.changeTextPrimaryColor()
        dialog.btnSubmit.changeBackgroundTint(getButtonColor())
        dialog.viewCloseDialog.onClick {
            dialog.dismiss()
        }
        dialog.btnSubmit.onClick {
            val requestModel = RequestModel()

            requestModel.product_id = mPId
            requestModel.reviewer = getFirstName()
            requestModel.reviewer_email = getEmail()
            requestModel.review = dialog.edtReview.textToString()
            requestModel.rating = dialog.ratingBar.rating.toString()

            if (isNetworkAvailable()) {
                showProgress(true)
                getRestApiImpl().updateProductReview(
                    mode.id,
                    requestModel,
                    onApiSuccess = {
                        showProgress(false)
                        toast(R.string.success_add)
                        dialog.dismiss()
                        listProductReviews()
                    },
                    onApiError = {
                        showProgress(false)
                        dialog.dismiss()
                        snackBarError(it)
                    })
            } else {
                showProgress(false)
                dialog.dismiss()
                noInternetSnackBar()
            }
        }
        dialog.show()
    }

    private fun listProductReviews() {
        showProgress(true)
        listReview(mPId) {
            if (it.isEmpty()) {
                showList(false)
                setRating(it)
            } else {
                showProgress(false)
                it.reverse()
                mReviewAdapter.clearItems()
                mReviewAdapter.addItems(it)
                setRating(it)
                showList(true)
            }
            checkBtnOption()
        }

    }

    private fun showList(isVisible: Boolean) {
        if (isVisible) {
            rlNoData.hide()
            rvReview.show()
            reviewView.show()
            tvLblReview.show()
        } else {
            rvReview.hide()
            tvLblReview.hide()
            rlNoData.show()
            tvMsg.text = getString(R.string.lbl_no_reviews)
        }
    }

    private fun changeColor() {
        tvLblRatings.changeTextPrimaryColor()
        btnRateNow.changePrimaryColorDark()
        tvReviewRate.changeTextPrimaryColor()
        tvTotalReview.changeTextSecondaryColor()
        tvLbl5.changeTextPrimaryColor()
        tv5Count.changeTextSecondaryColor()
        tvLbl4.changeTextPrimaryColor()
        tv4Count.changeTextSecondaryColor()
        tvLbl3.changeTextPrimaryColor()
        tv3Count.changeTextSecondaryColor()
        tvLbl2.changeTextPrimaryColor()
        tv2Count.changeTextSecondaryColor()
        tvLbl1.changeTextPrimaryColor()
        tv1Count.changeTextSecondaryColor()
        tvLblReview.changeTextPrimaryColor()
        llMain.changeBackgroundColor()
        rlNoData.changeBackgroundColor()
    }
}
