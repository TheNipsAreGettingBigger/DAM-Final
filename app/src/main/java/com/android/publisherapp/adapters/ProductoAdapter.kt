package com.android.publisherapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.FragmentProductoDetalle
import com.android.publisherapp.R
import com.android.publisherapp.models.Producto
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ProductoAdapter(options: FirestoreRecyclerOptions<Producto>,context:Context) :
    FirestoreRecyclerAdapter<Producto, ProductoAdapter.ProductViewHolder>(options) {
    var context_ = context
//    https://www.youtube.com/watch?v=sZ8D1-hNeWo
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Producto) {
        holder.tv_nombre?.setText(model.nombre)
        holder.tv_stock?.setText(model.stock)
        holder.tv_tipo?.setText(model.tipo)
        holder.tv_precio?.setText("${model.precio} soles")
        holder.fila?.tag = model.uid
        if(model.foto.trim().isNotEmpty()){
            Glide.with(holder.iv_producto?.context!!).load(model.foto).into(holder.iv_producto!!)
        }else{
            holder.iv_producto?.setImageResource(R.drawable.tele)
        }

        holder.fila?.setOnClickListener{
            Log.i("tag",holder.fila?.tag.toString())
            FirebaseFirestore.getInstance().collection("productos").document(holder.fila?.tag.toString()).get()
                .addOnSuccessListener {
                    val producto:Producto = Producto(
                        it.get("uid") as String,
                        it.get("nombre") as String,
                        it.get("tipo") as String,
                        it.get("stock") as String,
                        it.get("precio") as String,
                    )
                    if(it.get("filename") !=null){
                        producto.filename = it.get("filename") as String
                    }
                    if(it.get("foto")!=null){
                        producto.foto = it.get("foto") as String
                    }
                    val fragmentManager:FragmentManager = (context_ as AppCompatActivity).getSupportFragmentManager()
                    val fragmentTransaction:FragmentTransaction = fragmentManager.beginTransaction()
                    val clienteDetalle = FragmentProductoDetalle(context_)
                    clienteDetalle.producto = producto
                    fragmentTransaction.replace(R.id.frgPrincipal,clienteDetalle)
                    fragmentTransaction.commit()

                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.elemento_producto,parent,false)
        return ProductViewHolder(view)
    }

    class ProductViewHolder : RecyclerView.ViewHolder{

        var tv_nombre : TextView? = null
        var tv_stock : TextView? = null
        var iv_producto : ImageView ?= null
        var tv_precio : TextView?=null
        var tv_tipo: TextView ?= null
        var fila:ConstraintLayout?=null

        constructor(itemView:View) :super(itemView){

            tv_nombre = itemView.findViewById(R.id.tv_nombre)
            tv_stock = itemView.findViewById(R.id.tv_stock)
            iv_producto = itemView.findViewById(R.id.iv_producto)
            tv_tipo = itemView.findViewById(R.id.tv_tipo)
            tv_precio = itemView.findViewById(R.id.tv_precio)
            fila=itemView.findViewById(R.id.fila)

        }
    }

}