package com.nativa.myodelicious.ui.main.productos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.ui.MainActivity
import com.nativa.myodelicious.ui.auth.LoginActivity
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteUpdateDto(val favorito: Boolean)

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rv_productos)
        progressBar  = view.findViewById(R.id.pb_cargando)

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
                val mainActivity = activity as? MainActivity
                if (mainActivity?.esInvitado == true) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                } else {
                    adapter.toggleFavoritoUI(producto)
                    actualizarFavoritoEnSupabase(producto)
                }
            },
            onAddToCartClick = { producto ->
                val mainActivity = activity as? MainActivity
                if (mainActivity?.esInvitado == true) {
                    // Si es invitado, lo llevamos a Iniciar Sesión
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                } else {
                    Toast.makeText(requireContext(), "Agregado al carrito", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        cargarProductos()
        return view
    }

    private fun cargarProductos() {
        mostrarCargando(true)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val productos = withContext(Dispatchers.IO) {
                    SupabaseClient.client
                        .from("productos")
                        .select {
                            filter {
                                eq("estatus", true)
                            }
                        }
                        .decodeList<Producto>()
                }
                adapter.actualizarLista(productos)

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar productos: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                mostrarCargando(false)
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
                // Opcional: Revertir el cambio visual si falla la base de datos
                // adapter.toggleFavoritoUI(producto)
            }
        }
    }

    private fun mostrarCargando(cargando: Boolean) {
        progressBar.visibility  = if (cargando) View.VISIBLE else View.GONE
        recyclerView.visibility = if (cargando) View.GONE    else View.VISIBLE
    }
}