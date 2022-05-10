package com.adl.bookstore.data

data class BookModel(
    val judul:String,
    val gambar:String,
    val harga:String,
    val sinopsis:String
){
    constructor():this("","","","")
}
