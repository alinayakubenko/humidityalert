package com.alina.ally.humidmeter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.preference.PreferenceManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.app.ActivityManager



class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        val LOGTAG = "HUMIDMETER"
        val NOTIFICATION_CHANNEL_ID = "channel_id_1"
        val PENDING_INTENT_REQUEST_CODE = 1
        val NOTIFICATION_ID = 1
        val ALARM_INTERVAL_MILLISEC: Long = 900000
        val ETRASAVEDHUMIDVALUE = "Extra_humidSettings_value"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        startSensorService()
        if (this.intent.getStringExtra(getString(R.string.estras_on_destroy_sensor_service)) == "1") {
            startActivity(intent)
            fragmentManager.beginTransaction().replace(android.R.id.content, HomeFragment()).commit()
            moveTaskToBack(true)
            finish()
                }
                else {
            fragmentManager.beginTransaction().replace(android.R.id.content, HomeFragment()).commit()
        }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }
    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }
    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        //
    }
    // Home fragment to hold/start all UI and functionality
     class HomeFragment : Fragment(){
         lateinit var listPrefSum: CharSequence
         lateinit var sp: SharedPreferences
         var keyVal: String = ""
         /*
         val sp: SharedPreferences
            get() = PreferenceManager.getDefaultSharedPreferences(context.applicationContext) */
         companion object {
             lateinit var humidityValue: String
             var humidityValueraw:Double =0.0
             var alertSwitcherValue = false
                    set
         }
         @RequiresApi(Build.VERSION_CODES.M)
         @TargetApi(Build.VERSION_CODES.M)
         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             sp = PreferenceManager.getDefaultSharedPreferences(context)
             sp.edit().remove(getString(R.string.estras_on_destroy_sensor_service))
             if (!(isAlarmSet())){
                 setServiceAlarm()
             }
         }
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            val v = inflater?.inflate(R.layout.activity_main, container, false)
            val settings = v?.findViewById<Button>(R.id.settings_button) as Button
            Log.i(LOGTAG, "CHECKING DEFAULT HUMID VALUE IS: "+sp.getString(getString(R.string.extras_min_humid_level),""))
            settings.setOnClickListener(View.OnClickListener {

                fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment(), null).addToBackStack(null).commit()
                Log.i(LOGTAG, "Settings button clicked!")
            })

            return v
        }
         override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
             super.onViewCreated(view, savedInstanceState)
             if (sp.getString(getString(R.string.extras_last_updated_humid_level), "").isNotEmpty()) {
                 humidity_textview.text = sp.getString(getString(R.string.extras_last_updated_humid_level), "")
             } else {
                 Log.i(LOGTAG, "Humidity value is empty")
             }
             sp.registerOnSharedPreferenceChangeListener(object : SharedPreferences.OnSharedPreferenceChangeListener {
                 override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
                     Log.i(LOGTAG, "Preferences changed")
                     if (context != null){
                         if (sp.getString(getString(R.string.extras_last_updated_humid_level), "").isNotEmpty()) {
                             if (humidity_textview != null) {
                                 humidity_textview.text = sp.getString(getString(R.string.extras_last_updated_humid_level), "")
                             }
                         }
                         if (sp.getBoolean(getString(R.string.pref_key_switch_alert), true)) {
                             alertSwitcherValue = true
                         } else {
                             alertSwitcherValue = false
                         }
                         Log.i(LOGTAG, "Switcher status is " + alertSwitcherValue)
                     }
                 }
             })
         }
          override fun onAttach(context: Context?) {
             super.onAttach(context)
         }
        override fun onResume() {
            super.onResume()
            if (humidity_textview.text == "") {
                humidity_textview.text = sp.getString(getString(R.string.extras_last_updated_humid_level), "")
            }
        }
        override fun onPause() {
            super.onPause()
        }
         override fun onStop() {
             super.onStop()
         }
         @RequiresApi(Build.VERSION_CODES.M)
         private fun setServiceAlarm() {
             val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
             val serviceIntent = Intent (context, AlarmedService::class.java)
             if ((keyVal == "") || (keyVal == null)){
                 keyVal = sp.getString(getString(R.string.list_min_humid_default), "")
                 serviceIntent.putExtra(ETRASAVEDHUMIDVALUE, keyVal)
             }else {
                 serviceIntent.putExtra(ETRASAVEDHUMIDVALUE, keyVal)
            }
             val pendingIntent = PendingIntent.getService(context, R.string.service_repeating_alarm_request_code, serviceIntent, 0)
             alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL_MILLISEC, pendingIntent)
             Log.i(LOGTAG, "Alarm set ")
         }
         private fun isAlarmSet(): Boolean {
             val pendingIntent = activity.createPendingResult(R.string.service_repeating_alarm_request_code, Intent(), PendingIntent.FLAG_NO_CREATE)
             return pendingIntent != null
         }
    }
    @SuppressLint("ServiceCast")
    public fun startSensorService(){
        val sensorService = Intent (applicationContext, HumiditySensorService::class.java)
        applicationContext.startService(sensorService)
        Log.i(LOGTAG, "Sensor service started ")
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}



