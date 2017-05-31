package dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import adapters.CountryCodesAdapter;
import digitalbath.fansproject.R;
import listeners.OnCountrySelectorClickListener;

/**
 * Created by unexpected_err on 29/04/2017.
 */

public class FragmentProfile extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentProfile() {}

    public static FragmentProfile newInstance(int sectionNumber) {

        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        RecyclerView countryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        countryCodesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        countryCodesRecycler.setAdapter(new CountryCodesAdapter(getContext(), getActivity().getResources().getStringArray(R.array.country_codes)));

        Button selector = (Button) rootView.findViewById(R.id.country_selector);
        selector.setOnClickListener(new OnCountrySelectorClickListener(countryCodesRecycler));

        return rootView;
    }
}