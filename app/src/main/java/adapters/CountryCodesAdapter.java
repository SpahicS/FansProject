package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import digitalbath.fansproject.R;
import listeners.OnCountryCodeClickListener;
import models.news.Country;
import viewholders.CountryCodesViewHolder;

/**
 * Created by Spaja on 31-May-17.
 */

public class CountryCodesAdapter extends RecyclerView.Adapter<CountryCodesViewHolder> {

    private Activity mActivity;
    private RecyclerView mCountriesRecycler;
    private TextView mCountryName;
    private ArrayList<Country> mDataSet;
    private AppBarLayout appBarLayout;

    public CountryCodesAdapter(Activity activity, ArrayList<Country> countries,
                               RecyclerView countriesRecycler, TextView countryName,
                               AppBarLayout appBarLayout) {

        this.mActivity = activity;
        this.mCountriesRecycler = countriesRecycler;
        this.mCountryName = countryName;
        this.mDataSet = countries;
        this.appBarLayout = appBarLayout;

        String code = mActivity
                .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE)
                .getString("COUNTRY_CODE", null);

        for (int i = 0; i < mDataSet.size(); i++) {
            if (mDataSet.get(i).getCountryCode().equals(code)) {
                mDataSet.get(i).setSelected(true);
            }
        }
    }

    @Override
    public CountryCodesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_code_item, parent, false);
        return new CountryCodesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountryCodesViewHolder holder, int position) {

        holder.initializeViews(holder.itemView, mActivity);

        Country country = mDataSet.get(position);

        if (country.isSelected()) {
            holder.checkMark.setVisibility(View.VISIBLE);
        } else {
            holder.checkMark.setVisibility(View.GONE);
        }

        holder.countryCode.setText(country.getCountryName());

        holder.country.setOnClickListener(
                new OnCountryCodeClickListener(mActivity, mDataSet, mCountriesRecycler,
                        position, mCountryName));

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void expandToolbar() {
        appBarLayout.setExpanded(true, true);
    }
}
