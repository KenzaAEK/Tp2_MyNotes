package com.example.mynotes.model;

import java.io.Serializable;

/**
 * Représente une Note dans l'application.
 * Implémente Serializable pour pouvoir être passée entre les Activités via Intent.
 */
public class Note implements Serializable {

    // Attributs demandés par le cahier des charges
    private String nom;         // Titre de la note
    private String description; // Contenu détaillé
    private String date;        // Date formatée en String (ex: "17/12/2025")
    private String priority;    // "Basse", "Moyenne", "Haute"

    // Constructeur
    public Note(String nom, String description, String date, String priority) {
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.priority = priority;
    }

    // Getters (nécessaires pour lire les données dans l'UI)
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getPriority() { return priority; }

    // Setters (optionnels si les notes ne sont pas modifiables, mais bonne pratique)
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setPriority(String priority) { this.priority = priority; }
}