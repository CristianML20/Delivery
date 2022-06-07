package com.example.deliveryapp.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    EditText nombr, apelli, ema, passwo, tele;
    Button register;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Volley.newRequestQueue(RegisterActivity.this);


        nombr = findViewById(R.id.nombre);
        apelli = findViewById(R.id.apellido);
        ema = findViewById(R.id.email);
        passwo = findViewById(R.id.password);
        tele = findViewById(R.id.telefono);
        register = findViewById(R.id.btn_register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarcampos(nombr.getText().toString(), apelli.getText().toString(), ema.getText().toString(), passwo.getText().toString(), tele.getText().toString());
            }
        });
    }


    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void enviardatos(String nom, String apell, String emai, String pass, String tel) {
        String url = "https://restaurante2022.herokuapp.com/usuarios";
        JSONObject params = new JSONObject();

        try {
            params.put("nombre", nom);
            params.put("apellido", apell);
            params.put("telefono", tel);
            params.put("email", emai);
            params.put("password", pass);
            params.put("rol", "User");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Ha sido Registrado", Toast.LENGTH_LONG).show();
                        nombr.setText("");
                        apelli.setText("");
                        ema.setText("");
                        passwo.setText("");
                        tele.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        enviardatos(nombre, apellido, email, password, telefono);
                    }
                }
            } else {
                Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


