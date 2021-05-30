package com.android.publisherapp.views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.publisherapp.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class InicioActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private val db = FirebaseFirestore.getInstance()
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToogle:ActionBarDrawerToggle? = null
    var toolbar:Toolbar? = null
    var navigationView:NavigationView?=null
    var fragmentManager:FragmentManager ?=null
    var fragmentTransaction:FragmentTransaction?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.frmInicio)
        navigationView = findViewById(R.id.nvView)
        val bundle: Bundle? = intent.extras
        val email: String = bundle?.getString("email")?:""

        actionBarDrawerToogle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout?.addDrawerListener(actionBarDrawerToogle!!)
        actionBarDrawerToogle?.setDrawerIndicatorEnabled(true)
        actionBarDrawerToogle?.syncState()

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.frgPrincipal, InicioFragment())
        fragmentTransaction?.commit()
        navigationView?.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.inicio ->{
                fragmentManager = supportFragmentManager
                fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.frgPrincipal, InicioFragment())
                fragmentTransaction?.commit()
            }
            R.id.usuarios ->{
                fragmentManager = supportFragmentManager
                fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.frgPrincipal, UsuariosFragment())
                fragmentTransaction?.commit()
            }
            R.id.productos -> {
                fragmentManager = supportFragmentManager
                fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.frgPrincipal, ProductosFragment())
                fragmentTransaction?.commit()
            }

        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }
}