package com.oajstudios.pocketshop.network

import com.oajstudios.pocketshop.utils.extensions.failureCallback
import com.oajstudios.pocketshop.utils.extensions.successCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.oajstudios.pocketshop.models.*
import com.oajstudios.pocketshop.utils.extensions.getUserId
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class RestApiImpl(s: String) {

    private var getRestApis: RestApis = RetrofitClientFactory(s).getRestApis()

    fun getDashboardData(onApiSuccess: (Dashboard) -> Unit, onApiError: (aError: String) -> Unit) {
        getRestApis.getDashboardData().enqueue(object : Callback<Dashboard> {
            override fun onResponse(call: Call<Dashboard>, response: Response<Dashboard>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<Dashboard>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun listAllProducts(
        onApiSuccess: (ArrayList<StoreProductModel>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listAllProducts().enqueue(object : Callback<ArrayList<StoreProductModel>> {
            override fun onResponse(
                call: Call<ArrayList<StoreProductModel>>,
                response: Response<ArrayList<StoreProductModel>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<StoreProductModel>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun listAllCountry(
        onApiSuccess: (ArrayList<CountryModel>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listCountry().enqueue(object : Callback<ArrayList<CountryModel>> {
            override fun onResponse(
                call: Call<ArrayList<CountryModel>>,
                response: Response<ArrayList<CountryModel>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<CountryModel>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun listAllProductAttribute(
        onApiSuccess: (StoreProductAttribute) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getProductAttribute()
            .enqueue(object : Callback<StoreProductAttribute> {
                override fun onResponse(
                    call: Call<StoreProductAttribute>,
                    response: Response<StoreProductAttribute>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<StoreProductAttribute>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }

    fun productDetail(
        aProductId: Int,
        onApiSuccess: (ArrayList<StoreProductModel>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listSingleProducts(aProductId)
            .enqueue(object : Callback<ArrayList<StoreProductModel>> {
                override fun onResponse(
                    call: Call<ArrayList<StoreProductModel>>,
                    response: Response<ArrayList<StoreProductModel>>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<ArrayList<StoreProductModel>>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }

    fun listAllCategory(
        option: Map<String, Int>,
        onApiSuccess: (ArrayList<Category>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listAllCategory(option).enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun listSingleCategory(
        id: Int,
        onApiSuccess: (ArrayList<StoreProductModel>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listSingleCategory(id).enqueue(object : Callback<ArrayList<StoreProductModel>> {
            override fun onResponse(
                call: Call<ArrayList<StoreProductModel>>,
                response: Response<ArrayList<StoreProductModel>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<StoreProductModel>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun listAllCategoryProduct(
        option: Map<String, Int>,
        onApiSuccess: (ArrayList<StoreProductModel>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listAllCategoryProduct(option).enqueue(object : Callback<ArrayList<StoreProductModel>> {
            override fun onResponse(
                call: Call<ArrayList<StoreProductModel>>,
                response: Response<ArrayList<StoreProductModel>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<StoreProductModel>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun listReview(
        id: Int,
        onApiSuccess: (ArrayList<ProductReviewData>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.listReview(id).enqueue(object : Callback<ArrayList<ProductReviewData>> {
            override fun onResponse(
                call: Call<ArrayList<ProductReviewData>>,
                response: Response<ArrayList<ProductReviewData>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<ProductReviewData>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun createProductReview(
        request: RequestModel,
        onApiSuccess: (ProductReviewData) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.createProductReview(request).enqueue(object : Callback<ProductReviewData> {
            override fun onResponse(
                call: Call<ProductReviewData>,
                response: Response<ProductReviewData>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ProductReviewData>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun DeleteProductReview(
        id: Int,
        onApiSuccess: (ProductReviewData) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.deleteProductReview(id).enqueue(object : Callback<ProductReviewData> {
            override fun onResponse(
                call: Call<ProductReviewData>,
                response: Response<ProductReviewData>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ProductReviewData>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun updateProductReview(
        id: Int,
        request: RequestModel,
        onApiSuccess: (ProductReviewData) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.updateProductReview(id, request).enqueue(object : Callback<ProductReviewData> {
            override fun onResponse(
                call: Call<ProductReviewData>,
                response: Response<ProductReviewData>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ProductReviewData>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun listCoupons(
        onApiSuccess: (ArrayList<Coupons>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.GetCoupon().enqueue(object : Callback<ArrayList<Coupons>> {
            override fun onResponse(
                call: Call<ArrayList<Coupons>>,
                response: Response<ArrayList<Coupons>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<Coupons>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun FindCoupon(
        acode: String,
        onApiSuccess: (ArrayList<Coupons>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.FindCoupon(acode).enqueue(object : Callback<ArrayList<Coupons>> {
            override fun onResponse(
                call: Call<ArrayList<Coupons>>,
                response: Response<ArrayList<Coupons>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<Coupons>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun listSaleProducts(
        option: SearchRequest,
        onApiSuccess: (StoreProductModelNew) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getSaleProducts(option)
            .enqueue(object : Callback<StoreProductModelNew> {
                override fun onResponse(
                    call: Call<StoreProductModelNew>,
                    response: Response<StoreProductModelNew>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<StoreProductModelNew>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }


    fun search(
        options: SearchRequest,
        onApiSuccess: (StoreProductModelNew) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.Search(options).enqueue(object : Callback<StoreProductModelNew> {
            override fun onResponse(
                call: Call<StoreProductModelNew>,
                response: Response<StoreProductModelNew>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<StoreProductModelNew>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun CreateCustomer(
        request: RequestModel,
        onApiSuccess: (RegisterResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.createCustomer(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun Login(
        request: RequestModel,
        onApiSuccess: (loginResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.login(request).enqueue(object : Callback<loginResponse> {
            override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun TokenAPI(
        onApiSuccess: (TokenResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.tokenValidate().enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun retrieveCustomer(
        onApiSuccess: (CustomerData) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.retrieveCustomer().enqueue(object : Callback<CustomerData> {
            override fun onResponse(call: Call<CustomerData>, response: Response<CustomerData>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<CustomerData>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }


    fun updateCustomer(
        request: RequestModel,
        onApiSuccess: (CustomerData) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.updateCustomer(getUserId(), request).enqueue(object : Callback<CustomerData> {
            override fun onResponse(call: Call<CustomerData>, response: Response<CustomerData>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<CustomerData>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun addItemToCart(
        request: RequestModel,
        onApiSuccess: (AddCartResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.addItemToCart(request).enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>,
                response: Response<AddCartResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun removeCartItem(
        request: RequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.removeCartItem(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun addOrderNotes(
        oderID: Int,
        request: CreateOrderNotes,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.createOrderNotes(oderID, request)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }

    fun removeMultipleCartItem(
        request: CartRequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.removeMultipleCartItem(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun getCheckoutUrl(
        request: CheckoutUrlRequest,
        onApiSuccess: (CheckoutResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getCheckoutUrl(request)
            .enqueue(object : Callback<CheckoutResponse> {
                override fun onResponse(
                    call: Call<CheckoutResponse>,
                    response: Response<CheckoutResponse>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }


    fun createOrderRequest(
        request: OrderRequest,
        onApiSuccess: (CreateOrderResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.createOrder(request)
            .enqueue(object : Callback<CreateOrderResponse> {
                override fun onResponse(
                    call: Call<CreateOrderResponse>,
                    response: Response<CreateOrderResponse>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<CreateOrderResponse>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }

    fun getStripeClientSecret(
        request: RequestModel,
        onApiSuccess: (GetStripeClientSecret) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getStripeClientSecret(request)
            .enqueue(object : Callback<GetStripeClientSecret> {
                override fun onResponse(
                    call: Call<GetStripeClientSecret>,
                    response: Response<GetStripeClientSecret>,
                ) {
                    successCallback(onApiSuccess, onApiError, response, call.request())
                }

                override fun onFailure(call: Call<GetStripeClientSecret>, t: Throwable) {
                    failureCallback(onApiError, call.request(), t.toString())
                }
            })
    }
    fun getCart(
        onApiSuccess: (CartResponseModel) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getCart().enqueue(object : Callback<CartResponseModel> {
            override fun onResponse(
                call: Call<CartResponseModel>,
                response: Response<CartResponseModel>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<CartResponseModel>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

  /*  fun getCart(
        onApiSuccess: (ArrayList<CartResponse>) -> Unit,
        onApiError: (aError: String) -> Unit
    ) {
        getRestApis.getCart().enqueue(object : Callback<ArrayList<CartResponse>> {
            override fun onResponse(
                call: Call<ArrayList<CartResponse>>,
                response: Response<ArrayList<CartResponse>>
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<CartResponse>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }*/


    fun updateItemInCart(
        request: RequestModel,
        onApiSuccess: (UpdateCartResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.updateItemInCart(request).enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun addWishList(
        request: RequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.addWishList(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun removeWishList(
        request: RequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.removeWishList(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun getWishList(
        onApiSuccess: (ArrayList<WishList>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getWishList().enqueue(object : Callback<ArrayList<WishList>> {
            override fun onResponse(
                call: Call<ArrayList<WishList>>,
                response: Response<ArrayList<WishList>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<WishList>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun cancelOrder(
        orderId: Int,
        request: CancelOrderRequest,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.cancelOrder(orderId,request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun getOrderData(
        onApiSuccess: (ArrayList<Order>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getOrderData().enqueue(object : Callback<ArrayList<Order>> {
            override fun onResponse(
                call: Call<ArrayList<Order>>,
                response: Response<ArrayList<Order>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun getOrderTracking(
        id: Int,
        onApiSuccess: (ArrayList<OrderNotes>) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getOrderTracking(id).enqueue(object : Callback<ArrayList<OrderNotes>> {
            override fun onResponse(
                call: Call<ArrayList<OrderNotes>>,
                response: Response<ArrayList<OrderNotes>>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ArrayList<OrderNotes>>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun changePwd(
        request: RequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.changePwd(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    /*fun saveProfileImage(
        request: RequestModel,
        onApiSuccess: (ProfileImage) -> Unit,
        onApiError: (aError: String) -> Unit
    ) {
        getRestApis.saveProfileImage(request).enqueue(object : Callback<ProfileImage> {
            override fun onResponse(
                call: Call<ProfileImage>,
                response: Response<ProfileImage>
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ProfileImage>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }*/

    fun saveProfileImage(
        //request: RequestModel,
        onApiSuccess: (ProfileImage) -> Unit,
        onApiError: (aError: String) -> Unit,
        file: File?,
    ) {
        val fileReqBody: RequestBody
        var imagePart: MultipartBody.Part? = null
        if (file != null) {
             fileReqBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            imagePart = MultipartBody.Part.createFormData("profile_image", file.name, fileReqBody)
        }
        getRestApis.saveProfileImage(imagePart!!).enqueue(object : Callback<ProfileImage> {
            override fun onResponse(
                call: Call<ProfileImage>,
                response: Response<ProfileImage>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ProfileImage>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun forgetPassword(
        request: RequestModel,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.forgetPassword(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>,
                response: Response<BaseResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun getShippingMethod(
        request: RequestModel,
        onApiSuccess: (ShippingModel) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.getShippingMethod(request).enqueue(object : Callback<ShippingModel> {
            override fun onResponse(
                call: Call<ShippingModel>,
                response: Response<ShippingModel>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<ShippingModel>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
    fun deleteOrder(
        orderId: Int,
        onApiSuccess: (BaseResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.deleteOrder(orderId).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }

    fun socialLogin(
        request: RequestModel,
        onApiSuccess: (loginResponse) -> Unit,
        onApiError: (aError: String) -> Unit,
    ) {
        getRestApis.socialLogin(request).enqueue(object : Callback<loginResponse> {
            override fun onResponse(
                call: Call<loginResponse>,
                response: Response<loginResponse>,
            ) {
                successCallback(onApiSuccess, onApiError, response, call.request())
            }

            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                failureCallback(onApiError, call.request(), t.toString())
            }
        })
    }
}















