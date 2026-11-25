package com.example.warehouse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final List<Product> products; // chỉ chứa kết quả truy vấn để hiển thị

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    private static class ViewHolder {
        TextView tvName, tvQty, tvPrice, tvCategory, tvLow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false);
            h = new ViewHolder();
            h.tvName = convertView.findViewById(R.id.tvItemName);
            h.tvQty = convertView.findViewById(R.id.tvItemQty);
            h.tvPrice = convertView.findViewById(R.id.tvItemPrice);
            h.tvCategory = convertView.findViewById(R.id.tvItemCategory);
            h.tvLow = convertView.findViewById(R.id.tvItemLow);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        Product p = products.get(position);
        h.tvName.setText(p.getName());
        h.tvQty.setText("SL: " + p.getQuantity());
        h.tvPrice.setText("Giá bán: " + Product.formatMoney(p.getPrice()));
        h.tvCategory.setText(p.getCategory());

        if (p.isLowStock()) {
            h.tvLow.setVisibility(View.VISIBLE);
            h.tvQty.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            h.tvLow.setVisibility(View.GONE);
            h.tvQty.setTextColor(Color.parseColor("#212121"));
        }
        return convertView;
    }
}
