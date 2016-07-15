package org.lichen.garni.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.lichen.garni.data.GeghardSite;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

public class SitesAdapter extends BaseAdapter implements Action1<List<GeghardSite>>, Filterable {
    private final LayoutInflater _inflater;
    private List<GeghardSite> _sites = Collections.emptyList();
    private List<GeghardSite> _all_sites = Collections.emptyList();

    public SitesAdapter(Context ctx) {
        _inflater = LayoutInflater.from(ctx);
    }

    @Override
    public void call(List<GeghardSite> sites) {
        _all_sites = sites;
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
    public View getView(int i, View v, ViewGroup p) {
        if (null == v) {
            v = _inflater.inflate(android.R.layout.simple_dropdown_item_1line, p, false);
        }

        GeghardSite s = getItem(i);
        ((TextView) v).setText(s.url());

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence text) {
                FilterResults rv = new FilterResults();
                if (null != text) {
                    List<GeghardSite> suggestions = Lists.newArrayList(Iterables.filter(_all_sites, new Predicate<GeghardSite>() {
                        @Override
                        public boolean apply(GeghardSite input) {
                            return input.url().startsWith(text.toString());
                        }
                    }));
                    rv.values = suggestions;
                    rv.count = suggestions.size();
                }
                return rv;
            }

            @Override
            protected void publishResults(CharSequence text, FilterResults r) {
                if (r.count > 0) {
                    _sites = (List<GeghardSite>) r.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
