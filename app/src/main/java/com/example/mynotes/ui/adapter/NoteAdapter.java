package com.example.mynotes.ui.adapter;

import android.content.Context;
import android.content.Intent; // <--- Import nécessaire pour l'intent
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.mynotes.R;
import com.example.mynotes.data.NoteManager;
import com.example.mynotes.model.Note;
import com.example.mynotes.ui.activities.AddNoteActivity; // <--- Import de l'activité cible

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    // Le pattern ViewHolder optimisé
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        ImageButton btnDelete;
        ImageButton btnEdit;
    }

    public NoteAdapter(@NonNull Context context, @NonNull List<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Récupération de la donnée
        Note note = getItem(position);

        // 2. Recyclage de la vue (ViewHolder)
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewHolder.tvDate = convertView.findViewById(R.id.tv_date);
            viewHolder.btnDelete = convertView.findViewById(R.id.btn_delete);

            // 2. LIAISON AVEC LE XML (findViewById)
            viewHolder.btnEdit = convertView.findViewById(R.id.btn_edit);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 3. Remplissage des données
        if (note != null) {
            viewHolder.tvTitle.setText(note.getNom());
            viewHolder.tvDate.setText(note.getDate());

            // --- GESTION DES COULEURS ---
            int colorResId;
            String priority = note.getPriority() != null ? note.getPriority() : "Basse";

            switch (priority) {
                case "Haute": colorResId = R.color.priority_high; break;
                case "Moyenne": colorResId = R.color.priority_medium; break;
                default: colorResId = R.color.priority_low; break;
            }
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), colorResId));
        }

        // 4. --- GESTION DES CLICS ---

        // A. Clic sur SUPPRIMER
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteManager.getInstance().getNotes().remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Note supprimée", Toast.LENGTH_SHORT).show();
            }
        });

        // B. Clic sur MODIFIER (C'est la partie qu'il te manquait !)
        // <--- 3. LOGIQUE DU CLIC
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On crée un Intent pour aller vers l'écran AddNoteActivity
                Intent intent = new Intent(getContext(), AddNoteActivity.class);

                // On passe la POSITION pour dire "Hey, je veux modifier la note numéro X"
                intent.putExtra("EXTRA_POSITION", position);

                // On lance l'activité
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}