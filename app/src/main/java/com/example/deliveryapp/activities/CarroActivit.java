package com.example.deliveryapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.R;
import com.example.deliveryapp.models.CartModel;
import com.example.deliveryapp.models.HomeVerModel;
import com.example.deliveryapp.models.Menu;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CarroActivit extends AppCompatActivity {

    public static final String idlis = "idusr";
    private ArrayList<CartModel> listaPedido;
    private RequestQueue requestQueue;
    private RecyclerView carro;
    private AdaptadorCarro adaptadorCarro;

    String id;
    int totalp = 0;
    TextView totalpedi;
    Button enviar;

    public CarroActivit(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        id = getIntent().getStringExtra("idusr");
        listaPedido = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        cargarPedido();

        carro = findViewById(R.id.carro_rec);
        totalpedi = findViewById(R.id.total_pagar);
        enviar = findViewById(R.id.btnpedido);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        carro.setLayoutManager(linearLayoutManager);
        adaptadorCarro = new AdaptadorCarro();
        carro.setAdapter(adaptadorCarro);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaPedido.isEmpty()){
                    Toast.makeText(CarroActivit.this, "Seleccione el Menu", Toast.LENGTH_SHORT).show();
                }else{
                    revisar();
                }

            }
        });
    }


    private void cargarPedido() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String url = "https://restaurante2022.herokuapp.com:443/pedido/fecha?fecha="+date+"&preparando=false&enviado=false&cliente="+Integer.parseInt(id);

        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(CarroActivit.this, response, Toast.LENGTH_LONG).show();
                            JSONArray arreglo = new JSONArray(response);
                            for (int i = 0; i < arreglo.length(); i++) {
                                JSONObject objeto = arreglo.getJSONObject(i);
                                int idpedido = objeto.getInt("id");
                                String total = objeto.getString("totalPagar");
                                int ttl = Integer.parseInt(total);
                                String fecha = objeto.getString("fecha");
                                Boolean enviado = objeto.getBoolean("enviado");
                                Boolean preparando = objeto.getBoolean("preparando");
                                String cantidad = objeto.getString("cantidadProductos");
                                JSONObject menu = objeto.getJSONObject("idMenu");
                                String nombre = menu.getString("nombre");
                                String imagen = menu.getString("imagen");
                                String precio = menu.getString("precio");

                                totalp += ttl;
                                CartModel cartModel = new CartModel(idpedido,total,fecha,enviado,preparando,cantidad,imagen,nombre,precio);
                                listaPedido.add(cartModel);
                                adaptadorCarro.notifyItemRangeInserted(listaPedido.size(),1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }

    private class AdaptadorCarro extends RecyclerView.Adapter<AdaptadorCarro.AdaptadorCarroHolder> {
        @NonNull
        @Override
        public AdaptadorCarro.AdaptadorCarroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorCarro.AdaptadorCarroHolder(getLayoutInflater().inflate(R.layout.micarro_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorCarro.AdaptadorCarroHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return listaPedido.size();
        }

        class AdaptadorCarroHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView nombre,precio,cantidad;
            public AdaptadorCarroHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.detailed);
                nombre = itemView.findViewById(R.id.detailed_name);
                precio = itemView.findViewById(R.id.textView9);
                cantidad = itemView.findViewById(R.id.detailed_cantidad);

            }

            public void imprimir(int position) {
                nombre.setText(listaPedido.get(position).getNombre());
                precio.setText(listaPedido.get(position).getPrecio());
                Picasso.get().load(listaPedido.get(position).getImage()).error(R.drawable.bg1).into(imageView);
                cantidad.setText(listaPedido.get(position).getCantidad());
                String tot = String.valueOf(totalp);
                totalpedi.setText(tot);

                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostrarDialog(listaMenu.get(position),position);
                    }
                });*/
            }
        }

    }

    public void revisar(){
        int idr = Integer.parseInt(id);
        String url = "https://restaurante2022.herokuapp.com:443/lista/?id="+idr;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            enviarPedido();
                        }else{
                            int idLista = 0;
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    idLista = jsonObject.getInt("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            actualizarPedido(idLista);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void actualizarPedido(int idL){
        String url = "https://restaurante2022.herokuapp.com/lista";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JSONObject params = new JSONObject();
        try {
            params.put("id",idL);
            params.put("fecha",date);
            params.put("idUsuario",Integer.parseInt(id));
            params.put("metodoPago","En Casa");
            params.put("estado","Nuevos");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CarroActivit.this, "Pedido enviado en 40 minutos estara llegando a su hogar", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);

    }

    public void enviarPedido(){
        String url = "https://restaurante2022.herokuapp.com/lista";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JSONObject params = new JSONObject();
        try {
            params.put("fecha",date);
            params.put("idUsuario",Integer.parseInt(id));
            params.put("metodoPago","En Casa");
            params.put("estado","Nuevos");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CarroActivit.this, "Pedido enviado en 40 minutos estara llegando a su hogar", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }
}