package adapters;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import digitalbath.fansproject.R;
import listeners.OnCountryCodeClickListener;
import models.Country;

/**
 * Created by Spaja on 31-May-17.
 */

public class CountryCodesAdapter extends RecyclerView.Adapter<CountryCodesAdapter.MyViewHolder> {

    private Context mContext;
    private RecyclerView mCountriesRecycler;
    private TextView mCountryName;
    private ArrayList<Country> mDataSet;
    private AppBarLayout appBarLayout;

    public CountryCodesAdapter(Context context, ArrayList<Country> countries,
                               RecyclerView countriesRecycler, TextView countryName,
                               AppBarLayout appBarLayout) {

        this.mContext = context;
        this.mCountriesRecycler = countriesRecycler;
        this.mCountryName = countryName;
        this.mDataSet = countries;
        this.appBarLayout = appBarLayout;

        String code = mContext.getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE).getString("COUNTRY_CODE", null);

        for (int i = 0; i < mDataSet.size(); i++) {
            if (mDataSet.get(i).getCountryCode().equals(code)) {
                mDataSet.get(i).setSelected(true);
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_code_item, parent, false);
        return new CountryCodesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mDataSet.get(position).isSelected()) {
            holder.checkMark.setVisibility(View.VISIBLE);
        } else {
            holder.checkMark.setVisibility(View.GONE);
        }

        holder.countryCode.setText(mDataSet.get(position).getCountryName());

        holder.countryCode.setOnClickListener(
                new OnCountryCodeClickListener(mContext, mDataSet, mCountriesRecycler,
                        position, mCountryName));

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void expandToolbar() {
        appBarLayout.setExpanded(true, true);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView countryCode;
        ImageView checkMark;

        MyViewHolder(View itemView) {

            super(itemView);
            countryCode = (TextView) itemView.findViewById(R.id.country_code);
            checkMark = (ImageView) itemView.findViewById(R.id.check_mark);
        }
    }
}
