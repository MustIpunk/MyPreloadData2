package com.example.saiful.mypreloaddata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.saiful.mypreloaddata.adapter.MahasiswaAdapter;
import com.example.saiful.mypreloaddata.database.MahasiswaHelper;
import com.example.saiful.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MahasiswaAdapter mahasiswaAdapter;
    MahasiswaHelper mahasiswaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa);

        recyclerView = findViewById(R.id.recyclerview);

        mahasiswaHelper = new MahasiswaHelper(this);
        mahasiswaAdapter = new MahasiswaAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mahasiswaAdapter);

        mahasiswaHelper.open();
        ArrayList<MahasiswaModel> mahasiswaModels = mahasiswaHelper.getAllData();
        mahasiswaHelper.close();

        mahasiswaAdapter.setData(mahasiswaModels);


    }
}
