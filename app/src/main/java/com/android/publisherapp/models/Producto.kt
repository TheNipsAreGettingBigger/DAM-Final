package com.android.publisherapp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

//data class Producto(val nombre: String, val tipo: String,val foto:String,val stock:String)
data class Producto(
    @Exclude var uid: String = "",
    @PropertyName("nombre") var nombre: String = "",
    @PropertyName("tipo") var tipo: String = "",
    @PropertyName("foto") var foto: String="",
    @PropertyName("stock") var stock: String ="",
    @PropertyName("precio") var precio: String ="",
    @PropertyName("filename") var filename: String =""
)