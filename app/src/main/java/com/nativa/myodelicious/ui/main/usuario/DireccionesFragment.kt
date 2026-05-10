package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R


class DireccionesFragment : Fragment() {

    private val ListaDirecciones = listOf(
        Direcciones("Apartamento", "calle 13 # 30 - 163","Ciudad Verde","n/a",true),
        Direcciones("Casa","calle 48x#5a-22sur","Diana T.","n/a",false),
        Direcciones("Oficina", "calle 21#79b-19","Montevideo","n/a",false),
        Direcciones("Taller", "calle 15#34-12","Kennedy","n/a",false),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_direcciones, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_direcciones)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.adapter = DirecAdapter(ListaDirecciones)
        return view
    }

}