package com.nativa.myodelicious.ui.main.productos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class Producto(
    val id: String = "",

    @SerialName("created_at")
    val createdAt: String? = null,

    val nombre: String = "",

    val precio: Double = 0.0,

    @SerialName("imagen_url")
    val imagenUrl: String = "",

    val categoria: String = "",

    val favorito: Boolean = false,

    val estatus: Boolean = true,

    val descripcion: String = "",

    val ingredientes: String = "",

    @SerialName("tiempo_preparacion")
    val tiempoPreparacion: String = ""
)

data class Productos(
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    var esFavorito: Boolean,
    val categoria: String
)
