
package com.seniorlink.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Onboarding4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding4)

        val tela = findViewById<View>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(tela) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        findViewById<ImageButton>(R.id.btnAvancar)
            .setOnClickListener {
                abrirTelaPrincipal()
            }

        findViewById<TextView>(R.id.txtPular)
            .setOnClickListener {
                abrirTelaPrincipal()
            }
    }

    private fun abrirTelaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)

        // Informa que deve abrir diretamente a Home
        intent.putExtra("abrir_inicio", true)

        startActivity(intent)
        finish()
    }
}