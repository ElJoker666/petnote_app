package com.example.petnote_app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val goToLogin = findViewById<TextView>(R.id.go_to_login)
        goToLogin.setOnClickListener {
            // Regresar a la actividad de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual
        }
    }
}