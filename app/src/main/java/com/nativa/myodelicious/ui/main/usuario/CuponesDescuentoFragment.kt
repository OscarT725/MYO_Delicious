package com.nativa.myodelicious.ui.main.usuario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.main.carrito.CarritoFragment


class CuponesDescuentoFragment : Fragment() {

    private lateinit var img_OutCup: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cupones_descuento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_OutCup = view.findViewById(R.id.img_out_cup)
        img_OutCup.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CarritoFragment())
                .commit()
        }
    }
}