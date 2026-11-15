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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.seniorlink.app.BuildConfig

/**
 * Responsável pela captura e publicação da localização do dispositivo.
 *
 * Em produção, a publicação remota usa exclusivamente o UID do Firebase Auth.
 * Em debug, o ID do idoso salvo localmente pode ser usado para testar o fluxo
 * atual por PIN antes da migração definitiva para autenticação Firebase.
 */
class LocationRepository(
    context: Context,
    private val legacyUserId: String? = null
) {

    companion object {
        private const val TAG = "SeniorLinkLocation"
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val database =
        FirebaseDatabase.getInstance()

    private fun locationRef() =
        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
            database.getReference("localizacoes").child(uid)
        } ?: if (BuildConfig.DEBUG) {
            val debugId = legacyUserId
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: "idoso_teste"

            database.getReference("localizacoes").child(debugId)
        } else {
            null
        }

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

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Localização recebida")
                }

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

        if (!BuildConfig.DEBUG) {
            return
        }

        diagnosticRef
            .setValue(dados)
            .addOnSuccessListener {
                Log.d(TAG, "Firebase conectado e gravação de teste realizada")
            }
            .addOnFailureListener { erro ->
                Log.e(TAG, "ERRO AO GRAVAR NO FIREBASE", erro)
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

        val ref = locationRef() ?: return

        ref
            .setValue(dados)
            .addOnSuccessListener {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Localização salva no Firebase")
                }
            }
            .addOnFailureListener { erro ->
                Log.e(TAG, "ERRO AO SALVAR LOCALIZAÇÃO NO FIREBASE", erro)
            }
    }
}