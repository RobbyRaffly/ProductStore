package com.adl.bookstore.data

import android.graphics.SweepGradient

data class ProductModel(
    val category:String,
    val description:String,
    val fullDescription:String,
    val image:String,
    val price:String,
    val productName:String
){
    constructor():this("","","","","","")
}
