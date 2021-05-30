package com.android.publisherapp.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Usuario(val nombre: String, val edad: Int,val email:String,val dni:String,val foto:String)
