package com.alina.ally.humidmeter

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.preference.PreferenceManager
import android.util.Log

class HumiditySensorService : Service(), SensorEventListener {
    val LOGTAG = "HUMID Service"
    var humidityValueraw : Double = 0.0
    var humidityValue : String = ""
    val sensorManager: SensorManager by lazy {
        applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    lateinit var sp: SharedPreferences
    override fun onSensorChanged(p0: SensorEvent?) {
        humidityValueraw =  p0!!.values.get(0).toString().toDouble()
        humidityValue = Math.round(humidityValueraw).toString()
        sp.edit().putString(getString(R.string.extras_last_updated_humid_level),humidityValue).apply()

    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
       // if (intent != null) {
        Log.i(LOGTAG, "OnStartCommand")
        sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                    SensorManager.SENSOR_DELAY_NORMAL
            )
       // }
        return START_REDELIVER_INTENT
    }
    override fun onCreate() {
        super.onCreate()
        Log.i(LOGTAG, "OnCreate")
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        val startMainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        startMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startMainActivityIntent.putExtra(getString(R.string.estras_on_destroy_sensor_service), "1")
        applicationContext.startActivity(startMainActivityIntent)
        Log.i(LOGTAG, "OnDestroy")
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
