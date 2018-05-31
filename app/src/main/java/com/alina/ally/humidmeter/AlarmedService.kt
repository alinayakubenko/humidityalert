package com.alina.ally.humidmeter

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.content.Intent
import android.app.IntentService
import android.preference.*
import android.util.Log


class AlarmedService : IntentService("AlarmedService") {
    private val ILOGTAGALARMED = "HUMIDITY_SERVICE"
    lateinit var sp : SharedPreferences
    lateinit var savedListChoice: String
    companion object {
        //
    }
    override fun onHandleIntent(intent: Intent?){
        sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (MainActivity.HomeFragment.alertSwitcherValue == true) {
            Log.i(ILOGTAGALARMED, "Handle: Intent Received")
                 savedListChoice = sp.getString(getString(R.string.extras_min_humid_level), "")

                if (sp.getString(getString(R.string.extras_min_humid_level), "").isEmpty()) {
                    savedListChoice = getString(R.string.list_min_humid_default_string_value)}
                if ((savedListChoice.isNotEmpty()) && (sp.getString(getString(R.string.extras_last_updated_humid_level),"").isNotEmpty())) {
                    Log.i(ILOGTAGALARMED, savedListChoice)
                    if (savedListChoice.toDouble() >= sp.getString(getString(R.string.extras_last_updated_humid_level),"").toDouble()) {
                        val notification: HumidityNotifications = HumidityNotifications()
                        notification.createNotification(applicationContext)
                        Log.i(ILOGTAGALARMED, "listPref value AFTER CONVERTION is: " + savedListChoice)
                        Log.i(ILOGTAGALARMED, "humidValue value is: " + sp.getString(getString(R.string.extras_last_updated_humid_level),""))
                        Log.i(ILOGTAGALARMED, "HumidSavedChoice : " + savedListChoice)
                        Log.i(ILOGTAGALARMED, "HumidSavedChoice _EXTRA : " + sp.getString(getString(R.string.extras_min_humid_level), ""))
                        Log.i(ILOGTAGALARMED, "humidValue : " + sp.getString(getString(R.string.extras_last_updated_humid_level),""))
                    }
                }
            }
        }
    override fun onDestroy() {
        super.onDestroy()
    }

    fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    fun onProviderEnabled(provider: String) {

    }

    fun onProviderDisabled(provider: String) {

    }

    inner class GetHumidityData : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            //
            Log.i(ILOGTAGALARMED, "AlarmedService doInBackground")
            return null
        }
        /*
        override fun doInBackground(vararg params: Void?): Void? {
            // ...
        } */

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
            Log.i(ILOGTAGALARMED, "AlarmedService onPreExecute")
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // ...
            Log.i(ILOGTAGALARMED, "AlarmedService onPostExecute")
        }
    }
}
