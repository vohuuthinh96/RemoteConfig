package com.topsecurity.remoteconfig

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(10)
            .build()

        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.default_config)
        }

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("thinhvh", "Config params updated: $updated")
                    Toast.makeText(this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
                displayWelcomeMessage(remoteConfig)
            }
    }

    private fun displayWelcomeMessage(config: FirebaseRemoteConfig) {
        val mesage: String =
            FirebaseRemoteConfig.getInstance().getString(RemoteKey.WELCOME_MESSAGE)

        val enableADS: Boolean =
            FirebaseRemoteConfig.getInstance().getBoolean(RemoteKey.ENABLE_ADS)

        Toast.makeText(this, mesage.plus(" $enableADS"),
            Toast.LENGTH_SHORT).show()
    }
}