package com.satanbakespancakes.sqliteexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rView;
    static DBProducts connector;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connector = new DBProducts(this);
        connector.insert("Ливерная колбаса", 100);
        connector.insert("Мороженое", 20);
        connector.insert("Рыба-Фуга", 500);
        rView = (RecyclerView) findViewById(R.id.r_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rView.setLayoutManager(llm);
        DividerItemDecoration did = new DividerItemDecoration(rView.getContext(), LinearLayoutManager.VERTICAL);
        rView.addItemDecoration(did);
        adapter = new CustomAdapter(this, connector.selectAll());
        rView.setAdapter(adapter);
    }

    public static void updateRecView(){
        adapter.setProducts(connector.selectAll());
        adapter.notifyDataSetChanged();
    }

    public static void delete(long id){
        connector.delete(id);
        updateRecView();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

        private final LayoutInflater inflater;
        private List<Product> products;

        public CustomAdapter(Context context, List<Product> list) {
            this.inflater = LayoutInflater.from(context);
            this.products = list;
        }

        public void setProducts(List<Product> list){
            products = list;
        }

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public CustomHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item, parent, false);
            return new CustomHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull CustomAdapter.CustomHolder holder, int position) {
            Product product = products.get(position);
            holder.idText.setText(String.valueOf(product.getId()));
            holder.nameText.setText(product.getName());
            holder.priceText.setText(String.valueOf(product.getPrice()));
            holder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(product.getId());

                }
            });
        }


        @Override
        public int getItemCount() {
            return products.size();
        }


        public class CustomHolder extends RecyclerView.ViewHolder {
            final TextView idText, nameText, priceText;
            final Button buyButton;
            public CustomHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
                super(itemView);
                idText = (TextView) itemView.findViewById(R.id.id);
                nameText = (TextView) itemView.findViewById(R.id.name);
                priceText = (TextView) itemView.findViewById(R.id.price);
                buyButton = (Button) itemView.findViewById(R.id.button);
            }

        }
    }
}