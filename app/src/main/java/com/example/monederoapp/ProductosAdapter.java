package com.example.monederoapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ProductosAdapter extends ArrayAdapter<Category> {
    private final List<Category> list;

    public ProductosAdapter(Context context, int resource, List<Category> list) {
        super(context, resource, list);
        this.list = list;
    }

    static class ViewHolder {
        protected TextView categoryName;
        protected CheckBox categoryCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.row, null);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.textV);
            viewHolder.categoryCheckBox = (CheckBox) convertView.findViewById(R.id.chkItem);


            convertView.setTag(viewHolder);
            convertView.setTag(R.id.textV, viewHolder.categoryName);
            convertView.setTag(R.id.chkItem, viewHolder.categoryCheckBox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.categoryCheckBox.setTag(position);
        viewHolder.categoryName.setText(list.get(position).getCategory_Name());
        viewHolder.categoryCheckBox.setChecked(list.get(position).isSelected());

        return convertView;
    }
}