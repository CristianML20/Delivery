package com.example.deliveryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deliveryapp.MainActivity;
import com.example.deliveryapp.R;
import com.example.deliveryapp.models.Usuarios;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    RequestQueue requestQueue;
    TextInputEditText email,password;
    Button iniciar;
    TextView corr;
    public Usuarios usr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        iniciar = findViewById(R.id.btn_iniciar_sesion);
        corr = findViewById(R.id.correoMain);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();*/
                validarcampos(email.getText().toString(),password.getText().toString());
            }
        });

    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }


    private void validarcampos(String emai,String passwor){
        if (emai.isEmpty() || passwor.isEmpty()){
            Toast.makeText(this,"Por favor rellene todos los campos",Toast.LENGTH_SHORT).show();
        }else {
            if (Patterns.EMAIL_ADDRESS.matcher(emai).matches()){
                if (password.length() < 8 ){
                    Toast.makeText(this,"La contraseña debe ser mayor a ocho caracteres",Toast.LENGTH_SHORT).show();
                }else {
                    iniciarsesion(emai,passwor);
                }
            }else{
                    Toast.makeText(this,"Email Invalido",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onResponse(String response) {

        if (response.equals("[]")){
            Toast.makeText(LoginActivity.this, "Usuario no Registrado", Toast.LENGTH_SHORT).show();
            limpiar();
        }else {
            try {
                JSONArray jsonArray = new JSONArray(response);
                //Toast.makeText(LoginActivity.this, jsonArray.toString(), Toast.LENGTH_SHORT).show();

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int id = jsonObject.getInt("id");
                String nombre = jsonObject.getString("nombre");
                String apellido = jsonObject.getString("apellido");
                String email = jsonObject.getString("email");
                String password = jsonObject.getString("password");
                String telefono = jsonObject.getString("telefono");
                String rol = jsonObject.getString("rol");
                usr = new Usuarios(id,nombre,apellido,email,password,telefono,rol);

                limpiar();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent;
            if (usr.getRol().equals("User")){
                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.nombres,usr.getNombre());
                intent.putExtra(MainActivity.correos,usr.getEmail());
                intent.putExtra(MainActivity.id,String.valueOf(usr.getId()));

            }else{
                intent = new Intent(LoginActivity.this, AdminActivity.class);
                intent.putExtra(AdminActivity.id,String.valueOf(usr.getId()));
            }
            startActivity(intent);
            finish();

        }
    }




    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }

    public void iniciarsesion(String emai, String passwo){
        String url = "https://restaurante2022.herokuapp.com/usuarios/login?email="+ emai +"&password="+ passwo;
        StringRequest request = new StringRequest(Request.Method.GET,url,this,this);
        requestQueue.add(request);
    }


    public void limpiar(){
       email.setText("");
       password.setText("");
    }



}