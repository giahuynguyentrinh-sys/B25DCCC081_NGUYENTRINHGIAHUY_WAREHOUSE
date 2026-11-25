package com.example.warehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "warehouse.db";
    public static final int DB_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "category TEXT NOT NULL, "
                + "active INTEGER NOT NULL, "
                + "description TEXT)");

        // Tài khoản mẫu
        insertUser(db, "admin", "123456");

        // Dữ liệu sản phẩm mẫu
        insertProduct(db, new Product("Chuột không dây", 150000, 20,
                "Điện tử", true, "Chuột không dây 2.4GHz, pin AA"));
        insertProduct(db, new Product("Bàn phím cơ", 450000, 3,
                "Điện tử", true, "Bàn phím cơ switch xanh, có đèn nền"));
        insertProduct(db, new Product("Sổ tay A5", 25000, 40,
                "Văn phòng phẩm", true, "Sổ tay bìa cứng khổ A5, 100 trang"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------------------------------------------------------------
    // Users
    // ---------------------------------------------------------------

    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        return db.insert(TABLE_USERS, null, values);
    }

    /** Thêm tài khoản mới vào bảng users. */
    public long insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        return insertUser(db, username, password);
    }

    /** Trả về true nếu có dòng khớp trong bảng users. */
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE_USERS + " WHERE username = ? AND password = ?",
                new String[]{username, password});
        boolean ok = cursor.getCount() > 0;
        cursor.close();
        return ok;
    }

    // ---------------------------------------------------------------
    // Products
    // ---------------------------------------------------------------

    private long insertProduct(SQLiteDatabase db, Product p) {
        ContentValues values = new ContentValues();
        values.put("name", p.getName());
        values.put("price", p.getPrice());
        values.put("quantity", p.getQuantity());
        values.put("category", p.getCategory());
        values.put("active", p.isActive() ? 1 : 0);
        values.put("description", p.getDescription());
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    /** Thêm sản phẩm, trả về id vừa thêm. */
    public long insertProduct(Product p) {
        SQLiteDatabase db = getWritableDatabase();
        return insertProduct(db, p);
    }

    private Product cursorToProduct(Cursor c) {
        Product p = new Product(
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getLong(c.getColumnIndexOrThrow("price")),
                c.getInt(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getInt(c.getColumnIndexOrThrow("active")) == 1,
                c.getString(c.getColumnIndexOrThrow("description")));
        p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
        return p;
    }

    /** Lấy toàn bộ sản phẩm để hiển thị danh sách. */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, name, price, quantity, category, active, description FROM "
                        + TABLE_PRODUCTS + " ORDER BY id ASC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursorToProduct(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /** Lấy một sản phẩm theo id; trả về null nếu không tồn tại. */
    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, name, price, quantity, category, active, description FROM "
                        + TABLE_PRODUCTS + " WHERE id = ?",
                new String[]{String.valueOf(id)});
        Product p = null;
        if (cursor.moveToFirst()) {
            p = cursorToProduct(cursor);
        }
        cursor.close();
        return p;
    }

    /** Cập nhật số lượng (dùng cho Nhập thêm / Bán). */
    public int updateQuantity(int id, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        return db.update(TABLE_PRODUCTS, values, "id = ?",
                new String[]{String.valueOf(id)});
    }

    /** Xóa sản phẩm theo id. */
    public int deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PRODUCTS, "id = ?", new String[]{String.valueOf(id)});
    }

    // ---------------------------------------------------------------
    // Thống kê
    // ---------------------------------------------------------------

    /** Tổng số sản phẩm. */
    public int countProducts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /** Tổng giá trị kho: SUM(price * quantity). */
    public long getTotalValue() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(price * quantity) FROM " + TABLE_PRODUCTS, null);
        long total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getLong(0); // SUM rỗng -> NULL -> getLong trả 0
        }
        cursor.close();
        return total;
    }

    /** Số sản phẩm sắp hết hàng: COUNT với quantity < 5. */
    public int countLowStock() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE quantity < ?",
                new String[]{String.valueOf(Product.LOW_STOCK_THRESHOLD)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
