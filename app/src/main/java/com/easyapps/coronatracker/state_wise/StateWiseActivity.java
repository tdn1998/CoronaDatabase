package com.easyapps.coronatracker.state_wise;

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
import com.easyapps.coronatracker.R;
import com.easyapps.coronatracker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class StateWiseActivity extends AppCompatActivity {

    private RequestQueue mQueue;

    private RecyclerView mRecyclerView;
    private StateWiseAdapter mAdapter;
    private ArrayList<StateWiseItem> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise);

        Objects.requireNonNull(getSupportActionBar()).setTitle("State Wise Data");

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        stateList = new ArrayList<>();

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse();
    }

    private void jsonParse() {
        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");
                            for (int i = 1; i < jsonArray.length(); i++) {
                                JSONObject cases = jsonArray.getJSONObject(i);

                                String state = cases.getString("state");
                                String date = cases.getString("lastupdatedtime");

                                String total_confirmed = cases.getString("confirmed");
                                String total_active = cases.getString("active");
                                String total_recovered = cases.getString("recovered");
                                String total_deceased = cases.getString("deaths");


                                String daily_confirmed = cases.getString("deltaconfirmed");
                                String daily_recovered = cases.getString("deltarecovered");
                                String daily_deceased = cases.getString("deltadeaths");

                                stateList.add(new StateWiseItem(state,date,total_confirmed,total_active,
                                        total_recovered,total_deceased,daily_confirmed,daily_recovered,daily_deceased));
                            }

                            mAdapter=new StateWiseAdapter(StateWiseActivity.this,stateList);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
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
        MenuInflater inflater  =getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

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
