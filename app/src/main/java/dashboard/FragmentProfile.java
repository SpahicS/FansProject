package dashboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import activities.DashboardActivity;
import adapters.CountryCodesAdapter;
import digitalbath.fansproject.R;
import helpers.main.AppController;
import helpers.main.AppHelper;
import helpers.other.Rank;
import jp.wasabeef.glide.transformations.BlurTransformation;
import listeners.OnCountrySelectorClickListener;
import models.news.Country;

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
        bindProfileHeader(rootView);

        expandAppBar();

        initializeCountryList();
        initializeRecyclerView();

        getNumberOfPosts(rootView);

        mSelector.setOnClickListener(new OnCountrySelectorClickListener
                (mCountryCodesRecycler, mCountryList));

        return rootView;
    }

    private void expandAppBar() {
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true, true);
    }

    private void getNumberOfPosts(final View rootView) {

        AppController.getFirebaseDatabase().child("posts").child(AppController.getUser()
                .getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int numberOfPosts = 0;

                for (DataSnapshot ignored : dataSnapshot.getChildren()) {
                    numberOfPosts++;
                }

                TextView numberOfPostsTextView = (TextView)
                        rootView.findViewById(R.id.number_of_posts);

                numberOfPostsTextView.setText(String.valueOf(numberOfPosts));

                Rank rank = Rank.getRank(numberOfPosts);

                TextView rankLabel = (TextView)
                        rootView.findViewById(R.id.rank_label);

                rankLabel.setText(String.valueOf(rank.getRankLabel()));

                ImageView rankBadge = (ImageView)
                        rootView.findViewById(R.id.rank_badge);

                rankBadge.setColorFilter(ContextCompat.getColor(getContext(), rank.getRankBadgeColor()));

                Drawable logo = ResourcesCompat.getDrawableForDensity(getResources(),
                        getResources().getIdentifier(getResources().getString(R.string.app_icon),
                                "mipmap", getActivity().getPackageName()),
                        DisplayMetrics.DENSITY_XXXHIGH, getActivity().getTheme());

                ((ImageView) rootView.findViewById(R.id.posts_badge)).setImageDrawable(logo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bindProfileHeader(View rootView) {

        Glide.with(this)
                .load(AppController.getUser().getPhotoUrl())
                .error(R.drawable.profile_bcg)
                .bitmapTransform(new BlurTransformation(getContext(), 3))
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

//        TODO  Na ovaj fazon napraviti abecedni red
//        List<String> list = Arrays.asList(countryCodes);
//
//        PriorityQueue<String> priorityQueue = new PriorityQueue<>();
//        priorityQueue.addAll(list);
//
//        List<String> newList = new ArrayList<>();
//        while (!priorityQueue.isEmpty()) {
//            newList.add(priorityQueue.remove());
//        }

        int position = 0;

        String code = getActivity()
                .getSharedPreferences("COUNTRY_CODES", Context.MODE_PRIVATE)
                .getString("COUNTRY_CODE", null);

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

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mCountryCodesRecycler.setLayoutManager(mLayoutManager);
        mCountryCodesRecycler.setAdapter(new CountryCodesAdapter(getActivity(), mCountryList,
                mCountryCodesRecycler, mCountryName, mAppBarLayout));
    }

    private void initializeViews(View rootView) {

        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mCountryCodesRecycler = (RecyclerView) rootView.findViewById(R.id.country_codes_recycler);
        mCountryName = (TextView) rootView.findViewById(R.id.country_name);
        mSelector = (RelativeLayout) rootView.findViewById(R.id.country_selector);

    }

    public void hideCountriesRecycler() {
        if (mCountryCodesRecycler != null) {
            mCountryCodesRecycler.setVisibility(View.GONE);
        }
    }
}