package com.example.petnote_app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petnote_app.databinding.ActivityEditNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var noteId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Obtener el ID de la nota que se va a editar
        noteId = intent.getStringExtra("note_id") ?: ""

        // Cargar la nota en el formulario
        loadNote()

        // Configurar el botón de guardar
        binding.saveButton.setOnClickListener {
            saveNote()
        }

        // Configurar el botón de regresar
        binding.backButton.setOnClickListener {
            finish()  // Regresar a la actividad anterior
        }

        // Configurar el DatePickerDialog para el campo de fecha
        binding.dateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun loadNote() {
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email ?: "")
            .collection("notes")
            .document(noteId)
            .get()
            .addOnSuccessListener { document ->
                val title = document.getString("title") ?: ""
                val description = document.getString("description") ?: ""
                val endDate = document.getString("endDate") ?: ""

                // Rellenar los campos con los datos de la nota
                binding.titleEditText.setText(title)
                binding.descriptionEditText.setText(description)
                binding.dateEditText.setText(endDate)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar la nota", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveNote() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val endDate = binding.dateEditText.text.toString()

        val note = hashMapOf(
            "title" to title,
            "description" to description,
            "endDate" to endDate
        )

        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email ?: "")
            .collection("notes")
            .document(noteId)
            .set(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                finish()  // Volver a la actividad principal
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar la nota", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para mostrar el DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Formatear la fecha seleccionada
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                // Actualizar el campo de fecha con la fecha seleccionada
                binding.dateEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
