package com.example.mynotes.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.R;
import com.example.mynotes.data.NoteManager;
import com.example.mynotes.model.Note;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    // Déclaration des vues
    private EditText etTitle, etDesc;
    private Spinner spinnerPriority;

    // Boutons d'action (Enregistrer et Annuler)
    private Button btnSave, btnCancel;

    // Nouveaux composants pour la caméra
    private ImageView ivPreview;
    private Button btnPhoto;
    private String currentPhotoBase64 = null; // Stockage de l'image convertie

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Active la flèche retour native en haut à gauche
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 1. Liaison avec le XML
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        spinnerPriority = findViewById(R.id.spinner_priority);

        // Liaison des boutons du bas
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel); // <-- AJOUT IMPORTANT

        // Liaison Caméra
        ivPreview = findViewById(R.id.iv_preview);
        btnPhoto = findViewById(R.id.btn_take_photo_inline);

        setupSpinner();

        // 2. Gestion du clic Photo
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // 3. Gestion du clic Enregistrer
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        // 4. Gestion du clic Annuler (Ferme l'écran sans sauver)
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // --- Méthodes pour la Caméra ---

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir la caméra", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Affiche la miniature
            ivPreview.setImageBitmap(imageBitmap);

            // Convertit l'image en texte pour la sauvegarder
            currentPhotoBase64 = bitmapToString(imageBitmap);
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    // --- Méthodes Classiques ---

    private void setupSpinner() {
        List<String> priorities = new ArrayList<>();
        priorities.add("Basse");
        priorities.add("Moyenne");
        priorities.add("Haute");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorities
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        // DATE AUTOMATIQUE (Plus besoin de saisir)
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (title.isEmpty()) {
            etTitle.setError("Le titre est obligatoire");
            return;
        }

        Note newNote = new Note(title, desc, date, priority);

        // Ajout de l'image si elle existe
        if (currentPhotoBase64 != null) {
            newNote.setImageBase64(currentPhotoBase64);
        }

        NoteManager.getInstance().addNote(newNote);
        Toast.makeText(this, "Note enregistrée !", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Gère le clic sur la flèche retour de la barre d'action (en haut à gauche)
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}