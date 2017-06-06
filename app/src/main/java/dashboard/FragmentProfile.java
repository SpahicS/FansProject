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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import helpers.main.AppController;
import jp.wasabeef.glide.transformations.BlurTransformation;
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
    private View rootView;
    private AppBarLayout appBarLayout;
    private RecyclerView countryCodesRecycler;
    private TextView countryName, language;
    private RelativeLayout selector;
    private ArrayList<Country> countryList;
    private LinearLayoutManager manager;

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

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        RecyclerView countryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        RelativeLayout selector = (RelativeLayout) rootView.findViewById(R.id.country_selector);

        bindProfileHeader();
        initializeViews();
        initializeCountryList();
        initializeRecyclerView();

        selector.setOnClickListener(new OnCountrySelectorClickListener(countryCodesRecycler,
                countryList, manager));

        return rootView;
    }

    private void initializeViews() {

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        countryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        countryName = (TextView) rootView.findViewById(R.id.country_name);
        language = (TextView) rootView.findViewById(R.id.language);
        selector = (RelativeLayout) rootView.findViewById(R.id.country_selector);
    }

    private void bindProfileHeader() {

        Glide.with(this)
            .load(AppController.getUser().getPhotoUrl())
            .error(R.drawable.profile_bcg)
            .bitmapTransform(new BlurTransformation(getContext(), 10))
            .into((ImageView) rootView.findViewById(R.id.image_blur));

        Glide.with(this)
            .load(AppController.getUser().getPhotoUrl())
            .error(R.drawable.avatar)
            .into((ImageView) rootView.findViewById(R.id.avatar));

        ((TextView) rootView.findViewById(R.id.user_name))
            .setText(AppController.getUser().getDisplayName());

        ((TextView) rootView.findViewById(R.id.user_email))
            .setText(AppController.getUser().getEmail());
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
        countryName.setText(countryNames[position]);

        countryList = new ArrayList<>();

        for (int i = 0; i < countryCodes.length; i++) {
            Country country = new Country();
            country.setCountryCode(countryCodes[i]);
            country.setCountryName(countryNames[i]);
            countryList.add(country);
        }
    }

    private void initializeRecyclerView() {

        manager = new LinearLayoutManager(getContext());
        countryCodesRecycler.setLayoutManager(manager);
        countryCodesRecycler.setAdapter(new CountryCodesAdapter(getContext(), countryList,
                countryCodesRecycler, countryName, appBarLayout));
    }
}