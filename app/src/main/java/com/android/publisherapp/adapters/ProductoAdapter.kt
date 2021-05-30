package com.android.publisherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.R
import com.android.publisherapp.models.Producto
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.jetbrains.annotations.NotNull
import org.w3c.dom.Text

class ProductoAdapter(options: FirestoreRecyclerOptions<Producto>) :
    FirestoreRecyclerAdapter<Producto, ProductoAdapter.ProductViewHolder>(options) {
//    https://www.youtube.com/watch?v=sZ8D1-hNeWo
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Producto) {
        holder.tv_nombre?.setText(model.nombre)
        holder.tv_stock?.setText(model.stock)
        if(!model.foto.isNullOrEmpty()){
            Glide.with(holder.iv_producto?.context!!).load(model.foto).into(holder.iv_producto!!)
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

        constructor(itemView:View) :super(itemView){

            tv_nombre = itemView.findViewById(R.id.tv_nombre)
            tv_stock = itemView.findViewById(R.id.tv_stock)
            iv_producto = itemView.findViewById(R.id.iv_producto)
        }
    }

}