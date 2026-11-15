package com.seniorlink.app.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.database.FirebaseDatabase

class LocationRepository(
    context: Context
) {

    companion object {
        private const val TAG = "SeniorLinkLocation"
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val database =
        FirebaseDatabase.getInstance()

    private val locationRef =
        database
            .getReference("localizacoes")
            .child("idoso_teste")

    private val diagnosticRef =
        database
            .getReference("diagnostico")
            .child("localizacao")

    private val locationRequest =
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        )
            .setMinUpdateIntervalMillis(5_000L)
            .build()

    private var onLocationUpdated: ((Location) -> Unit)? = null

    private val locationCallback =
        object : LocationCallback() {

            override fun onLocationResult(
                result: LocationResult
            ) {

                val location =
                    result.lastLocation
                        ?: return

                Log.d(
                    TAG,
                    "Localização recebida: " +
                            "${location.latitude}, ${location.longitude}"
                )

                salvarLocalizacao(location)

                onLocationUpdated?.invoke(location)
            }
        }

    @SuppressLint("MissingPermission")
    fun iniciarAtualizacao(
        onLocationUpdated: (Location) -> Unit
    ) {

        this.onLocationUpdated =
            onLocationUpdated

        Log.d(
            TAG,
            "Iniciando atualização de localização"
        )

        testarConexaoFirebase()

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {

                    Log.d(
                        TAG,
                        "Última localização encontrada"
                    )

                    salvarLocalizacao(location)

                    onLocationUpdated(location)

                } else {

                    Log.d(
                        TAG,
                        "Última localização ainda não disponível"
                    )
                }
            }
            .addOnFailureListener { erro ->

                Log.e(
                    TAG,
                    "Erro ao obter última localização",
                    erro
                )
            }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
            .addOnSuccessListener {

                Log.d(
                    TAG,
                    "Atualizações de localização iniciadas"
                )
            }
            .addOnFailureListener { erro ->

                Log.e(
                    TAG,
                    "Erro ao iniciar atualizações",
                    erro
                )
            }
    }

    fun pararAtualizacao() {

        fusedLocationClient.removeLocationUpdates(
            locationCallback
        )

        onLocationUpdated = null

        Log.d(
            TAG,
            "Atualização de localização parada"
        )
    }

    private fun testarConexaoFirebase() {

        val dados = mapOf(
            "status" to "conexao_iniciada",
            "timestamp" to System.currentTimeMillis()
        )

        diagnosticRef
            .setValue(dados)
            .addOnSuccessListener {

                Log.d(
                    TAG,
                    "Firebase conectado e gravação de teste realizada"
                )
            }
            .addOnFailureListener { erro ->

                Log.e(
                    TAG,
                    "ERRO AO GRAVAR NO FIREBASE",
                    erro
                )
            }
    }

    private fun salvarLocalizacao(
        location: Location
    ) {

        val dados = mapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "precisao" to location.accuracy.toDouble(),
            "timestamp" to System.currentTimeMillis()
        )

        locationRef
            .setValue(dados)
            .addOnSuccessListener {

                Log.d(
                    TAG,
                    "Localização salva no Firebase"
                )
            }
            .addOnFailureListener { erro ->

                Log.e(
                    TAG,
                    "ERRO AO SALVAR LOCALIZAÇÃO NO FIREBASE",
                    erro
                )
            }
    }
}