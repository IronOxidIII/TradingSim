package com.tradingsim.client.feature.portfolio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;
import com.tradingsim.client.domain.model.PortfolioAsset;

import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {

    private final List<PortfolioAsset> assets;

    public PortfolioAdapter(List<PortfolioAsset> assets) {
        this.assets = assets;
    }

    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_portfolio_asset, parent, false);

        return new PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position) {
        PortfolioAsset asset = assets.get(position);

        holder.tvAssetName.setText(asset.getSymbol());
        holder.tvAssetAmount.setText(holder.itemView.getContext().getString(
                R.string.portfolio_amount,
                String.valueOf(asset.getAmount())
        ));
        holder.tvAssetPrice.setText(holder.itemView.getContext().getString(
                R.string.portfolio_price,
                String.valueOf(asset.getPrice())
        ));
        holder.tvAssetTotal.setText(holder.itemView.getContext().getString(
                R.string.portfolio_total,
                String.valueOf(asset.getTotalValue())
        ));
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    static class PortfolioViewHolder extends RecyclerView.ViewHolder {
        TextView tvAssetName;
        TextView tvAssetAmount;
        TextView tvAssetPrice;
        TextView tvAssetTotal;

        public PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAssetName = itemView.findViewById(R.id.tvAssetName);
            tvAssetAmount = itemView.findViewById(R.id.tvAssetAmount);
            tvAssetPrice = itemView.findViewById(R.id.tvAssetPrice);
            tvAssetTotal = itemView.findViewById(R.id.tvAssetTotal);
        }
    }
}