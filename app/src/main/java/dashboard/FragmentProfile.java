package dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.CountryCodesAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppHelper;
import listeners.OnCountrySelectorClickListener;
import models.Country;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentProfile extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private AppBarLayout mAppBarLayout;
    private RecyclerView mCountryCodesRecycler;
    private TextView mCountryName;
    private RelativeLayout mSelector;
    private ArrayList<Country> mCountryList;
    private LinearLayoutManager mLayoutManager;

    public FragmentProfile() {
    }

    public static FragmentProfile newInstance(int sectionNumber) {

        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews(rootView);
        initializeCountryList();
        initializeRecyclerView();

        mSelector.setOnClickListener(new OnCountrySelectorClickListener(mCountryCodesRecycler,
                mCountryList, mLayoutManager));

        return rootView;
    }

    private void initializeCountryList() {

        String[] countryCodes = getActivity().getResources().getStringArray(R.array.country_codes);
        String[] countryNames = getActivity().getResources().getStringArray(R.array.country_names);

        int position = 0;

        String code = getActivity().getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE).getString("COUNTRY_CODE", null);

        if (code == null) {
            code = AppHelper.getNewsEditionCode(getContext());
        }

        for (int i = 0; i < countryCodes.length; i++) {
            if (code.equals(countryCodes[i])) {
                position = i;
            }
        }

        mCountryName.setText(countryNames[position]);

        mCountryList = new ArrayList<>();

        for (int i = 0; i < countryCodes.length; i++) {
            Country country = new Country();
            country.setCountryCode(countryCodes[i]);
            country.setCountryName(countryNames[i]);
            mCountryList.add(country);
        }
    }

    private void initializeRecyclerView() {

        mLayoutManager = new LinearLayoutManager(getContext());
        mCountryCodesRecycler.setLayoutManager(mLayoutManager);
        mCountryCodesRecycler.setAdapter(new CountryCodesAdapter(getContext(), mCountryList,
                mCountryCodesRecycler, mCountryName, mAppBarLayout));
    }

    private void initializeViews(View rootView) {

        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mCountryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        mCountryName = (TextView) rootView.findViewById(R.id.country_name);
        mSelector = (RelativeLayout) rootView.findViewById(R.id.country_selector);
    }
}