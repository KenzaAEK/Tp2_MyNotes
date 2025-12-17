package com.example.mynotes.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.R;
import com.example.mynotes.data.NoteManager;
import com.example.mynotes.model.Note;
import com.example.mynotes.ui.adapter.NoteAdapter;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    // Déclaration des composants UI
    private ListView listViewNotes;
    private Button btnAddNote;
    private Button btnOpenCamera; // <--- AJOUT 1 : Déclaration du bouton caméra

    // Déclaration de l'Adapter et des données
    private NoteAdapter adapter;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // 1. Initialisation des Vues
        listViewNotes = findViewById(R.id.list_notes);
        btnAddNote = findViewById(R.id.btn_add_note);
        btnOpenCamera = findViewById(R.id.btn_open_camera); // <--- AJOUT 2 : Liaison XML

        // 2. Initialisation des Données via le Singleton
        notesList = NoteManager.getInstance().getNotes();

        // 3. Configuration de l'Adapter
        adapter = new NoteAdapter(this, notesList);
        listViewNotes.setAdapter(adapter);

        // 4. Gestion du Clic sur une note (Vers Détails)
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = notesList.get(position);
                Intent intent = new Intent(NoteListActivity.this, DetailsNoteActivity.class);
                intent.putExtra("EXTRA_NOTE", selectedNote);
                startActivity(intent);
            }
        });

        // 5. Gestion du Clic sur le bouton Ajouter (Vers Formulaire)
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // 6. Gestion du Clic sur le bouton Caméra (Vers CameraActivity)
        // <--- AJOUT 3 : La logique du bouton caméra
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancement de l'activité Caméra
                Intent intent = new Intent(NoteListActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}