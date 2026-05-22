package com.nativa.myodelicious.ui.main.carrito

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.main.productos.HomeFragment
import com.nativa.myodelicious.ui.main.usuario.CuponDescuentoActivity

class CarritoFragment : Fragment() {

    private lateinit var img_OutCAr: ImageView
    private lateinit var btn_CupCar: Button
    private lateinit var btn_PagarCAr: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_carrito, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_OutCAr = view.findViewById(R.id.img_out_car)
        img_OutCAr.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        btn_CupCar = view.findViewById(R.id.btn_cup_car)
        btn_CupCar.setOnClickListener {

            startActivity(Intent(requireContext(), CuponDescuentoActivity::class.java))
        }

        btn_PagarCAr = view.findViewById(R.id.btn_pago_car)
        btn_PagarCAr.setOnClickListener {
            startActivity(Intent(requireContext(), PasarelaPagoActivity::class.java))
        }
    }
}
