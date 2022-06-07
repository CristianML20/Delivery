package com.example.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.R;
import com.example.deliveryapp.models.Usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    public static final String id = "id";
    LinearLayout crear,listar,listarusuario;
    TextView adminwel,adminemail;
    LinearLayout home,profile,exit;
    public Usuarios usuario;
    public static int idUsuarioAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        requestQueue = Volley.newRequestQueue(this);
        usuario();
        crear = findViewById(R.id.crear);
        listar = findViewById(R.id.listar);
        listarusuario = findViewById(R.id.listar_usuarios);
        adminwel = findViewById(R.id.txtuser);
        adminemail = findViewById(R.id.txtemail);
        home = findViewById(R.id.inicio);
        profile = findViewById(R.id.perfil);
        exit = findViewById(R.id.salir);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Hasta Luego", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,AgregarActivity.class);
                intent.putExtra(AgregarActivity.idagr,String.valueOf(idUsuarioAdmin));
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ProfileAdminActivity.class);
                startActivity(intent);
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ListarMenuActivity.class);
                startActivity(intent);
            }
        });

        listarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,PedidosActivity.class);
                intent.putExtra(PedidosActivity.nombre,usuario.getNombre());
                intent.putExtra(PedidosActivity.correo,usuario.getEmail());
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        super.onStart();
        usuario();

    }

    public void usuario(){
        String admin = getIntent().getStringExtra("id");
        int i = Integer.parseInt(admin);
        idUsuarioAdmin = i;
        String url = "https://restaurante2022.herokuapp.com/usuarios/?id="+i;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    int id = jsonObject.getInt("id");
                    String nombre = jsonObject.getString("nombre");
                    String apellido = jsonObject.getString("apellido");
                    String email = jsonObject.getString("email");
                    String password = jsonObject.getString("password");
                    String telefono = jsonObject.getString("telefono");
                    String rol = jsonObject.getString("rol");

                    Usuarios usuarios = new Usuarios(id,nombre,apellido,email,password,telefono,rol);
                    adminwel.setText("¡Bienvenido "+usuarios.getNombre()+"!");
                    adminemail.setText(usuarios.getEmail());
                    usuario = usuarios;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}