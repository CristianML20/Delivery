package com.example.deliveryapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.deliveryapp.models.Menu;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarMenuActivity extends AppCompatActivity {

    private ArrayList<Menu> listaMenu;
    private RequestQueue requestQueue,rq;
    private RecyclerView menus;
    private AdaptadorMenu adaptadorMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_menu);

        listaMenu = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        rq = Volley.newRequestQueue(this);


        cargarMenu();

        menus = findViewById(R.id.listar_menu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        menus.setLayoutManager(linearLayoutManager);
        adaptadorMenu = new AdaptadorMenu();
        menus.setAdapter(adaptadorMenu);


    }


    private void cargarMenu() {

        String url = "https://restaurante2022.herokuapp.com/menu";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arreglo = new JSONArray(response);
                            for (int i = 0; i < arreglo.length(); i++) {
                                JSONObject objeto = arreglo.getJSONObject(i);
                                String nombre = objeto.getString("nombre");
                                String descripcion = objeto.getString("descripcion");
                                int id = objeto.getInt("id");
                                String imagen = objeto.getString("imagen");
                                String categoria = objeto.getString("categoria");
                                String precio = objeto.getString("precio");
                                Menu menu = new Menu(id,categoria,nombre,imagen,descripcion,precio);
                                listaMenu.add(menu);
                                adaptadorMenu.notifyItemRangeInserted(listaMenu.size(),1);
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
        requestQueue.add(jsonObjectRequest);
    }

    private class AdaptadorMenu extends RecyclerView.Adapter<AdaptadorMenu.AdaptadorMenuHolder>{

        @NonNull
        @Override
        public AdaptadorMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorMenuHolder(getLayoutInflater().inflate(R.layout.layout_target_menu,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorMenuHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return listaMenu.size();
        }

        class AdaptadorMenuHolder extends RecyclerView.ViewHolder {
            TextView tvNombre,tvDescripcion,tvId,tvCategoria,tvPrecio;
            ImageView ivFoto;
            public AdaptadorMenuHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.name);
                tvDescripcion = itemView.findViewById(R.id.descr);
                tvId = itemView.findViewById(R.id.idmenu);
                tvCategoria = itemView.findViewById(R.id.catego);
                tvPrecio = itemView.findViewById(R.id.precioa);
                ivFoto = itemView.findViewById(R.id.imgMenu);
            }

            public void imprimir(int position) {
                tvNombre.setText(listaMenu.get(position).getNombre());
                tvDescripcion.setText(listaMenu.get(position).getDescripcion());
                tvId.setText(String.valueOf(listaMenu.get(position).getId()));
                tvCategoria.setText(listaMenu.get(position).getCategoria());
                tvPrecio.setText(listaMenu.get(position).getPrecio());
                Picasso.get().load(listaMenu.get(position).getImagen()).error(R.drawable.bg1).into(ivFoto);

                /*.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostrarDialog(listaMenu.get(position));
                    }
                });*/
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostrarDialog(listaMenu.get(position),position);
                    }
                });
            }
        }

    }

    private void mostrarDialog(Menu s, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListarMenuActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_personalizado,null);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        TextView txtn = view.findViewById(R.id.text_dialog);
        txtn.setText("Que Desea Hacer con este Menu...");

        Button btnEliminar = view.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="https://restaurante2022.herokuapp.com/menu/delete?id="+s.getId();
                StringRequest json = new StringRequest(Request.Method.DELETE,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ListarMenuActivity.this, "Menu Eliminado", Toast.LENGTH_SHORT).show();
                                listaMenu.remove(position);
                                adaptadorMenu.notifyItemRemoved(position);
                                adaptadorMenu.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                error.getMessage();
                            }
                        });
                requestQueue.add(json);
                //Toast.makeText(ListarMenuActivity.this, String.valueOf(s.getId()), Toast.LENGTH_SHORT).show();
                dialog.dismiss();


            }
        });

        Button btnEditar = view.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder buildere = new AlertDialog.Builder(ListarMenuActivity.this);

                LayoutInflater inflatere = getLayoutInflater();

                View viewedit = inflatere.inflate(R.layout.dialog_personalizado_editar,null);

                buildere.setView(viewedit);

                final AlertDialog dialogf = buildere.create();
                dialogf.show();
                dialogf.setCancelable(false);

                Spinner catego = dialogf.findViewById(R.id.categoriaedit);
                EditText nombre = dialogf.findViewById(R.id.nombreedit);
                EditText url = dialogf.findViewById(R.id.urledit);
                EditText descripcion = dialogf.findViewById(R.id.descripcionedit);
                EditText precio = dialogf.findViewById(R.id.precioedit);
                Button editar = dialogf.findViewById(R.id.btnEditar);
                Button cancelar = dialogf.findViewById(R.id.btnCancelar);
                ImageView img = dialogf.findViewById(R.id.cargarimg);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ListarMenuActivity.this,R.array.opciones, R.layout.spinner_items2);
                catego.setAdapter(adapter);
                nombre.setText(s.getNombre());
                url.setText(s.getImagen());
                Picasso.get().load(url.getText().toString()).into(img);
                descripcion.setText(s.getDescripcion());
                precio.setText(s.getPrecio());
                //dialogf.dismiss();
                dialog.dismiss();

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogf.dismiss();
                    }
                });

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String urlweb = "https://restaurante2022.herokuapp.com/menu";
                        JSONObject params = new JSONObject();
                        try {
                            params.put("id",s.getId());
                            params.put("nombre",nombre.getText().toString());
                            params.put("imagen",url.getText().toString());
                            params.put("descripcion",descripcion.getText().toString());
                            params.put("precio",precio.getText().toString());
                            params.put("categoria",catego.getSelectedItem().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                                urlweb,
                                params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Toast.makeText(ListarMenuActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                        int idn = response.optInt("id");
                                        String nombren = response.optString("nombre");
                                        String categorian = response.optString("categoria");
                                        String imagenn = response.optString("imagen");
                                        String descripcionn = response.optString("descripcion");
                                        String precion = response.optString("precio");
                                        Toast.makeText(ListarMenuActivity.this, nombren + "" + categorian, Toast.LENGTH_SHORT).show();
                                        listaMenu.set(position,new Menu(idn,categorian,nombren,imagenn,descripcionn,precion));
                                        adaptadorMenu.notifyItemChanged(position);
                                        adaptadorMenu.notifyDataSetChanged();
                                        dialogf.dismiss();
                                    }
                                },new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                }
                        });
                        requestQueue.add(jsonObjectRequest);

                    }
                });
            }
        });


        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void eliminarMenu(String s){
        String url = "https://restaurante2022.herokuapp.com/menu";
        int id = Integer.parseInt(s);
        JSONObject params = new JSONObject();
        try {
            params.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ListarMenuActivity.this, "Menu Eliminado", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}