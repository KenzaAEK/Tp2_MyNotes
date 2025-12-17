package com.example.mynotes.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.R;
import com.example.mynotes.model.Note;

public class DetailsNoteActivity extends AppCompatActivity {

    // Déclaration des vues
    private TextView tvTitle, tvDate, tvPriority, tvDesc;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);

        // 1. Initialisation des vues
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvPriority = findViewById(R.id.tv_detail_priority);
        tvDesc = findViewById(R.id.tv_detail_desc);
        btnBack = findViewById(R.id.btn_back);

        // 2. Récupération des données passées par l'Intent
        // On utilise la même clé "EXTRA_NOTE" que dans NoteListActivity
        Note note = (Note) getIntent().getSerializableExtra("EXTRA_NOTE");

        // 3. Affichage des données (Programmation défensive : on vérifie si note n'est pas null)
        if (note != null) {
            tvTitle.setText(note.getNom());
            tvDate.setText(note.getDate());
            tvPriority.setText(note.getPriority()); // Pourrait être amélioré avec une couleur de fond ici aussi
            tvDesc.setText(note.getDescription());
        } else {
            Toast.makeText(this, "Erreur : Impossible de charger la note.", Toast.LENGTH_SHORT).show();
            finish(); // On ferme l'activité si pas de données
        }

        // 4. Gestion du bouton Retour
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Termine l'activité courante et revient à la précédente (la pile)
                finish();
            }
        });
    }
}