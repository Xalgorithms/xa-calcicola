package org.lichen.garni.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.lichen.garni.data.GeghardSite;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

public class SitesAdapter extends BaseAdapter implements Action1<List<GeghardSite>> {
    private final LayoutInflater _inflater;
    private List<GeghardSite> _sites = Collections.emptyList();

    public SitesAdapter(Context ctx) {
        _inflater = LayoutInflater.from(ctx);
    }

    @Override
    public void call(List<GeghardSite> sites) {
        _sites = sites;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _sites.size();
    }

    @Override
    public GeghardSite getItem(int i) {
        return _sites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).id();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View v, ViewGroup p) {
        if (null == v) {
            v = _inflater.inflate(android.R.layout.simple_list_item_1, p, false);
        }

        GeghardSite s = getItem(i);
        ((TextView) v).setText(s.url());

        return v;
    }
}
