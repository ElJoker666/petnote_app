package com.example.petnote_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Referencias a los elementos de la interfaz
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)
        forgotPasswordText = findViewById(R.id.forgot_password)

        // Configurar el botón de login
        loginButton.setOnClickListener { loginUser() }

        // Configurar el botón de registro (redirigir a la actividad de registro)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        // Configurar el texto de "Olvidaste tu contraseña" (opcional)
        forgotPasswordText.setOnClickListener {
            // Implementa la funcionalidad de recuperación de contraseña aquí
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = mAuth.currentUser
                        Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()

                        // Redirigir a MainActivity después del inicio de sesión exitoso
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        // Cerrar LoginActivity para evitar que el usuario regrese a ella
                        finish()
                    } else {
                        Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Por favor, ingrese el correo electrónico y la contraseña", Toast.LENGTH_SHORT).show()
        }
    }


}

