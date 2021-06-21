package com.android.publisherapp.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.R
import com.android.publisherapp.adapters.ProductoAdapter
import com.android.publisherapp.models.Producto
import com.android.publisherapp.utils.RealPathUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.File
import javax.annotation.Nullable


class ProductosFragment : Fragment() {

    var recyclerProductos : RecyclerView? = null
    var producoAdapter:ProductoAdapter?=null
    var dialogBuilder:AlertDialog.Builder? = null
    var dialog:AlertDialog? = null
    var txtNombreProducto : EditText?= null
    var txtTipoProducto : EditText?= null
    var txtStockProducto : EditText?= null
    var txtPrecioProducto : EditText?= null
    var btnAdd : Button?=null
    var btnCancelar : Button?=null
    var ivAddImagen :ImageView?=null
    var lblAddImage:TextView?=null
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri !=null){
            // mostrar imagen y guardar la url
            ivAddImagen?.setImageURI(uri)
            val filename =  context?.let { RealPathUtil.getRealPath(it, uri) } ?: ""

            val fullName = filename.substringAfterLast("/")
            val fileName = fullName.substringBeforeLast(".")
            val extension = fullName.substringAfterLast(".")
            lblAddImage?.textSize = 15F
            lblAddImage?.gravity = TextView.TEXT_ALIGNMENT_TEXT_START
            lblAddImage?.text = "${fileName}.${extension}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerProductos = view.findViewById(R.id.rv_lista_producto)

        val botonagregarProducto:FloatingActionButton = view.findViewById(R.id.btnAddProducto)


        botonagregarProducto.setOnClickListener {
            dialogBuilder = AlertDialog.Builder(context)
            var viewProducto:View = layoutInflater.inflate(R.layout.productos_add,null)
            txtNombreProducto = viewProducto.findViewById(R.id.et_AddnomProducto)
            txtTipoProducto = viewProducto.findViewById(R.id.et_AddtipoProducto)
            txtStockProducto = viewProducto.findViewById(R.id.et_AddStockProducto)
            txtPrecioProducto = viewProducto.findViewById(R.id.et_AddPrecioProducto)
            btnAdd =viewProducto.findViewById(R.id.btnAgregar)
            btnCancelar = viewProducto.findViewById(R.id.btnCancelar)
            ivAddImagen = viewProducto.findViewById(R.id.iv_AddProducto)
            lblAddImage = viewProducto.findViewById(R.id.lbl_imagen)

            dialogBuilder?.setView(viewProducto)
            dialog = dialogBuilder?.create()
            dialog?.show()

            ivAddImagen?.setOnClickListener {
                getContent.launch("image/*")
            }

            btnAdd?.setOnClickListener {

                val nom:String = txtNombreProducto?.text.toString().trim()
                val tipo:String = txtTipoProducto?.text.toString().trim()
                val precio:String = txtStockProducto?.text.toString().trim()
                val stock :String= txtPrecioProducto?.text.toString().trim()

                if(nom.isNullOrEmpty() || tipo.isNullOrEmpty() || precio.isNullOrEmpty() || stock.isNullOrEmpty()){
                    showAlert("ALERTA","Los campos son requeridos, no deben estar vacios")
                }else{
                    FirebaseFirestore.getInstance().collection("productos").add(hashMapOf(
                        "nombre" to nom,
                        "tipo" to tipo,
                        "precio" to precio,
                        "stock" to stock
                    ))
                        .addOnSuccessListener {
                            dialog?.dismiss()
                            showAlert("EXITO","Se creo el producto correctamente")
                        }
                        .addOnFailureListener {
                            showAlert("ERROR","Se produjo un error al crear un nuevo producto intentelo denueo")
                        }
                }

            }
            btnCancelar?.setOnClickListener {
                dialog?.dismiss()
            }
        }

        recyclerProductos?.layoutManager = LinearLayoutManager(activity)

        var query:Query = FirebaseFirestore.getInstance().collection("productos")

        var f_options :FirestoreRecyclerOptions<Producto> = FirestoreRecyclerOptions.Builder<Producto>().setQuery(query,Producto::class.java).build()
        producoAdapter = ProductoAdapter(f_options)
        producoAdapter?.notifyDataSetChanged()
        recyclerProductos?.adapter = producoAdapter
    }

    override fun onStart() {
        super.onStart()
        producoAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        producoAdapter?.stopListening()
    }
    private fun showAlert(title:String,message:String){
        var builder2 = AlertDialog.Builder(context)
        builder2.setTitle(title)
        builder2.setMessage(message)
        builder2.setPositiveButton("Aceptar",null)
        val dialog3:AlertDialog = builder2.create()
        dialog3.show()
    }


}