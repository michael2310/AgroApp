package com.example.myapplication.Adapters;

import android.hardware.camera2.CaptureRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.FieldsDetail;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FieldDetailAdapter extends RecyclerView.Adapter<FieldDetailAdapter.ViewHolder> {

    public ArrayList<FieldsDetail> fieldsDetailsArrayList = new ArrayList<>();
    String category;

    private Listener listener;
    private Listener listener1;




    //interfejs
    public interface Listener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_field_details, parent, false);

        return new FieldDetailAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView  = holder.cardView;

        FieldsDetail fieldsDetail = fieldsDetailsArrayList.get(position);


        TextView textViewDate = (TextView) cardView.findViewById(R.id.textViewDate);
        textViewDate.setText(fieldsDetail.getDate());
        TextView text = (TextView) cardView.findViewById(R.id.text);
        text.setText(fieldsDetail.getChemia());
        TextView text2 = (TextView) cardView.findViewById(R.id.text2);
        text2.setText(fieldsDetail.getPlant());
        category = fieldsDetail.getCategory();

        setFadeAnimation(holder.cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(listener != null) {
                        listener.onClick(position);
                    }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener1 != null){
                    listener1.onClick(position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldsDetailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void setListener1 (Listener listener1){
        this.listener1 = listener1;
    }

    public String getCategory(){
        return category;
    }

    public void deleteAtIndex(int index, Object object){
        fieldsDetailsArrayList.remove(object);
        notifyDataSetChanged();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150);
        view.startAnimation(anim);
    }

}
