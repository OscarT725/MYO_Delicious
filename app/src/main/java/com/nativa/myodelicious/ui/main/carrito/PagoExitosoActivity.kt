package com.nativa.myodelicious.ui.main.carrito

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.MainActivity
import com.nativa.myodelicious.ui.main.productos.CatalogoFragment
import com.nativa.myodelicious.ui.main.productos.HomeFragment
import com.nativa.myodelicious.ui.main.usuario.CrearDireccionActivity

class PagoExitosoActivity : AppCompatActivity() {

    private lateinit var img_Home: ImageView

    private lateinit var btn_Home: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pago_exitoso)

        img_Home = findViewById(R.id.img_out_hom)
        img_Home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btn_Home = findViewById(R.id.btn_home)
        btn_Home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}