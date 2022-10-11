package com.anmol.otpviewer.broadcastreceivers

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.anmol.otpviewer.R
import com.anmol.otpviewer.ui.MainActivity
import com.anmol.otpviewer.ui.smslist.data.SMSData
import com.anmol.otpviewer.utils.extractOTP


class SMSReceiver : BroadcastReceiver() {

    @TargetApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {

            val bundle = intent!!.extras
            val msgs: Array<SmsMessage?>
            var strMessage = ""
            val format = bundle!!.getString("format")
            val pdus = bundle["pdus"] as Array<Any>?

            if (pdus != null) {
                val isVersionM = Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.M
                msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (i in msgs.indices) {
                    // Check Android version and use appropriate createFromPdu.
                    if (isVersionM) {
                        // If Android version M or newer:
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                    } else {
                        // If Android version L or older:
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    strMessage += "SMS from " + msgs[i]?.originatingAddress
                    strMessage += " :" + msgs[i]?.messageBody + "\n"

                    if (context != null) {
                        showNotification(context, strMessage, msgs[i]?.originatingAddress ?: "")

                    }
                }
            }
        }
    }

    private fun showNotification(context: Context, strMessage: String, sender: String) {

        val otp = strMessage.extractOTP()

        if (!otp.isNullOrEmpty()) {

            val bundle = Bundle().apply {
                putString("content", strMessage)
                putString("otp", otp)
            }

            val pendingIntent = NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.sms_nav_graph)
                .setDestination(R.id.smsDetailsFragment)
                .setArguments(bundle)
                .createPendingIntent()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )

                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }


            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("New Message Received")
                .setSmallIcon(R.drawable.logo)
                .setContentText(otp)
                .setContentIntent(pendingIntent)

            val manager = NotificationManagerCompat.from(context)
            manager.notify(999, builder.build())
        }
    }

    companion object {
        const val CHANNEL_ID = "sms_channel"
        const val CHANNEL_NAME = "sms_notification_channel"
    }
}