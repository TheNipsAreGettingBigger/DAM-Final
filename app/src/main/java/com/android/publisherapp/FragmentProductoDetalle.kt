package com.android.publisherapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.publisherapp.models.Producto
import com.android.publisherapp.utils.RealPathUtil
import com.android.publisherapp.views.ProductosFragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

//https://github.com/Androchunk/CustomIconSpinner/blob/master/app/src/main/java/com/androchunk/customiconspinner/CustomAdapter.java
enum class TIPO_DE_ACCION(val tipo:Int){
    ACTUALIZAR(1),
    ELIMINAR(2)
}
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
    var imageUri :Uri?=null
    var nameImage:String= ""
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri !=null){
            // mostrar imagen y guardar la url
            img?.setImageURI(uri)
            val filenameCompleto =  context?.let { RealPathUtil.getRealPath(it, uri) } ?: ""
            if(filenameCompleto.trim().isNotEmpty()){
                imageUri = uri
                val fullName = filenameCompleto.substringAfterLast("/")
                val fileName = fullName.substringBeforeLast(".")
                val extension = fullName.substringAfterLast(".")
                filename?.textSize = 15F
                filename?.gravity = TextView.TEXT_ALIGNMENT_TEXT_START
                nameImage = "${fileName}.${extension}"
                filename?.text = nameImage
            }

        }
    }

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
        img?.setOnClickListener {
            getContent.launch("image/*")
        }
        btnActualizar?.setOnClickListener {
            Log.i("precio",producto?.precio+""+producto)

            Toast.makeText(activity,et_producto?.text,Toast.LENGTH_LONG).show()
            Log.i("assd","asdasdasd"+et_producto?.text+producto?.nombre)
            createDialog("¿Estas seguro que deseas actualizar?",TIPO_DE_ACCION.ACTUALIZAR)
        }
        btnCancelar?.setOnClickListener {
            val fragmentManager:FragmentManager = (context_ as AppCompatActivity).getSupportFragmentManager()
            val fragmentTransaction:FragmentTransaction = fragmentManager.beginTransaction()
            val productosFragment = ProductosFragment()
            fragmentTransaction.replace(R.id.frgPrincipal,productosFragment)
            fragmentTransaction.commit()
        }
        btnEliminar?.setOnClickListener {
            createDialog("¿Estas seguro que deseas eliminar?",TIPO_DE_ACCION.ELIMINAR)
        }
    }
    private fun showAlert(title:String,message:String){
        var builder2 = AlertDialog.Builder(context_)
        builder2.setTitle(title)
        builder2.setMessage(message)
        builder2.setPositiveButton("Aceptar",null)
        val dialog3: AlertDialog = builder2.create()
        dialog3.show()
    }
    fun updateWithoutImages(alertDialog4:AlertDialog){
        val data = hashMapOf(
            "uid" to producto?.uid.toString(),
            "foto" to producto?.foto.toString(),
            "filename" to producto?.filename.toString(),
            "nombre" to et_producto?.text.toString(),
            "tipo" to et_tipo?.text.toString(),
            "precio" to et_precio?.text.toString(),
            "stock" to et_stock?.text.toString(),
        )

        FirebaseFirestore.getInstance().collection("productos").document(producto?.uid?:"").set(data, SetOptions.merge())
            .addOnSuccessListener {
                alertDialog4.dismiss()
                showAlert("EXITO","Se actualizo el producto correctamente")

            }
            .addOnFailureListener{
                alertDialog4.dismiss()
                showAlert("ERROR","Se produjo un error al actualizar el producto intentelo denueo")
            }
    }
    fun updateWithImages(alertDialog4:AlertDialog){
        var ref : StorageReference = FirebaseStorage.getInstance().reference.child(UUID.randomUUID().toString()+nameImage)
        ref.putFile(imageUri!!).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                producto?.foto = downloadUri.toString()
                producto?.filename = nameImage
                updateWithoutImages(alertDialog4)
            }
        }
    }

    fun createDialog(message:String,tipo:TIPO_DE_ACCION){
        val alertDialog :AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        val view : View = layoutInflater.inflate(R.layout.mensaje_sistema,null)

        val btnAceptar :Button = view.findViewById(R.id.btnaceptarconsecuencias)
        val btnCancelaraccion:Button = view.findViewById(R.id.btncancelaraccion)
        val label :TextView = view.findViewById(R.id.tv_message)
        label.setText(message)

        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        btnCancelaraccion.setOnClickListener {
            alertDialog.dismiss()
        }
        btnAceptar.setOnClickListener {
            alertDialog.dismiss()

            var ad : AlertDialog ?= null
            if(tipo == TIPO_DE_ACCION.ACTUALIZAR){
                Log.i("actu","actualizar"+producto?.uid)
                if(et_producto?.text.isNullOrEmpty() || et_tipo?.text.isNullOrEmpty() || et_precio?.text.isNullOrEmpty() || et_stock?.text.isNullOrEmpty()){
                    showAlert("ALERTA","Los campos son requeridos, no deben estar vacios")
                }else{
                    val alertDialog4 : AlertDialog
                    val builder4 = AlertDialog.Builder(context_)

                    val view4 : View = LayoutInflater.from(context_).inflate(R.layout.loding_insert,null)
                    val label4 :TextView = view4.findViewById(R.id.tv_titulo_spinner)
                    label4.setText("Actualizando el producto en la base de datos")
                    builder4.setView(view4)
                    alertDialog4 = builder4.create()
                    alertDialog4.setCancelable(false)
                    alertDialog4.show()
                    if(imageUri!=null && nameImage.trim().isNotEmpty()){
                        updateWithImages(alertDialog4)
                    }else{
                        updateWithoutImages(alertDialog4)
                    }
                }

            }else{
                var uid:String = producto?.uid ?:""
                Log.i("eli","eliminar"+producto?.uid)
                val alertDialog : AlertDialog
                val builder = AlertDialog.Builder(context_)

                val view : View = LayoutInflater.from(context_).inflate(R.layout.loding_insert,null)
                val label :TextView = view.findViewById(R.id.tv_titulo_spinner)
                label.setText("Eliminando el producto en la Base de Datos")
                builder.setView(view)
                alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                FirebaseFirestore.getInstance().collection("productos").document(uid).delete()
                    .addOnCompleteListener{
                    alertDialog.dismiss()
                    }
            }
            val fragmentManager:FragmentManager = (context_ as AppCompatActivity).getSupportFragmentManager()
            val fragmentTransaction:FragmentTransaction = fragmentManager.beginTransaction()
            val productosFragment = ProductosFragment()
            fragmentTransaction.replace(R.id.frgPrincipal,productosFragment)
            fragmentTransaction.commit()


        }
    }
}