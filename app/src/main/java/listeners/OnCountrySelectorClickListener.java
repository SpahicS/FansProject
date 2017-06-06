package listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import models.Country;

/**
 * Created by Spaja on 31-May-17.
 */

public class OnCountrySelectorClickListener implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Country> mDataSet;
    private LinearLayoutManager mManager;

    public OnCountrySelectorClickListener(RecyclerView mRecyclerView, ArrayList<Country> countries, LinearLayoutManager manager) {
        this.mRecyclerView = mRecyclerView;
        this.mDataSet = countries;
        this.mManager = manager;

    }

    @Override
    public void onClick(View v) {

        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < mDataSet.size(); i++) {
                if (mDataSet.get(i).isSelected()) {
                    mManager.scrollToPositionWithOffset(i, 0);
                }
            }
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
