package com.example.petnote_app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var notesListView: ListView
    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeText = findViewById(R.id.welcome_text)
        notesListView = findViewById(R.id.notes_list)

        // Configuración de un adaptador de ejemplo para mostrar notas
        val notes = listOf("Nota 1", "Nota 2", "Nota 3") // Cambia esto por tus notas
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        notesListView.adapter = adapter

        val fabAddNote = findViewById<FloatingActionButton>(R.id.fab_add_note)
        fabAddNote.setOnClickListener {
            // Acción para agregar una nueva nota
        }
    }
}