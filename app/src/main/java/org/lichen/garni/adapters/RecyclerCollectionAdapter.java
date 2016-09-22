package org.lichen.garni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

public abstract class RecyclerCollectionAdapter<VHT extends RecyclerView.ViewHolder, T>
        extends RecyclerView.Adapter<VHT> {
    private final Context _ctx;
    private final Receiver<T> _receiver;
    private List<T> _collection = Lists.newArrayList();

    public RecyclerCollectionAdapter(Context ctx, Receiver<T> r) {
        _ctx = ctx;
        _receiver = r;
    }

    @Override
    public VHT onCreateViewHolder(ViewGroup container, int viewType) {
        return make_view_holder(LayoutInflater.from(container.getContext()).inflate(
                layout_res_id(), container, false));
    }

    @Override
    public void onBindViewHolder(VHT vh, int pos) {
        T it = _collection.get(pos);
        View target = init_view_holder(vh, it);
        _receiver.receive(target, it);
    }

    @Override
    public int getItemCount() {
        return _collection.size();
    }

    public void update(Collection<T> items) {
        _collection.clear();
        _collection.addAll(items);
        notifyDataSetChanged();
    }

    protected Context context() {
        return _ctx;
    }

    protected abstract int layout_res_id();
    protected abstract VHT make_view_holder(View v);
    protected abstract View init_view_holder(VHT vh, T it);
}
