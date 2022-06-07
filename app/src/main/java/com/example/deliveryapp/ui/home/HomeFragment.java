package com.example.deliveryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.deliveryapp.MainActivity;
import com.example.deliveryapp.R;
import com.example.deliveryapp.activities.CarroActivit;
import com.example.deliveryapp.adapters.HomeHorAdapter;
import com.example.deliveryapp.adapters.HomeVerAdapter;
import com.example.deliveryapp.adapters.UpdateVerticalRec;
import com.example.deliveryapp.models.HomeHorModel;
import com.example.deliveryapp.models.HomeVerModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UpdateVerticalRec{

    RecyclerView homeHorizontalRec,homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;
    String iduser;

    RequestQueue requestQueue;
    private HomeHorAdapter ho;

    /////////////////////////vertical
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,container,false);

        iduser = MainActivity.name;
        Toast.makeText(getContext(), iduser, Toast.LENGTH_SHORT).show();
        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        Button abrir = (Button) root.findViewById(R.id.carro);
        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CarroActivit.class);
                intent.putExtra(CarroActivit.idlis,iduser);
                startActivity(intent);
            }
        });

        ////////////////////////////Horizontal RecyclerView
        homeHorModelList = new ArrayList<>();

        homeHorModelList.add(new HomeHorModel(R.drawable.pizza,"Pizza"));
        homeHorModelList.add(new HomeHorModel(R.drawable.hamburger,"Hamburguesa"));
        homeHorModelList.add(new HomeHorModel(R.drawable.fried_potatoes,"Papas Fritas"));
        homeHorModelList.add(new HomeHorModel(R.drawable.ice_cream,"Helado"));
        homeHorModelList.add(new HomeHorModel(R.drawable.sandwich,"Sandwich"));

        homeHorAdapter = new HomeHorAdapter(this,getActivity(),homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);


        ////////////////////////////Vertical RecyclerView
        homeVerModelList = new ArrayList<>();


        homeVerAdapter = new HomeVerAdapter(getActivity(),homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));




        return root;
    }


    @Override
    public void callBack(int position, ArrayList<HomeVerModel> list) {

        homeVerAdapter = new HomeVerAdapter(getContext(),list);
        homeVerAdapter.notifyDataSetChanged();
        homeVerticalRec.setAdapter(homeVerAdapter);

    }




}