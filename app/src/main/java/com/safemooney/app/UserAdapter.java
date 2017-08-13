package com.safemooney.app;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.safemooney.R;
import com.safemooney.http.models.UserPreview;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserPreview>
{
    private LayoutInflater inflater;
    private int layout;
    private List<UserPreview> users;

    public UserAdapter(@NonNull Context context, @LayoutRes int resource,  List<UserPreview> users)
    {
        super(context, resource, users);
        this.users = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view = inflater.inflate(this.layout, parent, false);

        TextView usernameText = (TextView)  view.findViewById(R.id.username_text);
        TextView cityText = (TextView) view.findViewById(R.id.city_text);
        ImageView backgroudImg = (ImageView) view.findViewById(R.id.background_img);

        UserPreview userPreview = users.get(position);

        usernameText.setText(userPreview.getFirstName() + " " + userPreview.getLastName());
        cityText.setText(userPreview.getUsername());
        backgroudImg.setImageResource(R.color.colorOrange);

        return view;
    }
}
