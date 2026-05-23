package com.nativa.myodelicious.ui.main.admin.g_pedidos

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class Pedido(
    val id: String = "",

    @SerialName("created_at")
    val createdAt: String? = null,

    val item: String = "",

    val valor: Double = 0.0,

    val cliente: String = "",

    val direccion: String = "",

    val estatus: String = "",

    @SerialName("cantidad_items")
    val cantidadItems: Int = 0
)

data class Pedidos(
    val item: String,
    val valor: Double,
    var cliente: String,
    val direccion: String,
    val estatus: String
)
