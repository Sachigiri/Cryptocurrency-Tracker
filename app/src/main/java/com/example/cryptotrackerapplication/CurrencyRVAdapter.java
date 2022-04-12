package com.example.cryptotrackerapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
    private ArrayList<CurrencyModelRV>currencyModelRVArrayList;
    private Context context;
    public static DecimalFormat df2=new DecimalFormat("#.##");

    public CurrencyRVAdapter(ArrayList<CurrencyModelRV> currencyModelRVArrayList, Context context) {
        this.currencyModelRVArrayList = currencyModelRVArrayList;
        this.context = context;
    }
    public void filterlist(ArrayList<CurrencyModelRV>filteredlist)
    {
     currencyModelRVArrayList=filteredlist;
     notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent,false);
        return new  CurrencyRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyModelRV currencyModelRV=currencyModelRVArrayList.get(position);
        holder.currencyNameTV.setText(currencyModelRV.getName());
       holder.symbolTV.setText(currencyModelRV.getSymbol());
       holder.rateTV.setText(String.format("$" + df2.format(CurrencyModelRV.getPrice())));


    }

    @Override
    public int getItemCount() {
        return currencyModelRVArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyNameTV,symbolTV,rateTV;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            currencyNameTV=itemView.findViewById(R.id.idTvCurrencyName);
            symbolTV=itemView.findViewById(R.id.idTVsymbol);
            rateTV=itemView.findViewById(R.id.idTVCurrencyRate);


        }

    }
}
