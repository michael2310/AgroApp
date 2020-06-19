package com.example.myapplication.Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    String [] names;
    int [] imagesId;

    private Listener listener;
    //interfejs
    public interface Listener{
        void onClick(int position);
    }


    //konstruktory

    public MenuAdapter(String[] names, int[] imagesId){
        this.names = names;
        this.imagesId = imagesId;
    }



    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(names[position]);

        ImageView imageView = (ImageView) cardView.findViewById(R.id.imageView);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), imagesId[position]);
        imageView.setImageDrawable(drawable);
        //imageView.setImageDrawable(imagesId[position]);
        imageView.setContentDescription(names[position]);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
