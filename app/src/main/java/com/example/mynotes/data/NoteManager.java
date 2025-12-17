package com.example.mynotes.data;

import com.example.mynotes.model.Note;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton qui gère la source de données de l'application.
 * Simule une base de données locale.
 */
public class NoteManager {

    private static NoteManager instance;
    private List<Note> notes;

    // Constructeur privé pour empêcher l'instanciation directe (Règle du Singleton)
    private NoteManager() {
        notes = new ArrayList<>();
        // Données de test pour vérifier que l'affichage marche dès le lancement
        notes.add(new Note("Acheter du lait", "Prendre du demi-écrémé", "12/12/2025", "Basse"));
        notes.add(new Note("Projet Android", "Finir le TP MyNotes", "15/12/2025", "Haute"));
        notes.add(new Note("Sport", "Séance de gym", "18/12/2025", "Moyenne"));
    }

    // Méthode d'accès unique à l'instance
    public static synchronized NoteManager getInstance() {
        if (instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    // Récupérer toutes les notes
    public List<Note> getNotes() {
        return notes;
    }

    // Ajouter une note
    public void addNote(Note note) {
        notes.add(note);
    }
}