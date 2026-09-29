
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

class Onboarding3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding3)

        val tela = findViewById<View>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(
            tela
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

        // Imagem da localização
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

        // Animação suave da imagem
        imagem.alpha = 0f

        imagem.animate()
            .alpha(1f)
            .setDuration(1200)
            .start()

        // Avançar para o onboarding 4
        btnAvancar.setOnClickListener {

            val intent = Intent(
                this,
                Onboarding4Activity::class.java
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