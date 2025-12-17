package com.example.mynotes.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; // <--- Import nécessaire pour décoder l'image
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView; // <--- Pour changer le titre de l'écran si besoin
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

    // Boutons d'action
    private Button btnSave, btnCancel;

    // Composants pour la caméra
    private ImageView ivPreview;
    private Button btnPhoto;
    private String currentPhotoBase64 = null;

    // --- VARIABLE D'ÉTAT POUR LA MODIFICATION ---
    // Si -1 : on est en mode Création.
    // Si >= 0 : on est en mode Modification (c'est l'index de la note).
    private int notePositionToEdit = -1;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 1. Liaison avec le XML
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        spinnerPriority = findViewById(R.id.spinner_priority);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        ivPreview = findViewById(R.id.iv_preview);
        btnPhoto = findViewById(R.id.btn_take_photo_inline);

        setupSpinner();

        // 2. --- DÉTECTION DU MODE MODIFICATION ---
        // On regarde si l'intent contient "EXTRA_POSITION"
        if (getIntent().hasExtra("EXTRA_POSITION")) {
            notePositionToEdit = getIntent().getIntExtra("EXTRA_POSITION", -1);

            if (notePositionToEdit != -1) {
                // On est en mode MODIFICATION : on pré-remplit les champs
                prefillData(notePositionToEdit);

                // On change le texte du bouton pour que ce soit clair
                btnSave.setText("Modifier");

                // Optionnel : Changer le titre de l'écran si tu as un ID pour le TextView du titre
                // TextView tvScreenTitle = findViewById(R.id.tv_screen_title);
                // if(tvScreenTitle != null) tvScreenTitle.setText("Modifier la note");
            }
        }

        // 3. Listeners
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Remplit le formulaire avec les données de la note existante
     */
    private void prefillData(int position) {
        // On récupère la note depuis le Singleton
        Note note = NoteManager.getInstance().getNotes().get(position);

        // A. Textes
        etTitle.setText(note.getNom());
        etDesc.setText(note.getDescription());

        // B. Priorité (Spinner)
        // Il faut trouver l'index qui correspond au texte ("Haute", "Moyenne"...)
        String p = note.getPriority();
        if (p != null) {
            if (p.equals("Moyenne")) {
                spinnerPriority.setSelection(1);
            } else if (p.equals("Haute")) {
                spinnerPriority.setSelection(2);
            } else {
                spinnerPriority.setSelection(0); // Basse par défaut
            }
        }

        // C. Photo
        // Si la note avait déjà une photo, on l'affiche et on la stocke en mémoire
        if (note.getImageBase64() != null && !note.getImageBase64().isEmpty()) {
            currentPhotoBase64 = note.getImageBase64();
            try {
                byte[] decodedString = Base64.decode(currentPhotoBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivPreview.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --- Méthodes Caméra (Inchangées) ---

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
            ivPreview.setImageBitmap(imageBitmap);
            currentPhotoBase64 = bitmapToString(imageBitmap);
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

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

    // --- Logique de Sauvegarde Intelligente ---

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        // On met à jour la date pour montrer que la note a été modifiée
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (title.isEmpty()) {
            etTitle.setError("Le titre est obligatoire");
            return;
        }

        // Création de l'objet Note
        Note noteObj = new Note(title, desc, date, priority);

        // Si on a une photo (soit l'ancienne récupérée, soit une nouvelle prise), on l'ajoute
        if (currentPhotoBase64 != null) {
            noteObj.setImageBase64(currentPhotoBase64);
        }

        // --- C'EST ICI QUE TOUT CHANGE ---
        if (notePositionToEdit != -1) {
            // MODE MODIFICATION : On remplace l'ancienne note à l'index précis
            NoteManager.getInstance().getNotes().set(notePositionToEdit, noteObj);
            Toast.makeText(this, "Note modifiée !", Toast.LENGTH_SHORT).show();
        } else {
            // MODE CRÉATION : On ajoute à la fin de la liste
            NoteManager.getInstance().addNote(noteObj);
            Toast.makeText(this, "Note ajoutée !", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}