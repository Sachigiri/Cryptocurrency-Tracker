package com.example.cryptotrackerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText searchEdt;
    private RecyclerView rView;
    private ProgressBar progressBar;
    private ArrayList<CurrencyModelRV>currencyModelRVArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      searchEdt=findViewById(R.id.idEditSearch);
      rView=findViewById(R.id.idRVcurencies);
      progressBar=findViewById(R.id.idPBloading);
      currencyModelRVArrayList=new ArrayList<>();
      currencyRVAdapter=new CurrencyRVAdapter((currencyModelRVArrayList),this);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(currencyRVAdapter);
        getCurrentData();
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
             filterCurrencies(editable.toString());
            }
        });

    }
    private void filterCurrencies(String currency)
    {
        ArrayList<CurrencyModelRV>filteredlist=new ArrayList<>();
        for(CurrencyModelRV item:currencyModelRVArrayList)
        {
          if( item.name.toLowerCase().contains(currency.toLowerCase()))
          {
              filteredlist.add(item);
          }
          if(filteredlist.isEmpty())
          {
              Toast.makeText(MainActivity.this, "No currency found for search query", Toast.LENGTH_SHORT).show();
          }
          else
          {
              currencyRVAdapter.filterlist(filteredlist);
          }
        }
    }
    public void getCurrentData()
    {
        progressBar.setVisibility(View.VISIBLE);
        String url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray dataArray= response.getJSONArray("data");
                    for(int i=0;i< dataArray.length();i++)
                    {
                        JSONObject dataObj=dataArray.getJSONObject(i);
                        String name=dataObj.getString("name");
                        String symbol=dataObj.getString("symbol");
                        JSONObject quote=dataObj.getJSONObject("quote");
                        JSONObject USD=quote.getJSONObject("USD");
                        double price= USD.getDouble("price");
                        currencyModelRVArrayList.add(new CurrencyModelRV(name,symbol,price));
                    }
                    currencyRVAdapter.notifyDataSetChanged();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to get Data", Toast.LENGTH_SHORT).show();
                }
                

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Fail to get the data", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String>header=new HashMap<>();
                header.put("X-CMC_PRO_API_KEY","https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest");
                return header;
            }
        };



            requestQueue.add(jsonObjectRequest);

    }
}