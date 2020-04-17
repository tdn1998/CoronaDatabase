package com.easyapps.coronatracker.country_wise;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class CountryActivity extends AppCompatActivity {

    private RequestQueue mQueue;

    private RecyclerView mRecyclerView;
    private CountryAdapter mAdapter;
    private ArrayList<CountryItem> countryList;
    //private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Country Wise Data");

        /*dialog = new ProgressDialog(CountryActivity.this, R.style.AppCompatAlertDialogStyle);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Getting Data...");
        dialog.show();*/

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryList = new ArrayList<>();

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonParse();
    }

    private void jsonParse() {
        String url = "https://pomber.github.io/covid19/timeseries.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Iterator<String> keys = response.keys();

                            while (keys.hasNext()) {
                                String key = keys.next(); //state name

                                JSONArray country = response.getJSONArray(key);

                                JSONObject obj = response.getJSONArray(key).getJSONObject(country.length() - 1);

                                String confirmed = obj.getString("confirmed");
                                String recovered = obj.getString("recovered");
                                String deaths = obj.getString("deaths");
                                String date = obj.getString("date");

                                countryList.add(new CountryItem(key, date, confirmed, recovered, deaths));
                            }

                            mAdapter = new CountryAdapter(CountryActivity.this, countryList);
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
        //dialog.dismiss();
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
