package com.example.mynotes.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.R;
import com.example.mynotes.data.NoteManager;
import com.example.mynotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    // Déclaration des vues
    private EditText etTitle, etDesc, etDate;
    private Spinner spinnerPriority;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // 1. Liaison avec le XML
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        etDate = findViewById(R.id.et_date);
        spinnerPriority = findViewById(R.id.spinner_priority);
        btnSave = findViewById(R.id.btn_save);

        // 2. Configuration du Spinner (Menu déroulant)
        setupSpinner();

        // 3. Gestion du clic sur "Enregistrer"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void setupSpinner() {
        // On crée une liste simple pour les priorités
        List<String> priorities = new ArrayList<>();
        priorities.add("Basse");
        priorities.add("Moyenne");
        priorities.add("Haute");

        // On utilise un adapter standard d'Android pour afficher ces textes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorities
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
    }

    private void saveNote() {
        // A. Récupération des textes
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        // B. Validation basique (pour éviter les notes vides)
        if (title.isEmpty()) {
            etTitle.setError("Le titre est obligatoire");
            return;
        }

        // C. Création de l'objet Note
        Note newNote = new Note(title, desc, date, priority);

        // D. Sauvegarde dans le Singleton
        NoteManager.getInstance().addNote(newNote);

        // E. Feedback utilisateur
        Toast.makeText(this, "Note enregistrée !", Toast.LENGTH_SHORT).show();

        // F. Fermeture de l'activité pour revenir à la liste
        finish();
    }
}