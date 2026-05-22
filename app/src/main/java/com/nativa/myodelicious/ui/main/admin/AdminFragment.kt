package com.nativa.myodelicious.ui.main.admin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import com.nativa.myodelicious.ui.main.admin.g_pedidos.GestionPedidosFragment
import com.nativa.myodelicious.ui.main.admin.g_productos.GestionProductosFragment
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AdminFragment : Fragment(R.layout.fragment_admin) {

    private lateinit var tvValorVentas: TextView
    private lateinit var tvValorPedidos: TextView
    private lateinit var tvValorUsuarios: TextView
    private lateinit var btnHoy: Button
    private lateinit var btnSemana: Button
    private lateinit var btnMes: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvValorVentas   = view.findViewById(R.id.tv_valor_ventas)
        tvValorPedidos  = view.findViewById(R.id.tv_valor_pedidos)
        tvValorUsuarios = view.findViewById(R.id.tv_valor_usuarios)
        btnHoy          = view.findViewById(R.id.btn_hoy)
        btnSemana       = view.findViewById(R.id.btn_semana)
        btnMes          = view.findViewById(R.id.btn_mes)

        view.findViewById<View>(R.id.ly_gest_prod).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GestionProductosFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.ly_gest_pedid).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GestionPedidosFragment())
                .addToBackStack(null)
                .commit()
        }

        resaltarBoton(btnHoy)
        cargarEstadisticas("hoy")

        btnHoy.setOnClickListener {
            resaltarBoton(btnHoy)
            cargarEstadisticas("hoy")
        }
        btnSemana.setOnClickListener {
            resaltarBoton(btnSemana)
            cargarEstadisticas("semana")
        }
        btnMes.setOnClickListener {
            resaltarBoton(btnMes)
            cargarEstadisticas("mes")
        }
    }

    private fun resaltarBoton(botonActivo: Button) {
        listOf(btnHoy, btnSemana, btnMes).forEach { btn ->
            btn.alpha = if (btn == botonActivo) 1.0f else 0.4f
        }
    }

    private fun obtenerRango(filtro: String): Pair<String, String> {
        val hoy = LocalDate.now()
        val fmt = DateTimeFormatter.ISO_LOCAL_DATE
        val desde = when (filtro) {
            "hoy"    -> hoy
            "semana" -> hoy.with(DayOfWeek.MONDAY)
            "mes"    -> hoy.withDayOfMonth(1)
            else     -> hoy
        }
        return Pair(
            "${desde.format(fmt)}T00:00:00",
            "${hoy.format(fmt)}T23:59:59"
        )
    }

    private fun cargarEstadisticas(filtro: String) {
        val (desde, hasta) = obtenerRango(filtro)

        lifecycleScope.launch {
            try {
                val resPedidos = SupabaseClient.client
                    .postgrest["pedidos"]
                    .select {
                        filter {
                            gte("created_at", desde)
                            lte("created_at", hasta)
                        }
                    }
                    .data

                val totalPedidos = contarRegistros(resPedidos)
                val totalVentas = sumarValor(resPedidos)
                val resUsuarios = SupabaseClient.client
                    .postgrest["usuarios"]
                    .select {
                        filter {
                            gte("created_at", desde)
                            lte("created_at", hasta)
                            ilike("rol", "%cliente%")
                        }
                    }
                    .data

                val totalUsuarios = contarRegistros(resUsuarios)

                requireActivity().runOnUiThread {
                    tvValorVentas.text   = "$${"%,.0f".format(totalVentas)}"
                    tvValorPedidos.text  = "$totalPedidos"
                    tvValorUsuarios.text = "$totalUsuarios"
                }

            } catch (e: Exception) {
                android.util.Log.e("AdminFragment", "Error: ${e.message}", e)
                requireActivity().runOnUiThread {
                    tvValorVentas.text   = "–"
                    tvValorPedidos.text  = "–"
                    tvValorUsuarios.text = "–"
                }
            }
        }
    }

    private fun contarRegistros(json: String): Int {
        return try {
            org.json.JSONArray(json).length()
        } catch (e: Exception) { 0 }
    }

    private fun sumarValor(json: String): Double {
        return try {
            val array = org.json.JSONArray(json)
            var suma = 0.0
            for (i in 0 until array.length()) {
                suma += array.getJSONObject(i).optDouble("valor", 0.0)
            }
            suma
        } catch (e: Exception) { 0.0 }
    }
}