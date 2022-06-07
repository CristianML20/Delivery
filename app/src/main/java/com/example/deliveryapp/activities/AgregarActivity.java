package com.example.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AgregarActivity extends AppCompatActivity {
    public static final String idagr = "idagr";
    RequestQueue requestQueue;
    Spinner catego;
    LinearLayout inicio,salir;
    EditText nombre,precio,descripcion,urlimg;
    Button agregar;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        requestQueue = Volley.newRequestQueue(this);
        inicio = findViewById(R.id.inicio);
        catego = findViewById(R.id.categoria);
        nombre = findViewById(R.id.txtnombre);
        precio = findViewById(R.id.txtprecio);
        descripcion = findViewById(R.id.txtdescripcion);
        urlimg = findViewById(R.id.txtimagen);
        agregar = findViewById(R.id.btnagregar);
        salir = findViewById(R.id.salir_agregar);
        id = getIntent().getStringExtra("idagr");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.opciones, R.layout.spinner_items);
        catego.setAdapter(adapter);


        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarActivity.this,AdminActivity.class);
                intent.putExtra(AdminActivity.id,id);
                startActivity(intent);
                finish();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarMenu(catego.getSelectedItem().toString(),nombre.getText().toString(),descripcion.getText().toString(),urlimg.getText().toString(),precio.getText().toString());
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AgregarActivity.this, "Hasta Luego", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void agregarMenu(String cat,String nom,String des,String urli,String prec) {
        String url = "https://restaurante2022.herokuapp.com/menu";
        JSONObject params = new JSONObject();

        try {
            params.put("nombre",nom);
            params.put("imagen",urli);
            params.put("descripcion",des);
            params.put("precio",prec);
            params.put("categoria",cat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AgregarActivity.this,"Menu Registrado",Toast.LENGTH_LONG).show();

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