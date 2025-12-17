package com.example.mynotes.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.mynotes.R;
import com.example.mynotes.model.Note;

import java.util.List;

/**
 * Adaptateur personnalisé pour afficher les notes dans une ListView.
 * Responsabilités :
 * 1. Créer les vues pour chaque ligne (inflation).
 * 2. Remplir les vues avec les données (binding).
 * 3. Gérer la logique visuelle (couleurs) selon les données métier.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    // Le pattern ViewHolder permet d'éviter les appels coûteux à findViewById() lors du scroll
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
    }

    public NoteAdapter(@NonNull Context context, @NonNull List<Note> notes) {
        // On passe 0 pour la ressource car nous allons gérer l'inflation nous-mêmes
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Récupération de la donnée pour cette position
        Note note = getItem(position);

        // 2. Vérification si une vue existante est réutilisée (Recyclage), sinon inflation
        ViewHolder viewHolder;

        if (convertView == null) {
            // Création d'une nouvelle vue si aucune n'est disponible
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);

            // Initialisation du ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_item_title);
            viewHolder.tvDate = convertView.findViewById(R.id.tv_item_date);

            // Stockage du ViewHolder dans la vue pour réutilisation future
            convertView.setTag(viewHolder);
        } else {
            // Récupération du ViewHolder existant
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 3. Remplissage de la vue avec les données (Data Binding)
        if (note != null) {
            viewHolder.tvTitle.setText(note.getNom());
            viewHolder.tvDate.setText(note.getDate());

            // 4. Logique métier visuelle : La couleur selon la priorité
            // Cette partie répond directement à l'exigence technique "couleur distinctive"
            int colorResId;

            // On vérifie que la priorité n'est pas nulle pour éviter le crash
            String priority = note.getPriority() != null ? note.getPriority() : "Basse";

            switch (priority) {
                case "Haute":
                    colorResId = R.color.priority_high;
                    break;
                case "Moyenne":
                    colorResId = R.color.priority_medium;
                    break;
                case "Basse":
                default:
                    colorResId = R.color.priority_low;
                    break;
            }

            // Application de la couleur d'arrière-plan
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
        }

        return convertView;
    }
}