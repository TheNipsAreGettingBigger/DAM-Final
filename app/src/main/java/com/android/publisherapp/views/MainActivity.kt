package com.android.publisherapp.views

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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

        val preferences = getSharedPreferences("user", MODE_PRIVATE)
        val email = preferences.getString("email", "")
        if(!email.isNullOrEmpty()){
            showHome(email)
            return
        }


        binding.etEmail.requestFocus()
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etEmail, InputMethodManager.SHOW_IMPLICIT)

        binding.btnLogin.setOnClickListener{
            if(binding.etEmail.text.trim().isNotEmpty() and binding.etPassword.text.trim().isNotEmpty()){
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
                            binding.etEmail.requestFocus()
                            val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.showSoftInput(binding.etEmail, InputMethodManager.SHOW_IMPLICIT)
                        }
                    }
            }else if(binding.etPassword.text.trim().isNullOrEmpty() and binding.etEmail.text.trim().isNullOrEmpty()){
                showInputMessages(true,"password")
                binding.etPassword.requestFocus()
                val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etPassword, InputMethodManager.SHOW_IMPLICIT)
            }else if(binding.etPassword.text.trim().isNullOrEmpty()){
                showInputMessages(false,"password")

                binding.etPassword.requestFocus()
                val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etPassword, InputMethodManager.SHOW_IMPLICIT)

            }else if(binding.etEmail.text.trim().isNullOrEmpty()){
                showInputMessages(false,"email")

                binding.etEmail.requestFocus()
                val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etEmail, InputMethodManager.SHOW_IMPLICIT)

            }
        }

    }

    private fun showInputMessages(campos:Boolean,campo:String){
        var builder = AlertDialog.Builder(this)
        if(campos){
            builder.setTitle("Campos vacios")
            builder.setMessage("Los campos no deben estar vacios, intentelo denuevo")
            builder.setPositiveButton("Aceptar",null)
        }else{
            builder.setTitle("Campo vacio")
            builder.setMessage("El campo ${campo} no debe estar vacio, intentelo denuevo")
            builder.setPositiveButton("Aceptar",null)
        }
        val dialog:AlertDialog = builder.create()
        dialog.show()
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
        home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("email",email).apply()
        startActivity(home)
        finish()
    }
}
