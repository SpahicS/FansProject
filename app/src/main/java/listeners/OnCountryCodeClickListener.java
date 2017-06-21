package listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.CountryCodesAdapter;
import models.Country;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountryCodeClickListener implements View.OnClickListener {

    private Context mContext;
    private RecyclerView mCountriesRecycler;
    private TextView mCountryName;
    private ArrayList<Country> mDataSet;
    private int position;

    public OnCountryCodeClickListener(Context context, ArrayList<Country> countries,
                                      RecyclerView countriesRecycler,
                                      int position, TextView countryName) {
        this.mContext = context;
        this.mCountriesRecycler = countriesRecycler;
        this.mDataSet = countries;
        this.position = position;
        this.mCountryName = countryName;
    }

    @Override
    public void onClick(View v) {

        saveCountryCode();

        mCountriesRecycler.setVisibility(View.GONE);

        mCountryName.setText(mDataSet.get(position).getCountryName());

        for (int i = 0; i < mDataSet.size(); i++) {

            if (mDataSet.get(i).isSelected()) {
                mDataSet.get(i).setSelected(false);
                mCountriesRecycler.getAdapter().notifyItemChanged(i);
            }
        }

        mDataSet.get(position).setSelected(true);
        mCountriesRecycler.getAdapter().notifyItemChanged(position);
        ((CountryCodesAdapter) mCountriesRecycler.getAdapter()).expandToolbar();
    }

    private void saveCountryCode() {
        SharedPreferences prefs = mContext
                .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("COUNTRY_CODE", mDataSet.get(position).getCountryCode());
        editor.apply();
    }
}
