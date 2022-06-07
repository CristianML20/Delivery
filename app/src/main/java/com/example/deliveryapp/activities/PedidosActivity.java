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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.example.deliveryapp.models.ListaPModel;
import com.example.deliveryapp.models.PedidosAdminModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PedidosActivity extends AppCompatActivity {

    public static final String nombre = "nombre admin";
    public static final String correo = "correo admin";
    private ArrayList<ListaPModel> listaP;
    private RequestQueue requestQueue;
    private RecyclerView lista;
    private AdaptadorLista adaptadorLista;
    LinearLayout home,exit;
    String name,email;
    TextView nom,ema;
    Button prepa,env,nue;
    Boolean prepar = false;
    Boolean envi = false;


    //////////////////////////// lista de pedidos del dialog
    private ArrayList<PedidosAdminModel> list;
    private RecyclerView recyclerViewPedidos;
    private AdaptadorPedidos adaptadorPedidos;
    private int idCliente,idLista;
    private String fechaLista,metodoLista;
    TextView total;
    int totalPedido;
    CheckBox preparando,enviado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        listaP = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        home = findViewById(R.id.inicioL);
        exit = findViewById(R.id.salirL);
        nom = findViewById(R.id.txtuserp);
        ema = findViewById(R.id.txtemailp);
        prepa = findViewById(R.id.btn_preparando_listar);
        env = findViewById(R.id.btn_enviado_listar);
        nue = findViewById(R.id.btn_nuevos_listar);

        prepa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepar = true;
                envi = false;
                listaP = new ArrayList<>();
                String estado = "Preparandose";
                cargarLista(estado);
                adaptadorLista.notifyDataSetChanged();

            }
        });

        env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepar = true;
                envi = true;
                listaP = new ArrayList<>();
                String estadoe = "Enviados";
                cargarLista(estadoe);
                adaptadorLista.notifyDataSetChanged();
            }
        });

        nue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepar = false;
                envi = false;
                listaP = new ArrayList<>();
                String estadon = "Nuevos";
                cargarLista(estadon);
                adaptadorLista.notifyDataSetChanged();
            }
        });


        name = getIntent().getStringExtra("nombre admin");
        email = getIntent().getStringExtra("correo admin");
        nom.setText(name);
        ema.setText(email);

        //cargarLista();

        lista = findViewById(R.id.listar_pedidos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lista.setLayoutManager(linearLayoutManager);
        adaptadorLista = new AdaptadorLista();
        lista.setAdapter(adaptadorLista);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void cargarLista(String p) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String url = "https://restaurante2022.herokuapp.com/lista/fecha?fecha="+date+"&estado="+p;

        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")){
                            Toast.makeText(PedidosActivity.this, "No hay Pedidos "+p+" Registrados Hoy", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    int idlista = object.getInt("id");
                                    String fecha = object.getString("fecha");
                                    String metodoPago = object.getString("metodoPago");
                                    String estado = object.getString("estado");
                                    JSONObject usuario = object.getJSONObject("idUsuario");
                                    int idusr = usuario.getInt("id");
                                    String nombre = usuario.getString("nombre");
                                    String apellido = usuario.getString("apellido");
                                    String telefono = usuario.getString("telefono");
                                    String email = usuario.getString("email");

                                    ListaPModel model = new ListaPModel(idlista,fecha,metodoPago,estado,idusr,nombre,apellido,telefono,email);
                                    listaP.add(model);
                                    adaptadorLista.notifyItemRangeInserted(listaP.size(),1);



                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    private class AdaptadorLista extends RecyclerView.Adapter<AdaptadorListaHolder> {


        @NonNull
        @Override
        public AdaptadorListaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorListaHolder(getLayoutInflater().inflate(R.layout.layout_target_usuario,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorListaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return listaP.size();
        }
    }

    private class AdaptadorListaHolder extends RecyclerView.ViewHolder{
        TextView nombre,telefono,email,fecha,metodo,id;

        public AdaptadorListaHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombres_pedido);
            telefono = itemView.findViewById(R.id.telefono_pedido);
            email = itemView.findViewById(R.id.email_pedido);
            fecha = itemView.findViewById(R.id.fecha_pedido);
            metodo = itemView.findViewById(R.id.pago_pedido);
            id = itemView.findViewById(R.id.id_usuario_pedido);
        }

        public void imprimir(int position){
            nombre.setText(listaP.get(position).getNombre()+" "+listaP.get(position).getApellido());
            telefono.setText(listaP.get(position).getTelefono());
            email.setText(listaP.get(position).getEmail());
            fecha.setText(listaP.get(position).getFecha());
            metodo.setText(listaP.get(position).getMetodo_pago());
            id.setText(String.valueOf(listaP.get(position).getId_usuario()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idCliente = listaP.get(position).getId_usuario();
                    idLista = listaP.get(position).getId_lista();
                    fechaLista = listaP.get(position).getFecha();
                    metodoLista = listaP.get(position).getMetodo_pago();
                    mostrardialog();
                }
            });

        }
    }

    public void mostrardialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_personalizado_pedidos,null);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        preparando = view.findViewById(R.id.preparando);
        enviado = view.findViewById(R.id.enviado);
        total = view.findViewById(R.id.total_pagar_pedido);
        list = new ArrayList<>();
        cargarPedidos();

        recyclerViewPedidos = view.findViewById(R.id.listar_pedidos_admin);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPedidos.setLayoutManager(linearLayoutManager);
        adaptadorPedidos = new AdaptadorPedidos();
        recyclerViewPedidos.setAdapter(adaptadorPedidos);


        Button guardar = view.findViewById(R.id.btnGuadar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean p = preparando.isChecked();
                boolean e = enviado.isChecked();
                guardarCambios(p,e);
                Toast.makeText(PedidosActivity.this, "Cambios Guardados", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Button salir = view.findViewById(R.id.btnSalir);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                totalPedido = 0;
            }
        });

    }

    private void cargarPedidos() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String url = "https://restaurante2022.herokuapp.com/pedido/fecha?fecha="+date+"&preparando="+prepar+"&enviado="+envi+"&cliente="+idCliente;

        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                int idPedido = object.getInt("id");
                                String totalPagar = object.getString("totalPagar");
                                String fecha = object.getString("fecha");
                                Boolean enviado = object.getBoolean("enviado");
                                Boolean preparando = object.getBoolean("preparando");
                                String cantidad = object.getString("cantidadProductos");
                                JSONObject menu = object.getJSONObject("idMenu");
                                int idMenu = menu.getInt("id");
                                String nombre = menu.getString("nombre");
                                String imagen = menu.getString("imagen");
                                String precioUnidad = menu.getString("precio");
                                JSONObject usr = object.getJSONObject("idCliente");
                                int idUsuario = usr.getInt("id");

                                int totalCadaMenu = Integer.parseInt(totalPagar);
                                totalPedido += totalCadaMenu;

                                PedidosAdminModel model = new PedidosAdminModel(idPedido,idMenu,nombre,imagen,precioUnidad,idUsuario,totalPagar,fecha,cantidad,enviado,preparando);
                                list.add(model);
                                adaptadorPedidos.notifyItemRangeInserted(list.size(),1);

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

    private class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidosHolder> {


        @NonNull
        @Override
        public AdaptadorPedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPedidosHolder(getLayoutInflater().inflate(R.layout.layout_target_pedidos,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPedidosHolder holder, int position) {
            holder.imprimirPedidos(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class AdaptadorPedidosHolder extends RecyclerView.ViewHolder{
        TextView nombrep,cantidad,preciou,fechap,totalp;
        ImageView imgpedido;

        public AdaptadorPedidosHolder(@NonNull View itemView) {
            super(itemView);
            nombrep = itemView.findViewById(R.id.name_pedido);
            cantidad = itemView.findViewById(R.id.cantidad_pedido);
            preciou = itemView.findViewById(R.id.precio_unidad_pedido);
            fechap = itemView.findViewById(R.id.fecha_pedido_admin);
            totalp = itemView.findViewById(R.id.total_pagar_admin);
            imgpedido = itemView.findViewById(R.id.img_pedido);

        }

        public void imprimirPedidos(int position){
            nombrep.setText(list.get(position).getNombre());
            cantidad.setText(list.get(position).getCantidad());
            preciou.setText(list.get(position).getPrecioUnidad());
            fechap.setText(list.get(position).getFecha());
            totalp.setText(list.get(position).getTotalPagar());
            Picasso.get().load(list.get(position).getImagen()).error(R.drawable.coffe).into(imgpedido);
            total.setText(String.valueOf(totalPedido));

            preparando.setChecked(prepar);
            enviado.setChecked(envi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    public void guardarCambios(Boolean pre,Boolean en){
        String url = "https://restaurante2022.herokuapp.com/pedido";
        for (int i = 0; i < list.size(); i++) {
            int idPedido = list.get(i).getIdPedido();

            JSONObject params = new JSONObject();
            try {
                params.put("id",idPedido);
                params.put("preparando",pre);
                params.put("enviado",en);
                params.put("cantidadProductos",list.get(i).getCantidad());
                params.put("fecha",list.get(i).getFecha());
                params.put("idCliente",list.get(i).getIdUsuario());
                params.put("idMenu",list.get(i).getIdMenu());
                params.put("totalPagar",list.get(i).getTotalPagar());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            guardarLista(pre,en);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            requestQueue.add(objectRequest);

        }


    }

    private void guardarLista(Boolean prepar, Boolean envia) {
        String url = "https://restaurante2022.herokuapp.com:443/lista";
        String estado;
        if (prepar == true && envia == false){
            estado = "Preparandose";
        }else if (prepar && envia == true){
            estado = "Enviados";
        }else {
            estado = "Nuevos";
        }
        JSONObject param = new JSONObject();
        try {
            param.put("estado",estado);
            param.put("fecha",fechaLista);
            param.put("id",idLista);
            param.put("idUsuario",idCliente);
            param.put("metodoPago",metodoLista);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                url,
                param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listaP = new ArrayList<>();
                        adaptadorLista.notifyDataSetChanged();
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


}