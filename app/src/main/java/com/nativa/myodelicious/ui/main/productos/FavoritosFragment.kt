package com.nativa.myodelicious.ui.main.productos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.rv_favoritos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

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
        recyclerView.adapter = adapter

        cargarFavoritos()
        return view
    }

    private fun cargarFavoritos() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val productos = withContext(Dispatchers.IO) {
                    SupabaseClient.client
                        .from("productos")
                        .select {
                            filter {
                                eq("favorito", true)
                            }
                        }
                        .decodeList<Producto>()
                }
                adapter.actualizarLista(productos)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar favoritos: ${e.message}",
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
                // Al desmarcar de favoritos actualizar la lista
                if (!producto.favorito) {
                    cargarFavoritos()
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

    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }
}