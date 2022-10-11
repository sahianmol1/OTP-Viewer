package com.anmol.otpviewer.ui.smslist

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anmol.otpviewer.ui.smslist.data.SMSData
import com.anmol.otpviewer.utils.extractOTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsListViewModel : ViewModel() {
    private val _smsList = MutableLiveData<List<SMSData>>()
    val smsList: LiveData<List<SMSData>> get() = _smsList

    fun getSMS(cursor: Cursor?) {
        viewModelScope.launch(Dispatchers.IO) {
            val sms: MutableList<SMSData> = mutableListOf()
            val cur: Cursor? = cursor
            while (cur != null && cur.moveToNext()) {
                val address: String = cur.getString(cur.getColumnIndexOrThrow("address"))
                val body: String = cur.getString(cur.getColumnIndexOrThrow("body"))
                var id = 1
                val otp = extractOTP(body) ?: "0"
                sms.add(
                    SMSData(
                        id = id,
                        sender = address,
                        content = body,
                        otp = otp
                    )
                )
                id += 1
            }
            cur?.close()

            _smsList.postValue(sms)
        }
    }

    private suspend fun extractOTP(body: String): String? = withContext(Dispatchers.Default) {
        body.extractOTP()
    }

}