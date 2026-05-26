package com.nativa.myodelicious.ui.main.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nativa.myodelicious.R

class ProductAdapter(
    initialProductos: List<Producto> = listOf(),
    private val onItemClick: ((Producto) -> Unit)? = null,
    private val onFavoriteClick: ((Producto) -> Unit)? = null,
    private val onAddToCartClick: ((Producto) -> Unit)? = null // Nuevo lambda para el carrito
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productos: MutableList<Producto> = initialProductos.toMutableList()

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView      = itemView.findViewById(R.id.img_producto)
        val nombre: TextView       = itemView.findViewById(R.id.tv_nombre_pro)
        val precio: TextView       = itemView.findViewById(R.id.tv_precio_pro)
        val btnAgregar: Button     = itemView.findViewById(R.id.btn_agregar_carrito)
        val btnFavorito: ImageButton = itemView.findViewById(R.id.btn_favorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_producto, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val producto = productos[position]

        holder.nombre.text = producto.nombre
        holder.precio.text = "$ ${String.format("%,.0f", producto.precio)}"

        val imageToLoad = if (producto.imagenRes != null && producto.imagenRes != 0) producto.imagenRes else producto.imagenUrl

        Glide.with(holder.itemView.context)
            .load(imageToLoad)
            .placeholder(R.drawable.fron_productos1)
            .error(R.drawable.fron_productos1)
            .centerCrop()
            .into(holder.imagen)

        actualizarIconoCorazon(holder.btnFavorito, producto.favorito)
        holder.btnFavorito.setOnClickListener {
            onFavoriteClick?.invoke(producto)
        }
        holder.imagen.setOnClickListener {
            onItemClick?.invoke(producto)
        }
        holder.btnAgregar.setOnClickListener {
            onAddToCartClick?.invoke(producto)
        }
    }

    fun actualizarLista(nuevosProductos: List<Producto>) {
        productos.clear()
        productos.addAll(nuevosProductos)
        notifyDataSetChanged()
    }
    fun toggleFavoritoUI(producto: Producto) {
        producto.favorito = !producto.favorito
        notifyDataSetChanged()
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