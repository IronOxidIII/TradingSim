package com.tradingsim.client.feature.history;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;
import com.tradingsim.client.data.repository.history.HistoryRepository;
import com.tradingsim.client.data.repository.history.InMemoryHistoryRepository;
import com.tradingsim.client.domain.model.Transaction;

import java.util.ArrayList;

import timber.log.Timber;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerTransactions;

    private final HistoryRepository repository =
            new InMemoryHistoryRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.plant();
        Timber.i("Entering History Screen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerTransactions = findViewById(R.id.recyclerTransactions);

        ArrayList<Transaction> transactions =
                new ArrayList<>(repository.getTransactions());

        TransactionAdapter adapter = new TransactionAdapter(transactions);

        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransactions.setAdapter(adapter);
    }
}