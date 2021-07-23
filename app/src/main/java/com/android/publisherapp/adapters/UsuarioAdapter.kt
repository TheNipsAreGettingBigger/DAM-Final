package com.android.publisherapp.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.R
import com.android.publisherapp.models.Usuario
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UsuarioAdapter(options: FirestoreRecyclerOptions<Usuario>,context:Context) :
FirestoreRecyclerAdapter<Usuario, UsuarioAdapter.UsuarioViewHolder>(options) {
    var context_ = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.element_usuario,parent,false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int, model: Usuario) {
        holder.tv_nombre?.setText(model.nombre)
        holder.dni?.setText(model.dni)
        holder.rol?.setText(model.rol)

        if(model.foto.trim().isNotEmpty()){
            Glide.with(holder.foto_usuario?.context!!).load(model.foto).into(holder.foto_usuario!!)
        }else{
            holder.foto_usuario?.setImageResource(R.drawable.tele)
        }
    }

    class UsuarioViewHolder : RecyclerView.ViewHolder{
        var foto_usuario : ImageView ? = null
        var tv_nombre : TextView? = null
        var rol : TextView? = null
        var dni : TextView ? = null
        constructor(itemView: View) :super(itemView){
            tv_nombre = itemView.findViewById(R.id.tv_nombre)
            rol = itemView.findViewById(R.id.tv_rol)
            foto_usuario = itemView.findViewById(R.id.foto_usuario)
            dni = itemView.findViewById(R.id.tv_dni)
        }
    }
}