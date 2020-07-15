package com.example.myapplication.Adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Fields;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    public ArrayList<Fields> fieldsArrayList = new ArrayList<>();

    private Listener listener;
    private Listener listener1;
    private Listener listener2;

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

        TextView textViewArea = (TextView) cardView.findViewById(R.id.areaText);
        textViewArea.setText(cardView.getContext().getResources().getString(R.string.fieldArea, fmt(fields.getArea())));
        TextView textViewName = (TextView) cardView.findViewById(R.id.nameText);
        textViewName.setText(cardView.getContext().getResources().getString(R.string.fieldInfo, fields.getName()));


        TextView textViewNumber = (TextView) cardView.findViewById(R.id.numberButton);
        textViewNumber.setText(fields.getNumber());

        ImageButton imageButton = (ImageButton) cardView.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(imageButton, position);
            }
        });


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
        return fieldsArrayList.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void setListener1 (Listener listener1){
        this.listener1 = listener1;
    }

    public void setListener2 (Listener listener2){
        this.listener2 = listener2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }

    private void showMenu(View view,int position) {

        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_field_cardview, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Edit:
                        Toast.makeText(view.getContext(),"Edytuj", Toast.LENGTH_SHORT).show();
                        // INTERFEJS
                        if(listener1 != null){
                            listener1.onClick(position);
                        }
                        return true;
                    case R.id.Delete:
                        // INTERFEJS
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

    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

}
