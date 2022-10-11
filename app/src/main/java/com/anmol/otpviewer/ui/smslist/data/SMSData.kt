package com.anmol.otpviewer.ui.smslist.data

import java.io.Serializable

data class SMSData(
    val id: Int,
    val sender: String,
    val content: String,
    val otp: String,
): Serializable
