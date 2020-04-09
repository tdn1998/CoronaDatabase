package com.easyapps.coronatracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easyapps.coronatracker.R;
import com.easyapps.coronatracker.date_wise.DateWiseActivity;
import com.easyapps.coronatracker.district_wise.DistrictActivity;
import com.easyapps.coronatracker.state_wise.StateWiseActivity;
import com.easyapps.coronatracker.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class IndiaFragment extends Fragment {

    private TextView confirmed, active, recovered, deceased;
    private TextView confirmed_on, recovered_on, deceased_on;
    private TextView update;
    private RequestQueue mQueue;
    private CardView state_wise,date_wise,district_wise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_india, container, false);

        confirmed = root.findViewById(R.id.data_confirmed);
        active = root.findViewById(R.id.data_active);
        recovered = root.findViewById(R.id.data_recovered);
        deceased = root.findViewById(R.id.data_deceased);

        confirmed_on = root.findViewById(R.id.data_confirmed_on);
        recovered_on = root.findViewById(R.id.data_recovered_on);
        deceased_on = root.findViewById(R.id.data_deceased_on);

        update=root.findViewById(R.id.updated);

        mQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        state_wise=root.findViewById(R.id.card_8);
        state_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StateWiseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        date_wise=root.findViewById(R.id.card_9);
        date_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DateWiseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        district_wise=root.findViewById(R.id.card_10);
        district_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DistrictActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        String url = "https://api.covid19india.org/data.json";
        jsonParse(url);

        return root;
    }

    private void jsonParse(String url) {
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
            JSONArray array = response.getJSONArray("statewise");

            JSONObject cases = array.getJSONObject(0);

            String total_active = cases.getString("active");
            String total_confirmed = cases.getString("confirmed");
            String total_deceased = cases.getString("deaths");
            String total_recovered = cases.getString("recovered");

            String daily_confirmed = cases.getString("deltaconfirmed");
            String daily_deceased = cases.getString("deltadeaths");
            String daily_recovered = cases.getString("deltarecovered");

            String date= cases.getString("lastupdatedtime");

            confirmed.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(total_confirmed)));
            recovered.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(total_recovered)));
            active.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(total_active)));
            deceased.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(total_deceased)));

            confirmed_on.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(daily_confirmed)));
            recovered_on.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(daily_recovered)));
            deceased_on.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(daily_deceased)));

            update.append(date);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
