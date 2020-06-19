package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Fields;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    public ArrayList<Fields> fieldsArrayList = new ArrayList<>();

    private Listener listener;


    //interfejs
    public interface Listener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_fields,parent,false);

        return new FieldAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        Fields fields = fieldsArrayList.get(position);

       // TextView textViewNumber = (TextView) cardView.findViewById(R.id.numberText);
       // textViewNumber.setText("Działka nr: " + fields.getNumber());

        TextView textViewArea = (TextView) cardView.findViewById(R.id.areaText);
        textViewArea.setText("Powierzchnia: " + fields.getArea() + " ha");

        TextView textViewName = (TextView) cardView.findViewById(R.id.nameText);
        textViewName.setText("Info: " + fields.getName());

        Button numberButton = (Button) cardView.findViewById(R.id.numberButton);
        numberButton.setText(fields.getNumber());
        numberButton.setTextSize(20);

//        TextView textViewId = (TextView) cardView.findViewById(R.id.textViewId);
//        textViewId.setText(fields.getFieldId());


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });


        //holder.itemView
    }

    @Override
    public int getItemCount() {
        return fieldsArrayList.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
