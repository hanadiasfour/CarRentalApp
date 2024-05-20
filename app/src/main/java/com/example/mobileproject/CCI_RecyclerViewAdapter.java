package com.example.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CCI_RecyclerViewAdapter extends RecyclerView.Adapter<CCI_RecyclerViewAdapter.CCI_ViewHolder> {

    //variables
    private Context context;
    private ArrayList<CarCatalogItemModel> catalogList;

    //constructor
    public CCI_RecyclerViewAdapter(Context context, ArrayList<CarCatalogItemModel> catalogList){
        this.catalogList = catalogList;
        this.context = context;


    }

    @NonNull
    @Override
    public CCI_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.car_catalog_item,parent,false);

        return new CCI_RecyclerViewAdapter.CCI_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CCI_ViewHolder holder, int position) {

        holder.carImg.setImageResource(catalogList.get(position).getImage());
        holder.price.setText(catalogList.get(position).getPrice() + " / Per Day");
        holder.name.setText(catalogList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    public static class CCI_ViewHolder extends RecyclerView.ViewHolder{


        ImageView carImg ;
        TextView price,name;

        public CCI_ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImg =(ImageView) itemView.findViewById(R.id.car_catalog_item_image);
            price =(TextView) itemView.findViewById(R.id.car_catalog_item_price);
            name =(TextView) itemView.findViewById(R.id.car_catalog_item_name);




        }
    }
}

