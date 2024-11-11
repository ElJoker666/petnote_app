package com.example.petnote_app

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var goToLoginText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Referencias a los elementos de la interfaz
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        repeatPasswordEditText = findViewById(R.id.repeat_password)
        registerButton = findViewById(R.id.register_button)
        goToLoginText = findViewById(R.id.go_to_login)

        // Configurar el botón de registro
        registerButton.setOnClickListener { registerUser() }

        // Configurar el texto de "Regresar a Iniciar Sesión" con colores diferentes
        val text = "¿Ya tienes una cuenta? Regresar a Iniciar Sesión"
        val spannable = SpannableString(text)

        // Cambiar color negro al inicio
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.black)), 0, text.length - "Iniciar Sesión".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Cambiar color azul a "Iniciar Sesión"
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)),
            text.length - "Iniciar Sesión".length, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        goToLoginText.text = spannable

        // Configurar el texto de clic para redirigir al login
        goToLoginText.setOnClickListener {
            // Redirigir a LoginActivity cuando el texto sea clickeado
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Cierra RegisterActivity para evitar que el usuario regrese a ella al presionar el botón de retroceso
        }
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val repeatPassword = repeatPasswordEditText.text.toString().trim()

        // Validación de campos
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != repeatPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el usuario con FirebaseAuth
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()

                    // Redirigir a LoginActivity después del registro exitoso
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    // Cerrar RegisterActivity si ya no es necesaria
                    finish()  // Esto cierra RegisterActivity para que no se quede en la pila de actividades
                } else {
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
