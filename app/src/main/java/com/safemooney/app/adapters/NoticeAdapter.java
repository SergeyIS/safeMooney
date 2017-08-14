package com.safemooney.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.safemooney.R;
import com.safemooney.http.models.TransactionPreview;

import java.util.List;

public class NoticeAdapter extends ArrayAdapter<TransactionPreview>
{

    private LayoutInflater inflater;
    private int layout;
    private List<TransactionPreview> transactions;

    public NoticeAdapter(Context context, int resource, List<TransactionPreview> transactions)
    {
        super(context, resource, transactions);
        this.transactions = transactions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view = inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.background_img);
        TextView usernameView = (TextView) view.findViewById(R.id.username_text);
        TextView largenameView = (TextView) view.findViewById(R.id.largename_text);
        TextView countView = (TextView) view.findViewById(R.id.count_text);
        Button confirmBtn = (Button) view.findViewById(R.id.confirm_btn);

        TransactionPreview trans = transactions.get(position);
        confirmBtn.setId(trans.getTransactionData().getId());
        imageView.setImageResource(R.color.colorOrange);
        largenameView.setText(trans.getUserData().getFirstName() + " " + trans.getUserData().getLastName());
        usernameView.setText("@" + trans.getUserData().getUsername());
        countView.setText(trans.getTransactionData().getCount());

        return view;
    }
}