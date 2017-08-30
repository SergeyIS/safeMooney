package com.safemooney.app.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.safemooney.R;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.TransactionPreview;
import com.safemooney.http.models.User;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<TransactionPreview>
{

    private LayoutInflater inflater;
    private int layout;
    private List<TransactionPreview> transactions;
    private Context context;
    private User currentUser;

    public TransactionAdapter(Context context, int resource, List<TransactionPreview> transactions, User currentUser)
    {
        super(context, resource, transactions);
        this.transactions = transactions;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUser = currentUser;
    }
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        View view = inflater.inflate(this.layout, parent, false);

        final ImageView imageView = (ImageView) view.findViewById(R.id.background_img);
        TextView usernameView = (TextView) view.findViewById(R.id.username_text);
        TextView largenameView = (TextView) view.findViewById(R.id.largename_text);
        TextView countView = (TextView) view.findViewById(R.id.count_text);

        final TransactionPreview trans = transactions.get(position);

        imageView.setImageResource(R.color.colorOrange);

        if(trans.getUserData().getBitmap() != null)
            imageView.setImageBitmap(trans.getUserData().getBitmap());

        largenameView.setText(trans.getUserData().getFirstName() + " " + trans.getUserData().getLastName());
        usernameView.setText("@" + trans.getUserData().getUsername());
        countView.setText(trans.getTransactionData().getCount());

        countView.setBackgroundResource(R.color.colorOrangeBright);

        if(trans.getTransactionData().getUser1Id() == currentUser.getId())
            countView.setBackgroundResource(R.color.colorBrightgreen);

        view.setBackgroundResource(R.color.colorWhite);
        ((ImageView)view.findViewById(R.id.mask_img)).setImageResource(R.drawable.round);

        if(!trans.getTransactionData().isPermited())
        {
            view.setBackgroundResource(R.color.notAccepted);
            ((ImageView)view.findViewById(R.id.mask_img)).setImageResource(R.drawable.roundgray);
        }


        view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                if(trans.getTransactionData().getUser1Id() != currentUser.getId())
                    return false;

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final int userId = currentUser.getId();
                final String tokenKey = currentUser.getTokenkey();
                final int transId = trans.getTransactionData().getId();

                builder.setMessage("Close transaction?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        transactions.remove(position);
                        notifyDataSetChanged();

                        Thread th = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                TransactionClient transactionClient = new TransactionClient(userId, tokenKey);
                                transactionClient.closeTransaction(transId);
                            }
                        });

                        th.start();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                    }
                });

                builder.create().show();

                return false;
            }
        });

        return view;
    }
}