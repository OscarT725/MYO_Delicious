package com.nativa.myodelicious.ui.main.admin.g_productos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.main.usuario.EditarPerfilActivity

class GestionProductosFragment : Fragment(R.layout.fragment_gestion_productos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegresar = view.findViewById<ImageView>(R.id.img_out_admin)
        btnRegresar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val btnNuevo = view.findViewById<View>(R.id.ly_nuevo_prod)
        btnNuevo.setOnClickListener {
            startActivity(Intent(requireContext(), NuevoProductoActivity::class.java))
        }

    }
}