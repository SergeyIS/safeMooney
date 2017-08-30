package com.safemooney.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safemooney.R;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.TransactionPreview;
import com.safemooney.http.models.User;

import java.util.List;

public class NoticeAdapter extends ArrayAdapter<TransactionPreview>
{

    private LayoutInflater inflater;
    private int layout;
    private List<TransactionPreview> transactions;
    private User currentUser;

    public NoticeAdapter(Context context, int resource, List<TransactionPreview> transactions, User currentUser)
    {
        super(context, resource, transactions);
        this.transactions = transactions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.currentUser = currentUser;
    }
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.background_img);
        TextView usernameView = (TextView) view.findViewById(R.id.username_text);
        TextView largenameView = (TextView) view.findViewById(R.id.largename_text);
        TextView countView = (TextView) view.findViewById(R.id.count_text);
        Button confirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        final TransactionPreview trans = transactions.get(position);

        imageView.setImageResource(R.color.colorOrange);

        if(trans.getUserData().getBitmap() != null)
            imageView.setImageBitmap(trans.getUserData().getBitmap());

        largenameView.setText(trans.getUserData().getFirstName() + " " + trans.getUserData().getLastName());
        usernameView.setText("@" + trans.getUserData().getUsername());
        countView.setText(trans.getTransactionData().getCount());

        countView.setBackgroundResource(R.color.colorOrangeBright);

        if(trans.getTransactionData().getId() == trans.getUserData().getUserId())
            countView.setBackgroundResource(R.color.colorBrightgreen);


        confirmBtn.setOnClickListener(new View.OnClickListener()
        {
            final int userId = currentUser.getId();
            final String tokenKey = currentUser.getTokenkey();
            final int tid = trans.getTransactionData().getId();

            @Override
            public void onClick(View view)
            {
                transactions.remove(position);
                notifyDataSetChanged();

                Thread th = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TransactionClient transactionClient = new TransactionClient(userId, tokenKey);
                        transactionClient.confirmTransaction(tid);
                    }
                });

                th.start();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
//                    transactions.remove(position);
//                    notifyDataSetChanged();

                    Toast toast = Toast.makeText(getContext(), "it's not implemented yet", Toast.LENGTH_LONG);
                    toast.show();
                }
                catch (Exception e)
                {
                    //// TODO: 8/30/17 write log
                }
            }
        });
        return view;
    }
}