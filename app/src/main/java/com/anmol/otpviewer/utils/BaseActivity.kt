package com.anmol.otpviewer.utils

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

open class BaseActivity : AppCompatActivity() {

    fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (!hasReadSMSPermission()) {
            permissionsToRequest.add(Manifest.permission.READ_SMS)
        }
        if (!hasReceiveSMSPermission()) {
            permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivityPermissions", "${permissions[i]} granted")
                }
            }
        }
    }
}