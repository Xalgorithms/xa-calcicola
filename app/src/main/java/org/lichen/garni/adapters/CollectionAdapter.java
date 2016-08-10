package org.lichen.garni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

public abstract class CollectionAdapter<T> extends BaseAdapter {
    private List<T> _all_objects = Collections.emptyList();
    private List<T> _objects = Collections.emptyList();

    private final LayoutInflater _inflater;

    public CollectionAdapter(Context ctx) {
        _inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public T getItem(int i) {
        return _objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return id(getItem(i));
    }

    @Override
    public View getView(int i, View v, ViewGroup p) {
        if (null == v) {
            v = _inflater.inflate(android.R.layout.simple_list_item_1, p, false);
        }

        update(v, getItem(i));

        return v;
    }

    public void update(List<T> os) {
        _objects = os;
        notifyDataSetChanged();
    }

    protected abstract void update(View v, T o);
    protected abstract long id(T o);
}
