package com.example.broadcastreseiversms

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    private var stateSwitch: Boolean = false
    val br: BroadcastReceiver = MyBroadcastReceiver()
    lateinit var  swt : Switch
    lateinit var btn : Button
    lateinit var Num : String
    lateinit var Sms : String
    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        //addAction("android.intent.action.PHONE_STATE")
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    private lateinit var numero: EditText
    private lateinit var sms: EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveConfigButton = findViewById<Button>(R.id.btn_enviar)
        saveConfigButton.setOnClickListener {


            numero = findViewById(R.id.edt_num)
            sms = findViewById(R.id.edt_sms)

            var Numero=numero.text.toString()
            var SMS=sms.text.toString()

            // Obtener el número de teléfono y el mensaje de respuesta automáticos ingresados por el usuario
            // Guardarlos en las preferencias compartidas usando la clase SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("NumeroGuardado", Numero)
            editor.putString("Mensaje", SMS)
            editor.apply()
            Toast.makeText(this, "¡La configuracion se ha guardado!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "¡El numero es $Numero", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "¡El sms es $SMS", Toast.LENGTH_SHORT).show();
        }



        swt = findViewById(R.id.switchET)





        stateSwitch = isMyServiceRunning(ServicePhoneStateWithBroadcastReceiver::class.java)

        swt.isChecked = stateSwitch



        swt.setOnClickListener{
            stateSwitch = swt.isChecked()
            if (stateSwitch) {
                val callService = Intent(this,
                    ServicePhoneStateWithBroadcastReceiver::class.java)
                try {
                    startService(callService)
                    Toast.makeText(this, "¡El servicio se ha iniciado!", Toast.LENGTH_SHORT).show();
                } catch (ex: Exception) {
                    Log.d(packageName, ex.toString())
                }
            } else {
                stopService(
                    Intent(this,
                    ServicePhoneStateWithBroadcastReceiver::class.java)
                )
                Toast.makeText(this, "¡El servicio se ha detenido!", Toast.LENGTH_SHORT).show();
            }
        }

        registerReceiver(br, filter)
    }




}