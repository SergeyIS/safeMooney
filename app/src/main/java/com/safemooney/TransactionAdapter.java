package com.safemooney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private LayoutInflater inflater;
    private int layout;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, int resource, List<Transaction> transactions) {
        super(context, resource, transactions);
        this.transactions = transactions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.background_img);
        TextView nameView = (TextView) view.findViewById(R.id.username_text);
        TextView countView = (TextView) view.findViewById(R.id.count_text);

        Transaction state = transactions.get(position);

        imageView.setImageResource(R.color.colorOrange);
        nameView.setText(state.getUsername());
        countView.setText(state.getCount());

        return view;
    }
}