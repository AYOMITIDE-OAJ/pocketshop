/*
 * Copyright 2017 Goldenmace IT Solutions
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing storagePermissions and
 *    limitations under the License.
 */

package com.oajstudios.pocketshop.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log



import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.Arrays
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.oajstudios.pocketshop.BuildConfig

object ImagePicker {

    private const val DEFAULT_REQUEST_CODE = 234
    private const val BASE_IMAGE_NAME = "i_prefix_"
    private const val DEFAULT_MIN_WIDTH_QUALITY = 800        // min pixels
    private const val DEFAULT_MIN_HEIGHT_QUALITY = 800        // min pixels
    private val TAG = ImagePicker::class.java.simpleName
    private var minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY
    private var minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY
    private var mChooserTitle: String? = null
    var mPickImageRequestCode = DEFAULT_REQUEST_CODE
    private var mGalleryOnly = false

    /**
     * Launch a dialog to pick an image from camera/gallery apps.
     * @param fragment     which will launch the dialog and will get the result in
     * onActivityResult()
     * @param chooserTitle will appear on the picker dialog.
     * @param requestCode REQUEST code that will be returned in result.
     */
    @JvmOverloads
    fun pickImage(
        fragment: Fragment, chooserTitle: String,
        requestCode: Int = DEFAULT_REQUEST_CODE, galleryOnly: Boolean = false
    ) {
        mGalleryOnly = galleryOnly
        mPickImageRequestCode = requestCode
        mChooserTitle = chooserTitle
        startChooser(fragment)
    }

    fun pickImageActivity(
        activity: Activity, chooserTitle: String,
        requestCode: Int, galleryOnly: Boolean
    ) {
        mGalleryOnly = galleryOnly
        mPickImageRequestCode = requestCode
        mChooserTitle = chooserTitle
        startChoosers(activity)
    }

    private fun startChoosers(activity: Activity) {
        val chooseImageIntent = getPickImageIntent(activity.baseContext, mChooserTitle)
        activity.startActivityForResult(chooseImageIntent, mPickImageRequestCode)
    }

    private fun startChooser(fragmentContext: Fragment) {
        val chooseImageIntent = getPickImageIntent(fragmentContext.context, mChooserTitle)
        fragmentContext.startActivityForResult(chooseImageIntent, mPickImageRequestCode)
    }

    /**
     * Get an Intent which will launch a dialog to pick an image from camera/gallery apps.
     *
     * @param context      context.
     * @param chooserTitle will appear on the picker dialog.
     * @return intent launcher.
     */
    fun getPickImageIntent(context: Context?, chooserTitle: String?): Intent? {
        var chooserIntent: Intent? = null
        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intentList = addIntentsToList(context!!, intentList, pickIntent)



        if (!mGalleryOnly) {
            if (!appManifestContainsPermission(context, Manifest.permission.CAMERA) || hasCameraAccess(context)) {
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhotoIntent.putExtra("return-data", true)
                takePhotoIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider",
                        getTemporalFile(context, mPickImageRequestCode.toString())
                    )
                )
                intentList = addIntentsToList(context, intentList, takePhotoIntent)
            }
        }

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                chooserTitle
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun addIntentsToList(context: Context, list: MutableList<Intent>, intent: Intent): MutableList<Intent> {
        Log.i(TAG, "Adding intents of type: " + intent.action!!)
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
            Log.i(TAG, "App package: $packageName")
        }
        return list
    }

    /**
     * Checks if the current context has permission to access the camera.
     * @param context             context.
     */
    private fun hasCameraAccess(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if the androidmanifest.xml contains the given permission.
     * @param context             context.
     * @return Boolean, indicating if the permission is present.
     */
    private fun appManifestContainsPermission(context: Context, permission: String): Boolean {
        val pm = context.packageManager
        try {
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            var requestedPermissions: Array<String>? = null
            if (packageInfo != null) {
                requestedPermissions = packageInfo.requestedPermissions
            }
            if (requestedPermissions == null) {
                return false
            }

            if (requestedPermissions.isNotEmpty()) {
                val requestedPermissionsList = Arrays.asList(*requestedPermissions)
                return requestedPermissionsList.contains(permission)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return false
    }


    /**
     * Called after launching the picker with the same values of Activity.getImageFromResult
     * in order to resolve the result and get the image path.
     *
     * @param context             context.
     * @param requestCode         used to identify the pick image action.
     * @param resultCode          -1 means the result is OK.
     * @param imageReturnedIntent returned intent where is the image data.
     * @param
     * @return path to the saved image.
     */
    fun getImagePathFromResult(
        context: Context, requestCode: Int, resultCode: Int,
        imageReturnedIntent: Intent?
    ): String? {
        Log.i(TAG, "getImagePathFromResult() called with: resultCode = [$resultCode]")
        var selectedImage: Uri? = null
        if (resultCode == Activity.RESULT_OK && requestCode == mPickImageRequestCode) {
            val imageFile = getTemporalFile(context, mPickImageRequestCode.toString())
            val isCamera = (imageReturnedIntent == null
                    || imageReturnedIntent.data == null
                    || imageReturnedIntent.data!!.toString().contains(imageFile.toString()))
            if (isCamera) {
                return imageFile.absolutePath
            } else {
                selectedImage = imageReturnedIntent!!.data
            }
            Log.i(TAG, "selectedImage: " + selectedImage!!)
        }
        return if (selectedImage == null) {
            null
        } else getFilePathFromUri(context, selectedImage)
    }

    /**
     * Get stream, save the picture to the temp file and return path.
     *
     * @param context context
     * @param uri uri of the incoming file
     * @return path to the saved image.
     */
    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var `is`: InputStream? = null
        if (uri.authority != null) {
            try {
                `is` = context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(`is`)
                return savePicture(context, bmp, uri.path!!.hashCode().toString())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    fun savePicture(context: Context, bitmap: Bitmap, imageSuffix: String): String {
        val savedImage = getTemporalFile(context, "$imageSuffix.jpeg")
        var fos: FileOutputStream? = null
        if (savedImage.exists()) {
            savedImage.delete()
        }
        try {
            fos = FileOutputStream(savedImage.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return savedImage.absolutePath
    }

    fun getTemporalFile(context: Context, payload: String): File {
        return File(context.externalCacheDir, BASE_IMAGE_NAME + payload)
    }


    /*
    GETTERS AND SETTERS
     */

    fun setMinQuality(minWidthQuality: Int, minHeightQuality: Int) {
        ImagePicker.minWidthQuality = minWidthQuality
        ImagePicker.minHeightQuality = minHeightQuality
    }
}