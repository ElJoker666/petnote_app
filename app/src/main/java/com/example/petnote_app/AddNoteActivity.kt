package com.example.petnote_app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button  // Referencia al botón "Regresar"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Inicializa FirebaseAuth y Firestore
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Referencia a los campos de la interfaz
        titleEditText = findViewById(R.id.title_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        dateEditText = findViewById(R.id.date_edit_text)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.back_button)  // Obtener la referencia al botón de regresar

        // Configura el botón de guardar nota
        saveButton.setOnClickListener {
            saveNote()
        }

        // Configura el clic en el EditText de fecha para abrir el DatePickerDialog
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // Configura el botón de regresar para volver al MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Cierra la actividad actual
        }
    }

    // Muestra el DatePickerDialog para seleccionar la fecha
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // La fecha seleccionada se muestra en el EditText
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                dateEditText.setText(selectedDate)
            },
            year, month, dayOfMonth
        )

        datePickerDialog.show()
    }

    // Guarda la nota en Firestore
    private fun saveNote() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val endDate = dateEditText.text.toString().trim()

        // Verifica que los campos no estén vacíos
        if (title.isEmpty() || description.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtiene el correo del usuario autenticado
        val userEmail = mAuth.currentUser?.email

        if (userEmail != null) {
            // Crea un mapa con los datos de la nota
            val note = hashMapOf(
                "title" to title,
                "description" to description,
                "endDate" to endDate
            )

            // Guarda la nota en Firestore dentro de la subcolección "notes" del usuario autenticado
            firestore.collection("users")
                .document(userEmail)
                .collection("notes")
                .add(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "Nota guardada correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad después de guardar la nota
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar la nota: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
