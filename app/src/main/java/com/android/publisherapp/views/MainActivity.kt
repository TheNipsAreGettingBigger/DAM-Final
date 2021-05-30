package com.android.publisherapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.publisherapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    var loader : ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loader = ProgressDialog(this)

        binding.btnLogin.setOnClickListener{
            if(binding.etEmail.text.isNotEmpty() and binding.etPassword.text.isNotEmpty()){
                loader?.setMessage("Login en progreso")
                loader?.setCanceledOnTouchOutside(false)
                loader?.show()
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener{
                        loader?.dismiss()
                        if(it.isSuccessful){
                            showHome(it.result?.user?.email ?: "")
                        }else{
                            showAlert()
                        }
                    }
            }
        }

    }
    private fun showAlert(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error de autenticacion, intentelo denuevo")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String){
        val home = Intent(this, InicioActivity::class.java).apply {
            putExtra("email",email)
        }
        val preferences = getSharedPreferences("usuarios", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user",email).apply()
        startActivity(home)
    }
}
