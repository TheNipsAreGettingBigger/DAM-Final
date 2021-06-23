package com.android.publisherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.publisherapp.models.Producto
import com.android.publisherapp.views.InicioFragment
import com.android.publisherapp.views.ProductosFragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_producto_detalle.*

class FragmentProductoDetalle(context:Context) : Fragment() {
    var context_ = context
    var producto: Producto? =null
    var et_producto:TextInputEditText?=null
    var et_precio:TextInputEditText?=null
    var et_stock:TextInputEditText?=null
    var et_tipo:TextInputEditText?=null
    var img:ImageView?=null
    var btnEliminar:Button?=null
    var btnCancelar:Button?=null
    var btnActualizar:Button?=null
    var filename:TextView?=null
//    https://luismasdev.com/mostrar-mensajes-al-usuario-con-toast-fragment-activity-adapter/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_producto_detalle, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        et_producto = view.findViewById(R.id.et_nom_actualizar)
        et_precio = view.findViewById(R.id.et_precio_actu)
        et_stock = view.findViewById(R.id.et_stock_actua)
        et_tipo = view.findViewById(R.id.et_tipo_actua)
        btnActualizar = view.findViewById(R.id.btnEditarActualizar)
        btnCancelar = view.findViewById(R.id.btnCancelarActualizar)
        btnEliminar = view.findViewById(R.id.btnEliminarActualizar)
        img = view.findViewById(R.id.iv_actualizarProducto)
        filename = view.findViewById(R.id.lbl_filename)

        et_producto?.setText( producto?.nombre)
        et_precio?.setText( producto?.precio)
        et_stock?.setText(producto?.stock)
        et_tipo?.setText( producto?.tipo)
        Log.i("asasd","as"+producto?.foto)
        if(producto?.foto?.trim()?.isNotEmpty() == true){
            Glide.with(img?.context!!).load(producto?.foto).into(img!!)
        }else{
            img?.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
        }

        if(!producto?.filename.isNullOrEmpty()){
            filename?.setText(producto?.filename)
        }

        btnActualizar?.setOnClickListener {

            Toast.makeText(activity,et_producto?.text,Toast.LENGTH_LONG).show()
            Log.i("assd","asdasdasd"+et_producto?.text+producto?.nombre)
        }
        btnCancelar?.setOnClickListener {
            val fragmentManager:FragmentManager = (context_ as AppCompatActivity).getSupportFragmentManager()
            val fragmentTransaction:FragmentTransaction = fragmentManager.beginTransaction()
            val productosFragment = ProductosFragment()
            fragmentTransaction.replace(R.id.frgPrincipal,productosFragment)
            fragmentTransaction.commit()
        }
//        et_producto?.text

    }
}