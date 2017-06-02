package listeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import digitalbath.fansproject.R;
import models.Country;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountryCodeClickListener implements View.OnClickListener {

    private Context mContext;
    private RecyclerView mCountriesRecycler;
    private TextView mCountryNameView, mCountryName;
    private ArrayList<Country> mDataSet;
    private LinearLayoutManager mManager;
    private boolean isRecyclerVisible;
    private boolean isCountryCode;
    private int position;

    public OnCountryCodeClickListener(Context context, ArrayList<Country> countries, RecyclerView countriesRecycler,
                                      TextView countryNameView, int position, LinearLayoutManager manager,
                                      TextView countryName, boolean isCountryCode) {
        this.mContext = context;
        this.mCountriesRecycler = countriesRecycler;
        this.mCountryNameView = countryNameView;
        this.mDataSet = countries;
        this.position = position;
        this.mManager = manager;
        this.isCountryCode = isCountryCode;
        this.mCountryName = countryName;
    }

    @Override
    public void onClick(View v) {

        if (isCountryCode) {
            SharedPreferences prefs = mContext.getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("COUNTRY_CODE", mDataSet.get(position).getCountryCode());
            editor.apply();

            //TODO Provjeriti sa harom zašto je isRecyclerVisible true nakon klika na drzavu - zato ne radi klik na prvu
            mCountriesRecycler.setVisibility(View.GONE);
            isRecyclerVisible = false;

            mCountryName.setText(mDataSet.get(position).getCountryName());

            for (int i = 0; i < mDataSet.size(); i++) {

                if (mDataSet.get(i).isSelected()) {
                    mDataSet.get(i).setSelected(false);
                    mCountriesRecycler.getAdapter().notifyItemChanged(i);
                }
            }

            mDataSet.get(position).setSelected(true);
            mCountriesRecycler.getAdapter().notifyItemChanged(position);
        } else {
            if (!isRecyclerVisible) {
                mCountriesRecycler.setVisibility(View.VISIBLE);
                isRecyclerVisible = true;
                for (int i = 0; i < mDataSet.size(); i++) {
                    if (mDataSet.get(i).isSelected()) {
                        mManager.scrollToPositionWithOffset(i, 20);
                    }
                }
            } else {
                mCountriesRecycler.setVisibility(View.GONE);
                isRecyclerVisible = false;
            }
        }
    }
}
