package com.newton.mustmarket.core.util


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionUtils {

    /**
     * Checks for the appropriate permissions and requests them if not granted.
     *
     * @param context The context for checking permission state.
     * @param permissionLauncher The ActivityResultLauncher for multiple permissions.
     * @param onGranted Callback to invoke if permissions are already granted.
     */
    fun checkAndRequestPermission(
        context: Context,
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        onGranted: () -> Unit
    ) {
        when {
            // For Android 14+ (UPSIDE_DOWN_CAKE)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                val imagesPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                val selectedPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
                if (imagesPermission == PackageManager.PERMISSION_GRANTED &&
                    selectedPermission == PackageManager.PERMISSION_GRANTED
                ) {
                    onGranted()
                } else {
                    // Request both permissions together
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        )
                    )
                }
            }
            // For Android 13+ (TIRAMISU)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onGranted()
                } else {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                    )
                }
            }
            // For pre-Tiramisu devices
            else -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onGranted()
                } else {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    )
                }
            }
        }
    }
}
