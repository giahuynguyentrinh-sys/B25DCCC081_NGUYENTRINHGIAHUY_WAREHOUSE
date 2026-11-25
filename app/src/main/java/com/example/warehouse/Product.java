package com.example.warehouse;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    public static final int LOW_STOCK_THRESHOLD = 5;

    private int id;
    private String name;
    private long price;
    private int quantity;
    private String category;
    private boolean active;
    private String description;

    public Product(String name, long price, int quantity, String category,
                   boolean active, String description) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /** Sản phẩm sắp hết hàng khi số lượng < 5. */
    public boolean isLowStock() {
        return quantity < LOW_STOCK_THRESHOLD;
    }

    /** Giá trị tồn kho = giá bán x số lượng. */
    public long getStockValue() {
        return price * quantity;
    }

    /** Định dạng tiền có dấu phân cách hàng nghìn, ví dụ 150.000 đ */
    public static String formatMoney(long value) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(value) + " đ";
    }
}
