package com.nativa.myodelicious.ui.main.admin.g_productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nativa.myodelicious.R
import com.nativa.myodelicious.ui.main.productos.Producto

class ProductoAdapter(
    private val esAdmin: Boolean = false,
    private val onItemClick: (Producto) -> Unit = {},
    private val onFavoritoClick: (Producto) -> Unit = {},
    private val onAgregarCarritoClick: (Producto) -> Unit = {}
) : ListAdapter<Producto, ProductoAdapter.ProductoViewHolder>(ProductoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgProducto: ImageView = itemView.findViewById(R.id.img_producto)
        private val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre_pro)
        private val tvPrecio: TextView = itemView.findViewById(R.id.tv_precio_pro)
        private val btnFavorito: ImageButton = itemView.findViewById(R.id.btn_favorito)
        private val btnAgregarCarrito: Button = itemView.findViewById(R.id.btn_agregar_carrito)

        fun bind(producto: Producto) {
            tvNombre.text = producto.nombre
            tvPrecio.text = itemView.context.getString(R.string.formato_precio, producto.precio)

            // Ícono de favorito según estado
            val favIcon = if (producto.favorito) R.drawable.ic_corazon_rojo
            else R.drawable.ic_corazon_blanco
            btnFavorito.setImageResource(favIcon)

            // Cargar imagen con Glide
            if (producto.imagenUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(producto.imagenUrl)
                    .placeholder(R.drawable.ic_productos)
                    .error(R.drawable.ic_productos)
                    .centerCrop()
                    .into(imgProducto)
            } else {
                imgProducto.setImageResource(R.drawable.ic_productos)
            }

            itemView.setOnClickListener { onItemClick(producto) }
            btnFavorito.setOnClickListener { onFavoritoClick(producto) }
            btnAgregarCarrito.setOnClickListener { onAgregarCarritoClick(producto) }
            btnFavorito.visibility      = if (esAdmin) View.GONE else View.VISIBLE
            btnAgregarCarrito.visibility = if (esAdmin) View.GONE else View.VISIBLE
        }
        }
    }

    class ProductoDiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto) =
            oldItem == newItem
    }
