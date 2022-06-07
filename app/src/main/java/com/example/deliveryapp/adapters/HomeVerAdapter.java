package com.example.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.MainActivity;
import com.example.deliveryapp.R;
import com.example.deliveryapp.activities.CarroActivit;
import com.example.deliveryapp.models.CartModel;
import com.example.deliveryapp.models.HomeVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    CarroActivit cartActivity;
    RequestQueue rq;
    String  nombre;
    private BottomSheetDialog bottomSheetDialog;
    Context context;
    ArrayList<HomeVerModel> list;
    ArrayList<CartModel> pizza = new ArrayList<>();
    ArrayList<CartModel> hambur = new ArrayList<>();



    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        nombre = MainActivity.name;
        rq = Volley.newRequestQueue(parent.getContext());
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String nName = list.get(position).getName();
        final String nTiming = list.get(position).getTiming();
        final String nRating = list.get(position).getRating();
        final String nPrice = list.get(position).getPrecio();
        final String nDescrip = list.get(position).getDescription();
        final String nImage = list.get(position).getImage();
        int cantidad = 0;

        Picasso.get().load(list.get(position).getImage()).error(R.drawable.bg1).into(holder.imageView);
        //holder.imageView.setImageResource();
        holder.name.setText(list.get(position).getName());
        holder.timing.setText(list.get(position).getTiming());
        holder.rating.setText(list.get(position).getRating());
        holder.precio.setText(list.get(position).getPrecio());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetTheme);

                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,null);

                ImageView bottomImg = sheetView.findViewById(R.id.bottom_img);
                TextView bottomName = sheetView.findViewById(R.id.bottom_name);
                TextView bottomPrice = sheetView.findViewById(R.id.bottom_price);
                TextView bottomDescrip = sheetView.findViewById(R.id.detailed_des);
                ImageView bottommenos = sheetView.findViewById(R.id.minusCardBtn);
                ImageView bottommas = sheetView.findViewById(R.id.plusCardBtn);
                TextView bottomcantidad = sheetView.findViewById(R.id.numberItemTxt);


                bottomName.setText(nName);
                bottomPrice.setText(nPrice);
                bottomDescrip.setText(nDescrip);
                Picasso.get().load(list.get(position).getImage()).error(R.drawable.bg1).into(bottomImg);

                bottommas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int cant = Integer.parseInt(bottomcantidad.getText().toString());
                        int precioI = Integer.parseInt(list.get(position).getPrecio()) ;


                        if (cant > 0){
                            cant++;

                        }

                        bottomcantidad.setText(String.valueOf(cant));
                        int preciof = precioI * Integer.parseInt(bottomcantidad.getText().toString());
                        bottomPrice.setText(String.valueOf(preciof));
                    }
                });
                bottommenos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int cant = Integer.parseInt(bottomcantidad.getText().toString());
                        int precioI = Integer.parseInt(list.get(position).getPrecio()) ;
                        int precioP = Integer.parseInt(bottomPrice.getText().toString());
                        int preciof = 0;
                        if (cant > 1){
                            cant--;
                            preciof = precioP - precioI;
                        }else if (cant == 1){
                            cant = 1;
                            preciof = precioI;
                        }
                        bottomcantidad.setText(String.valueOf(cant));
                        bottomPrice.setText(String.valueOf(preciof));
                    }
                });
                //bottomImg.setImageResource(nImage);

                sheetView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://restaurante2022.herokuapp.com/pedido";
                        JSONObject params = new JSONObject();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        try {
                            params.put("idMenu",list.get(position).getId());
                            params.put("idCliente",Integer.parseInt(nombre));
                            params.put("totalPagar",bottomPrice.getText().toString());
                            params.put("fecha",date);
                            params.put("enviado",false);
                            params.put("preparando",false);
                            params.put("cantidadProductos",bottomcantidad.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                                url,
                                params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(context, "Añadido al Carro", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                        rq.add(jsonObjectRequest);



                    }
                });


                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,timing,rating,precio;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timing);
            rating = itemView.findViewById(R.id.rating);
            precio = itemView.findViewById(R.id.precio);
        }
    }


}
