package com.technest.needfood.admin.stok.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technest.needfood.R;
import com.technest.needfood.models.kategori.Kategori;
import com.technest.needfood.admin.stok.item_stok.ItemStokActivity;
import com.technest.needfood.utils.Constanta;

import java.util.ArrayList;

public class KategoriStokAdapter extends RecyclerView.Adapter<KategoriStokAdapter.MyHolderView> {

    private Context mContext;
    private ArrayList<Kategori> kategoris;

    public KategoriStokAdapter(Context mContext, ArrayList<Kategori> kategoris) {
        this.mContext = mContext;
        this.kategoris = kategoris;
    }

    @NonNull
    @Override
    public KategoriStokAdapter.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_kategori_stok, parent, false);
        KategoriStokAdapter.MyHolderView vHolder = new KategoriStokAdapter.MyHolderView(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriStokAdapter.MyHolderView holder, final int position) {

        holder.titel_kategori_stok.setText(kategoris.get(position).getKategori());
        Glide.with(mContext)
                .load(Constanta.url_foto_kategori+kategoris.get(position).getFoto())
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .into(holder.image_kategori_stok);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ItemStokActivity.class);
                intent.putExtra(ItemStokActivity.EXTRA_DATA, kategoris.get(position));
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return kategoris.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {

        private TextView titel_kategori_stok;
        private ImageView image_kategori_stok;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);

            titel_kategori_stok = itemView.findViewById(R.id.titel_kategori_stok);
            image_kategori_stok = itemView.findViewById(R.id.image_kategori_stok);

        }
    }
}
