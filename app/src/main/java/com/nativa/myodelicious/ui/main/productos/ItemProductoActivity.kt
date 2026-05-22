package com.nativa.myodelicious.ui.main.productos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.auth.RegistroActivity

class ItemProductoActivity : AppCompatActivity() {

    private lateinit var img_Produc: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_producto)

        img_Produc = findViewById(R.id.img_producto)
        img_Produc.setOnClickListener {
            startActivity(Intent(this, DetalleProductoActivity::class.java))
            finish()
        }
    }
}