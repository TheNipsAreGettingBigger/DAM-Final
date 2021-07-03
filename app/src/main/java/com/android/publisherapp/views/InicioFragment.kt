package com.android.publisherapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.android.publisherapp.R
import com.android.publisherapp.models.Usuario
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore


class InicioFragment : Fragment() {
    var container:ConstraintLayout ? = null
    var barra:BarChart? = null

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
        crearGraficoBarra()
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

    private fun crearGraficoBarra() {
        val description = Description()
        description.setText("Grafico de Barras")
        description.setTextSize(13f)
        barra!!.setFitBars(true)
        val barEntries = arrayListOf<BarEntry>()
        barEntries.add(BarEntry(1f, 10f))
        barEntries.add(BarEntry(2f, 15f))
        barEntries.add(BarEntry(3f, 5f))
        barEntries.add(BarEntry(4f, 2f))
        val barDataSet = BarDataSet(barEntries, "Data set")
        barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        barDataSet.setDrawValues(true)
        val data = BarData(barDataSet)
        barra!!.data = data
        barra!!.invalidate()
        barra!!.animate()
    }


}