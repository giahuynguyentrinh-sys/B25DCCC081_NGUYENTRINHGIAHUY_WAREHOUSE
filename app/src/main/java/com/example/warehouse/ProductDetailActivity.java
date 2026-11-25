package com.example.warehouse;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvName, tvCategory, tvStatus, tvPrice, tvQuantity,
            tvLowBadge, tvTotal, tvDescription;
    private Button btnImport, btnSell, btnBack, btnDelete;

    private DatabaseHelper db;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);

        tvName = findViewById(R.id.tvDetailName);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvQuantity = findViewById(R.id.tvDetailQuantity);
        tvLowBadge = findViewById(R.id.tvDetailLow);
        tvTotal = findViewById(R.id.tvDetailTotal);
        tvDescription = findViewById(R.id.tvDetailDescription);
        btnImport = findViewById(R.id.btnImport);
        btnSell = findViewById(R.id.btnSell);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        // Nhận id qua Intent
        productId = getIntent().getIntExtra(MainActivity.EXTRA_PRODUCT_ID, -1);

        if (!showProduct()) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(+1);
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(-1);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteProduct(productId);
                Toast.makeText(ProductDetailActivity.this,
                        "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                finish(); // quay lại danh sách, onResume() cập nhật thống kê
            }
        });
    }

    /** Truy vấn lại theo id và hiển thị; trả về false nếu không có sản phẩm. */
    private boolean showProduct() {
        Product p = db.getProductById(productId);
        if (p == null) {
            return false;
        }

        tvName.setText(p.getName());
        tvCategory.setText(p.getCategory());
        tvStatus.setText(p.isActive() ? "Đang kinh doanh" : "Ngừng kinh doanh");
        tvPrice.setText(Product.formatMoney(p.getPrice()));
        tvQuantity.setText(String.valueOf(p.getQuantity()));
        tvTotal.setText(Product.formatMoney(p.getStockValue()));

        String desc = p.getDescription();
        tvDescription.setText(desc == null || desc.trim().isEmpty()
                ? "(Không có mô tả)" : desc);

        if (p.isLowStock()) {
            tvLowBadge.setVisibility(View.VISIBLE);
            tvQuantity.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            tvLowBadge.setVisibility(View.GONE);
            tvQuantity.setTextColor(Color.parseColor("#212121"));
        }
        return true;
    }

    private void changeQuantity(int delta) {
        Product p = db.getProductById(productId);
        if (p == null) {
            finish();
            return;
        }

        int newQty = p.getQuantity() + delta;
        if (delta < 0 && p.getQuantity() <= 0) {
            Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
            return; // không giảm nữa
        }

        db.updateQuantity(productId, newQty);
        showProduct(); // cập nhật lại các thông tin hiển thị
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
