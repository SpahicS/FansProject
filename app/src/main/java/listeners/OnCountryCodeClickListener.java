package listeners;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import activities.DashboardActivity;
import adapters.CountryCodesAdapter;
import dashboard.FragmentNews;
import helpers.main.AppConfig;
import models.news.Country;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountryCodeClickListener implements View.OnClickListener {

    private Activity mActivity;
    private RecyclerView mCountriesRecycler;
    private TextView mCountryName;
    private ArrayList<Country> mDataSet;
    private int position;

    public OnCountryCodeClickListener(Activity activity, ArrayList<Country> countries,
                                      RecyclerView countriesRecycler,
                                      int position, TextView countryName) {
        this.mActivity = activity;
        this.mCountriesRecycler = countriesRecycler;
        this.mDataSet = countries;
        this.position = position;
        this.mCountryName = countryName;
    }

    @Override
    public void onClick(View v) {

        FragmentNews fragment = (FragmentNews) ((DashboardActivity) mActivity).getSupportFragmentManager().getFragments().get(1);

        saveCountryCode();

        mCountriesRecycler.setVisibility(View.GONE);

        fragment.getNewsList(AppConfig.getNewsQuery());
        mCountryName.setText(mDataSet.get(position).getCountryName());

        deselectCountry();

        mDataSet.get(position).setSelected(true);
        mCountriesRecycler.getAdapter().notifyItemChanged(position);
        ((CountryCodesAdapter) mCountriesRecycler.getAdapter()).expandToolbar();
    }

    private void deselectCountry() {

        int listSize = mDataSet.size();

        for (int i = 0; i < listSize; i++) {

            if (mDataSet.get(i).isSelected()) {
                mDataSet.get(i).setSelected(false);
                mCountriesRecycler.getAdapter().notifyItemChanged(i);
                break;
            }
        }
    }

    private void saveCountryCode() {
        SharedPreferences prefs = mActivity
                .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("COUNTRY_CODE", mDataSet.get(position).getCountryCode());
        editor.apply();
    }
}
