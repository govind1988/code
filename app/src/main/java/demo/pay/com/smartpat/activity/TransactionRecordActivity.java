package demo.pay.com.smartpat.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import demo.pay.com.smartpat.R;

/**
 * Created by ARUN on 2/11/18.
 */

public class TransactionRecordActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trancation);

        RecyclerView transactionRv = findViewById(R.id.rv_transactions);
        transactionRv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<JSONObject> transactionList = new ArrayList<>();
        try {
            String jsonLocation = AssetJSONFile("data.json", this);
            JSONArray formArray = new JSONArray(jsonLocation);
            JSONObject place = formArray.getJSONObject(0);
            String name = place.getString("nm");
            Log.d("trancationactivity",name);

            for(int i = 0; i < formArray.length(); i++){
                transactionList.add(formArray.getJSONObject(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TransactionListAdapter adapter = new TransactionListAdapter(transactionList);
        transactionRv.setAdapter(adapter);
    }

    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
}
