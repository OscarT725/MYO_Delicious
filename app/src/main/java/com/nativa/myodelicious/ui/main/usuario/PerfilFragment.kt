package com.nativa.myodelicious.ui.main.usuario

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.main.productos.HomeFragment

class PerfilFragment : Fragment() {

    private lateinit var img_Salir_Perfil: ImageView
    private lateinit var tvEditar_Perfil: TextView
    private lateinit var lyOrdenes: LinearLayout
    private lateinit var lyMis_Direcciones: LinearLayout

    private lateinit var lyConfiguracion: LinearLayout
    private lateinit var tvAyuda: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_Salir_Perfil = view.findViewById(R.id.img_out_per)
        img_Salir_Perfil.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        tvEditar_Perfil = view.findViewById(R.id.tv_edit_per)
        tvEditar_Perfil.setOnClickListener {
            startActivity(Intent(requireContext(), EditarPerfilActivity::class.java))
        }

        lyOrdenes = view.findViewById(R.id.ly_ordenes)
        lyOrdenes.setOnClickListener {
            // startActivity(Intent(requireContext(), OrdenesActivity::class.java)) //pendiente
        }

        lyMis_Direcciones = view.findViewById(R.id.ly_direcciones)
        lyMis_Direcciones.setOnClickListener {
            startActivity(Intent(requireContext(), DireccionesActivity::class.java))
        }

        lyConfiguracion = view.findViewById(R.id.ly_config)
        lyConfiguracion.setOnClickListener {
            startActivity(Intent(requireContext(), ConfiguracionActivity::class.java))
        }

        tvAyuda = view.findViewById(R.id.tv_ayuda)
        tvAyuda.setOnClickListener {
            // startActivity(Intent(requireContext(), AyudaActivity::class.java)) //pendiente
        }
    }
}
