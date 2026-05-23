package com.nativa.myodelicious.ui.main.admin.g_pedidos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray

class GestionPedidosFragment : Fragment(R.layout.fragment_gestion_pedidos) {

    private lateinit var btnTodos: Button
    private lateinit var btnActivos: Button
    private lateinit var btnEntregados: Button
    private lateinit var btnCancelados: Button
    private lateinit var rvPedidos: RecyclerView
    private lateinit var adapter: PedidosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTodos      = view.findViewById(R.id.btn_todos_p)
        btnActivos    = view.findViewById(R.id.btn_p_activos)
        btnEntregados = view.findViewById(R.id.btn_p_entreg)
        btnCancelados = view.findViewById(R.id.btn_p_cancel)
        rvPedidos     = view.findViewById(R.id.rv_pedidos) //Pendiente creacion

        adapter = PedidosAdapter(emptyList())
        rvPedidos.layoutManager = LinearLayoutManager(requireContext())
        rvPedidos.adapter = adapter

        view.findViewById<ImageView>(R.id.img_out_g_pedido).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        btnTodos.setOnClickListener      { resaltarBoton(btnTodos);      cargarPedidos(null)        }
        btnActivos.setOnClickListener    { resaltarBoton(btnActivos);    cargarPedidos("Activo")    }
        btnEntregados.setOnClickListener { resaltarBoton(btnEntregados); cargarPedidos("Entregado") }
        btnCancelados.setOnClickListener { resaltarBoton(btnCancelados); cargarPedidos("Cancelado") }

        resaltarBoton(btnTodos)
        cargarPedidos(null)
    }

    private fun resaltarBoton(botonActivo: Button) {
        listOf(btnTodos, btnActivos, btnEntregados, btnCancelados).forEach { btn ->
            btn.alpha = if (btn == botonActivo) 1.0f else 0.4f
        }
    }

    private fun cargarPedidos(estatus: String?) {
        lifecycleScope.launch {
            try {
                val json = if (estatus == null) {
                    // Todos los pedidos — bloque vacío igual que AdminFragment
                    SupabaseClient.client
                        .postgrest["pedidos"]
                        .select { }
                        .data
                } else {
                    // Filtrado por el campo "estatus" de la tabla
                    SupabaseClient.client
                        .postgrest["pedidos"]
                        .select {
                            filter {
                                eq("estatus", estatus)
                            }
                        }
                        .data
                }

                android.util.Log.d("GestionPedidos", "JSON: $json")

                val lista = parsearPedidos(json)

                requireActivity().runOnUiThread {
                    adapter.actualizarLista(lista)
                    if (lista.isEmpty()) {
                        Toast.makeText(requireContext(),
                            "No hay pedidos para mostrar", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("GestionPedidos", "Error: ${e.message}", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun parsearPedidos(json: String): List<Pedido> {
        return try {
            val array = JSONArray(json)
            val lista = mutableListOf<Pedido>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                lista.add(
                    Pedido(
                        id            = obj.optString("id", ""),
                        createdAt     = obj.optString("created_at", null),
                        item          = obj.optString("item", ""),
                        valor         = obj.optDouble("valor", 0.0),
                        cliente       = obj.optString("cliente", ""),
                        direccion     = obj.optString("direccion", ""),
                        estatus       = obj.optString("estatus", ""),
                        cantidadItems = obj.optInt("cantidad_items", 0)
                    )
                )
            }
            lista
        } catch (e: Exception) {
            android.util.Log.e("GestionPedidos", "Error al parsear: ${e.message}", e)
            emptyList()
        }
    }
}