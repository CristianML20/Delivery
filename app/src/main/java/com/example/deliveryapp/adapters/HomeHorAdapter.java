package com.example.deliveryapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.R;
import com.example.deliveryapp.models.HomeHorModel;
import com.example.deliveryapp.models.HomeVerModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder>{
    RequestQueue requestQueue;
    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    String ensalada,pizza,hamburguesa,helado,salchipapa,sandwich;
    ArrayList<HomeHorModel> list;




    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        requestQueue = Volley.newRequestQueue(parent.getContext());
        Pizza();
        Hamburguesa();
        Salchipapa();
        Sandwich();
        Helado();
        Ensalada();

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_items,parent,false));
    }


    private void Ensalada() {
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Ensalada";
        StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ensalada = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void Pizza(){
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Pizza";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pizza = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void Hamburguesa(){
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Hamburguesa";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hamburguesa = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void Salchipapa(){
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Salchipapa";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                salchipapa = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void Sandwich(){
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Sandwich";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sandwich = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void Helado(){
        String url = "https://restaurante2022.herokuapp.com/menu/categoria?categoria=Helado";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                helado = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());




        if (check) {
           ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

            updateVerticalRec.callBack(position,homeVerModels);
            check = false;

        }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();

                    if (position == 0){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(pizza);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                homeVerModels.add(new HomeVerModel(jsonObject.optInt("id"),jsonObject.optString("imagen"),jsonObject.optString("nombre"),jsonObject.optString("descripcion"),"10:00 - 23:00","4.5",jsonObject.optString("precio"),jsonObject.optString("categoria")));
                            }
                            updateVerticalRec.callBack(position,homeVerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (position == 1){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(hamburguesa);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                homeVerModels.add(new HomeVerModel(jsonObject.optInt("id"),jsonObject.optString("imagen"),jsonObject.optString("nombre"),jsonObject.optString("descripcion"),"10:00 - 23:00","4.5",jsonObject.optString("precio"),jsonObject.optString("categoria")));
                            }
                            updateVerticalRec.callBack(position,homeVerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (position == 2){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(salchipapa);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                homeVerModels.add(new HomeVerModel(jsonObject.optInt("id"),jsonObject.optString("imagen"),jsonObject.optString("nombre"),jsonObject.optString("descripcion"),"10:00 - 23:00","4.5",jsonObject.optString("precio"),jsonObject.optString("categoria")));
                            }
                            updateVerticalRec.callBack(position,homeVerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (position == 3){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(helado);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                homeVerModels.add(new HomeVerModel(jsonObject.optInt("id"),jsonObject.optString("imagen"),jsonObject.optString("nombre"),jsonObject.optString("descripcion"),"10:00 - 23:00","4.5",jsonObject.optString("precio"),jsonObject.optString("categoria")));
                            }
                            updateVerticalRec.callBack(position,homeVerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (position == 4){
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(sandwich);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                homeVerModels.add(new HomeVerModel(jsonObject.optInt("id"),jsonObject.optString("imagen"),jsonObject.optString("nombre"),jsonObject.optString("descripcion"),"10:00 - 23:00","4.5",jsonObject.optString("precio"),jsonObject.optString("categoria")));
                            }
                            updateVerticalRec.callBack(position,homeVerModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

            if (select){
                if (position == 0){
                    holder.cardView.setBackgroundResource(R.drawable.change_bg);
                    select = false;
                }
            }
            else {
                if (row_index == position){
                    holder.cardView.setBackgroundResource(R.drawable.change_bg);
                }else {
                    holder.cardView.setBackgroundResource(R.drawable.default_bg);
                }
            }
        }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


}
