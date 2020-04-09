package com.easyapps.coronatracker.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easyapps.coronatracker.R;
import com.easyapps.coronatracker.VolleySingleton;
import com.easyapps.coronatracker.country_wise.CountryActivity;
import com.easyapps.coronatracker.state_wise.StateWiseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;

public class GlobalFragment extends Fragment {

    private TextView confirmed, active, recovered, deceased;
    private TextView update;
    private RequestQueue mQueue;
    private CardView country_wise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_global, container, false);

        confirmed = root.findViewById(R.id.data_confirmed);
        active = root.findViewById(R.id.data_active);
        recovered = root.findViewById(R.id.data_recovered);
        deceased = root.findViewById(R.id.data_deceased);

        update=root.findViewById(R.id.updated);

        mQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        jsonParse();

        country_wise=root.findViewById(R.id.card_5);
        country_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CountryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return root;
    }

    private void jsonParse() {
        String url = "https://covid.mathdro.id/api";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void showData(JSONObject response) {
        try {

            JSONObject confirm=response.getJSONObject("confirmed");
            String confirmed_no=confirm.getString("value");

            JSONObject recover=response.getJSONObject("recovered");
            String recover_no=recover.getString("value");

            JSONObject death=response.getJSONObject("deaths");
            String death_no=death.getString("value");

            String updated=response.getString("lastUpdate");

            confirmed.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(confirmed_no)));
            recovered.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(recover_no)));
            deceased.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(death_no)));

            Integer active_no=Integer.valueOf(confirmed_no)-Integer.valueOf(recover_no)-Integer.valueOf(death_no);
            active.setText(NumberFormat.getNumberInstance(Locale.US).format(active_no));
            update.append(updated);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
