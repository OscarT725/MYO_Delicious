package com.nativa.myodelicious.ui.main.usuario

data class Direcciones(

    val nombreDirec: String,
    val direccion: String,
    val barrio: String,
    val notas: String,
    var principal: Boolean = false
)
