package com.alina.ally.humidmeter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.content.SharedPreferences
import android.preference.*
import kotlinx.android.synthetic.main.activity_main.view.*
import android.preference.Preference


class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    val LOGTAG = "PREF SCREEN"
    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var listPref: ListPreference
    //lateinit var switchPreference: SwitchPreference
    companion object {
      //
   }
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
        Log.i(LOGTAG, "Settings Fragment loaded!")
        sp = PreferenceManager.getDefaultSharedPreferences(context) as SharedPreferences
        val keys: Map<String,*> = sp.all
        val minHumidKey = keys.get(getString(R.string.pref_key_list_min_humid_pref))
        Log.i(LOGTAG, "Min Humidity key" +minHumidKey)

        editor = sp.edit()
       // switchPreference = findPreference(getString(R.string.pref_key_switch_alert)) as SwitchPreference
        listPref = findPreference(getString(R.string.pref_key_list_min_humid_pref)) as ListPreference
        listPref.setDefaultValue(0)
        Log.i(LOGTAG, "Min Humidity setDefaultkey" +listPref.value)
        Log.i(LOGTAG, "Min Humidity entry" +listPref.entry.toString())
        Log.i(LOGTAG, "Min Humidity SAVED CHOICE " +listPref.entry.toString())

    }

    override fun onStart() {
        super.onStart()
        if (view != null) {
            view.setBackgroundColor(context!!.getColor(R.color.background_color))
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOGTAG, "onDestroy ")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        editor.putString(getString(R.string.extras_min_humid_level), listPref.summary.toString()).apply()
        Log.i(MainActivity.LOGTAG, "Settings Fragment humid lavel saved as : "+listPref.summary.toString())
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(LOGTAG, "onDetach ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(LOGTAG, "onPause ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(LOGTAG, "onStop ")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            if (key.equals(getString(R.string.pref_key_switch_alert))) {
               // sp.getString(getString(R.string.extr)) .set(sharedPreferences.getString(key, ""))
                MainActivity.HomeFragment.alertSwitcherValue = sp.getBoolean(key, true)
                Log.i(LOGTAG, "SHARED PREF FRAGM, SWITCH ALERT VALUE IS: "+sp.getBoolean(key, true))
            }
        }

}