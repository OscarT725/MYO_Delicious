package com.nativa.myodelicious.ui.main.admin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.nativa.myodelicious.R

class AdminActivity : AppCompatActivity() {

    private lateinit var ly_GestionProd: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Usamos el mismo layout que tenía el fragmento, pero ahora como actividad
        setContentView(R.layout.fragment_admin)
    }


}
