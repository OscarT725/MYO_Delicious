package com.nativa.myodelicious.ui.main.admin

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ListaUsuariosFragment : Fragment(R.layout.activity_lista_usuarios) {

    private lateinit var tableLayout: TableLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tableLayout = view.findViewById(R.id.table_usuarios)
        view.findViewById<ImageView>(R.id.img_out_list_usuario).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val json = SupabaseClient.client
                    .postgrest["usuarios"]
                    .select {
                        filter {
                            ilike("rol", "%cliente%")
                        }
                    }
                    .data
                android.util.Log.d("ListaUsuarios", "JSON: $json")
                val usuarios = parsearUsuarios(json)
                requireActivity().runOnUiThread {
                    poblarTabla(usuarios)
                }

            } catch (e: Exception) {
                android.util.Log.e("ListaUsuarios", "Error: ${e.message}", e)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun poblarTabla(usuarios: List<Pair<String, String>>) {
        if (!isAdded) return
        val context         = requireContext()
        val colorEncabezado = ContextCompat.getColor(context, R.color.blue)
        val colorFilaImpar = ContextCompat.getColor(context, R.color.white)
        val colorFilaPar = ContextCompat.getColor(context, R.color.azul_claro)
        val colorTexto = ContextCompat.getColor(context, R.color.black)
        val colorTextoHeader= ContextCompat.getColor(context, R.color.white)

        tableLayout.removeAllViews()

        val encabezado = TableRow(context).apply {
            setBackgroundColor(colorEncabezado)
            setPadding(0, 8, 0, 8)
        }
        encabezado.addView(crearCelda("#",      colorTextoHeader, true, 50))
        encabezado.addView(crearCelda("Nombre", colorTextoHeader, true, 0, 1f))
        encabezado.addView(crearCelda("Correo", colorTextoHeader, true, 0, 1f))
        tableLayout.addView(encabezado)

        if (usuarios.isEmpty()) {
            val filaVacia = TableRow(context)
            val tvVacio = crearCelda("No hay usuarios registrados",
                colorTexto, false, 0, 3f).apply { gravity = Gravity.CENTER }
            filaVacia.addView(tvVacio)
            tableLayout.addView(filaVacia)
            return
        }

        usuarios.forEachIndexed { index, (nombreCompleto, correo) ->
            val fila = TableRow(context).apply {
                setBackgroundColor(if (index % 2 == 0) colorFilaImpar else colorFilaPar)
                setPadding(0, 6, 0, 6)
            }
            fila.addView(crearCelda("${index + 1}", colorTexto, false, 50))
            fila.addView(crearCelda(nombreCompleto,  colorTexto, false, 0, 1f))
            fila.addView(crearCelda(correo,          colorTexto, false, 0, 1f))
            tableLayout.addView(fila)
        }
    }
    private fun parsearUsuarios(json: String): List<Pair<String, String>> {
        return try {
            val array = JSONArray(json)
            val lista = mutableListOf<Pair<String, String>>()

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)

                if (i == 0) {
                    val claves = obj.keys().asSequence().toList()
                    android.util.Log.d("ListaUsuarios", "Columnas disponibles: $claves")
                }

                val nombreCompleto = resolverNombre(obj)
                val correo         = resolverCorreo(obj)
                lista.add(Pair(nombreCompleto, correo))
            }
            lista
        } catch (e: Exception) {
            android.util.Log.e("ListaUsuarios", "Error al parsear: ${e.message}", e)
            emptyList()
        }
    }
    private fun resolverNombre(obj: JSONObject): String {
        val campoUnico = listOf(
            "nombre_completo", "nombreCompleto",
            "full_name",       "fullName",
            "display_name",    "displayName"
        ).firstOrNull { obj.has(it) && obj.optString(it, "").isNotBlank() }

        if (campoUnico != null) return obj.optString(campoUnico, "")
        val nombre   = listOf("nombres",   "nombre",    "first_name", "firstName")
            .firstOrNull { obj.has(it) }?.let { obj.optString(it, "") } ?: ""
        val apellido = listOf("apellidos", "apellido",  "last_name",  "lastName")
            .firstOrNull { obj.has(it) }?.let { obj.optString(it, "") } ?: ""

        val nombreUnido = "$nombre $apellido".trim()
        if (nombreUnido.isNotBlank()) return nombreUnido

        return "–"
    }
    private fun resolverCorreo(obj: JSONObject): String {
        return listOf("correo", "email", "mail", "email_address")
            .firstOrNull { obj.has(it) && obj.optString(it, "").isNotBlank() }
            ?.let { obj.optString(it, "") }
            ?: "–"
    }
    private fun crearCelda(
        texto: String,
        colorTexto: Int,
        negrita: Boolean,
        anchoFijo: Int = 0,
        peso: Float = 0f
    ): TextView {
        return TextView(requireContext()).apply {
            text     = texto
            setTextColor(colorTexto)
            textSize = 13f
            setPadding(12, 8, 12, 8)
            gravity  = Gravity.CENTER_VERTICAL
            if (negrita) setTypeface(typeface, android.graphics.Typeface.BOLD)
            layoutParams = TableRow.LayoutParams(
                if (anchoFijo > 0) dpToPx(anchoFijo) else 0,
                TableRow.LayoutParams.WRAP_CONTENT,
                peso
            )
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()
}