package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CCI_RecyclerViewAdapter extends RecyclerView.Adapter<CCI_RecyclerViewAdapter.CCI_ViewHolder> {

    //variables
    private Context context;
    private ArrayList<CarCatalogItemModel> catalogList;
    private CarCatalogItemModel car;




    public void setFiltered (ArrayList<CarCatalogItemModel> catalogList){

        this.catalogList = catalogList;
        notifyDataSetChanged();

    }


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

//        CardView v = (CardView) LayoutInflater.from(context).inflate(R.layout.car_catalog_item,
//                parent,
//                false);

        return new CCI_RecyclerViewAdapter.CCI_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CCI_ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        car = catalogList.get(position);
        ImageView image = holder.carImg;
        TextView name = holder.name;
        TextView price = holder.price;


        Glide.with(context).load(car.getImage()).into(image);
        price.setText(car.getPrice()+"");
        name.setText(car.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car = catalogList.get(position);

                //constructing intent and putting data into it
                Intent intent = new Intent(context,ViewCarActivity.class);
                intent.putExtra(context.getString(R.string.CAR_ID),car.getCar_id());
                intent.putExtra(context.getString(R.string.VIEW_CAR_SOURCE),context.getString(R.string.FROM_BROWSE));

                context.startActivity(intent);//starting activity

            }
        });

//        holder.carImg.setImageResource(catalogList.get(position).getImage());
//        holder.price.setText(catalogList.get(position).getPrice() + " / Per Day");
//        holder.name.setText(catalogList.get(position).getName());
//
//        TextView price = holder.price;
//        TextView name = holder.name;



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

