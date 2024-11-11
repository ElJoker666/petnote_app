package com.example.petnote_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petnote_app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configura el RecyclerView
        notesAdapter = NotesAdapter(this)  // Pasa el contexto aquí
        binding.notesList.layoutManager = LinearLayoutManager(this)
        binding.notesList.adapter = notesAdapter

        // Mostrar el correo del usuario en el texto de bienvenida
        val userEmail = mAuth.currentUser?.email?.substringBefore("@")
        binding.welcomeText.text = "Bienvenido, $userEmail"

        // Cargar las notas del usuario
        loadNotes()

        // Configurar el botón de cerrar sesión
        binding.logoutButton.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Cierra la actividad al cerrar sesión
        }

        // Configurar el FloatingActionButton para agregar una nueva nota
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)  // Inicia la actividad AddNoteActivity
        }
    }

    // Recargar las notas cada vez que se regresa a la actividad
    override fun onResume() {
        super.onResume()
        loadNotes()  // Llamamos a loadNotes para asegurarnos de que las notas estén actualizadas
    }

    private fun loadNotes() {
        val userEmail = mAuth.currentUser?.email

        if (userEmail != null) {
            firestore.collection("users")
                .document(userEmail)
                .collection("notes")
                .get()
                .addOnSuccessListener { result ->
                    val notesList = mutableListOf<Note>()
                    for (document in result) {
                        val title = document.getString("title") ?: ""
                        val description = document.getString("description") ?: ""
                        val endDate = document.getString("endDate") ?: ""
                        val id = document.id

                        val note = Note(id, title, description, endDate)
                        notesList.add(note)
                    }
                    notesAdapter.submitList(notesList) // Actualizar el adaptador con las notas
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al cargar las notas: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}


