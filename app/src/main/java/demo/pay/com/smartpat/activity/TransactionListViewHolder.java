package demo.pay.com.smartpat.activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import demo.pay.com.smartpat.R;

/**
 * Created by ARUN on 2/12/18.
 */

public class TransactionListViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView place;
    TextView city;
    TextView house;

    public TransactionListViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        place = itemView.findViewById(R.id.tv_hse);
        city = itemView.findViewById(R.id.tv_yrs);
        house = itemView.findViewById(R.id.tv_city);


    }

    public void bind(JSONObject data) {
        try {
            name.setText(data.getString("nm"));
            place.setText(data.getString("cty"));
            city.setText(data.getString("hse"));
            house.setText(data.getString("yrs"));
        }
        catch (JSONException exception) {
            Log.e("Error While parsing",exception.getMessage());
        }
    }
}
