package com.tradingsim.client.feature.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.time.format.DateTimeFormatter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;
import com.tradingsim.client.domain.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);

        holder.tvType.setText(transaction.getType());
        holder.tvAmount.setText(
                holder.itemView.getContext().getString(
                        R.string.transaction_amount,
                        transaction.getAmount().toString()
                )
        );

        holder.tvPrice.setText(
                holder.itemView.getContext().getString(
                        R.string.transaction_price,
                        transaction.getPrice().toString()
                )
        );

        holder.tvDate.setText(
                transaction.getDateTime().format(DATE_TIME_FORMATTER)
        );
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvAmount;
        TextView tvPrice;
        TextView tvDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvTransactionType);
            tvAmount = itemView.findViewById(R.id.tvTransactionAmount);
            tvPrice = itemView.findViewById(R.id.tvTransactionPrice);
            tvDate = itemView.findViewById(R.id.tvTransactionDate);
        }
    }
}