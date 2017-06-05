package dashboard;

import android.content.Context;
import android.os.Bundle;
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
import listeners.OnCountryCodeClickListener;
import listeners.OnCountrySelectorClickListener;
import models.Country;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentProfile extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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

        RecyclerView countryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        countryCodesRecycler.setLayoutManager(manager);

        String[] countryCodes = getActivity().getResources().getStringArray(R.array.country_codes);
        String[] countryNames = getActivity().getResources().getStringArray(R.array.country_names);
        TextView countryName = (TextView) rootView.findViewById(R.id.country_name);
        int position = 0;
        String code = getActivity().getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE).getString("COUNTRY_CODE", null);
        for (int i = 0; i < countryCodes.length; i++) {
            if (code.equals(countryCodes[i])) {
                position = i;
            }
        }
        countryName.setText(countryNames[position]);

        ArrayList<Country> countryList = new ArrayList<>();

        for (int i = 0; i <  countryCodes.length; i++) {
            Country country = new Country();
            country.setCountryCode(countryCodes[i]);
            country.setCountryName(countryNames[i]);
            countryList.add(country);
        }

        countryCodesRecycler.setAdapter(new CountryCodesAdapter(getContext(), countryList ,
                countryCodesRecycler, countryName, manager));

        RelativeLayout selector = (RelativeLayout) rootView.findViewById(R.id.country_selector);
        selector.setOnClickListener(new OnCountrySelectorClickListener(countryCodesRecycler, countryList, manager));

        return rootView;
    }
}