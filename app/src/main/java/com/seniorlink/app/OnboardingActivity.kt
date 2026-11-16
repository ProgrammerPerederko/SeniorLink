package com.seniorlink.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : ComponentActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicadores: LinearLayout

    private val handler = Handler(Looper.getMainLooper())

    private val abrirLoginRunnable = Runnable {
        abrirLogin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        indicadores = findViewById(R.id.indicadores)

        /*
         * TODAS AS PÁGINAS DO ONBOARDING
         *
         * 0 = Splash antiga
         * 1 = Cuidado hoje, mais liberdade amanhã
         * 2 = Sempre por perto
         * 3 = Aprenda no seu ritmo
         */
        val paginas = listOf(
            R.layout.activity_splash,
            R.layout.activity_onboarding2,
            R.layout.activity_onboarding3,
            R.layout.activity_onboarding4
        )

        viewPager.adapter = OnboardingAdapter(paginas)

        // Começa obrigatoriamente na primeira página
        viewPager.setCurrentItem(0, false)

        atualizarIndicadores(0)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    atualizarIndicadores(position)

                    /*
                     * Ao chegar na última página,
                     * espera 1,5 segundo e abre o Login.
                     */
                    if (position == paginas.lastIndex) {

                        handler.removeCallbacks(
                            abrirLoginRunnable
                        )

                        handler.postDelayed(
                            abrirLoginRunnable,
                            1500L
                        )

                    } else {

                        handler.removeCallbacks(
                            abrirLoginRunnable
                        )
                    }
                }
            }
        )
    }

    private fun atualizarIndicadores(
        posicaoAtual: Int
    ) {

        for (i in 0 until indicadores.childCount) {

            val indicador = indicadores.getChildAt(i)

            indicador.background =
                ContextCompat.getDrawable(
                    this,
                    if (i == posicaoAtual) {
                        R.drawable.bg_dot_active
                    } else {
                        R.drawable.bg_dot_inactive
                    }
                )
        }
    }

    private fun abrirLogin() {

        val intent = Intent(
            this,
            MainActivity::class.java
        )

        intent.putExtra(
            "abrir_login",
            true
        )

        startActivity(intent)

        finish()
    }

    override fun onDestroy() {

        handler.removeCallbacks(
            abrirLoginRunnable
        )

        super.onDestroy()
    }
}


/*
 * Adapter do ViewPager2
 */
class OnboardingAdapter(
    private val paginas: List<Int>
) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                paginas[viewType],
                parent,
                false
            )

        /*
         * Os controles antigos não serão usados.
         * A navegação acontece pelo deslize.
         */
        view.findViewById<ImageButton?>(
            R.id.btnAvancar
        )?.visibility = View.GONE

        view.findViewById<TextView?>(
            R.id.txtPular
        )?.visibility = View.GONE

        /*
         * Esconde as bolinhas internas das telas antigas.
         */
        view.findViewById<LinearLayout?>(
            R.id.indicadores
        )?.visibility = View.GONE

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        // Os XMLs já possuem todo o conteúdo.
    }

    override fun getItemCount(): Int {
        return paginas.size
    }

    override fun getItemViewType(
        position: Int
    ): Int {
        return position
    }
}