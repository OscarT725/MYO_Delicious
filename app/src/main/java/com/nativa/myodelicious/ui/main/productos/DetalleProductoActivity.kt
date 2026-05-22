package com.nativa.myodelicious.ui.main.productos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.auth.RecuperacionPassActivity
import com.nativa.myodelicious.ui.auth.RegistroActivity

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var img_OutProduc: ImageView

    private lateinit var btn_Reseña: Button

    private lateinit var btn_AgregarCarrito: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        img_OutProduc = findViewById(R.id.img_out_prodc)
        img_OutProduc.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        btn_Reseña = findViewById(R.id.btn_reseña)
        btn_Reseña.setOnClickListener {
            startActivity(Intent(this, RecuperacionPassActivity::class.java))
            finish()
        }

        btn_AgregarCarrito = findViewById(R.id.btn_agre_carrito)
        btn_AgregarCarrito.setOnClickListener {
            startActivity(Intent(this, RecuperacionPassActivity::class.java))
            finish()
        }

    }
}