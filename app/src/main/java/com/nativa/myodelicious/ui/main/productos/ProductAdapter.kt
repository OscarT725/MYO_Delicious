package com.nativa.myodelicious.ui.main.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nativa.myodelicious.R
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter (private val productos: List<Productos>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imagen: ImageView = itemView.findViewById(R.id.img_producto)
        val nombre: TextView = itemView.findViewById(R.id.tv_nombre_pro)
        val precio: TextView = itemView.findViewById(R.id.tv_precio_pro)
        val btnAgregar: Button = itemView.findViewById(R.id.btn_agregar_carrito)
        val btnFavorito: ImageButton = itemView.findViewById(R.id.btn_favorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_producto,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productos[position]
        holder.imagen.setImageResource(producto.imagenRes)
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio}"
        
        holder.btnAgregar.setOnClickListener {

        }

        actualizarIconoCorazon(holder.btnFavorito, producto.esFavorito)

        holder.btnFavorito.setOnClickListener {
            producto.esFavorito = !producto.esFavorito
            actualizarIconoCorazon(holder.btnFavorito, producto.esFavorito)
        }
    }

    private fun actualizarIconoCorazon(btn: ImageButton, esFavorito: Boolean) {
        if (esFavorito) {
            btn.setImageResource(R.drawable.ic_corazon_rojo)
            btn.setColorFilter(
                ContextCompat.getColor(btn.context, android.R.color.holo_red_light)
            )
        } else {
            btn.setImageResource(R.drawable.ic_corazon_blanco)
            btn.clearColorFilter()
        }
    }
}
