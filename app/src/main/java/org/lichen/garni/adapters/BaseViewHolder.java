package org.lichen.garni.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class BaseViewHolder extends RecyclerView.ViewHolder{
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected TextView textView(View v, int resId) {
        return (TextView) v.findViewById(resId);
    }

    protected String format(Date d) {
        return DateFormat.getDateInstance().format(d);
    }
}
