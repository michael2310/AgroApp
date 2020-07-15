package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.FieldsDetailCultivation;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FieldDetailAdapterCultivation extends RecyclerView.Adapter<FieldDetailAdapterCultivation.ViewHolder> {

    public ArrayList<FieldsDetailCultivation> fieldsDetailsArrayList = new ArrayList<>();
    String category;

    private Listener listener;
    private Listener listener1;
    private Listener listener2;

    public interface Listener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_field_details, parent, false);

        return new FieldDetailAdapterCultivation.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView  = holder.cardView;

        FieldsDetailCultivation fieldsDetail = fieldsDetailsArrayList.get(position);

        TextView textViewDate = (TextView) cardView.findViewById(R.id.textViewDate);
        textViewDate.setText(fieldsDetail.getDate());

        TextView text2 = (TextView) cardView.findViewById(R.id.text2);
        text2.setText(fieldsDetail.getPlant());

        category = fieldsDetail.getCategory();

        ImageButton imageButton = (ImageButton) cardView.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(imageButton, position);
            }
        });

        setFadeAnimation(holder.cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(position);
                }
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


    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150);
        view.startAnimation(anim);
    }

    public void setListener2 (Listener listener2){
        this.listener2 = listener2;
    }


    private void showMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_field_cardview, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Edit:
                        if(listener1 != null){
                            listener1.onClick(position);
                        }
                        return true;
                    case R.id.Delete:
                        if(listener2 != null){
                            listener2.onClick(position);
                        }
                        return true;
                    default:
                }
                return false;
            }
        });
        popup.show();
    }

}
