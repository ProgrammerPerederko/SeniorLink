
package com.seniorlink.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_onboarding2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding2)

        // Ajustar a tela às barras do celular
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { view, insets ->

            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                barras.left,
                barras.top,
                barras.right,
                barras.bottom
            )

            insets
        }

        // Imagem do onboarding 2
        val imagem = findViewById<ImageView>(
            R.id.imgIlustracao
        )

        // Botões
        val btnAvancar = findViewById<ImageButton>(
            R.id.btnAvancar
        )

        val txtPular = findViewById<TextView>(
            R.id.txtPular
        )

        // Aparição suave da imagem
        imagem.alpha = 0f

        imagem.animate()
            .alpha(1f)
            .setDuration(1200)
            .start()

        // Avançar para o onboarding 3
        btnAvancar.setOnClickListener {

            val intent = Intent(
                this,
                Onboarding3Activity::class.java
            )

            startActivity(intent)
            finish()
        }

        // Pular direto para a tela principal
        txtPular.setOnClickListener {
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal() {

        val intent = Intent(
            this,
            MainActivity::class.java
        )

        intent.putExtra("abrir_inicio", true)

        startActivity(intent)
        finish()
    }
}