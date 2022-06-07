package com.example.deliveryapp.ui.slideshow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.deliveryapp.MainActivity;
import com.example.deliveryapp.R;
import com.example.deliveryapp.models.Usuarios;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PerfilFragment extends Fragment {

    RequestQueue requestQueue;
    TextView namecompletd;
    TextInputEditText nombre,apellido,correo,telefono,password;
    Button actualizar;
    int id;
    public Usuarios usuario;


    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        String idM = MainActivity.name;
        id = Integer.parseInt(idM);
        requestQueue = Volley.newRequestQueue(getContext());

        traerDatos();

        namecompletd = root.findViewById(R.id.nombre_completo);
        nombre = root.findViewById(R.id.nombre_actualizar);
        apellido = root.findViewById(R.id.apellido_actualizar);
        correo = root.findViewById(R.id.correo_actualizar);
        telefono = root.findViewById(R.id.telefono_actualizar);
        password = root.findViewById(R.id.password_actualizar);
        actualizar = root.findViewById(R.id.btn_actualizar_usuario);


        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarcampos(nombre.getText().toString(),apellido.getText().toString(),correo.getText().toString(),password.getText().toString(),telefono.getText().toString());
            }
        });


        return root;
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
            Toast.makeText(getContext(), "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length() < 8) {
                    Toast.makeText(getContext(), "La contraseña debe ser mayor a ocho caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    if (telefono.length() < 10) {
                        Toast.makeText(getContext(), "El numero de telefono es muy corto", Toast.LENGTH_SHORT).show();

                    } else if (telefono.length() > 10) {
                        Toast.makeText(getContext(), "El numero de telefono es muy largo", Toast.LENGTH_SHORT).show();
                    } else {
                        actualizarDatos(nombre, apellido, email, password, telefono);
                    }
                }
            } else {
                Toast.makeText(getContext(), "Email Invalido", Toast.LENGTH_SHORT).show();
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
            params.put("rol","User");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show();
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