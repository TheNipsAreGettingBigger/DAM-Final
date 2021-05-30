package com.android.publisherapp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

//data class Producto(val nombre: String, val tipo: String,val foto:String,val stock:String)
data class Producto(
    @Exclude val uid: String = "",
    @PropertyName("nombre") val nombre: String = "",
    @PropertyName("tipo") val email: String = "",
    @PropertyName("foto") val foto: String="",
    @PropertyName("stock") val stock: String =""
)