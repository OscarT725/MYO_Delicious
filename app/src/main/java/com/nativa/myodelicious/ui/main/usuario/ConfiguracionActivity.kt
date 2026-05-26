package com.nativa.myodelicious.ui.main.usuario

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.auth.LoginActivity

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var imgOutConfig: ImageView
    private lateinit var swNotiPed: Switch
    private lateinit var swNotiProm: Switch
    private lateinit var swNotiSop: Switch
    private lateinit var lyNotiPedidos: LinearLayout
    private lateinit var lyNotiPromociones: LinearLayout
    private lateinit var imgTemaClaro: ImageView
    private lateinit var imgTemaOscuro: ImageView
    private lateinit var imgTemaSistema: ImageView

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion)

        prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        imgOutConfig = findViewById(R.id.img_out_conf)
        swNotiPed = findViewById(R.id.sw_noti_ped)
        swNotiProm = findViewById(R.id.sw_noti_prom)
        swNotiSop = findViewById(R.id.sw_noti_sop)
        lyNotiPedidos = findViewById(R.id.ly_noti_pedidos)
        lyNotiPromociones = findViewById(R.id.ly_noti_promociones)
        imgTemaClaro = findViewById(R.id.Img_tem_claro)
        imgTemaOscuro = findViewById(R.id.Img_tem_oscu)
        imgTemaSistema = findViewById(R.id.Img_tem_siste)

        val esAdmin = intent.getBooleanExtra("esAdmin", false)

        if (esAdmin) {
            lyNotiPedidos.visibility     = android.view.View.GONE
            lyNotiPromociones.visibility = android.view.View.GONE
        } else {
            lyNotiPedidos.visibility     = android.view.View.VISIBLE
            lyNotiPromociones.visibility = android.view.View.VISIBLE
        }

        swNotiPed.isChecked  = prefs.getBoolean("noti_pedidos", true)
        swNotiProm.isChecked = prefs.getBoolean("noti_promociones", true)
        swNotiSop.isChecked  = prefs.getBoolean("noti_soporte", true)

        swNotiPed.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("noti_pedidos", isChecked).apply()
            val msg = if (isChecked) "Notificaciones de pedidos activadas"
            else "Notificaciones de pedidos desactivadas"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        swNotiProm.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("noti_promociones", isChecked).apply()
            val msg = if (isChecked) "Notificaciones de promociones activadas"
            else "Notificaciones de promociones desactivadas"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        swNotiSop.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("noti_soporte", isChecked).apply()
            val msg = if (isChecked) "Notificaciones de soporte activadas"
            else "Notificaciones de soporte desactivadas"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        actualizarResaltadoTema(prefs.getInt("tema", 0))

        imgTemaClaro.setOnClickListener {
            aplicarTema(AppCompatDelegate.MODE_NIGHT_NO, 0)
        }
        imgTemaOscuro.setOnClickListener {
            aplicarTema(AppCompatDelegate.MODE_NIGHT_YES, 1)
        }
        imgTemaSistema.setOnClickListener {
            aplicarTema(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, 2)
        }

        imgOutConfig.setOnClickListener { finish() }

        findViewById<android.widget.Button>(R.id.btn_elim_cuent).setOnClickListener {//Pendiente configuracion para eliminar cuenta
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
    private fun aplicarTema(modo: Int, opcion: Int) {
        prefs.edit().putInt("tema", opcion).apply()
        AppCompatDelegate.setDefaultNightMode(modo)
        actualizarResaltadoTema(opcion)
    }
    private fun actualizarResaltadoTema(temaActivo: Int) {
        imgTemaClaro.alpha   = if (temaActivo == 0) 1.0f else 0.3f
        imgTemaOscuro.alpha  = if (temaActivo == 1) 1.0f else 0.3f
        imgTemaSistema.alpha = if (temaActivo == 2) 1.0f else 0.3f
    }
}