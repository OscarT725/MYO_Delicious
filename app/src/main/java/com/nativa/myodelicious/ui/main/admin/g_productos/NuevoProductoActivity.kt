package com.nativa.myodelicious.ui.main.admin.g_productos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.ui.main.productos.Producto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class NuevoProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etUrlImg: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etCategoria: EditText
    private lateinit var etPrecio: EditText
    private lateinit var swEstatus: Switch
    private lateinit var etIngredientes: EditText
    private lateinit var etTiempoPreparacion: EditText // Este es tu et_stock
    private lateinit var btnGuardar: Button
    private lateinit var imgRegresar: ImageView

    private var productoId: String? = null
    private var esFavoritoOriginal: Boolean = false
    private var createdAtOriginal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_producto)
        initViews()


        productoId = intent.getStringExtra("PRODUCTO_ID")
        if (productoId != null) {
            cargarDatosProducto()
        }

        btnGuardar.setOnClickListener {
            guardarProductoEnSupabase()
        }

        imgRegresar.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        imgRegresar = findViewById(R.id.img_out_nuevo_prod)
        etNombre = findViewById(R.id.et_nombre_prod)
        etUrlImg = findViewById(R.id.et_url_img)
        etDescripcion = findViewById(R.id.et_descrip_prod)
        etCategoria = findViewById(R.id.et_categoria)
        etPrecio = findViewById(R.id.et_precio)
        swEstatus = findViewById(R.id.sw_estatus)
        etIngredientes = findViewById(R.id.et_ingre_prod)
        etTiempoPreparacion = findViewById(R.id.et_stock) // Vinculado a et_stock
        btnGuardar = findViewById(R.id.btn_guardar_producto)
    }

    private fun guardarProductoEnSupabase() {
        val nombre = etNombre.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val categoria = etCategoria.text.toString().trim()

        if (nombre.isEmpty() || precioStr.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos obligatorios (Nombre, Precio, Categoría)", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull() ?: 0.0
        val urlImg = etUrlImg.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val ingredientes = etIngredientes.text.toString().trim()
        val tiempoPrep = etTiempoPreparacion.text.toString().trim()
        val estatus = swEstatus.isChecked

        val producto = Producto(
            id = productoId ?: "",
            nombre = nombre,
            precio = precio,
            imagenUrl = urlImg,
            categoria = categoria,
            estatus = estatus,
            descripcion = descripcion,
            ingredientes = ingredientes,
            tiempoPreparacion = tiempoPrep,
            favorito = esFavoritoOriginal, // Mantenemos el estado de favorito
            createdAt = createdAtOriginal  // Mantenemos la fecha de creación si existe
        )

        lifecycleScope.launch {
            try {
                
                SupabaseClient.client.postgrest["productos"].upsert(producto)

                runOnUiThread {
                    Toast.makeText(this@NuevoProductoActivity, "¡Producto guardado con éxito!", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad y regresa
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@NuevoProductoActivity, "Error al guardar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cargarDatosProducto() {
        productoId?.let { id ->

            lifecycleScope.launch {
                try {
                    val producto = SupabaseClient.client.postgrest["productos"]
                        .select {
                            filter {
                                eq("id", id)
                            }
                        }.decodeSingleOrNull<Producto>()

                    if (producto != null) {
                        productoId = producto.id
                        esFavoritoOriginal = producto.favorito
                        createdAtOriginal = producto.createdAt

                        runOnUiThread {
                            etNombre.setText(producto.nombre)
                            etPrecio.setText(producto.precio.toString())
                            etUrlImg.setText(producto.imagenUrl)
                            etCategoria.setText(producto.categoria)
                            swEstatus.isChecked = producto.estatus
                            etDescripcion.setText(producto.descripcion)
                            etIngredientes.setText(producto.ingredientes)
                            etTiempoPreparacion.setText(producto.tiempoPreparacion)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@NuevoProductoActivity, "El producto no existe o fue eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@NuevoProductoActivity, "Error al cargar los datos: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
