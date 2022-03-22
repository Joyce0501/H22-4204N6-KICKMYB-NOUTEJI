package com.example.kickmyb;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccueilAdapter extends RecyclerView.Adapter<AccueilAdapter.ViewHolder>{
    public List<Taches> list;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTache;
        public View Image;
        public TextView textViewTempsEcoule;
        public TextView textViewDateLimite;
        public TextView textViewPourcentageFait;
        public LinearLayout linearLayout;

        public ViewHolder(LinearLayout view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textViewTache = view.findViewById(R.id.tvtaches);
            textViewDateLimite = view.findViewById(R.id.deadline);
            textViewTempsEcoule = view.findViewById(R.id.tempsecoule);
            textViewPourcentageFait = view.findViewById(R.id.pourcentagefait);

            Image = view.findViewById(R.id.imgButton);

            linearLayout = view;
        }
    }

    public AccueilAdapter() {
        list = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AccueilAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)  {
        // Create a new view, which defines the UI of the list item
        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tache_item, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Taches tacheactuel = list.get(position);

//        String pattern = "MM/dd/yy";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        viewHolder.textViewTache.setText(tacheactuel.nom);
        viewHolder.textViewDateLimite.setText(tacheactuel.deadline.toString());
        viewHolder.textViewPourcentageFait.setText(tacheactuel.percentageDone);
        viewHolder.textViewTempsEcoule.setText(tacheactuel.percentageSpent);


        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent consulter = new Intent(view.getContext(),ConsultationActivity.class);
                view.getContext().startActivity(consulter);
            }
        });

//        viewHolder.Image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent results = new Intent(view.getContext(),ConsultationActivity.class);
//                view.getContext().startActivity(results);
//            }
//        });
//        viewHolder.textViewDateLimite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent results = new Intent(view.getContext(),ConsultationActivity.class);
//                view.getContext().startActivity(results);
//            }
//        });
//        viewHolder.textViewTempsEcoule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent results = new Intent(view.getContext(),ConsultationActivity.class);
//                view.getContext().startActivity(results);
//            }
//        });


    }


    // Return the size of your list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }
}
