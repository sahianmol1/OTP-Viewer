package com.anmol.otpviewer.ui

import android.os.Bundle
import com.anmol.otpviewer.R
import com.anmol.otpviewer.databinding.ActivityMainBinding
import com.anmol.otpviewer.utils.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_OTPViewer)

        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        requestPermissions()
    }
}