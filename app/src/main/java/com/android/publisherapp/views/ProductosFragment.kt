package com.android.publisherapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.publisherapp.R
import com.android.publisherapp.adapters.ProductoAdapter
import com.android.publisherapp.models.Producto
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProductosFragment : Fragment() {

    var recyclerProductos : RecyclerView? = null
    var producoAdapter:ProductoAdapter?=null

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
}