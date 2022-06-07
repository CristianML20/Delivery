package com.example.deliveryapp;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.example.deliveryapp.models.Usuarios;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deliveryapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    public static final String nombres = "names";
    public static final String correos = "emails";
    public static final String id = "idusuario";
    public static String name;
    TextView hola;
    String usuario,correo,idusr;
    Button abrir;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idusr = getIntent().getStringExtra("idusuario");
        name=idusr;
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(binding.appBarMain.toolbar);

        hola = findViewById(R.id.textSaludo);
        usuario = getIntent().getStringExtra("names");
        correo = getIntent().getStringExtra("emails");

        abrir =findViewById(R.id.carro);


        hola.setText("¡Bienvenido "+ usuario + " !");




        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    protected void onStart() {
        super.onStart();
        cargardatos();

    }




    private void cargardatos() {
        final View view = binding.navView.getHeaderView(0);
        final TextView nobre= view.findViewById(R.id.nombreMain),
        correom = view.findViewById(R.id.correoMain);
        nobre.setText(usuario);
        correom.setText(correo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void salir(View view) {
        finish();
    }


}