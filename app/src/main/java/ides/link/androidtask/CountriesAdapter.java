package ides.link.androidtask;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ides.link.androidtask.models.CountriesModel;

/**
 * Created by Eman on 9/27/2017.
 */

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    Context mContext;
    ArrayList<CountriesModel> list;
    public CountriesAdapter(Context mContext){
        this.mContext = mContext;

    }

    @Override
    public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.countries_list_item, parent, false);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountriesViewHolder holder, int position) {
        String countryName = list.get(position).getCapital();
        holder.countriesTV.setText(countryName);

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    class CountriesViewHolder extends RecyclerView.ViewHolder{
        TextView countriesTV;
        public CountriesViewHolder(View itemView) {
            super(itemView);
            countriesTV = (TextView) itemView.findViewById(R.id.Country_name_text_view);
        }
    }

    public void swapData( ArrayList<CountriesModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
