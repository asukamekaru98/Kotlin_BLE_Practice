package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class PermissionManager(private val activity: AppCompatActivity) {
    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val fineLocation = it[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocation = it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            val blueConnect = it[Manifest.permission.BLUETOOTH_CONNECT] ?: false
            val blueScanner = it[Manifest.permission.BLUETOOTH_SCAN] ?: false
            val writeExtStorage = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

            if (fineLocation && coarseLocation && blueConnect && blueScanner && writeExtStorage) {
                Toast.makeText(activity, "許可されました", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(activity, "否認されました", Toast.LENGTH_SHORT)
                    .show()
            }
        }



    fun checkPermission() {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )
    }

    init{
        checkPermission()
    }


}