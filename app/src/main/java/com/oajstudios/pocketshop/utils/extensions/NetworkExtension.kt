package com.oajstudios.pocketshop.utils.extensions

import android.app.Activity
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.oajstudios.pocketshop.WooBoxApp.Companion.getAppInstance
import com.oajstudios.pocketshop.WooBoxApp.Companion.restApiImpl
import com.oajstudios.pocketshop.AppBaseActivity
import com.oajstudios.pocketshop.BuildConfig
import com.oajstudios.pocketshop.R
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.network.RestApiImpl
import com.oajstudios.pocketshop.utils.Constants
import com.oajstudios.pocketshop.utils.Constants.SharedPref.APPURL
import com.oajstudios.pocketshop.utils.Constants.SharedPref.IS_LOGGED_IN
import com.oajstudios.pocketshop.utils.Constants.SharedPref.IS_SOCIAL_LOGIN
import com.oajstudios.pocketshop.utils.Constants.SharedPref.KEY_WISHLIST_COUNT
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_DISPLAY_NAME
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_EMAIL
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_FIRST_NAME
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_ID
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_LAST_NAME
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_NICE_NAME
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_PROFILE
import com.oajstudios.pocketshop.utils.Constants.SharedPref.USER_TOKEN
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


fun bodyToString(request: Request?): String {
    try {
        val buffer = Buffer()
        if (request!!.body != null) {
            request.body!!.writeTo(buffer)
        } else
            return ""
        return buffer.readUtf8()
    } catch (e: Exception) {
        return "Request Body is Null"
    }
}


fun getJsonMsg(errorBody: ResponseBody): String {
    try {
        val jsonObject = JSONObject(errorBody.string().getHtmlString().toString())
        Log.d("api_", jsonObject.toString())
        return if (jsonObject.has("message")) {
            (jsonObject.getString("message"))
        } else {
            getAppInstance().getString(R.string.error_something_went_wrong)
        }
    } catch (e: JSONException) {
        if (BuildConfig.DEBUG) {
            return e.toString()
        }
        e.printStackTrace()
    }
    return getAppInstance().getString(R.string.error_something_went_wrong)
}

fun logData(request: Request, response: String, time: Long = 0L, isError: Boolean = false) {
    try {
        Log.d("api_headers", Gson().toJson(request.headers))
        Log.d("api_response_arrived in", (time / 1000L).toString() + " second")
        Log.d("api_url", request.url.toString())
        Log.d("api_request", bodyToString(request))
        if (isError) {
            Log.e("api_response", response)
        } else {
            Log.d("api_response", response)
        }
        Log.d("api_", "------------------")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//fun getRestApis(useSignature: Boolean = true): RestApis {
//    return RetrofitClientFactory().getRetroFitClient(useSignature).create(RestApis::class.java)
//}

fun <T> callApi(
    call: Call<T>,
    onApiSuccess: (T) -> Unit = {},
    onApiError: (aError: String) -> Unit = {},
    onNetworkError: () -> Unit = {},
) {
    Log.d("api_calling", call.request().url.toString() + " " + bodyToString(call.request()))
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        onApiSuccess(body)
                        logData(
                            call.request(),
                            Gson().toJson(body),
                            response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis
                        )
                    } else {
                        onApiError("Please try again later.")
                        logData(
                            call.request(),
                            "Response body is null",
                            response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis,
                            true
                        )
                    }
                }
                else -> {
                    val string = getJsonMsg(response.errorBody()!!)
                    onApiError(string)
                    logData(
                        call.request(),
                        string,
                        response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis,
                        isError = true
                    )
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!isNetworkAvailable()) {
                onNetworkError()
                logData(
                    call.request(),
                    getAppInstance().resources.getString(R.string.error_no_internet),
                    isError = true
                )
            } else {
                if (!BuildConfig.DEBUG) {
                    onApiError("Please try again later.")
                } else {
                    if (t.message != null) {
                        onApiError(t.message!!)
                    }
                }
                if (t.message != null) {
                    logData(call.request(), t.message!!, isError = true)
                }
            }
        }
    })
}

fun <T> successCallback(
    onApiSuccess: (T) -> Unit,
    onApiError: (aError: String) -> Unit,
    response: Response<T>,
    request: Request,
) {
    when {
        response.isSuccessful -> {
            onApiSuccess(response.body()!!)
            logData(
                request,
                Gson().toJson(response.body()),
                response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis
            )
        }
        else -> {
            onApiError(getJsonMsg(response.errorBody()!!))
            logData(
                request,
                Gson().toJson(response.errorBody()),
                response.raw().receivedResponseAtMillis - response.raw().sentRequestAtMillis
            )
        }
        //else -> onApiError(getErrorMessageByHttpCode(response.code()))
    }
}


