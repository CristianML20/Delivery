package com.example.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.deliveryapp.models.Usuarios;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileAdminActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    TextView namecompletd;
    TextInputEditText nombre,apellido,correo,telefono,password;
    Button actualizar;
    int id;
    public Usuarios usuario;
    LinearLayout inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);
        id = AdminActivity.idUsuarioAdmin;

        requestQueue = Volley.newRequestQueue(this);

        traerDatos();

        namecompletd = findViewById(R.id.nombre_completo_admin);
        nombre = findViewById(R.id.nombre_actualizar_admin);
        apellido = findViewById(R.id.apellido_actualizar_admin);
        correo = findViewById(R.id.correo_actualizar_admin);
        telefono = findViewById(R.id.telefono_actualizar_admin);
        password = findViewById(R.id.password_actualizar_admin);
        actualizar = findViewById(R.id.btn_actualizar_usuario_admin);
        inicio = findViewById(R.id.inicio_admin);


        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarcampos(nombre.getText().toString(),apellido.getText().toString(),correo.getText().toString(),password.getText().toString(),telefono.getText().toString());
            }
        });


        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void traerDatos() {
        String url = "https://restaurante2022.herokuapp.com:443/usuarios/?id="+id;
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
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

                            usuario = usuarios;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        namecompletd.setText(usuario.getNombre()+" "+usuario.getApellido());
                        nombre.setText(usuario.getNombre());
                        apellido.setText(usuario.getApellido());
                        correo.setText(usuario.getEmail());
                        telefono.setText(usuario.getTelefono());
                        password.setText(usuario.getPassword());

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

    private void validarcampos(String nombre, String apellido, String email, String password, String telefono) {

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length() < 8) {
                    Toast.makeText(this, "La contraseña debe ser mayor a ocho caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    if (telefono.length() < 10) {
                        Toast.makeText(this, "El numero de telefono es muy corto", Toast.LENGTH_SHORT).show();

                    } else if (telefono.length() > 10) {
                        Toast.makeText(this, "El numero de telefono es muy largo", Toast.LENGTH_SHORT).show();
                    } else {
                        actualizarDatos(nombre, apellido, email, password, telefono);
                    }
                }
            } else {
                Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarDatos(String nombre, String apellido, String email, String password, String telefono) {
        String url = "https://restaurante2022.herokuapp.com/usuarios";

        JSONObject params = new JSONObject();
        try {
            params.put("id",id);
            params.put("nombre",nombre);
            params.put("apellido",apellido);
            params.put("telefono",telefono);
            params.put("email",email);
            params.put("password",password);
            params.put("rol","Admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ProfileAdminActivity.this, "Datos Actualizados", Toast.LENGTH_SHORT).show();
                        limpiar();
                        traerDatos();
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

    private void limpiar() {
        nombre.setText("");
        apellido.setText("");
        correo.setText("");
        password.setText("");
        telefono.setText("");
    }

}