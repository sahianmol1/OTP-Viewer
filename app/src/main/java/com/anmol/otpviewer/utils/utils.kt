package com.anmol.otpviewer.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.regex.Pattern

fun AppCompatActivity.hasReadSMSPermission() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_SMS
    ) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.hasReceiveSMSPermission() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.RECEIVE_SMS
    ) == PackageManager.PERMISSION_GRANTED

fun String.extractOTP(): String? {
    val pattern = Pattern.compile("\\d+(?=\\sis)|(?<=is\\s)\\d+")
    val matcher = pattern.matcher(this)
    val doesHaveOtp = matcher.find()
    return if (doesHaveOtp) {
        matcher.group()
    } else {
        null
    }
}