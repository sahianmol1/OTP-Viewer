package com.anmol.otpviewer.ui.smsdetails

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.anmol.otpviewer.databinding.FragmentSmsDetailsBinding

class SmsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSmsDetailsBinding

    private val args: SmsDetailsFragmentArgs? by navArgs<SmsDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSmsDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val sms = args?.smsData
        var smsContent: String? = sms?.content
        var otp: String? = sms?.otp

        val bundleArgs = this.arguments
        if (bundleArgs!= null && !bundleArgs.isEmpty && smsContent.isNullOrEmpty()) {
            this.arguments?.let {
                smsContent = it.getString("content", "")
                otp = it.getString("otp", "")
            }
        }

        val spannableString = otpHighlightedText(smsContent!!, otp ?: "")

        binding.tvMessageContent.text = spannableString ?: smsContent

        return view
    }

    private fun otpHighlightedText(
        smsContent: String,
        otp: String
    ): SpannableString? {

        try {
            val spannableString = SpannableString(smsContent)

            val startIndex = smsContent.indexOf(otp)
            val endIndex = smsContent.lastIndexOf(otp.last()) + 1

            val yellow = BackgroundColorSpan(Color.YELLOW)
            val boldSpan = StyleSpan(Typeface.BOLD)

            spannableString.setSpan(yellow, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            spannableString.setSpan(AbsoluteSizeSpan(55), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            return spannableString

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}