package com.nativa.myodelicious.ui.main.admin.g_pedidos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nativa.myodelicious.R

class PedidosAdapter(
    private var pedidos: List<Pedido>
) : RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumPedido:   TextView = itemView.findViewById(R.id.tv_num_pedido)
        val tvEstatus:     TextView = itemView.findViewById(R.id.tv_estatus)
        val tvFecha:       TextView = itemView.findViewById(R.id.tv_fecha_pedido)
        val tvItems:       TextView = itemView.findViewById(R.id.tv_items_pedido)
        val tvTotal:       TextView = itemView.findViewById(R.id.tv_total_pedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_pedidos, parent, false)
        return PedidoViewHolder(view)
    }

    override fun getItemCount(): Int = pedidos.size

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        val idCorto = if (pedido.id.length > 8) pedido.id.substring(0, 8) else pedido.id
        holder.tvNumPedido.text = idCorto
        holder.tvEstatus.text = pedido.estatus
        val colorFondo = when (pedido.estatus.lowercase()) {
            "activo"    -> Color.parseColor("#2196F3") // azul
            "entregado" -> Color.parseColor("#4CAF50") // verde
            "cancelado" -> Color.parseColor("#F44336") // rojo
            else        -> Color.parseColor("#9E9E9E") // gris
        }
        holder.tvEstatus.setBackgroundColor(colorFondo)
        holder.tvFecha.text = pedido.createdAt?.let { raw ->
            try {
                val partes = raw.split("T")
                val fecha  = partes[0]
                val hora   = if (partes.size > 1) partes[1].substring(0, 5) else ""
                "$fecha  $hora"
            } catch (e: Exception) { raw }
        } ?: ""

        holder.tvItems.text = pedido.cantidadItems.toString()
        holder.tvTotal.text = "$${"%,.0f".format(pedido.valor)}"
    }

    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}