fun failureCallback(onApiError: (aError: String) -> Unit, request: Request, aError: String) {
    onApiError(aError)
    logData(request, aError)
}


fun ImageView.loadImageFromUrl(
    aImageUrl: String,
    aPlaceHolderImage: Int = R.drawable.placeholder,
    aErrorImage: Int = R.drawable.placeholder,
) {
    if (!aImageUrl.checkIsEmpty()) {
        Glide.with(getAppInstance())
            .load(aImageUrl)
            .placeholder(aPlaceHolderImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(aErrorImage)
            .into(this)
    } else {
        displayBlankImage(getAppInstance(), aPlaceHolderImage)
    }
}


fun getRestApiImpl(url: String = getSharedPrefInstance().getStringValue(APPURL)): RestApiImpl {
    return if (restApiImpl == null) {
        restApiImpl = RestApiImpl(url)
        RestApiImpl(url)
    } else {
        restApiImpl!!
    }
}

fun AppBaseActivity.listReview(id:Int, onApiSuccess: (ArrayList<ProductReviewData>) -> Unit) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().listReview(id, onApiSuccess = {
            showProgress(false)
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    }
}

fun AppBaseActivity.listCoupons(onApiSuccess: (ArrayList<Coupons>) -> Unit) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().listCoupons(onApiSuccess = {
            showProgress(false)
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    }
}

fun AppBaseActivity.createCustomer(
    requestModel: RequestModel,
    onApiSuccess: (RegisterResponse) -> Unit,
) {
    showProgress(true)
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().CreateCustomer(requestModel, onApiSuccess = {
            showProgress(false)
            if (it.code == 200) {
                getSharedPrefInstance().setValue(USER_DISPLAY_NAME, it.data.first_name+it.data.last_name)
                getSharedPrefInstance().setValue(USER_EMAIL, it.data.user_email)
                getSharedPrefInstance().setValue(USER_FIRST_NAME, it.data.first_name)
                getSharedPrefInstance().setValue(USER_LAST_NAME, it.data.last_name)
                onApiSuccess(it)
                sendProfileUpdateBroadcast()
            } else {
                snackBar(it.message)
            }
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    } else {
        showProgress(false)
        snackBarError(getString(R.string.error_no_internet))
    }

}

fun AppBaseActivity.updateCustomer(
    requestModel: RequestModel,
    onApiSuccess: (CustomerData) -> Unit,
) {
    showProgress(true)
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().updateCustomer(requestModel, onApiSuccess = {
            showProgress(false)
            getSharedPrefInstance().setValue(USER_DISPLAY_NAME, it.username)
            getSharedPrefInstance().setValue(USER_EMAIL, it.email)
            getSharedPrefInstance().setValue(USER_FIRST_NAME, it.first_name)
            getSharedPrefInstance().setValue(USER_LAST_NAME, it.last_name)
            sendProfileUpdateBroadcast()
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    } else {
        showProgress(false)
        snackBarError(getString(R.string.error_no_internet))
    }
}

fun AppBaseActivity.signIn(
    email: String,
    password: String,
    onError: (String) -> Unit,
    onApiSuccess: (loginResponse) -> Unit,
) {
    val requestModel = RequestModel()
    requestModel.username = email
    requestModel.password = password
    showProgress(true)
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().Login(requestModel, onApiSuccess = {
            showProgress(false)
            saveLoginResponse(it, false, password, onError)
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            onError(it)
        })
    }
    else
    {
        showProgress(false)
        snackBarError(getString(R.string.error_no_internet))
    }
}

fun AppBaseActivity.saveLoginResponse(
    it: loginResponse,
    isSocialLogin: Boolean,
    password: String,
    onError: (String) -> Unit,
) {
    if (it.user_role.isNotEmpty()) {
        if (it.user_role[0] == "administrator") {
            showProgress(false)
            onError("Admin is not allowed")
        } else {
            getSharedPrefInstance().setValue(USER_ID, it.user_id.toString())
            getSharedPrefInstance().setValue(USER_DISPLAY_NAME, it.user_display_name)
            getSharedPrefInstance().setValue(USER_FIRST_NAME, it.first_name)
            getSharedPrefInstance().setValue(USER_LAST_NAME, it.last_name)
            getSharedPrefInstance().setValue(USER_EMAIL, it.user_email)
            getSharedPrefInstance().setValue(USER_NICE_NAME, it.user_nicename)
            getSharedPrefInstance().setValue(USER_TOKEN, it.token)
            if (it.woobox_profile_image.isNotEmpty()) {
                getSharedPrefInstance().setValue(USER_PROFILE, it.woobox_profile_image)
            }
            getSharedPrefInstance().setValue(IS_SOCIAL_LOGIN, isSocialLogin)
            getSharedPrefInstance().setValue(Constants.SharedPref.USER_PASSWORD, password)
            getSharedPrefInstance().setValue(
                Constants.SharedPref.BILLING,
                Gson().toJson(it.billing)
            )
            getSharedPrefInstance().setValue(
                Constants.SharedPref.SHIPPING,
                Gson().toJson(it.shipping)
            )
            getSharedPrefInstance().setValue(IS_LOGGED_IN, true)
        }
    }
}

fun AppBaseActivity.addItemToCart(
    requestModel: RequestModel,
    onApiSuccess: (AddCartResponse) -> Unit,
) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().addItemToCart(request = requestModel, onApiSuccess = {
            showProgress(false)
            snackBar(getString(R.string.success_add))
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    } else {
        noInternetSnackBar()
    }
}

fun AppBaseActivity.removeCartItem(
    requestModel: RequestModel,
    onApiSuccess: (BaseResponse) -> Unit,
) {
    getAlertDialog(getString(R.string.msg_confirmation), onPositiveClick = { dialog, i ->
        showProgress(true)
        if (isNetworkAvailable()) {
            getRestApiImpl().removeCartItem(requestModel, onApiSuccess = {
                showProgress(false)
                snackBar(getString(R.string.success))
                onApiSuccess(it)
            },
                onApiError = {
                    showProgress(false)
                    snackBarError(it)
                })
        } else {
            showProgress(false)
            noInternetSnackBar()
        }
    }, onNegativeClick = { dialog, i ->
        dialog.dismiss()
    }).show()
}


fun AppBaseActivity.removeMultipleCartItem(
    requestModel: CartRequestModel,
    onApiSuccess: (BaseResponse) -> Unit,
) {
    getRestApiImpl().removeMultipleCartItem(requestModel, onApiSuccess = {
        showProgress(false)
        onApiSuccess(it)
    },
        onApiError = {
            showProgress(false)
            snackBarError(it)
        })
}

fun AppBaseActivity.removeFromWishList(requestModel: RequestModel, onSuccess: (Boolean) -> Unit) {
    showProgress(true)
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().removeWishList(request = requestModel, onApiSuccess = {
            showProgress(false)
            onSuccess(true)
        }, onApiError = {
            snackBarError(it); fetchAndStoreWishListData(onApiSuccess = {}); onSuccess(false)
        })
    } else {
        showProgress(false)
        onSuccess(false)
        noInternetSnackBar()
    }

}

fun AppBaseActivity.addToWishList(requestModel: RequestModel, onSuccess: (Boolean) -> Unit) {
    showProgress(true)
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().addWishList(request = requestModel, onApiSuccess = {
            showProgress(false)
            onSuccess(true)
        }, onApiError = {
            snackBarError(it); fetchAndStoreWishListData(onApiSuccess = {}); onSuccess(false)
        })
    } else {
        showProgress(false)
        onSuccess(false)
        noInternetSnackBar()
    }

}

