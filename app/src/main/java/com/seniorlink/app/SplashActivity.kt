
package com.seniorlink.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(
                bars.left, bars.top, bars.right, bars.bottom
            )
            insets
        }

        findViewById<ImageButton>(R.id.btnAvancar)
            .setOnClickListener {
                abrirOnboarding2()
            }

        findViewById<TextView>(R.id.txtPular)
            .setOnClickListener {
                abrirLogin()
            }
    }

    private fun abrirOnboarding2() {
        startActivity(
            Intent(this, activity_onboarding2::class.java)
        )
        finish()
    }

    private fun abrirLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("abrir_login", true)
        startActivity(intent)
        finish()
    }
}