package com.example.mynotes.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.R;
import com.example.mynotes.model.Note;

public class DetailsNoteActivity extends AppCompatActivity {

    // Déclaration des vues
    private TextView tvTitle, tvDate, tvPriority, tvDesc;
    private ImageView ivPhoto; // <--- NOUVEAU
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);

        // Active la flèche retour native
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 1. Initialisation des vues
        tvTitle = findViewById(R.id.tv_detail_title);
        tvDate = findViewById(R.id.tv_detail_date);
        tvPriority = findViewById(R.id.tv_detail_priority);
        tvDesc = findViewById(R.id.tv_detail_desc);
        ivPhoto = findViewById(R.id.iv_detail_photo); // <--- Liaison XML
        btnBack = findViewById(R.id.btn_back);

        // 2. Récupération des données
        Note note = null;
        if (getIntent().hasExtra("EXTRA_NOTE")) {
            note = (Note) getIntent().getSerializableExtra("EXTRA_NOTE");
        }

        // 3. Affichage
        if (note != null) {
            tvTitle.setText(note.getNom());
            tvDate.setText(note.getDate());
            tvPriority.setText(note.getPriority());
            tvDesc.setText(note.getDescription());

            // --- MAGIE DE L'IMAGE ---
            // On vérifie si la note contient une image encodée
            if (note.getImageBase64() != null && !note.getImageBase64().isEmpty()) {
                try {
                    // Étape A : On décode le String Base64 en tableau de bytes
                    byte[] decodedString = Base64.decode(note.getImageBase64(), Base64.DEFAULT);

                    // Étape B : On transforme les bytes en Bitmap (Image Android)
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    // Étape C : On affiche l'image et on la rend visible
                    ivPhoto.setImageBitmap(decodedByte);
                    ivPhoto.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Si pas d'image, on cache l'ImageView pour ne pas laisser un trou vide
                ivPhoto.setVisibility(View.GONE);
            }

        } else {
            Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 4. Gestion du bouton Retour (celui en bas)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Gestion du bouton Retour (celui en haut à gauche)
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}