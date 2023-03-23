package com.example.broadcastreseiversms;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast


private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {
    private lateinit var prefs: SharedPreferences

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state != null) {
                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                    var phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.d("MENSAJES Y LLAMADAS",intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString())

                    phoneNumber = phoneNumber.toString().substring(3)
                    Log.d("NUMBER MESSAGE",phoneNumber)
                    if (phoneNumber == NumeroSMS(context).toString()&& phoneNumber.toString()!=null) {

                        Log.d("INSIDE MESSAGE","Estaaaa dentrooooo")
                        enviarMensaje(context)
                    }
                }

            }
        }
    }
    private fun enviarMensaje(context: Context) {
        val smsManager = SmsManager.getDefault()
        val enviarText = SMS(context)
        val enviarNumber = NumeroSMS(context)
        smsManager.sendTextMessage("+52"+enviarNumber, null, enviarText, null, null)
    }

    private fun NumeroSMS(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("NumeroGuardado", null)
    }

    private fun SMS(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("Mensaje", null)
    }
}