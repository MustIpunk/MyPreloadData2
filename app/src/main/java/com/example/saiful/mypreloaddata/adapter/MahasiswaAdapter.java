package com.example.saiful.mypreloaddata.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saiful.mypreloaddata.R;
import com.example.saiful.mypreloaddata.model.MahasiswaModel;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.Mahasiswaholder> {
    private ArrayList<MahasiswaModel> listMahasiswa = new ArrayList<>();

    public MahasiswaAdapter(){

    }
    public void setData(ArrayList<MahasiswaModel> listMahasiswa){
        if (listMahasiswa.size() > 0){
            this.listMahasiswa.clear();
        }
        this.listMahasiswa.addAll(listMahasiswa);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Mahasiswaholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa_row, parent, false);
        return new Mahasiswaholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Mahasiswaholder holder, int position) {
        holder.textViewNim.setText(listMahasiswa.get(position).getNim());
        holder.textViewNama.setText(listMahasiswa.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    class Mahasiswaholder extends RecyclerView.ViewHolder {
        private TextView textViewNim;
        private TextView textViewNama;
        Mahasiswaholder(@NonNull View itemView) {
            super(itemView);
            textViewNim = itemView.findViewById(R.id.txt_nim);
            textViewNama = itemView.findViewById(R.id.txt_nama);
        }
    }
}