fun AppBaseActivity.fetchAndStoreWishListData(
    onApiSuccess: (ArrayList<WishList>) -> Unit,
) {
    getRestApiImpl().getWishList(onApiSuccess = {
        showProgress(false)
        getSharedPrefInstance().setValue(KEY_WISHLIST_COUNT, it.size)
        sendWishlistBroadcast()
        onApiSuccess(it)

    }, onApiError = {
        if (it == "no product available") {
            getSharedPrefInstance().setValue(KEY_WISHLIST_COUNT, 0)
            sendWishlistBroadcast()
        }
    })

}


fun AppBaseActivity.getOrder(onApiSuccess: (ArrayList<Order>) -> Unit) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().getOrderData(onApiSuccess = {
            showProgress(false)
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    } else {
        showProgress(false)
        noInternetSnackBar()
    }
}


fun AppBaseActivity.getOrderTracking(id: Int, onApiSuccess: (ArrayList<OrderNotes>) -> Unit) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().getOrderTracking(id, onApiSuccess = {
            showProgress(false)
            onApiSuccess(it)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
        })
    } else {
        showProgress(false)
        noInternetSnackBar()
    }
}


fun AppBaseActivity.saveProfileImage(requestModel: File, onApiSuccess: (Boolean) -> Unit) {
    if (isNetworkAvailable()) {
        showProgress(true)
        getRestApiImpl().saveProfileImage( onApiSuccess = {
            showProgress(false)
            getSharedPrefInstance().removeKey(USER_PROFILE)
            getSharedPrefInstance().setValue(USER_PROFILE, it.woobox_profile_image)
            sendProfileUpdateBroadcast()
            setResult(Activity.RESULT_OK)
            finish()
            onApiSuccess(true)
        }, onApiError = {
            showProgress(false)
            snackBar(it)
            onApiSuccess(false)
        },file = requestModel )
    } else {
        showProgress(false)
        onApiSuccess(false)
        noInternetSnackBar()
    }
}