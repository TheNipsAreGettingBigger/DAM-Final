package com.android.publisherapp.views

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.android.publisherapp.R
import com.android.publisherapp.models.Producto
import com.android.publisherapp.models.Usuario
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore


class InicioFragment : Fragment(), OnChartValueSelectedListener {
    var container:ConstraintLayout ? = null
    var barra:BarChart? = null

    var productos:ArrayList<Producto> = ArrayList();

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
        barra = view.findViewById(R.id.graficoBarras)
        val preferences = activity?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val email = preferences?.getString("email", "")

        FirebaseFirestore.getInstance().collection("productos").get().addOnSuccessListener {
                result ->
            for (document in result) {
                productos.add( Producto(
                    document.id,
                    document.data.get("nombre").toString(),
                    document.data.get("tipo").toString(),
                    document.data.get("foto").toString(),
                    document.data.get("stock").toString(),
                    document.data.get("precio").toString(),
                    document.data.get("filename").toString()
                ))
            }
            crearGraficoBarra()

        }

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

    private fun crearGraficoBarra() {
        val description = Description()
        description.text = "Grafico de Barras"
        description.textSize = 20f
        val barEntries = arrayListOf<BarEntry>()

        productos.forEachIndexed { index,producto->
            barEntries.add(BarEntry((index + 1).toFloat(), producto.stock.toFloat(),producto.nombre+" "+producto.tipo))
        }
        val barDataSet = BarDataSet(barEntries, "Stock de Productos")
        barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        barDataSet.setDrawValues(true)
        barDataSet.valueTextSize = 16f

        val data = BarData(barDataSet)

        barra!!.data = data
        barra!!.setFitBars(true)
        barra!!.invalidate()
        barra!!.animate()
        barra!!.animateY(2000)
        barra!!.setOnChartValueSelectedListener(this)

    }
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        barra!!.highlightValue(h)
        Toast.makeText(context,e!!.data.toString()+"",Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
    }

}



