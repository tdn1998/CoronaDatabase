package com.darkndev.coronatracker.district_wise;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.darkndev.coronatracker.R;
import com.darkndev.coronatracker.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class DistrictActivity extends AppCompatActivity {

    private RequestQueue mQueue;

    private RecyclerView mRecyclerView;
    private DistrictAdapter mAdapter;
    private ArrayList<DistrictItem> districtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        Objects.requireNonNull(getSupportActionBar()).setTitle("District Wise Data");

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        districtList = new ArrayList<>();

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse();
    }

    private void jsonParse() {
        String url = "https://api.covid19india.org/state_district_wise.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();

                            while (keys.hasNext()) {
                                String key = keys.next(); //state name
                                StringBuilder value_all = new StringBuilder();
                                StringBuilder key1_all = new StringBuilder();

                                JSONObject district = response.getJSONObject(key).getJSONObject("districtData");
                                Iterator<String> keys1 = district.keys();

                                while (keys1.hasNext()) {
                                    String key1 = keys1.next(); //district name

                                    JSONObject confirmed = district.getJSONObject(key1);

                                    String value = confirmed.getString("confirmed"); //cases confirmed

                                    key1_all.append("\n").append(key1).append("\n");
                                    value_all.append("\n").append(value).append("\n");
                                }
                                districtList.add(new DistrictItem(key, key1_all.toString(), value_all.toString()));
                            }

                            mAdapter = new DistrictAdapter(DistrictActivity.this, districtList);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
