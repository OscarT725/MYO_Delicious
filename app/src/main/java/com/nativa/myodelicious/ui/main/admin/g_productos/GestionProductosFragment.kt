@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)
package com.nativa.myodelicious.ui.main.admin.g_productos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.ui.main.productos.Producto
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GestionProductosFragment : Fragment(R.layout.fragment_gestion_productos) {

    private var listaCompleta: List<Producto> = emptyList()
    private var categoriaActual: String = CATEGORIA_TODOS
    private var busquedaActual: String = ""
    private lateinit var adapter: ProductoAdapter
    private lateinit var tvTotalProd: TextView
    private lateinit var tvActivos: TextView
    private lateinit var tvAgotados: TextView
    private lateinit var etBuscar: EditText

    companion object {
        const val CATEGORIA_TODOS   = "TODOS"
        const val CATEGORIA_TORTA   = "Tortas"
        const val CATEGORIA_POSTRE  = "Postres"
        const val CATEGORIA_GALLETA = "Galletas"
        const val TABLA_PRODUCTOS   = "productos"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.img_out_admin).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<View>(R.id.ly_nuevo_prod).setOnClickListener {
            startActivity(Intent(requireContext(), NuevoProductoActivity::class.java))
        }

        tvTotalProd = view.findViewById(R.id.tv_valor_ventas)
        tvActivos   = view.findViewById(R.id.tv_valor_pedidos)
        tvAgotados  = view.findViewById(R.id.tv_valor_usuarios)

        adapter = ProductoAdapter(
            esAdmin = true,
            onItemClick = { producto ->
                val intent = Intent(requireContext(), NuevoProductoActivity::class.java).apply {
                    putExtra("PRODUCTO_ID", producto.id)
                }
                startActivity(intent)
            }
        )
        view.findViewById<RecyclerView>(R.id.rv_productos).apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@GestionProductosFragment.adapter
        }

        etBuscar = view.findViewById(R.id.et_buscar)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                busquedaActual = s?.toString()?.trim() ?: ""
                aplicarFiltros()
            }
        })

        view.findViewById<ImageView>(R.id.imn_torta_choc).setOnClickListener {
            categoriaActual = CATEGORIA_TORTA
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_postre).setOnClickListener {
            categoriaActual = CATEGORIA_POSTRE
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_galleta).setOnClickListener {
            categoriaActual = CATEGORIA_GALLETA
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_all_prod).setOnClickListener {
            categoriaActual = CATEGORIA_TODOS
            aplicarFiltros()
        }
        cargarProductos()
    }
    private fun cargarProductos() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Obtenemos el JSON crudo primero para diagnosticar en caso de error
                val response = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest[TABLA_PRODUCTOS]
                        .select()
                }
                
                android.util.Log.d("GestionProductos", "JSON recibido: ${response.data}")

                val productos = response.decodeList<Producto>()

                listaCompleta = productos

                val total    = productos.size
                val activos  = productos.count { it.estatus }
                val agotados = productos.count { !it.estatus }

                tvTotalProd.text = total.toString()
                tvActivos.text   = activos.toString()
                tvAgotados.text  = agotados.toString()

                aplicarFiltros()

            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("GestionProductos", "Error al cargar productos: ${e.message}", e)
                Toast.makeText(
                    requireContext(),
                    "Error al cargar productos: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun aplicarFiltros() {
        var resultado = listaCompleta

        if (categoriaActual != CATEGORIA_TODOS) {
            resultado = resultado.filter {
                it.categoria.equals(categoriaActual, ignoreCase = true)
            }
        }

        if (busquedaActual.isNotEmpty()) {
            resultado = resultado.filter {
                it.nombre.contains(busquedaActual, ignoreCase = true)
            }
        }

        adapter.submitList(resultado)
    }
    override fun onResume() {
        super.onResume()
        cargarProductos()
    }
}