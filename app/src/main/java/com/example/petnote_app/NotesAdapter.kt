package com.example.petnote_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.example.petnote_app.databinding.NoteItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesList = mutableListOf<Note>()

    fun submitList(list: List<Note>) {
        notesList.clear()
        notesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Al hacer clic en un item, muestra un diálogo con las opciones de editar y borrar
            binding.root.setOnClickListener {
                val note = notesList[adapterPosition]
                showEditDeleteDialog(note)
            }
        }

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteDescription.text = note.description
            binding.noteEndDate.text = note.endDate
        }

        private fun showEditDeleteDialog(note: Note) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Opciones")
            builder.setItems(arrayOf("Editar", "Borrar")) { _, which ->
                when (which) {
                    0 -> { // Editar
                        editNote(note)
                    }
                    1 -> { // Borrar
                        deleteNote(note)
                    }
                }
            }
            builder.show()
        }

        private fun editNote(note: Note) {
            // Abrir un Activity para editar la nota
            val intent = Intent(context, EditNoteActivity::class.java)
            intent.putExtra("note_id", note.id)  // Pasar el ID de la nota
            context.startActivity(intent)
        }

        private fun deleteNote(note: Note) {
            // Lógica para borrar la nota
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.email ?: "")
                .collection("notes")
                .document(note.id)
                .delete()
                .addOnSuccessListener {
                    // Actualizar la lista después de borrar
                    notesList.remove(note)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar la nota", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
