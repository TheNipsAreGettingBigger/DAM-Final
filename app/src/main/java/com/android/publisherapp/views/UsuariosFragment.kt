package com.android.publisherapp.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.R
import com.android.publisherapp.adapters.ProductoAdapter
import com.android.publisherapp.adapters.UsuarioAdapter
import com.android.publisherapp.models.Producto
import com.android.publisherapp.models.Usuario
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class UsuariosFragment : Fragment() {

    var recyclerUsuarios : RecyclerView? = null
    var usuariosAdapter:UsuarioAdapter?=null


    var dialogBuilder: AlertDialog.Builder? = null
    var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerUsuarios = view.findViewById(R.id.rv_usuarios)

        var btnAddUsuario:FloatingActionButton = view.findViewById(R.id.btnAddUsuarios)

        btnAddUsuario.setOnClickListener {
            Log.i("TEST","click +")
            dialogBuilder = AlertDialog.Builder(context)
            var viewUsuario:View = layoutInflater.inflate(R.layout.usuarios_add,null)
            dialogBuilder?.setView(viewUsuario)
            dialog = dialogBuilder?.create()
            dialog?.setCancelable(false)
            dialog?.show()


            var btnAdd =viewUsuario.findViewById<Button>(R.id.btnAddUsuarios)
            var btnCancelar = viewUsuario.findViewById<Button>(R.id.btnCancelarUser)

            btnCancelar.setOnClickListener {
                dialog?.dismiss()
            }
        }

        recyclerUsuarios?.layoutManager = LinearLayoutManager(activity)
        var query:Query = FirebaseFirestore.getInstance().collection("users")
        var f_options : FirestoreRecyclerOptions<Usuario> = FirestoreRecyclerOptions.Builder<Usuario>().setQuery(query,
            Usuario::class.java).build()
        usuariosAdapter = UsuarioAdapter(f_options,requireContext())
        usuariosAdapter?.notifyDataSetChanged()
        recyclerUsuarios?.adapter = usuariosAdapter

    }
    override fun onStop() {
        super.onStop()
        usuariosAdapter?.stopListening()
    }
    override fun onStart() {
        super.onStart()
        usuariosAdapter?.startListening()
    }
}