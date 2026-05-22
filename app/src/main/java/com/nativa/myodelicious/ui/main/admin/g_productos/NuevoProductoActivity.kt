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
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val nombre: String,
    val precio: Double,
    val imagen_url: String?,
    val categoria: String,
    val estatus: Boolean,
    val descripcion: String?,
    val ingredientes: String?,
    val tiempo_preparacion: String?
)
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
        val urlImg = etUrlImg.text.toString().trim().ifEmpty { null }
        val descripcion = etDescripcion.text.toString().trim().ifEmpty { null }
        val ingredientes = etIngredientes.text.toString().trim().ifEmpty { null }
        val tiempoPrep = etTiempoPreparacion.text.toString().trim().ifEmpty { null }
        val estatus = swEstatus.isChecked

        val producto = Producto(
            nombre = nombre,
            precio = precio,
            imagen_url = urlImg,
            categoria = categoria,
            estatus = estatus,
            descripcion = descripcion,
            ingredientes = ingredientes,
            tiempo_preparacion = tiempoPrep
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
                        runOnUiThread {
                            etNombre.setText(producto.nombre)
                            etPrecio.setText(producto.precio.toString())
                            etUrlImg.setText(producto.imagen_url ?: "")
                            etCategoria.setText(producto.categoria)
                            swEstatus.isChecked = producto.estatus
                            etDescripcion.setText(producto.descripcion ?: "")
                            etIngredientes.setText(producto.ingredientes ?: "")
                            etTiempoPreparacion.setText(producto.tiempo_preparacion ?: "")
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