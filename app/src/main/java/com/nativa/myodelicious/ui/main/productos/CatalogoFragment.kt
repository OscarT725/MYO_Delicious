package com.nativa.myodelicious.ui.main.productos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.ui.main.admin.g_productos.GestionProductosFragment
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogoFragment : Fragment(R.layout.fragment_catalogo) {

    private var listaCompleta: List<Producto> = emptyList()
    private var categoriaActual: String = GestionProductosFragment.CATEGORIA_TODOS
    private var busquedaActual: String = ""
    private lateinit var adapter: ProductAdapter

    companion object {
        private const val TAG = "CatalogoFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter(
            onItemClick = { producto ->
                val intent = Intent(requireContext(), DetalleProductoActivity::class.java).apply {
                    putExtra("producto_nombre",producto.nombre)
                    putExtra("producto_precio",producto.precio)
                    putExtra("producto_imagen",producto.imagenUrl)
                    putExtra("producto_descripcion",producto.descripcion)
                    putExtra("producto_ingredientes",producto.ingredientes)
                    putExtra("producto_tiempo",producto.tiempoPreparacion)
                }
                startActivity(intent)
            },
            onFavoriteClick = { producto ->
                actualizarFavoritoEnSupabase(producto)
            }
        )

        view.findViewById<RecyclerView>(R.id.rv_productos).apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@CatalogoFragment.adapter
        }

        view.findViewById<ImageView>(R.id.imn_torta_choc).setOnClickListener {
            categoriaActual = GestionProductosFragment.CATEGORIA_TORTA
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_postre).setOnClickListener {
            categoriaActual = GestionProductosFragment.CATEGORIA_POSTRE
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_galleta).setOnClickListener {
            categoriaActual = GestionProductosFragment.CATEGORIA_GALLETA
            aplicarFiltros()
        }
        view.findViewById<ImageView>(R.id.img_all_prod).setOnClickListener {
            categoriaActual = GestionProductosFragment.CATEGORIA_TODOS
            aplicarFiltros()
        }
        cargarProductos()
    }

    private fun cargarProductos() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    SupabaseClient.client.postgrest[GestionProductosFragment.TABLA_PRODUCTOS]
                        .select()
                }

                android.util.Log.d("GestionProductos", "JSON recibido: ${response.data}")

                val productos = response.decodeList<Producto>()
                listaCompleta = productos
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

    private fun actualizarFavoritoEnSupabase(producto: Producto) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    SupabaseClient.client
                        .from("productos")
                        .update(FavoriteUpdateDto(producto.favorito)) {
                            filter {
                                eq("id", producto.id)
                            }
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al actualizar favorito: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun aplicarFiltros() {
        var resultado = listaCompleta

        if (categoriaActual != GestionProductosFragment.CATEGORIA_TODOS) {
            resultado = resultado.filter {
                it.categoria.equals(categoriaActual, ignoreCase = true)
            }
        }

        if (busquedaActual.isNotEmpty()) {
            resultado = resultado.filter {
                it.nombre.contains(busquedaActual, ignoreCase = true)
            }
        }

        adapter.actualizarLista(resultado)
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }
}