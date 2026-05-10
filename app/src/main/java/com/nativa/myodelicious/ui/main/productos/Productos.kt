package com.nativa.myodelicious.ui.main.productos

import android.R

data class Productos(
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    var esFavorito: Boolean = false,
    val categoria: String

)
