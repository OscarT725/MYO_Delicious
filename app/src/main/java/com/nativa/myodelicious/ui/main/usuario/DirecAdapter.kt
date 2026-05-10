package com.nativa.myodelicious.ui.main.usuario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nativa.myodelicious.R
import androidx.recyclerview.widget.RecyclerView

class DirecAdapter (private val direcciones: List<Direcciones>) : RecyclerView.Adapter<DirecAdapter.ProductViewHolder>(){

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nomDireccion: TextView = itemView.findViewById(R.id.tv_nombre_direc)

        val direccion: TextView = itemView.findViewById(R.id.tv_direccion)

        val barrio: TextView = itemView.findViewById(R.id.tv_barrio)

        val btnEditarDir: Button = itemView.findViewById(R.id.btn_editar_direc)

        val btnEliminarDir: Button = itemView.findViewById(R.id.btn_elimin_direc)

        val swDirecPrin: Switch = itemView.findViewById(R.id.sw_dir_prin)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_direccion,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = direcciones.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val direccion = direcciones[position]
        holder.nomDireccion.text = direccion.nombreDirec
        holder.direccion.text = direccion.direccion
        holder.barrio.text = direccion.barrio
        
        holder.btnEditarDir.setOnClickListener {

        }

        holder.btnEliminarDir.setOnClickListener {

        }

    }

}
