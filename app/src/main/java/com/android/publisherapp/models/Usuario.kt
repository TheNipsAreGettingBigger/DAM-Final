package com.android.publisherapp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

//data class Usuario(val nombre: String, val edad: Int,val email:String,val dni:String,val foto:String)
data class Usuario(
    @Exclude val uid: String = "",
    @PropertyName("nombre") val nombre: String = "",
    @PropertyName("edad") val edad: String = "",
    @PropertyName("email") val email: String="",
    @PropertyName("dni") val dni: String ="",
    @PropertyName("foto") val foto: String =""
)
