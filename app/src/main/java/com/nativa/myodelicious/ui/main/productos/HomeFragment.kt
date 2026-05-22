package com.nativa.myodelicious.ui.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R


class HomeFragment : Fragment() {



    private val ListaProductos = listOf(
        Productos("Torta de chocolate", precio = 45.000, imagenRes = R.drawable.torta_de_chocolate,esFavorito = true,categoria = "Tortas"),
        Productos("Postre de maracuya", precio = 15.000, imagenRes = R.drawable.postre_maracuya,esFavorito = false,categoria = "Postres"),
        Productos("Galleta chip de chocolate", precio = 3.000, imagenRes = R.drawable.galleta_chip,esFavorito = false,categoria = "Galletas"),
        Productos("Postre de tres leches, canela", precio = 15.000, imagenRes = R.drawable.postre_tre_leches,esFavorito = true,categoria = "Postres"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductAdapter(ListaProductos)
        return view
        }




    }

