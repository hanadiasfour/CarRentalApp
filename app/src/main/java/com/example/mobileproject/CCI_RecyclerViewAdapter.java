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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CCI_RecyclerViewAdapter extends RecyclerView.Adapter<CCI_RecyclerViewAdapter.CCI_ViewHolder> {

    //variables
    private Context context;
    private ArrayList<CarCatalogItemModel> catalogList;
    private CarCatalogItemModel car;
    private String USER_KEY;//who is this user (gmail)


    //constructor
    public CCI_RecyclerViewAdapter(Context context, ArrayList<CarCatalogItemModel> catalogList, String USER_KEY){
        this.catalogList = catalogList;
        this.context = context;
        this.USER_KEY = USER_KEY;
    }

    @NonNull
    @Override
    public CCI_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);//initialize inflater
        //inflating the customized layout as a view in the recycler view
        View view = inflater.inflate(R.layout.car_catalog_item,parent,false);
        return new CCI_RecyclerViewAdapter.CCI_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CCI_ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //getting car object coming to screen
        car = catalogList.get(position);

        //holding the views to manipulate their contents
        ImageView image = holder.carImg;
        TextView name = holder.name;
        TextView price = holder.price;

        // to show a placeholder while loading
        RequestOptions reqOp = new RequestOptions()
                .placeholder(R.drawable.loading_dots) // the placeholder image
                .error(R.drawable.ic_launcher_foreground); // the error image

        //inserting image from url using glide
        Glide.with(context)
                .setDefaultRequestOptions(reqOp).load(car.getImage()).into(image);

        //setting text data
        price.setText(car.getPrice()+"");
        name.setText(car.getName());

        //adding action for when a recycler view item is selected
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car = catalogList.get(position);//getting car selected

                //constructing intent and putting relevant data into it for the next activity
                Intent intent = new Intent(context,ViewCarActivity.class);
                intent.putExtra(context.getString(R.string.CAR_ID),car.getCar_id());
                intent.putExtra(context.getString(R.string.VIEW_CAR_SOURCE),context.getString(R.string.FROM_BROWSE));
                intent.putExtra(context.getString(R.string.USER_KEY),USER_KEY);

                context.startActivity(intent);//starting activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }



    //used to update the recyclerview with a new list
    public void setFiltered (ArrayList<CarCatalogItemModel> catalogList){
        this.catalogList = catalogList;//update with new list
        notifyDataSetChanged();// notify change to update GUI?
    }

    //holder of the adapter
    public static class CCI_ViewHolder extends RecyclerView.ViewHolder{
        ImageView carImg ;//image of car
        TextView price,name;// other view info of car

        public CCI_ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing views
            carImg =(ImageView) itemView.findViewById(R.id.car_catalog_item_image);
            price =(TextView) itemView.findViewById(R.id.car_catalog_item_price);
            name =(TextView) itemView.findViewById(R.id.car_catalog_item_name);


        }
    }
}

