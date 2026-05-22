package com.nativa.myodelicious.ui.main.admin.g_pedidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nativa.myodelicious.R

class GestionPedidosFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnOutPedidos = view.findViewById<ImageView>(R.id.img_out_g_pedidos)
        btnOutPedidos.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }
}