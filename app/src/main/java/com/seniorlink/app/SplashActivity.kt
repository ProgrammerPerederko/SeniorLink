package com.seniorlink.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(
            this,
            OnboardingActivity::class.java
        )

        startActivity(intent)

        finish()
    }
}