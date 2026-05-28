package com.tradingsim.client.feature.trading.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {

    public interface OnAssetClickListener {
        void onAssetClick(String asset);
    }

    private final List<String> assets;
    private final OnAssetClickListener listener;

    public AssetAdapter(List<String> assets, OnAssetClickListener listener) {
        this.assets = assets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trading_asset, parent, false);

        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {

        String asset = assets.get(position);

        holder.tvAssetSymbol.setText(asset);

        holder.itemView.setOnClickListener(v -> {
            listener.onAssetClick(asset);
        });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    static class AssetViewHolder extends RecyclerView.ViewHolder {

        TextView tvAssetSymbol;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAssetSymbol = itemView.findViewById(R.id.tvAssetSymbol);
        }
    }
}