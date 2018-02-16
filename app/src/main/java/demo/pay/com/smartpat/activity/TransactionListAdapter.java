package demo.pay.com.smartpat.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import demo.pay.com.smartpat.R;

/**
 * Created by ARUN on 2/12/18.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListViewHolder>{
    ArrayList<JSONObject> dataObjects;

    public TransactionListAdapter(ArrayList<JSONObject> dataObjects) {
        this.dataObjects = dataObjects;
    }

    @Override
    public TransactionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transactionlist,null));
    }

    @Override
    public void onBindViewHolder(TransactionListViewHolder holder, int position) {
        holder.bind(dataObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return dataObjects == null ? 0 : dataObjects.size();
    }
}
