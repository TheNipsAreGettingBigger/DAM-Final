package com.android.publisherapp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Usuario(
//    @Exclude val uid: String = "",
    @PropertyName("nombre") var nombre: String = "",
    @PropertyName("edad") var edad: String = "",
    @PropertyName("email") var email: String="",
    @PropertyName("dni") var dni: String ="",
    @PropertyName("rol") var rol: String ="",
    @PropertyName("foto") var foto: String =""
)
