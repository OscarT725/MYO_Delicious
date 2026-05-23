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
import android.widget.Toast
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import androidx.lifecycle.lifecycleScope
import com.nativa.myodelicious.R
import com.nativa.myodelicious.data.UsuarioRepository
import com.nativa.myodelicious.ui.main.admin.AdminFragment
import com.nativa.myodelicious.ui.main.productos.HomeFragment
import kotlinx.coroutines.launch

class PerfilFragment : Fragment() {

    private lateinit var ivFoto: ImageView

    private lateinit var tvNombreUsuario: TextView
    private var currentFotoUrl: String? = null
    private lateinit var img_Salir_Perfil: ImageView
    private lateinit var tvEditar_Perfil: TextView
    private lateinit var lyOrdenes: LinearLayout
    private lateinit var lyMis_Direcciones: LinearLayout

    private lateinit var lyConfiguracion: LinearLayout
    private lateinit var tvAyuda: TextView


    companion object {
        fun newInstance(esAdmin: Boolean): PerfilFragment {
            val fragment = PerfilFragment()
            val args = Bundle()
            args.putBoolean("esAdmin", esAdmin)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val esAdmin = arguments?.getBoolean("esAdmin") ?: false

        img_Salir_Perfil = view.findViewById(R.id.img_out_per)
        img_Salir_Perfil.setOnClickListener {

            val fragmentDestino = if (esAdmin) AdminFragment() else HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentDestino)
                .commit()
        }
        tvNombreUsuario = view.findViewById(R.id.tv_nombre_usuario)
        ivFoto = view.findViewById(R.id.img_usuario)

        viewLifecycleOwner.lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()

            if (usuario != null) {
                tvNombreUsuario.text = "${usuario.nombres} ${usuario.apellidos}"
                if (!usuario.foto_url.isNullOrEmpty()) {
                    currentFotoUrl = usuario.foto_url
                    val urlConTimestamp =
                        "${usuario.foto_url}?timestamp=${System.currentTimeMillis()}"
                    ivFoto.load(urlConTimestamp) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.logo2)
                        error(R.drawable.logo2)
                        memoryCachePolicy(CachePolicy.DISABLED)
                        diskCachePolicy(CachePolicy.DISABLED)
                    }
                } else {
                    Toast.makeText(requireContext(), "No hay foto", Toast.LENGTH_SHORT).show()
                }
            }
        }
        tvEditar_Perfil = view.findViewById(R.id.tv_edit_per)
        tvEditar_Perfil.setOnClickListener {
            startActivity(Intent(requireContext(), EditarPerfilActivity::class.java))
        }

        lyOrdenes = view.findViewById(R.id.ly_ordenes)
        lyMis_Direcciones = view.findViewById(R.id.ly_direcciones)
        lyConfiguracion = view.findViewById(R.id.ly_config)
        tvAyuda = view.findViewById(R.id.tv_ayuda)


        if (esAdmin) {
            lyOrdenes.visibility = View.GONE
            lyMis_Direcciones.visibility = View.GONE
        } else {
            lyOrdenes.visibility = View.VISIBLE
            lyMis_Direcciones.visibility = View.VISIBLE

            lyOrdenes.setOnClickListener {
                startActivity(Intent(requireContext(), HistorialPedidosActivity::class.java))
            }
            lyMis_Direcciones.setOnClickListener {
                startActivity(Intent(requireContext(), DireccionesActivity::class.java))
            }
        }

        lyConfiguracion.setOnClickListener {
            val intent = Intent(requireContext(), ConfiguracionActivity::class.java)
            intent.putExtra("esAdmin", esAdmin)
            startActivity(intent)
        }
        tvAyuda.setOnClickListener {
            startActivity(Intent(requireContext(), AyudaActivity::class.java))
        }
    }
}
