package com.example.warehouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_PRODUCT_ID = "product_id";

    private TextView tvCount, tvValue, tvLow, tvEmpty;
    private ListView lvProducts;
    private Button btnAdd;

    private DatabaseHelper db;
    private final List<Product> products = new ArrayList<>(); // chỉ để hiển thị
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        tvCount = findViewById(R.id.tvStatCount);
        tvValue = findViewById(R.id.tvStatValue);
        tvLow = findViewById(R.id.tvStatLow);
        tvEmpty = findViewById(R.id.tvEmpty);
        lvProducts = findViewById(R.id.lvProducts);
        btnAdd = findViewById(R.id.btnAddProduct);

        adapter = new ProductAdapter(this, products);
        lvProducts.setAdapter(adapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = products.get(position);
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra(EXTRA_PRODUCT_ID, p.getId()); // truyền id qua Intent
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddProductActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // tải lại danh sách và thống kê sau khi thêm / sửa / xóa
    }

    private void loadData() {
        products.clear();
        products.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();

        tvEmpty.setVisibility(products.isEmpty() ? View.VISIBLE : View.GONE);

        tvCount.setText(String.valueOf(db.countProducts()));
        tvValue.setText(Product.formatMoney(db.getTotalValue()));
        tvLow.setText(String.valueOf(db.countLowStock()));
    }
}
