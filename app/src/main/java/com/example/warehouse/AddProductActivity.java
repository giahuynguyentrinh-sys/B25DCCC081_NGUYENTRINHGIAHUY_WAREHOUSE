package com.example.warehouse;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class AddProductActivity extends AppCompatActivity {
    private static final String[] CATEGORIES =
            {"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"};

    private EditText etName, etPrice, etQuantity, etDescription;
    private Spinner spCategory;
    private SwitchCompat swActive;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        swActive.setChecked(true); // mặc định bật

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Tên không được trống
        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên sản phẩm");
            etName.requestFocus();
            return;
        }

        // Giá bán phải là số lớn hơn 0
        long price;
        try {
            price = Long.parseLong(priceStr);
        } catch (NumberFormatException e) {
            etPrice.setError("Giá bán không hợp lệ");
            etPrice.requestFocus();
            return;
        }
        if (price <= 0) {
            etPrice.setError("Giá bán phải lớn hơn 0");
            etPrice.requestFocus();
            return;
        }

        // Số lượng phải là số nguyên không âm
        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Số lượng không hợp lệ");
            etQuantity.requestFocus();
            return;
        }
        if (quantity < 0) {
            etQuantity.setError("Số lượng không được âm");
            etQuantity.requestFocus();
            return;
        }

        String category = spCategory.getSelectedItem().toString();
        boolean active = swActive.isChecked();

        Product p = new Product(name, price, quantity, category, active, description);
        long id = db.insertProduct(p);

        if (id == -1) {
            Toast.makeText(this, "Lưu sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
        finish(); // quay về danh sách; MainActivity.onResume() sẽ tải lại dữ liệu
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
