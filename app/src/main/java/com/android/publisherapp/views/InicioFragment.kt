package com.android.publisherapp.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.publisherapp.R
import com.android.publisherapp.models.Usuario
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class InicioFragment : Fragment() {
    var container:ConstraintLayout ? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view.findViewById(R.id.container_mi_info)
        val preferences = activity?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val email = preferences?.getString("email", "")
        FirebaseFirestore.getInstance().collection("users").document(email.toString())
            .get().addOnSuccessListener {
                var user = Usuario(it.get("nombre").toString(),it.get("edad").toString(),it.get("email").toString(),it.get("dni").toString())
                user.foto = it.get("foto").toString()
                user.rol = it.get("rol").toString()
                drawUser(user)
            }
    }

    fun drawUser(user:Usuario){
        val imagen = container?.findViewById<ImageView>(R.id.iv_usuario)
        Glide.with(imagen?.context!!).load(user.foto).into(imagen!!)
        container?.findViewById<TextView>(R.id.txt_nombre)?.setText(user.nombre)
        container?.findViewById<TextView>(R.id.txt_dni)?.setText(user.dni)
        container?.findViewById<TextView>(R.id.txt_email)?.setText(user.email)
        container?.findViewById<TextView>(R.id.txt_rol)?.setText(user.rol)
    }

}