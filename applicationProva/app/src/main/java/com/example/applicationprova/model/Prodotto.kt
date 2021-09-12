package com.example.applicationprova.model

/**
 * Data class per la classe prodotto
 */
data class Prodotto(
    val nome: String?=null, val categoria: String?=null, val quantita: String? =null,
    val note: String?=null,
    val iduser: String?=null, val nomeutente:String?=null, val buy:String="0")
